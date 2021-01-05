package gov.sandia.geotess.extensions.libcorr3d;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.PointMap;
import gov.sandia.gmp.util.containers.arraylist.ArrayListInt;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.numerical.vector.VectorGeo;
import gov.sandia.gmp.util.numerical.vector.VectorUnit;
import gov.sandia.gmp.util.profiler.Profiler;

public class LibCorr3DJavaTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		LibCorr3DJavaTest test = new LibCorr3DJavaTest();
		
		try
		{
			if (args.length == 0)
				throw new Exception("must specify command line argument");

			if (args[0].equalsIgnoreCase("test"))
				test.test(args);
			else if (args[0].equalsIgnoreCase("profileTest"))
				test.profileTest(args);
			else if (args[0].equalsIgnoreCase("interogatePoint"))
				test.interogatePoint(args);
			else if (args[0].equalsIgnoreCase("testIOFunctions"))
				test.testIOFunctions(args);
			else
				throw new Exception(args[0] + " is not a recognized test.");

			System.out.println("Done.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void testIOFunctions(String[] args) throws Exception
	{
		String tess = "../../tess";
		
		LibCorr3DModel model = new LibCorr3DModel(new File("models3D/ak135/tt/ABKAR_P_TT_2004084_2286324"), tess);
		
		// save the model in both binary and ascii formats, read them back in 
		// and ensure they are still equal.
		for (String ext : new String[] {"binary", "ascii"})
		{
			File modelFile = new File("IOTest/java/ABKAR_P_TT_2004084_2286324."+ext);

			model.writeModel(modelFile);

			LibCorr3DModel newModel = new LibCorr3DModel(modelFile);

			System.out.println(newModel.equals(model));
		}

		// load the models written by java and c++ and ensure they are equal.
		for (String ext : new String[] {"binary", "ascii"})
		{
			LibCorr3DModel javaModel = new LibCorr3DModel("IOTest/java/ABKAR_P_TT_2004084_2286324."+ext);
			LibCorr3DModel cppModel = new LibCorr3DModel("IOTest/c++/ABKAR_P_TT_2004084_2286324."+ext);

			System.out.println(javaModel.equals(cppModel));
		}

	}

	protected void interogatePoint(String[] args) throws IOException
	{
		File modelFile = new File(args[1]);
		
		LibCorr3DModel model = new LibCorr3DModel(modelFile);
		
		System.out.println(model);
		
		PointMap pm = model.getPointMap();
		
		for (int i=0; i< 100; ++i)
		System.out.printf("%s %8.3f %8.3f %8.3f%n", 
				pm.getPointLatLonString(i),
				pm.getPointDepth(i),
				pm.getPointValue(i, 0),
				pm.getPointValue(i, 1));
	}

	/**
	 * Make a bunch of points and grab a bunch of stations from a libcorr3d object.
	 * Extract path corrections in two ways.  First, use a single instance of
	 * libcorr3d and loop over all points and all stations.  
	 * Second, for each station get a new instance of libcorr3d and loop 
	 * over all the points and get path corrections.  The first method
	 * is going to call GeoTessPosition.setMode() a lot, changing the 
	 * model on the fly.  The two methods should give the same result.
	 * @param args
	 * @throws GeoTessException
	 * @throws IOException
	 */
	public boolean profileTest(String[] args) throws GeoTessException, IOException
	{
		File libCorrDir = new File(args[1]);

		LibCorr3D libcorr = new LibCorr3D(libCorrDir, "../../tess", true, 
				InterpolatorType.LINEAR, InterpolatorType.LINEAR);

		ArrayListInt lookupIndexes = new ArrayListInt(libcorr.getLibCorrModels().getNSiteNames());
		for (String sta : libcorr.getLibCorrModels().getSupportedSites().keySet())
		{
			Site station = libcorr.getLibCorrModels().getSupportedSites().get(sta).get(0);
			lookupIndexes.add(libcorr.getLookupIndex(station, "P", "TT_DELTA_AK135"));
		}

		// here are some points that are centers of triangles
		// where all three nodes are == 700 km depth
		// and which are separated from each other by at least
		// 10 degrees:
		ArrayList<double[]> deepPoints = new ArrayList<double[]>();
		deepPoints.add(VectorGeo.getVectorDegrees( 28.457094,  140.072878));
		deepPoints.add(VectorGeo.getVectorDegrees( 48.522777,  143.521194));
		deepPoints.add(VectorGeo.getVectorDegrees( 52.110013,  178.079973));
		deepPoints.add(VectorGeo.getVectorDegrees( 55.052448,  159.711891));
		deepPoints.add(VectorGeo.getVectorDegrees(  2.366276,   99.466293));
		deepPoints.add(VectorGeo.getVectorDegrees(  2.702832,  121.648595));
		deepPoints.add(VectorGeo.getVectorDegrees( 26.202460,  125.235534));
		deepPoints.add(VectorGeo.getVectorDegrees( -4.537735,  108.000000));
		deepPoints.add(VectorGeo.getVectorDegrees( -7.922699,  119.580607));
		deepPoints.add(VectorGeo.getVectorDegrees(-12.461138,  172.889248));
		deepPoints.add(VectorGeo.getVectorDegrees(-19.987492,  180.000000));
		deepPoints.add(VectorGeo.getVectorDegrees( -6.252635,  150.128639));
		deepPoints.add(VectorGeo.getVectorDegrees( 18.074561,  144.000000));
		deepPoints.add(VectorGeo.getVectorDegrees( -6.654458,  160.261870));
		deepPoints.add(VectorGeo.getVectorDegrees(-19.532224,  -65.769749));
		deepPoints.add(VectorGeo.getVectorDegrees(-34.161155,  176.960077));

		double[] earthRadius = new double[deepPoints.size()];
		for (int i=0; i<deepPoints.size(); ++i)
			earthRadius[i] = VectorGeo.getEarthRadius(deepPoints.get(i));

		double[] depths = new double[] {0, 0, 0, 0, 0, 300, 300, 400, 500, 600, 700, 700, 700};

		//		for (int ntimes=0; ntimes < 10000; ++ntimes)
		//			for (int x=0; x<deepPoints.size(); ++x)
		//				for (int z=0; z<depths.length; ++z)
		//					for (int i=0; i<lookupIndexes.size(); ++i)
		//						libcorr.getPathCorrection(lookupIndexes.get(i), deepPoints.get(x), earthRadius[x]-depths[z]);
		//		

		double lat = 28.457094;
		double lon = 140.072878;
		ArrayList<double[]> points = new ArrayList<double[]>();
		for (int x=0; x < 1000; ++x)
		{
			lat += 1e-4;
			points.add(VectorGeo.getVectorDegrees(lat, lon));
		}

		Profiler profiler = new Profiler(Thread.currentThread(), 1, "GeoTessPosition profile");

		//profiler.setTopClass("gov.sandia.gmp.libcorr3djava.LibCorr3D");

		//profiler.setTopMethod("getPathCorrection");

		profiler.accumulateOn();

		for (int ntimes=0; ntimes < 1000000; ++ntimes)
			for (int x=0; x < points.size(); ++x)
				libcorr.getPathCorrection(lookupIndexes.get(0), points.get(x), 6000.);

		profiler.accumulateOff();

		profiler.printAccumulationString();

		return true;

	}

	/**
	 * Make a bunch of points and grab a bunch of stations from a libcorr3d object.
	 * Extract path corrections in two ways.  First, use a single instance of
	 * libcorr3d and loop over all points and all stations.  
	 * Second, for each station get a new instance of libcorr3d and loop 
	 * over all the points and get path corrections.  The first method
	 * is going to call GeoTessPosition.setMode() a lot, changing the 
	 * model on the fly.  The two methods should give the same result.
	 * @param args
	 * @throws GeoTessException
	 * @throws IOException
	 */
	public boolean test(String[] args) throws GeoTessException, IOException
	{
		File libCorrDir = new File(args[1]);
		String gridDir = ".";
		if (args.length > 2)
			gridDir = args[2];

		int nPoints = 100;

		int nStations = 20;

		ArrayList<double[]> points = new ArrayList<double[]>(nPoints);

		for (int i=0; i<nPoints; ++i)
			points.add(VectorGeo.getVectorDegrees(30. + i/(double)nPoints, 90. + i/(double)nPoints));

		LibCorr3D libcorr = new LibCorr3D(libCorrDir, gridDir, false, 
				InterpolatorType.NATURAL_NEIGHBOR, InterpolatorType.LINEAR);

		// find nStations in libcorr models that are within 70 degrees of the points.
		ArrayList<Site> stations = new ArrayList<>(nStations); 
		ArrayList<Integer> lookupIndices = new ArrayList<Integer>(nStations);

		for (String sta : libcorr.getLibCorrModels().getSupportedSites().keySet())
		{
			Site station = libcorr.getLibCorrModels().getSupportedSites().get(sta).iterator().next();
			if (VectorUnit.angleDegrees(points.get(nPoints/2), station.getUnitVector()) < 70)
			{
				stations.add(station);
				lookupIndices.add(libcorr.getLookupIndex(station, "P", "TT_DELTA_AK135"));
			}
			if (stations.size() == nStations)
				break;
		}

		double[][][] values = new double[2][stations.size()][points.size()];

		// for each point, loop over the stations and get path correction value.
		// This will cause a call to GeoTessPosition.setModel() to be made for each 
		// model without changing the location of the point.
		System.out.println("Start timer");
		long timer = System.nanoTime();
		for (int j=0; j<points.size(); ++j)
			for (int i=0; i<lookupIndices.size(); ++i)
				values[0][i][j] = libcorr.getPathCorrection(lookupIndices.get(i), points.get(j), 6371.-i);
		timer = System.nanoTime()-timer;
		System.out.printf("Timer %10.6f seconds%n%n", timer*1e-9);

		// for each station instantiate a brand new instance of LibCorr3D.  
		// then loop over the points and get the path correction.  There 
		// are no calls to GeoTessPosition.setModel() in this approach.
		System.out.println("Start timer");
		timer = System.nanoTime();
		for (int i=0; i<lookupIndices.size(); ++i)
		{
			libcorr = new LibCorr3D(libCorrDir, ".", false,  
					InterpolatorType.NATURAL_NEIGHBOR, InterpolatorType.LINEAR);
			for (int j=0; j<points.size(); ++j)
				values[1][i][j] = libcorr.getPathCorrection(lookupIndices.get(i), points.get(j), 6371.-i);
		}
		timer = System.nanoTime()-timer;
		System.out.printf("Timer %10.6f seconds%n%n", timer*1e-9);

		int nErrors = 0;
		for (int j=0; j<points.size(); ++j)
			for (int i=0; i<stations.size(); ++i)
				if (values[0][i][j] != values[1][i][j])
				{
					++nErrors;
					System.out.printf("%3d %3d %7.3f %7.3f %7.3f%n",
							i, j, values[0][i][j], values[1][i][j],
							values[0][i][j] - values[1][i][j]);
				}

		System.out.printf("Detected %d errors.\n\n", nErrors);

		return nErrors == 0;
	}
}
