package geotesstest;

import gov.sandia.geotess.Data;
import gov.sandia.geotess.DataArrayOfBytes;
import gov.sandia.geotess.DataArrayOfDoubles;
import gov.sandia.geotess.DataArrayOfFloats;
import gov.sandia.geotess.DataArrayOfInts;
import gov.sandia.geotess.DataArrayOfLongs;
import gov.sandia.geotess.DataArrayOfShorts;
import gov.sandia.geotess.DataByte;
import gov.sandia.geotess.DataDouble;
import gov.sandia.geotess.DataFloat;
import gov.sandia.geotess.DataInt;
import gov.sandia.geotess.DataLong;
import gov.sandia.geotess.DataShort;
import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessGrid;
import gov.sandia.geotess.GeoTessMetaData;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.geotess.GeoTessPosition;
import gov.sandia.geotess.Profile;
import gov.sandia.geotess.ProfileConstant;
import gov.sandia.geotess.ProfileEmpty;
import gov.sandia.geotess.ProfileNPoint;
import gov.sandia.geotess.ProfileSurface;
import gov.sandia.geotess.ProfileThin;
import gov.sandia.geotess.ProfileType;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.numerical.vector.VectorGeo;
import gov.sandia.gmp.util.numerical.vector.VectorUnit;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author sballar
 * 
 */
public class TestAll
{

	static protected Random rand = new Random(1);

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			TestAll test = new TestAll();

