package gov.sandia.geotess.apps;

import gov.sandia.geotess.Data;
import gov.sandia.geotess.DataArrayOfDoubles;
import gov.sandia.geotess.GeoTessGrid;
import gov.sandia.geotess.GeoTessMetaData;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.geotess.GeoTessPosition;
import gov.sandia.geotess.GeoTessUtils;
import gov.sandia.geotess.PointMap;
import gov.sandia.geotess.Profile;
import gov.sandia.geotess.ProfileNPoint;
import gov.sandia.geotess.ProfileThin;
import gov.sandia.gmp.util.containers.arraylist.ArrayListDouble;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.globals.InterpolatorType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class PedalModelGeometry
{
	public static void main(String[] args)
	{
		try
		{
			GeoTessModel crust20 = new GeoTessModel("C:\\Users\\sballar\\workspace\\GeoTessModels\\crust20.bin");
			System.out.println("Crust 2.0 Model:\n"+crust20.toString());
			System.out.println();

			GeoTessModel topo = new GeoTessModel("T:\\models\\etopo1\\geotess_etopo1_00500.bin");
			System.out.println("Topography Model:\n"+topo.toString());
			System.out.println();

			GeoTessGrid grid = topo.getGrid();

			File depthFile = new File("T:\\LibCorr3D\\depth_model\\reb_depths.dat");

			// read the reb depth information from text file generated with sql script
			Scanner input = new Scanner(depthFile);

			// skip comment line
			input.nextLine();

			// max depth raw
			double[] maxDepth = new double[grid.getNVertices()];

			Arrays.fill(maxDepth, Float.NaN);

			// max depth smoothed
			double[] maxDepthSmooth = new double[grid.getNVertices()];

			Arrays.fill(maxDepthSmooth, 0F);

			double[] x = new double[3];
			double z;

			int tessid = 0;
			int level = grid.getTopLevel(tessid);

			while (input.hasNext())
			{
				topo.getEarthShape().getVectorDegrees(input.nextDouble(), input.nextDouble(), x);
				int[] triangle = grid.getTriangleVertexIndexes(grid.getTriangle(0, x));

				// read the depth value
				z = input.nextFloat();

				for (int vertex : triangle)
					if (Double.isNaN(maxDepth[vertex]) || z > maxDepth[vertex])
						maxDepth[vertex] = z;

				for (int corner : triangle)
					for (Integer vertex : grid.getVertexNeighbors(0, level, corner, 2))
						if (Double.isNaN(maxDepthSmooth[vertex]) || z > maxDepthSmooth[vertex])
							maxDepthSmooth[vertex] = z;
			}

			input.close();


			// Create a MetaData object in which we can specify information
			// needed for model contruction.
			GeoTessMetaData metaData = new GeoTessMetaData();

			// Specify a description of the model. This information is not
			// processed in any way by GeoTess. It is carried around for
			// information purposes.
			metaData.setDescription(String
					.format("Uniform 0.5 degree grid populated with layers from the crust2.0 model.%n"));

			// Specify a list of layer names. A model could have many layers,
			// e.g., ("core", "mantle", "crust"), specified in order of
			// increasing radius. This simple example has only one layer.
			metaData.setLayerNames("seismicity_layer");

			// Set layerID equal to the index of the one-and-only layer 
			// in this model.
			int layerID = 0;

			// specify the names of the attributes and the units of the
			// attributes in two String arrays. This model only includes
			// one attribute.
			// If this model had two attributes, they would be specified 
			// like this: setAttributes("Distance; Depth", "degrees; km");
			metaData.setAttributes("zbottom; ztop; zthick; n; dzmin; dzmax", " ; ; ; ; ; ");

			// specify the DataType for the data. All attributes, in all
			// profiles, will have the same data type.
			metaData.setDataType(DataType.DOUBLE);

			// specify the name of the software that is going to generate
			// the model.  This gets stored in the model for future reference.
			metaData.setModelSoftwareVersion("gov.sandia.geotess.apps.PedalModelGeometry 1.0.0");

			// specify the date when the model was generated.  This gets 
			// stored in the model for future reference.
			metaData.setModelGenerationDate(new Date().toString());

			// call a GeoTessModel constructor to build the model. This will
			// load the grid, and initialize all the data structures to null.
			// To be useful, we will have to populate the data structures.
			GeoTessModel model = new GeoTessModel(grid, metaData);

			GeoTessPosition crust = crust20.getGeoTessPosition(InterpolatorType.NATURAL_NEIGHBOR);

			double zbottom, ztopo, zmoho;
			ArrayListDouble elevation = new ArrayListDouble(100);

			double dzmin, dzmax;

			int n;
			double dz;

			Profile profile;

			// generate some data and store it in the model. 
			for (int vtx = 0; vtx < grid.getNVertices(); ++vtx)
			{
				// set the interpolation position to the current vertex
				crust.set(grid.getVertex(vtx), 6400.);

				elevation.clear();

				// z is elevation in km.

				zmoho = -crust.getDepthTop(0);

				ztopo = topo.getProfile(vtx, 0).getData(0).getDouble(0)*1e-3;

				zbottom = -maxDepthSmooth[vtx];

				if (zbottom > -2 || zbottom > ztopo - 2)
				{
					// max event elevation is very close to zero, or is above the bathymetry
					// we will make a ProfileThin profile with radius = topo.
					zbottom = ztopo;
					elevation.add(ztopo);
				}
				else
				{
					// we will make a ProfileNPoint profile
					zbottom -= 50;

					if (zbottom > zmoho)
						zbottom = zmoho;
					if (zbottom > -410 && zbottom < -350)
						zbottom = -410;
					if (zbottom > -660 && zbottom < -600)
						zbottom = -660;
					if (zbottom < -660)
						zbottom = -700.;

					if (zbottom < -250)
					{
						n = (int) Math.ceil((-200 - zbottom)/40.);
						dz = (-200 - zbottom)/n;
						for (int i=0; i<n; ++i)
							elevation.add((float)(zbottom + i*dz));
						zbottom = -200;
					}

					n = (int) Math.ceil((ztopo-zbottom)/20.);
					dz = (ztopo-zbottom)/(n-1);
					for (int i=0; i<n; ++i)
						elevation.add(zbottom + i*dz);
				}

				if (elevation.size() == 1)
				{

					double[] data = new double[]
							{
							elevation.get(0), elevation.get(elevation.size()-1), 
							elevation.get(elevation.size()-1)-elevation.get(0), 1., Double.NaN, Double.NaN
							};
					profile = new ProfileThin((float)(crust.getEarthRadius()+ztopo), 
							new DataArrayOfDoubles(data));
				}
				else
				{
					// convert elevation to radius
					float[] radii = new float[elevation.size()];
					Data[] data = new Data[radii.length];

					dzmin = 1e30;
					dzmax = -1e30;
					for (int i=1; i<elevation.size(); ++i)
					{
						if (elevation.get(i)-elevation.get(i-1) < dzmin)
							dzmin = elevation.get(i)-elevation.get(i-1);
						if (elevation.get(i)-elevation.get(i-1) > dzmax)
							dzmax = elevation.get(i)-elevation.get(i-1);
					}

					if (dzmax > 75)
					{
						System.out.println("debug");
						dzmin = 1e30;
						dzmax = -1e30;
						for (int i=1; i<elevation.size(); ++i)
						{
							if (elevation.get(i)-elevation.get(i-1) < dzmin)
								dzmin = elevation.get(i)-elevation.get(i-1);
							if (elevation.get(i)-elevation.get(i-1) > dzmax)
								dzmax = elevation.get(i)-elevation.get(i-1);
						}
					}

					double[] d = new double[]
							{
							elevation.get(0), elevation.get(elevation.size()-1), 
							elevation.get(elevation.size()-1)-elevation.get(0), 
							(double)radii.length, dzmin, dzmax
							};

					for (int i=0; i<elevation.size(); ++i)
					{
						radii[i] = (float)(crust.getEarthRadius() + elevation.get(i));
						data[i] = new DataArrayOfDoubles(d);
					}

					profile = new ProfileNPoint(radii, data);
				}

				// set the Profile object at the specified vertex and layer in
				// the model.
				model.setProfile(vtx, layerID, profile);
			}

			// At this point, we have a fully functional GeoTessModel object
			// that we can work with.

			// print a bunch of information about the model to the screen.
			System.out.println(model.toString());

			System.out.println("NPoints = "+model.getNPoints());

			GeoTessModelUtils.vtk(model, "T:\\LibCorr3D\\depth_model\\pedal_depth_model.vtk", 0, false, null);

			model.writeModel("T:\\LibCorr3D\\depth_model\\pedal_depth_model.bin", "*");

			PointMap pointMap = model.getPointMap();

			BufferedWriter output = new BufferedWriter(new FileWriter(new File(
					"T:\\LibCorr3D\\depth_model\\pedal_depth_model_all_points.dat")));

			output.append("point_index  origin_lat origin_lon origin_depth phase\n");
			// ASAR = -23.66513,133.90526,0.6273
			double[] asar = model.getEarthShape().getVectorDegrees(-23.66513,133.90526);
			//			for (int vertex=0; vertex < model.getNVertices(); ++vertex)
			//			{
			//				int point = pointMap.getPointIndexLast(vertex, 0);
			//				double distance = GeoTessUtils.angleDegrees(asar, pointMap.getPointUnitVector(point));
			//				if (distance < 96)
			//				output.write(String.format("%d %s %1.6f %s%n",
			//				 point, pointMap.getPointLatLonString(point), pointMap.getPointDepth(point),
			//				 	distance <= 15 ? "Pn" : "P"));
			//			}
			for (int point=0; point<pointMap.size(); ++point)
			{
				double distance = GeoTessUtils.angleDegrees(asar, pointMap.getPointUnitVector(point));
				//if (distance < 96)
				output.write(String.format("%d %s %1.6f %s%n",
						point, pointMap.getPointLatLonString(point), pointMap.getPointDepth(point),
						distance <= 15 ? "Pn" : "P"));
			}
			output.close();

			System.out.printf("%nDone%n%n");

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

}
