package geotesstest;

import gov.sandia.geotess.Data;
import gov.sandia.geotess.DataFloat;
import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessGrid;
import gov.sandia.geotess.GeoTessMetaData;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.geotess.Profile;
import gov.sandia.geotess.ProfileSurface;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.numerical.vector.VectorGeo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author sballar
 *
 */
public class GeoTessGeoid
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			// writeLatLonToFile(args);
			// populateModel(args);
			plotVTK_gmp(args);
			System.out.println("Done.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected static void plotVTK_geoid(String[] args) throws GeoTessException,
			IOException
	{
		File modelDir = new File(args[0]);

		for (String s : new String[] { "_04000", "_02000", "_01000", "_00500" })
		// for (String s : new String[] {"_04000"})
		{

			GeoTessModel model = new GeoTessModel(new File(modelDir, "geotess_geoid" + s + ".nc"));

			System.out.println(model);

			GeoTessModelUtils.vtk(model, new File(modelDir, "vtk/geotess_geoid"
					+ s + ".vtk").getCanonicalPath(), 0, 0, false, new int[]{0});
		}
	}

	private static void plotVTK_gmp(String[] args) throws GeoTessException,
			IOException
	{
		File modelDir = new File(args[0]);

		GeoTessModel model = new GeoTessModel(new File(modelDir, "unified_crust20_ak135_110926_profiles.nc"));

		System.out.println(model);

		File outputFile = new File(modelDir,
				"vtk/unified_crust20_ak135_110926_profiles_%d.vtk");

		GeoTessModelUtils.vtk(model, outputFile.getCanonicalPath(), 0, 100, true,  new int[]{0});
	}

	protected static void writeLatLonToFile(String[] args)
			throws GeoTessException, IOException
	{
		File modelDir = new File(args[0]);

		for (String s : new String[] { "_04000", "_02000", "_01000", "_00500" })
		{

			GeoTessGrid grid = new GeoTessGrid(new File(modelDir, "geotess_grid" + s + ".bin"));

			BufferedWriter output = new BufferedWriter(new FileWriter(new File(
					modelDir, "latlon" + s + ".dat")));

			for (int i = 0; i < grid.getNVertices(); ++i)
				output.append(String.format("%1.12f %1.12f%n",
						VectorGeo.getLatDegrees(grid.getVertex(i)),
						VectorGeo.getLonDegrees(grid.getVertex(i))));

			output.close();
		}
	}

	protected static void populateModel(String[] args) throws GeoTessException,
			IOException
	{
		File modelDir = new File(args[0]);

		for (String s : new String[] { "_04000", "_02000", "_01000", "_00500" })
		{
			// Create a MetaData object in which we can specify information
			// needed
			// for model contruction.
			GeoTessMetaData metaData = new GeoTessMetaData();

			// Specify a description of the model. This information is not
			// processed
			// in any way by GeoTess. It is carried around for information
			// purposes.
			metaData.setDescription(String
					.format("Geoid height above ellipsoid WGS84.%n%n"
							+ "author: Sandy Ballard%n"
							+ "contact: sballar@sandia.gov%n"
							+ "November, 2011%n"));

			// Specify a list of layer names. A model could have many layers,
			// e.g., ("core", "mantle", "crust"), specified in order of
			// increasing
			// radius. This simple example has only one layer.
			metaData.setLayerNames("surface");

			// specify the names of the attributes and the units of the
			// attributes in
			// two String arrays. This model only includes one attribute.
			metaData.setAttributes(new String[] { "Geoid" },
					new String[] { "meters" });

			// specify the DataType for the data. All attributes, in all
			// profiles, will
			// have the same data type.
			metaData.setDataType(DataType.FLOAT);

			// specify the path to the file containing the grid to be use for
			// this test.
			String gridFile = new File(modelDir, "geotess_grid" + s + ".nc")
					.getCanonicalPath();

			// call a GeoTessModel constructor to build the model. This will
			// build the
			// grid, and initialize all the data structures to null. To be
			// useful, we
			// will have to populate the data structures.
			GeoTessModel model = new GeoTessModel(gridFile, metaData);

			Scanner input = new Scanner(
					new File(modelDir, "geoid" + s + ".dat"));

			// generate some data and store it in the model. The data consists
			// of
			// the angular distance in degrees from each vertex of the model
			// grid to
			// station ANMO near Albuquerque, NM, USA.
			for (int i = 0; i < model.getGrid().getNVertices(); ++i)
			{
				//double lat = 
						input.nextDouble();
				//double lon = 
						input.nextDouble();

				float geoid = input.nextFloat();

				// make a new Data object that supports only a single value of
				// type float.
				Data data = new DataFloat(geoid);

				// make a Profile object that supports zero radii and a single
				// Data object
				Profile profile = new ProfileSurface(data);

				// set the Profile object at the specified vertex and layer in
				// the model.
				model.setProfile(i, 0, profile);
			}

			// Now let's write the model out to a file, delete it, reload it
			// from the
			// same file and test it.

			// specify the name of the file to which to write the model.
			String modelFileName = new File(modelDir, "geotess_geoid" + s
					+ ".nc").getCanonicalPath();

			// write the model to an ascii file. The first string is the name of
			// the file
			// that is to receive the output data. The second string is the
			// relative path
			// from where the data file is going to reside back to the existing
			// GeoTessGeometry file. By specifying '*', the grid information
			// will be
			// stored in the same file with the data
			model.writeModel(modelFileName, "*");

			System.out.println(model);

		}

	}

}