			for (String arg : args)
				if (arg.equalsIgnoreCase("borehole"))
					test.borehole();
				else if (arg.equalsIgnoreCase("bigTest"))
					test.bigTest();
				else if (arg.equals("compareCPPFiles"))
					test.compareCPPFiles();
				else if (arg.equals("loopTest"))
					test.loopTest();
				else if (arg.equals("mapValuesTest"))
					test.mapValuesTest();
				else if (arg.equals("translateAsciiFiles"))
					test.translateAsciiFiles();
				else if (arg.equals("testGridIndexes"))
					test.testGridIndexes();
				else if (arg.equals("positionTest"))
					test.positionTest();
				else if (arg.equals("testInterpolationNaN"))
					test.testInterpolationNaN();
				else if (arg.equals("getWeightsTest"))
					test.getWeightsTest();
				else
					throw new GeoTessException(args[0]
							+ " is not a recognized method");

		}
		catch (GeoTessException e)
		{
			e.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Load the standard ascii model and grid files and translate them into
	 * binary and netcdf versions.
	 * 
	 * @param args
	 * @throws GeoTessException
	 * @throws IOException
	 */
	protected void translateAsciiFiles() throws GeoTessException, IOException
	{
		for (String s : new String[] { "crust20",
				"unified_crust20_ak135_110926_profiles" })
		{
			System.out.println(s);
			GeoTessModel model = new GeoTessModel(new File(s + ".ascii"));

			model.writeModel(new File(s + ".geotess"), "*");

			//model.writeModel(new File(s + ".nc"), "*");
		}

		for (String s : new String[] {
				"geotess_grid_64000",
				"geotess_grid_32000",
				"geotess_grid_16000",
				"geotess_grid_08000",
				"geotess_grid_04000",
				"geotess_grid_02000", 
				"geotess_grid_01000",
				"geotess_grid_00500" })
		{
			System.out.println(s);
			GeoTessGrid grid = new GeoTessGrid(new File(s + ".ascii"));
			//grid.writeGrid(new File(s + ".nc"));
			grid.writeGrid(new File(s + ".geotess"));
		}
		
		System.out.println("\nDone.");

	}

	protected void borehole() throws GeoTessException, IOException
	{
		String modelName = "unified_crust20_ak135_110926_profiles.geotess";

		GeoTessModel model = new GeoTessModel(modelName);
		
		System.out.println(model);
		
		model.getGrid().testGrid();
		
		double lat = 30;
		double lon = 90;
		double maxSpacing = 200; // max radial spacing in km.

		GeoTessPosition pos = model.getGeoTessPosition(InterpolatorType.LINEAR);

		pos.set(lat, lon, 0.);

		double[][] linear = GeoTessModelUtils.getBorehole(pos, maxSpacing, 0,
				99, true, true, new int[] { 0 });

		pos = model.getGeoTessPosition(InterpolatorType.NATURAL_NEIGHBOR);

		pos.set(lat, lon, 0.);

		double[][] nn = GeoTessModelUtils.getBorehole(pos, maxSpacing, 0, 99,
				true, true, new int[] { 0 });

		System.out.println("Java borehole test\n");

		System.out.println("model = " + modelName + "\n");

		System.out.printf("Lat, Lon = %1.3f, %1.3f%n%n", lat, lon);

		System.out.println("    Depth    Vp linear     Depth     Vp NN");
		System.out.println("    (km)      (km/sec)     (km)     (km/sec)");
		for (int i = 0; i < nn.length; ++i)
			System.out.printf("%10.3f %10.3f %10.3f %10.3f%n", linear[i][0],
					linear[i][1], nn[i][0], nn[i][1]);

		System.out.println("\nDone.");

	}

	// TODO added test
	/**
	 * Test the ability to call GeoTessPosition.setModel() and get the right
	 * answer.
	 * <ul>
	 * <li>Load all 54 libcorr3d models in alphabetic order.
	 * <li>loop over the models, create a new GeoTessPosition object and
	 * interpolate values at N closely spaced points.
	 * <li>Create a common GeoTessPosition object.
	 * <li>loop over the models again and use the common position object to
	 * interpolate the same values at the same locations.
	 * <li>compare the results and the time required to compute each.
	 * <ul>
	 * 
	 * @throws GeoTessException
	 * @throws IOException
	 */
	protected void positionTest() throws GeoTessException, IOException
	{
		int layid = 0;

		// create a bunch of points near the surface of the earth.
		// lat,lon = -2,97 is in the Sumatra Trench.
		double lat = -2;
		double lon = 97;
		double radius = 6360.;

		// make 20 points, and have them be very close together, but not
		// identical.
		double[][] points = new double[20][3];
		for (int i = 0; i < points.length; ++i)
			points[i] = VectorGeo.getVector(lat -= 0.001, lon += 0.001);

		InterpolatorType interp = InterpolatorType.NATURAL_NEIGHBOR;

		// load all the models from disk
		TreeMap<String, GeoTessModel> modelset = new TreeMap<String, GeoTessModel>();
		for (File m : new File(".").listFiles())
			if (m.isFile() && m.getName().endsWith("geotess")
					&& !m.getName().contains("grid"))
			{
				// System.out.println(m.getName());
				modelset.put(m.getName(), new GeoTessModel(m));
			}

		// copy the models to an array list ordered by model name.
		ArrayList<GeoTessModel> models = new ArrayList<GeoTessModel>(
				modelset.values());

		// create an array list of positions, one for each model.
		ArrayList<GeoTessPosition> positions = new ArrayList<GeoTessPosition>(
				models.size());

		for (GeoTessModel model : models)
			positions.add(model.getGeoTessPosition(interp));

		GeoTessPosition pos = null, gtp = models.get(0).getGeoTessPosition(
				interp);

		// array to hold radii and values
		double[][][] values = new double[points.length][models.size()][6];

		long[] t = new long[2];

		for (int n = 0; n < 100; ++n)
		{
			// run 0 uses separate position objects, run 1 uses same position
			// object.
			for (int run = 0; run < 2; ++run)
			{
				if (run == 1)
					pos = gtp;

				t[run] = System.nanoTime();
				// loop over points.
				for (int i = 0; i < points.length; ++i)
				{
					double[][] vals = values[i];

					if (run == 1)
						// set pos to points[i]
						pos.set(points[i], radius);

					// loop over models
					for (int j = 0; j < models.size(); ++j)
					{
						if (run == 0)
						{
							// identify position object for this model and
							// set pos to points[i]
							pos = positions.get(j);
							pos.set(points[i], radius);
						}
						else if (run == 1)
							pos.setModel(models.get(j));

						double[] v = vals[j];
						v[0 + run] = pos.getRadiusBottom(layid);
						v[2 + run] = pos.getValue(0);
						v[4 + run] = pos.getValue(1);
					}
				}
				t[run] = System.nanoTime() - t[run];

				// System.out.printf("Timer = %6.3f millisec%n", t[run] * 1e-6);
			}
			System.out.printf("%6.3f%n", (double) t[0] / (double) t[1]);
		}

		for (int i = 0; i < points.length; ++i)
		{
			double[][] vals = values[i];
			for (int j = 0; j < vals.length; ++j)
			{
				double[] v = vals[j];
				if (v[0] != v[1]
						|| (v[2] != v[3] && !Double.isNaN(v[2]) && !Double
								.isNaN(v[3]))
						|| (v[4] != v[5] && !Double.isNaN(v[4]) && !Double
								.isNaN(v[5])))
				{
					System.out.printf("%4d %4d", i, j);
					for (int k = 0; k < v.length; ++k)
						System.out.printf(" %-20s", Double.toString(v[k]));
					System.out.println();
				}
			}
			System.out.println();
		}

	}

	// TODO added this test
	/**
	 * Load the crust2.0 model and make a vtk map of the p velocity in the upper
	 * crust using linear and NN interpolation. Also include the difference
	 * between the two which ends up looking like the continental outlines
	 * (after rescaling to +- 0.01 km/sec).
	 * 
	 * @param args
	 * @throws GeoTessException
	 * @throws IOException
	 */
	protected void mapValuesTest() throws GeoTessException, IOException
	{

		File modelFile = new File("crust20.geotess");
		GeoTessModel model = new GeoTessModel(modelFile);

		System.out.println(model);

		GeoTessPosition nn = model
				.getGeoTessPosition(InterpolatorType.NATURAL_NEIGHBOR);
		GeoTessPosition ln = model.getGeoTessPosition(InterpolatorType.LINEAR);

		// TODO jim: don't have to create these directories in c++ so long as we
		// always run java first.
		File dir = new File("mapValuesTest");
		dir.mkdirs();
		File cppDir = new File(dir, "cpp");
		cppDir.mkdir();
		File javaDir = new File(dir, "java");
		javaDir.mkdir();

		String[] attributes = new String[] { "vp_linear_(km/sec",
				"vp_nn_(km/sec)", "linear - nn" };

		int nlat = 911, nlon = 181;
		double dlat = 180. / (nlat - 1);
		double dlon = 360. / (nlon - 1);
		double depth = -10;

		double[] lat = new double[nlat];
		double[] lon = new double[nlon];
		double[][][] vp = new double[nlon][nlat][attributes.length];

		for (int j = 0; j < nlat; ++j)
			lat[j] = -90. + j * dlat;

		for (int i = 0; i < nlon; ++i)
			lon[i] = -180. + i * dlon;

		long timer = 0;
		;
		for (int layer = 3; layer < 4; ++layer)
		{

			File vtkFile = new File(javaDir, String.format("mapValues_%s.vtk",
					model.getMetaData().getLayerNames()[layer]));

			long t = System.nanoTime();
			for (int j = 0; j < nlat; ++j)
			{
				for (int i = 0; i < nlon; ++i)
				{
					ln.set(layer, lat[j], lon[i], depth);
					nn.set(layer, lat[j], lon[i], depth);
					vp[i][j][0] = ln.getValue(0);
					vp[i][j][1] = nn.getValue(0);
					vp[i][j][2] = vp[i][j][1] - vp[i][j][0];
				}
			}
			timer += System.nanoTime() - t;

			vtk(lon, lat, vp, attributes, vtkFile);

			System.out.println(vtkFile.getCanonicalPath());
		}
		System.out.printf("%nTime spent interpolating data %1.3f seconds%n%n",
				timer * 1e-9);
		System.out.println("\nDone.");
	}

	// TODO added this method to generate vtk file.
	/**
	 * Generate vtk file for a map on a lat-lon grid.
	 * 
	 * @param x
	 *            longitude values in degrees
	 * @param y
	 *            latitude values in degrees
	 * @param v
	 *            nlon x nlat x nattributes
	 * @param attributes
	 *            String[] names of attributes
	 * @param vtkFile
	 * @throws IOException
	 */
	static public void vtk(double[] x, double[] y, double[][][] v,
			String[] attributes, File vtkFile) throws IOException
	{
		int ni = x.length, nj = y.length, j0, j1;

		DataOutputStream output = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(vtkFile)));

		output.writeBytes(String.format("# vtk DataFile Version 2.0%n"));
		output.writeBytes(String.format("NCNS_Velocity_model%n"));
		output.writeBytes(String.format("BINARY%n"));

		output.writeBytes(String.format("DATASET UNSTRUCTURED_GRID%n"));

		output.writeBytes(String.format("POINTS %d float%n", ni * nj));

		// iterate over all the grid vertices and write out their position
		for (int j = 0; j < nj; ++j)
			for (int i = 0; i < ni; ++i)
			{
				output.writeFloat((float) x[i]);
				output.writeFloat((float) y[j]);
				output.writeFloat(0F);

			}

		int nCells = (x.length - 1) * (y.length - 1);
		output.writeBytes(String.format("CELLS %d %d%n", nCells, nCells * 5));
		for (int j = 0; j < y.length - 1; ++j)
		{
			j0 = j * ni;
			j1 = (j + 1) * ni;
			for (int i = 0; i < x.length - 1; ++i)
			{
				output.writeInt(4);
				output.writeInt(j0 + i);
				output.writeInt(j0 + i + 1);
				output.writeInt(j1 + i);
				output.writeInt(j1 + i + 1);
			}
		}

		output.writeBytes(String.format("CELL_TYPES %d%n", nCells));
		for (int t = 0; t < nCells; ++t)
			output.writeInt(8); // VTK_VOXEL = 11

		output.writeBytes(String.format("POINT_DATA %d%n", ni * nj));

		for (int k = 0; k < attributes.length; ++k)
		{

			output.writeBytes(String.format("SCALARS %s float 1%n",
					attributes[k].replaceAll(" ", "_")));
			output.writeBytes(String.format("LOOKUP_TABLE default%n"));

			for (int j = 0; j < nj; ++j)
				for (int i = 0; i < ni; ++i)
					output.writeFloat((float) v[i][j][k]);
		}
		output.close();
	}

	/**
	 * 
	 * @param args
	 * @throws GeoTessException
	 * @throws IOException
	 */
	protected void loopTest() throws GeoTessException, IOException
	{
		File gridFile = new File("geotess_grid_04000.geotess");

		// create c++ and java subdirectories where models will be output.
		// If it is difficult for c++ to do this, that is ok. I can be done
		// here and everything will be fine so long as java is run first.
		File loopTestDir = new File("loopTest");
		loopTestDir.mkdirs();
		File outDir = new File(loopTestDir, "cpp");
		outDir.mkdirs();
		outDir = new File(loopTestDir, "java");
		outDir.mkdirs();

		// test the ability to read the grid id from a file without
		// reading the whole file. This has nothing to do with loopTest
		// but is too simple to deserve a test of its own.
		String gridid = GeoTessGrid.getGridID(gridFile.getCanonicalPath());
		System.out.println(gridid);

		int nPoints = 10000;

		int testNumber = 0;

		System.out.printf("loopTest  nPoints=%d%n%n", nPoints);
		System.out.println("Average interpolation time in microseconds\n");

		InterpolatorType[] interps = new InterpolatorType[] {
				InterpolatorType.LINEAR, InterpolatorType.NATURAL_NEIGHBOR };

		for (double delta : new double[] { -1., 0., 1e-3, 17, -999999. })
		{
			System.out
					.println("==================================================");
			System.out.printf("Delta = %1.3f degrees%n%n", delta);

			if (delta == -1.)
				System.out
						.println("Locations randomly distributed over the globe\n");
			else if (delta == 0.)
				System.out.println("Locations exactly colocated\n");
			else if (delta == 1.e-3)
				System.out.println("Locations very, very close together\n");
			else if (delta == 17)
				System.out
						.println("Locations 17 degrees apart along the equator\n");
			else if (Double.isNaN(delta))
			  System.out.println("Locations all coincide with grid nodes\n");
			else if (delta == 1.e-3)
			  System.out.println("delta is not recognized.\n");

			System.out.printf("%-8s %9s %9s%n", " ", interps[0], interps[1]);

			for (InterpolatorType interpType : interps)
			{

			  GeoTessGrid grid = new GeoTessGrid(gridFile);

			  GeoTessModel model = syntheticModel(grid);

			  GeoTessPosition x = model.getGeoTessPosition(interpType);

			  double timer = timeTest(x, nPoints, delta);

			  System.out.printf(" %9.3f", timer * 1e6);

			  model.writeModel(
			      new File(outDir, String.format("loopTest_%02d.geotess",
			          testNumber++)), gridFile.getName());
			}
			System.out.println();
		}
		System.out.println("Done.");
	}

	/**
	 * 
	 * @param x
	 * @param nPoints
	 * @param delta
	 * @return
	 * @throws GeoTessException
	 */
	private double timeTest(GeoTessPosition x, int nPoints, double delta)
			throws GeoTessException
	{
		double lat = 0, lon = 0;
		double[] u = new double[3];
		int layer = 0;

		long timer = System.nanoTime();
		if (delta == -999999.)
		{
			double[][] vertices = x.getModel().getGrid().getVertices();
			int i = 0, vertex = 0;
			while (i < nPoints)
			{
				if (vertex == vertices.length)
					vertex = 0;
				x.set(layer, vertices[vertex++], 5500.);
				x.getValue(0);
				++i;
			}
		}
		else if (delta < 0.)
			for (int i = 0; i < nPoints; ++i)
			{
				// move position to a random location
				randomize(u);
				x.set(layer, u, 5500);
				x.getValue(0);
			}
		else
			for (int i = 0; i < nPoints; ++i)
			{
				// move delta distance along the equator
				lon += delta;
				x.set(layer, lat, lon, 6371. - 5500);
				x.getValue(0);
			}
		timer = System.nanoTime() - timer;
		return (timer * 1e-9) / nPoints;
	}

	/**
	 * 
	 * @param u
	 */
	private void randomize(double[] u)
	{
		for (int i = 0; i < 3; ++i)
			u[i] = rand.nextDouble() - 0.5;
		VectorUnit.normalize(u);
	}

	/**
	 * 
	 * @param args
	 * @throws GeoTessException
	 * @throws IOException
	 */
	protected void compareCPPFiles() throws GeoTessException, IOException
	{
		File javaDir = new File("bigTest/java");
		File cppDir = new File("bigTest/cpp");
		ArrayList<String> files = new ArrayList<String>(100);

		for (File javaFile : javaDir.listFiles())
			if (javaFile.isFile())
			{
				files.add(javaFile.getCanonicalPath());
			}
		Collections.sort(files);

		for (String s : files)
		{
			File javaFile = new File(s);
			System.out.printf("%-22s", javaFile.getName());
			File cppFile = new File(cppDir, javaFile.getName());
			if (!cppFile.exists())
				System.out.println("cpp file does not exist");
			else
			{
				GeoTessModel javamodel = new GeoTessModel(javaFile, "../..");

				GeoTessModel cppmodel = new GeoTessModel(cppFile, "../..");

				System.out.println(javamodel.equals(cppmodel) ? "EQUAL"
						: "NOT EQUAL");
			}

		}
	}

	/**
	 * Big test. Create a model that has all 6 ProfileTypes represented. For
	 * every DataType, and for nAttributes = 1 and 2 (12 combinations) generate
	 * a model. For each output format (nc, ascii and geotess) write the model to a
	 * file, read the model back into a new Model object and compare the loaded
	 * model with the original model. If they are not equal, report that they
	 * are not equal.
	 * 
	 * @param args
	 * @throws GeoTessException
	 * @throws IOException
	 */
	protected void bigTest() throws GeoTessException, IOException
	{
		File gridFile = new File("geotess_grid_04000.geotess");

		File dir = new File("bigTest/cpp");
		dir.mkdirs();

		dir = new File("bigTest/java");
		dir.mkdirs();

		// load the grid that will be used for all models generated during this
		// test.
		GeoTessGrid grid = new GeoTessGrid(gridFile);

		// specify some radii that will be used
		float radiusBottom = 6000F;
		float radiusTop = 6300F;
		float[] radii = new float[] { 6000F, 6100F, 6200F, 6300F };

		// build an array of Profiles that are independent of any model.
		// These profiles have no Data since the data will be replaced below.
		// They just have a ProfileType and some radii.
		Profile[][] profiles = new Profile[grid.getNVertices()][1];
		for (int vertex = 0; vertex < grid.getNVertices(); ++vertex)
		{
			// loop over the vertices. Get the latitude of the vertex and
			// determine the ProfileType based on the latitude. This will
			// results in 5 bands of different ProfileTypes.
			double lat = VectorGeo.getLatDegrees(grid.getVertex(vertex));

			int band = (int) Math.floor((lat + 90) / 180. * 4.999999);

			ProfileType pType = ProfileType.values()[band];

			switch (pType)
			{
			case CONSTANT:
				profiles[vertex][0] = new ProfileConstant(radiusBottom,
						radiusTop, null);
				break;
			case EMPTY:
				profiles[vertex][0] = new ProfileEmpty(radiusBottom, radiusTop);
				break;
			case NPOINT:
				profiles[vertex][0] = new ProfileNPoint(radii, getDoubles(lat,
						radii.length, 1));
				break;
			case SURFACE:
				profiles[vertex][0] = new ProfileSurface(null);
				break;
			case THIN:
				profiles[vertex][0] = new ProfileThin(radiusTop, null);
				break;
			default:
				throw new GeoTessException(band
						+ " is not a recognized ProfileType");
			}

		}

		boolean testPassed = true;

		HashMap<String, Long> timers = new HashMap<String, Long>();
		//timers.put("nc", 0L);
		timers.put("ascii", 0L);
		timers.put("geotess", 0L);

		// for each data type (double, float, etc) and for single values or
		// array of values
		for (DataType dataType : DataType.values())
			//if (dataType != DataType.LONG)
				for (int nAttributes = 1; nAttributes <= 2; ++nAttributes)
				{
					// figure out the filen name of this test.
					String name = (nAttributes > 1 ? "ArrayOf"
							+ dataType.toString() + "s" : dataType.toString())
							.toLowerCase();

					// set up the metadata for the model
					GeoTessMetaData md = new GeoTessMetaData();
					md.setDescription("name");
					md.setLayerNames("testLayer");
					md.setDataType(dataType);
					if (nAttributes == 1)
						md.setAttributes("value1", "na");
					else
						md.setAttributes("value1;value2", "na1;na2");
					md.setModelSoftwareVersion("TestAll 1.0.0");
					md.setModelGenerationDate(new Date().toString());

					// build the model with the specified DataType, single value
					// or array.
					GeoTessModel model = new GeoTessModel(grid, md);

					// Specify the Profiles in the model. These are references
					// to the
					// independent Profiles defined outside this loop, only with
					// the
					// Data objects replaced.
					for (int vertex = 0; vertex < model.getNVertices(); ++vertex)
					{
						double[] u = model.getGrid().getVertex(vertex);
						double value = u[0] * u[1] * u[2];
						value = value > 0 ? 1-value : -1-value;

						Profile p = profiles[vertex][0];
						switch (dataType)
						{
						case DOUBLE:
							profiles[vertex][0].setData(getDoubles(value,
									p.getNData(), nAttributes));
							break;
						case FLOAT:
							profiles[vertex][0].setData(getFloats(value,
									p.getNData(), nAttributes));
							break;
						case LONG:
							profiles[vertex][0].setData(getLongs(value,
									p.getNData(), nAttributes));
							break;
						case INT:
							profiles[vertex][0].setData(getInts(value,
									p.getNData(), nAttributes));
							break;
						case SHORT:
							profiles[vertex][0].setData(getShorts(value,
									p.getNData(), nAttributes));
							break;
						case BYTE:
							profiles[vertex][0].setData(getBytes(value,
									p.getNData(), nAttributes));
							break;
						}
						// set the Profile in the model to a reference to the
						// computed value.
						model.setProfile(vertex, 0, p);
					}

					// now for each output file format write the model to a
					// file,
					// read in the model back in to a new model, and compare
					// them.
					for (String frmt : new String[] { "geotess", "ascii" /*, "nc" */ })
					{
						// figure out the file name for the output file
						File outFile = new File(dir, String.format("%s.%s",
								name, frmt));

						// if (!outFile.getName().equals("arrayoflongs.geotess"))
						// continue;

						// print the output file name, with no end-of-file
						System.out.printf("%-20s ", outFile.getName());

						try
						{
							// save the model to file
							model.writeModel(outFile, gridFile.getName());

							long timer = System.nanoTime();

							// read the model back in
							GeoTessModel test_model = new GeoTessModel(outFile, "../");

							timer = System.nanoTime() - timer;
							timers.put(frmt, timers.get(frmt) + timer);

							// compare the models
							boolean equal = test_model.equals(model);
							if (!equal)
								testPassed = false;

							System.out.println(equal ? "equal" : "NOT EQUAL");
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						// System.out.println(test_model);

					}
				}
		System.out.println();
		for (String frmt : timers.keySet())
			System.out.printf("%-8s %9.6f seconds%n", frmt,
					timers.get(frmt) * 1e-9);

		System.out.println();
		System.out.println(testPassed ? "bigTest PASSED" : "bigTest FAILED");

	}

	/**
	 * Get either a DataDouble[nPoints] or DataArrayOfDoubles[nPoints] where the
	 * array has 2 elements. The values are random values between -1 and 1.
	 * 
	 * @param npoints
	 * @param nattributes
	 * @return
	 */
	private Data[] getDoubles(double value, int npoints, int nattributes)
	{
		Data[] data = new Data[npoints];
		for (int i = 0; i < npoints; i++)
			data[i] = nattributes == 1 ? new DataDouble(value)
					: new DataArrayOfDoubles(new double[] { value, value });
		return data;
	}

	/**
	 * Get either a DataFloat[nPoints] or DataArrayOfFloats[nPoints] where the
	 * array has 2 elements. The values are random values between -1 and 1.
	 * 
	 * @param npoints
	 * @param nattributes
	 * @return
	 */
	private Data[] getFloats(double value, int npoints, int nattributes)
	{
		Data[] data = new Data[npoints];
		for (int i = 0; i < npoints; i++)
			data[i] = nattributes == 1 ? new DataFloat((float) value)
					: new DataArrayOfFloats(new float[] { (float) value,
							(float) value });
		return data;
	}

	/**
	 * Get either a DataLong[nPoints] or DataArrayOfLongs[nPoints] where the
	 * array has 2 elements. The values are random values between min_value and
	 * max_value.
	 * 
	 * @param npoints
	 * @param nattributes
	 * @return
	 */
	private Data[] getLongs(double value, int npoints, int nattributes)
	{
		Data[] data = new Data[npoints];
		long val = (long) (value * Long.MAX_VALUE);
		for (int i = 0; i < npoints; i++)
			data[i] = nattributes == 1 ? new DataLong(val)
					: new DataArrayOfLongs(new long[] { val, val });
		return data;
	}

	/**
	 * Get either a DataInt[nPoints] or DataArrayOfIntss[nPoints] where the
	 * array has 2 elements. The values are random values between min_value and
	 * max_value.
	 * 
	 * @param npoints
	 * @param nattributes
	 * @return
	 */
	private Data[] getInts(double value, int npoints, int nattributes)
	{
		Data[] data = new Data[npoints];
		int val = (int) (value * Integer.MAX_VALUE);
		for (int i = 0; i < npoints; i++)
			data[i] = nattributes == 1 ? new DataInt(val)
					: new DataArrayOfInts(new int[] { val, val });
		return data;
	}

	/**
	 * Get either a DataShort[nPoints] or DataArrayOfShorts[nPoints] where the
	 * array has 2 elements. The values are random values between min_value and
	 * max_value.
	 * 
	 * @param npoints
	 * @param nattributes
	 * @return
	 */
	private Data[] getShorts(double value, int npoints, int nattributes)
	{
		Data[] data = new Data[npoints];
		short val = (short) (value * Short.MAX_VALUE);
		for (int i = 0; i < npoints; i++)
			data[i] = nattributes == 1 ? new DataShort(val)
					: new DataArrayOfShorts(new short[] { val, val });
		return data;
	}

	/**
	 * Get either a DataByte[nPoints] or DataArrayOfBytes[nPoints] where the
	 * array has 2 elements. The values are random values between min_value and
	 * max_value.
	 * 
	 * @param npoints
	 * @param nattributes
	 * @return
	 */
	private Data[] getBytes(double value, int npoints, int nattributes)
	{
		Data[] data = new Data[npoints];
		byte val = (byte) (value * Byte.MAX_VALUE);
		for (int i = 0; i < npoints; i++)
			data[i] = nattributes == 1 ? new DataByte(val)
					: new DataArrayOfBytes(new byte[] { val, val });
		return data;
	}

	/**
	 * Build a synthetic model filled with random data. The model has 4 layers,
	 * each of a different ProfileType. Layer 0 is NPOINT with 25 radii ranging
	 * from 5000 to 6000 km radius (40 km spacing). Layer 1 is CONSTANT with
	 * radii at 6000 and 6100. Layer 2 is THIN with radius 6100. Layer 3 is
	 * EMPTY.
	 * <p>
	 * All layers have data of type DataArryaOfFloats with nAttributes = 2.
	 * 
	 * @param grid
	 * @return
	 * @throws GeoTessException
	 * @throws IOException 
	 */
	private GeoTessModel syntheticModel(GeoTessGrid grid)
			throws GeoTessException, IOException
	{
		ProfileType[] pTypes = new ProfileType[] { ProfileType.NPOINT,
				ProfileType.CONSTANT, ProfileType.THIN };

		int nLayers = pTypes.length;

		// generate the radii in the layers.
		float[][] radii = new float[nLayers][];
		float r = 5000F;
		radii[0] = new float[26];
		float dr = 1000F / (radii[0].length - 1);
		for (int i = 0; i < radii[0].length; ++i)
		{
			radii[0][i] = r;
			r += dr;
		}

		radii[1] = new float[2];
		radii[1][0] = radii[0][radii[0].length - 1];
		radii[1][1] = radii[1][0] + 100;
		radii[2] = new float[1];
		radii[2][0] = radii[1][radii[1].length - 1];

		// set up the metadata for the model
		GeoTessMetaData md = new GeoTessMetaData();
		md.setDescription("name");
		md.setLayerNames("npoint constant thin".split(" "));
		md.setDataType("float");
		md.setAttributes("value1;value2", "na1;na2");
		md.setModelSoftwareVersion("GeoTessJava syntheticModel()");
		md.setModelGenerationDate(new Date().toString());

		// build the model with the specified DataType, single value or array.
		GeoTessModel model = new GeoTessModel(grid, md);

		for (int vertex = 0; vertex < grid.getNVertices(); ++vertex)
		{
			double[] u = model.getGrid().getVertex(vertex);
			double value = u[0] * u[1] * u[2];

			for (int layer = 0; layer < nLayers; ++layer)
			{
				Profile p = null;
				switch (pTypes[layer])
				{
				case NPOINT:
					p = new ProfileNPoint(radii[0], getFloats(value,
							radii[0].length, 2));
					break;
				case CONSTANT:
					p = new ProfileConstant(radii[1][0], radii[1][1],
							getFloats(value, 1, 2)[0]);
					break;
				case THIN:
					p = new ProfileThin(radii[2][0], getFloats(value, 1, 2)[0]);
					break;
				default:
					throw new GeoTessException(layer
							+ " is not a recognized ProfileType");
				}
				model.setProfile(vertex, layer, p);
			}
		}

		return model;
	}

	/**
	 * @param args
	 * @throws GeoTessException
	 * @throws IOException
	 */
	protected void testGridIndexes() throws GeoTessException, IOException
	{
		File gridFile = new File("geotess_grid_04000.geotess");
		GeoTessGrid grid = new GeoTessGrid(gridFile);

		System.out.println(grid);

		int n = 0;
		for (int tess = 0; tess < grid.getNTessellations(); ++tess)
			for (int level = 0; level < grid.getNLevels(tess); ++level)
				n += grid.getNTriangles(tess, level);
		if (n != grid.getNTriangles())
			System.out.printf("test1: counted %d triangles but grid has %d%n",
					n, grid.getNTriangles());
		else
			System.out.println("passed test 1");

		n = 0;
		for (int tess = 0; tess < grid.getNTessellations(); ++tess)
			for (int level = 0; level < grid.getNLevels(tess); ++level)
				n += grid.getLastTriangle(tess, level)
						- grid.getFirstTriangle(tess, level) + 1;
		if (n != grid.getNTriangles())
			System.out.printf("test2: counted %d triangles but grid has %d%n",
					n, grid.getNTriangles());
		else
			System.out.println("passed test 2");

		HashSet<Integer> triangles = new HashSet<Integer>(grid.getNTriangles());
		for (int tess = 0; tess < grid.getNTessellations(); ++tess)
			for (int level = 0; level < grid.getNLevels(tess); ++level)
				for (int t = grid.getFirstTriangle(tess, level); t <= grid
						.getLastTriangle(tess, level); ++t)
					triangles.add(t);
		if (triangles.size() != grid.getNTriangles())
			System.out.printf("test3: counted %d triangles but grid has %d%n",
					triangles.size(), grid.getNTriangles());
		else
			System.out.println("passed test 3");
	}

	protected void testInterpolationNaN() throws GeoTessException, IOException
	{
		String modelName = "WRA.geotess";

		GeoTessModel model = new GeoTessModel(modelName);

		double lat = -2;
		double lon = 97;

		GeoTessPosition pos = model.getGeoTessPosition(InterpolatorType.LINEAR);

		pos.set(0, lat, lon, 10.);

		System.out.printf("pos.set(0, lat, lon, 10.)  = %8.2f%n",
				pos.getValue(0));

		pos.set(lat, lon, 10.);

		System.out.printf("pos.set(lat, lon, 10.)     = %8.2f%n",
				pos.getValue(0));

		pos.set(0, lat, lon, -10.);

		System.out.printf("pos.set(0, lat, lon, -10.) = %8.2f%n",
				pos.getValue(0));

		pos.set(lat, lon, -10.);

		System.out.printf("pos.set(lat, lon, -10.)    = %8.2f%n",
				pos.getValue(0));

		System.out.println("\nDone.");

	}

	/**
	 * Compute the circumcenters of all triangles at the top level of the
	 * tessellation that supports the specified layer.
	 * 
	 * @param model
	 * @param layer
	 */
	protected void computeAllCircumcenters(GeoTessModel model, int layer)
	{
		GeoTessGrid grid = model.getGrid();
		int tessid = model.getMetaData().getTessellation(layer);
		int level = grid.getTopLevel(tessid);

		for (int t = grid.getFirstTriangle(tessid, level); t <= grid
				.getLastTriangle(tessid, level); ++t)
			grid.getCircumCenter(t);
	}

	/**
	 * Compute the weights of model points arrayed along a great circle
	 * path through a model.  Sum  of the weights should be very close
	 * to equal to the length of the great circle path.
	 * @throws IOException
	 * @throws GeoTessException
	 */
	protected void getWeightsTest() throws IOException, GeoTessException
	{
		String modelName = "unified_crust20_ak135_110926_profiles.geotess";

		GeoTessModel model = new GeoTessModel(modelName);
		
		//System.out.println(model);		
		
		// get the origin of the ray path
		double[] u = VectorGeo.getVectorDegrees(20., 90.);
		
		int n = 100;
		// construct raypth: unit vectors and radii.
		ArrayList<double[]> v = new ArrayList<double[]>();
		//ArrayList<Double> r = new ArrayList<Double>();
		double[] r = new double[n];

		// add a bunch of points along a great circle path at 
		// a constant radius.
		v.clear();
		//r.clear();
		double[][] gc = VectorUnit.getGreatCircle(u, Math.PI/2);
		double len = Math.PI/6 /(n-1.);
		double radius = 5350;
		for (int i=0; i<n; ++i)
		{
			v.add(VectorUnit.getGreatCirclePoint(gc, i* len));
			//r.add(radius);
			r[i] = radius;
		}
		
		// get weights from the model.
		HashMap<Integer, Double> w = new HashMap<Integer, Double>();
		model.getWeights(v, r, null, InterpolatorType.LINEAR, InterpolatorType.LINEAR, w);
		
		// print out the weights, the locations of the points.
		System.out.println("Pt Index    weight   layer     lat         lon    depth");
		double sum=0;
		for (Map.Entry<Integer, Double> e : w.entrySet())
		{
			sum += e.getValue();
			System.out.printf("%6d  %10.4f   %3d %s%n", e.getKey(), e.getValue(),  
					model.getPointMap().getLayerIndex(e.getKey()),
					model.getPointMap().toString(e.getKey()));
			
		}
		
		// compute the length of the great circle path in km.
		System.out.printf("%nActual length of great circle path = %1.4f km%n%n", (n-1)*len*radius);
		
		// sum of the weights should equal length of great circle path.
		System.out.printf("Size = %d   Sum weights = %1.4f km%n", w.size(), sum);
		

		
		System.out.println("\n\nnow test method GeoTessPosition.getCoefficients() with linear interpolation\n");
		System.out.println(" pointIndex  coefficient");
		
		GeoTessPosition pos = model.getGeoTessPosition(InterpolatorType.LINEAR);
		pos.set(30., 90., 2000.);
		
		HashMap<Integer, Double> coefficients = pos.getCoefficients();
		
		sum = 0;
		for (Integer pointIndex : coefficients.keySet())
		{
			sum += coefficients.get(pointIndex);
			System.out.printf("%8d %13.6f%n", pointIndex, coefficients.get(pointIndex));
		}
		System.out.printf("\nSum of coefficients = %1.12f (should equal 1.)%n", sum);
		
		
		
		System.out.println("\n\nnow test method GeoTessPosition.getCoefficients() with natural neighbor interpolation\n");
		System.out.println(" pointIndex  coefficient");
		
		pos = model.getGeoTessPosition(InterpolatorType.NATURAL_NEIGHBOR);
		pos.set(30., 90., 2000.);
		
		coefficients = pos.getCoefficients();
		
		sum = 0;
		for (Integer pointIndex : coefficients.keySet())
		{
			sum += coefficients.get(pointIndex);
			System.out.printf("%8d %13.6f%n", pointIndex, coefficients.get(pointIndex));
		}
		System.out.printf("\nSum of coefficients = %1.12f (should equal 1.)%n", sum);
		
	}

}
