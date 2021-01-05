package gov.sandia.geotess.apps;

import gov.sandia.geotess.Data;
import gov.sandia.geotess.GeoTessGrid;
import gov.sandia.geotess.GeoTessMetaData;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.gmp.util.containers.arraylist.ArrayListDouble;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.globals.Globals;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.numerical.vector.EarthShape;
import gov.sandia.gmp.util.numerical.vector.VectorGeo;
import gov.sandia.gmp.util.numerical.vector.VectorUnit;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

public class AK135_Uncertainty {
	
	// uncertainty vs distance from bottom of seismicBaseData ak135 model
	static double[] ak135_distance = new double[] {0.0, 0.2, 0.5, 1.0, 2.0, 3.0, 4.0, 8.0, 
			12.0, 15.0, 17.0, 19.0, 25.0, 33.0, 37.0, 49.0, 57.0, 65.0, 79.0, 
			83.0, 91.0, 93.0, 97.0, 101.0, 104.0, 109.0, 114.0, 119.0, 121.0, 
			129.0, 137.0, 139.0, 142.0, 145.0, 180.0};
	
	static double[] ak135_uncertainty = new double[] {0.1, 0.5, 1.0, 1.5, 2.1, 2.4, 2.6, 2.7, 
			2.9, 3.6, 3.0, 2.1, 1.4, 1.4, 1.3, 1.3, 1.2, 1.1, 1.1, 1.2, 1.3, 
			1.4, 1.4, 2.0, 2.3, 2.6, 1.4, 1.4, 1.3, 1.3, 2.3, 3.3, 3.7, 1.8, 1.8};
	
	public static void main(String[] args) 
	{
		try 
		{
			ArrayListDouble dist = new ArrayListDouble(200);
			ArrayListDouble mean = new ArrayListDouble(200);
			ArrayListDouble std = new ArrayListDouble(200);
			
			Scanner sc = new Scanner(new File("S:\\salsa3d_journal_article\\1d_uncertainty\\1d_uncertainty.dat"));
			while (sc.hasNext())
			{
				String line = sc.nextLine();
				if (!line.startsWith("#"))
				{
					Scanner in = new Scanner(line);
					dist.add(in.nextDouble());
					mean.add(in.nextDouble());
					std.add(in.nextDouble());
					in.close();
				}
			}
			sc.close();
			
			run(dist.toArray(), std.toArray());
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static protected void run(double[] distance, double[] uncertainty) throws Exception
	{
			// Create a MetaData object in which we can specify information
			// needed for model contruction.
			GeoTessMetaData metaData = new GeoTessMetaData();

			// Specify a description of the model. This information is not
			// processed in any way by GeoTess. It is carried around for
			// information purposes.
			metaData.setDescription(String
					.format("# 1D distant dependent uncertainty for tomography data set through the ak135 model\n"
							+ "from Mike Begnaud 9/21/2015"));

			// Specify a list of layer names. A model could have many layers,
			// e.g., ("core", "mantle", "crust"), specified in order of
			// increasing radius. This simple example has only one layer.
			metaData.setLayerNames("surface");
			
			// Set layerID equal to the index of the one-and-only layer 
			// in this model.
			int layerID = 0;

			// specify the names of the attributes and the units of the
			// attributes in two String arrays. This model only includes
			// one attribute.
			// If this model had two attributes, they would be specified 
			// like this: setAttributes("Distance; Depth", "degrees; km");
			metaData.setAttributes("Uncertainty", "seconds");

			// specify the DataType for the data. All attributes, in all
			// profiles, will have the same data type.
			metaData.setDataType(DataType.FLOAT);
			
			// specify the name of the software that is going to generate
			// the model.  This gets stored in the model for future reference.
			metaData.setModelSoftwareVersion("gov.sandia.geotess.apps.AK135_Uncertainty");
			
			// specify the date when the model was generated.  This gets 
			// stored in the model for future reference.
			metaData.setModelGenerationDate(new Date().toString());

			GeoTessGrid grid = new GeoTessGrid(new File("C:\\Users\\sballar\\git\\geo-tess-java\\resources\\permanent_files\\geotess_grid_01000.geotess"));
			GeoTessModel model = new GeoTessModel(grid, metaData);

			// Each grid vertex will be assigned a single data value consisting
			// of the epicentral distance in degrees from the location of the 
			// grid vertex to seismic station ANMO near Albuquerque, NM.
			// Get unit vector representation of position of station ANMO.
			double[] mjar = VectorGeo.getVectorDegrees(36.524717,  138.24718);

			// generate some data and store it in the model. The data consists
			// of the angular distance in degrees from each vertex of the model
			// grid to station ANMO near Albuquerque, NM, USA.
			for (int vtx = 0; vtx < model.getGrid().getNVertices(); ++vtx)
			{
				// retrieve the unit vector corresponding to the i'th vertex of
				// the grid.
				double[] vertex = model.getGrid().getVertex(vtx);

				// compute the distance from the vertex to station ANMO.
				double dist = VectorUnit.angleDegrees(mjar, vertex);
				
				double uncert = Globals.interpolate(distance, uncertainty, dist);
				
				// Construct a new Data object that holds a single value of 
				// type float. Data.getData() can be called with multiple values
				// (all of the same type), or an array of values.  In this 
				// very simple example, there is only one value: distance.
				Data data = Data.getDataFloat(dist > 95 ? Float.NaN : (float)uncert);
				
				// associate the Data object with the specified vertex of the model.  
				// This instance of setProfile always creates a ProfileSurface object.
				model.setProfile(vtx, data);
			}

			// At this point, we have a fully functional GeoTessModel object
			// that we can work with.
			
			GeoTessModelUtils.vtkRobinson(model, new File("S:\\salsa3d_journal_article\\1d_uncertainty\\1d_uncertainty.vtk"), 
					EarthShape.WGS84.getLonDegrees(mjar), 0, 0, true, InterpolatorType.LINEAR, false, null);

			// print a bunch of information about the model to the screen.
			System.out.println(model.toString());

	}

}
