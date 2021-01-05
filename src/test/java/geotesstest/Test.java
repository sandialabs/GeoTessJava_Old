package geotesstest;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.geotess.GeoTessPosition;
import gov.sandia.geotess.Profile;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.numerical.polygon.GreatCircle;
import gov.sandia.gmp.util.numerical.polygon.Polygon;
import gov.sandia.gmp.util.numerical.vector.VectorGeo;
import gov.sandia.gmp.util.numerical.vector.VectorUnit;
import gov.sandia.gmp.util.profiler.Profiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Test
{

	Random random = new Random(0);

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			if (args[0].equalsIgnoreCase("ttlookup"))
				new Test().ttlookup(args);
			else if (args[0].equalsIgnoreCase("profiler1"))
				new Test().profiler1(args);
			else if (args[0].equalsIgnoreCase("profiler2"))
				new Test().profiler2(args);
			else if (args[0].equalsIgnoreCase("stats"))
				new Test().stats(args);
			else if (args[0].equalsIgnoreCase("loopPoints"))
				new Test().loopPoints(args);
			else if (args[0].equalsIgnoreCase("getWeights"))
				new Test().getWeights(args);
			else if (args[0].equalsIgnoreCase("debug"))
				new Test().debug(args);
			else if (args[0].equalsIgnoreCase("nnTest"))
				new Test().nnTest(args);
            else if (args[0].equalsIgnoreCase("polygonTest"))
              new Test().polygonTest(args);
            else if (args[0].equalsIgnoreCase("equal"))
              new Test().equal();
			else
				throw new Exception(args[0]+" is not a recognized command.");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
    protected void equal() throws IOException
    {
      File modelFiles = new File("/Users/sballar/Documents/salsa3d/model_files.txt");
      Scanner input = new Scanner(modelFiles);

      GeoTessModel main = new GeoTessModel("/Users/sballar/Documents/salsa3d/from_website/SALSA3D.geotess");
      
      Set<File> equalSet = new LinkedHashSet<>();
      
      while (input.hasNext())
      {
        File f = new File(input.nextLine());
        
        try {
          GeoTessModel m = new GeoTessModel(f);
          if (m.equals(main))
          {
            System.out.printf("EQUAL!  %s%n", f.getAbsoluteFile());
            equalSet.add(f);
          }
          else
            System.out.printf("        %s%n", f.getAbsoluteFile());
          
          m = null;
          
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      input.close();
      
      System.out.println("\n\nEqualSet:\n");
      for (File f : equalSet)
        System.out.println(f.getAbsolutePath());
    }
    
    protected void equal1() throws IOException
    {
      File directory = new File("/Volumes/GNEM/devlpool/sballar/GMP_testing/models");
      //directory = new File("/Users/sballar/Documents/salsa3d");
      Set<File> models = new LinkedHashSet<File>();
      searchModels(directory, models);
      System.out.printf("found %d models%n%n", models.size());
      for (File f : models)
        System.out.println(f.getAbsolutePath());
      
      GeoTessModel main = new GeoTessModel("/Users/sballar/Documents/salsa3d/from_website/SALSA3D.geotess");
      for (File f : models)
      {
        try {
          GeoTessModel m = new GeoTessModel(f);
          if (m.equals(main))
            System.out.printf("EQUAL!  %s%n", f.getAbsoluteFile());
          else
            System.out.printf("        %s%n", f.getAbsoluteFile());
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

    }
	private void searchModels(File directory, Set<File> models)
	{
	  for (String s : new String[] {"adaptive_gridding", "lsinv"})
	  if (directory.getName().endsWith(s))
	    return;

	  System.out.printf("Searching directory %s%n", directory.getAbsolutePath());
	  for (File f : directory.listFiles())
	    if (f.isDirectory())
	      searchModels(f, models);
	    else if (f.getName().endsWith("geotess"))
	      models.add(f);
	}
	
	protected void polygonTest(String[] args) throws Exception
	{
		Polygon polygon = new Polygon(new File("T:\\GeoTessBuilder\\david_yang\\eurasia.kmz"));
		
		double[] u = VectorGeo.getVectorDegrees(65,  165);
		
		System.out.printf("%b%n", polygon.contains(u));
		
	}

	protected void nnTest(String[] args) throws Exception
	{
		GeoTessModel model = new GeoTessModel(args[1]);
		
		System.out.println(model);
		
		double[] u = VectorUnit.center(model.getGrid().getTriangleVertices(208));
		
		double lat = VectorGeo.getLatDegrees(u);
		double lon = VectorGeo.getLonDegrees(u)+8;
		
		VectorGeo.getVectorDegrees(lat, lon, u);
		
		System.out.println(VectorGeo.getLatLonString(u));
		
		GeoTessPosition pos = model.getGeoTessPosition(InterpolatorType.NATURAL_NEIGHBOR, InterpolatorType.LINEAR);
		
		pos.set(0, u, 6371.);
		
		System.out.println(pos.getValue(0));
		
	}
	
	protected void getWeights(String[] args) throws Exception
	{
		GeoTessModel crust = new GeoTessModel(new File(new File(args[1]), "crust20.geotess"));
		
		HashMap<Integer, Double> w = new HashMap<Integer, Double>();
		HashMap<Integer, Double> wnn = new HashMap<Integer, Double>();
		
		for (int deg=5; deg<=170; deg += 5)
		{
			GreatCircle gc = new GreatCircle(new double[] {1,0,0}, Math.toRadians(deg), 0.);
			
			ArrayList<double[]> rayPath = gc.getPoints(deg*3, false);
			
			double[] radii = new double[rayPath.size()];
			Arrays.fill(radii, 1e4);
			
			crust.getWeights(rayPath, radii, null, InterpolatorType.LINEAR, InterpolatorType.LINEAR, w);
			
			crust.getWeights(rayPath, radii, null, InterpolatorType.NATURAL_NEIGHBOR, InterpolatorType.LINEAR, wnn);
			
			System.out.printf("%6d %10.4f %10.4f%n", deg, w.size()/(double)deg, wnn.size()/(double)deg);
		}
	}
	
//	protected void getWeights(String[] args) throws Exception
//	{
//		GeoTessModel crust = new GeoTessModel(new File(new File(args[1]), "crust20.geotess"));
//		
//		ArrayList<Double> radii = new ArrayList<Double>(1000);
//		
//		for (int deg=5; deg<=170; deg += 5)
//		{
//			GreatCircle gc = new GreatCircle(new double[] {1,0,0}, Math.toRadians(deg), 0.);
//			
//			ArrayList<double[]> rayPath = gc.getPoints(deg*3, false);
//			
//			while (radii.size() < rayPath.size()) radii.add(10000.);
//			
//			HashMap<Integer, Double> w = crust.getWeights(rayPath, radii, InterpolatorType.LINEAR, InterpolatorType.LINEAR);
//			
//			HashMap<Integer, Double> wnn = crust.getWeights(rayPath, radii, InterpolatorType.NATURAL_NEIGHBOR, InterpolatorType.LINEAR);
//			
//			System.out.printf("%6d %10.4f %10.4f%n", deg, w.size()/(double)deg, wnn.size()/(double)deg);
//		}
//	}
	
	protected void loopPoints(String[] args) throws Exception
	{
		GeoTessModel model = new GeoTessModel(args[1]);
		
		for (int vertexId : new int[] {0, 12, 42})
		{
			double[] u = model.getGrid().getVertex(vertexId);
			
			double lat = VectorGeo.getLatDegrees(u);
			double lon = VectorGeo.getLonDegrees(u);
			double earthRadius = VectorGeo.getEarthRadius(u);
			
			System.out.printf("Vertex: %d lat=%1.3f lon=%1.3f R=%1.3f%n%n", vertexId, lat, lon, earthRadius);
			
			System.out.print("layerId layer_name       profile_type nodeId   radius");
			for (int attributeId=0; attributeId<model.getNAttributes(); ++attributeId)
				System.out.printf(" %10s", String.format("%s(%s)", 
						model.getMetaData().getAttributeNames()[attributeId],
						model.getMetaData().getAttributeUnits()[attributeId]));
			System.out.println();	
			
			for (int layerId=model.getNLayers()-1; layerId>=0; --layerId)
			{
				Profile profile = model.getProfile(vertexId, layerId);
				
				for (int z = profile.getNRadii()-1; z >= 0; --z)
				{
					System.out.printf("%7d %-16s %-12s", 
							layerId, 
							model.getMetaData().getLayerNames()[layerId], 
							profile.getType());
					
					System.out.printf(" %6d %8.3f", z, profile.getRadius(z));
					
					for (int attributeId = 0; attributeId< model.getNAttributes(); ++attributeId)
						System.out.printf(" %10.3f", profile.getValue(attributeId, z));
					
					System.out.println();
				}
				System.out.println();
			}
		}
				
	}
	
	protected void stats(String[] args) throws Exception
	{
		GeoTessModel model = new GeoTessModel(args[1]);
		
		System.out.println(model.toString());
		
		System.out.println(GeoTessModelUtils.statistics(model));
		
	}
	
	protected void debug(String[] args) throws Exception
	{
		GeoTessModel model = new GeoTessModel("T:\\LibCorr3D\\depth_model\\pedal_depth_model_2.bin");
		
		GeoTessPosition pos = model.getGeoTessPosition(InterpolatorType.LINEAR);
		
		double lat = 30, lon = 90;
		
		//double[] v = VectorGeo.getVectorDegrees(lat, lon);
		
		pos.set(0, lat, lon, 0.);
		
		int index = pos.getIndexOfClosestVertex();
		
		double[] u = model.getGrid().getVertex(index);
		
		System.out.printf("lat = %1.4f  lon = %1.4f%n", VectorGeo.getLatDegrees(u),
				VectorGeo.getLonDegrees(u));
		
		u = pos.getClosestVertex();
		
		System.out.printf("lat = %1.4f  lon = %1.4f%n", VectorGeo.getLatDegrees(u),
				VectorGeo.getLonDegrees(u));
		
	}

	protected void profiler2(String[] args) throws IOException, GeoTessException
	{
		int nTimes = Integer.parseInt(args[1]);
		
		double lat = Math.toRadians(40.);
		
		VectorGeo.setApproximateLatitudes(false);
		
		long timer = System.nanoTime();

		for (int j=0; j<nTimes; ++j)
			for (int i=0; i<nTimes; ++i)
				VectorGeo.getGeoCentricLatitude(lat);
		
		timer = System.nanoTime()-timer;
		
		VectorGeo.setApproximateLatitudes(true);
		
		long timer2 = System.nanoTime();

		for (int j=0; j<nTimes; ++j)
			for (int i=0; i<nTimes; ++i)
				VectorGeo.getGeoCentricLatitude(lat);
		
		timer2 = System.nanoTime()-timer2;
		
		System.out.printf("%12.9f%c %12.9f%c %8.4f%n",
				timer2*1e-9, 9, timer*1e-9, 9, 
				((double)timer)/((double)timer2));
		
		System.exit(0);
		
	}
	

	protected void profiler1(String[] args) throws IOException, GeoTessException
	{
		File modelFile = new File(args[1]);
		InterpolatorType interp = InterpolatorType.LINEAR;
		String layerName = args[4];
		
		
		GeoTessModel model = new GeoTessModel(modelFile);
		System.out.println(model);
		
		//model.writeModel(new File(modelFile.getParent(), "salsa3d.1.8.nc"), "*");
		
		int layerId = model.getMetaData().getLayerIndex(layerName);
		
		GeoTessPosition pos = model.getGeoTessPosition(interp);
		
		VectorGeo.getVectorDegrees(33, 99);
		
		Profiler profiler = new Profiler(Thread.currentThread(), 1, "GeoTessPosition profile");
		
		profiler.setTopClass("geotesstest.Test");
		
		profiler.setTopMethod("infiniteLoop");
		
		profiler.accumulateOn();
		
		infiniteLoop(pos, layerId);
		
		profiler.accumulateOff();
		
		profiler.printAccumulationString();
		
		System.exit(0);
		
	}
	
	private void infiniteLoop(GeoTessPosition pos, int layerId) throws GeoTessException
	{
		double value;
		
		ArrayList<Double> values = new ArrayList<Double>();
		
		double lat = Math.toRadians(30);
		double lon = Math.toRadians(90);
		
		double[] move = new double[] {-0.1, 0.1};
		double[] vector = new double[3];
		
		VectorGeo.setApproximateLatitudes(false);
		
		for (int i=0; i<100000000; ++i)
		{
//			double[] vector = new double[] { random.nextDouble(), random.nextDouble(), random.nextDouble() };
//			VectorGeo.normalize(vector);
			
			VectorGeo.getVector(lat, lon, vector);
			
//			pos.set(layerId, vector, 5000.);
//			value = pos.getValue(0);
//			if (values.size() < 20)
//				values.add(value);
		}
	}

	protected void ttlookup(String[] args) throws GeoTessException, IOException
	{
		File modelFile = new File(args[1]);
		File vtkDir = new File(args[2]);
		vtkDir.mkdirs();
		if (modelFile.isDirectory())
		{
			for (File f : modelFile.listFiles())
				if (f.isFile() && GeoTessModel.isGeoTessModel(f))
				{
					GeoTessModel model = new GeoTessModel(f);
					String name = f.getName();
					name = name.substring(0, name.indexOf("."))+".vtk";
					File outputFile = new File(vtkDir, name);
					System.out.println(outputFile.getCanonicalPath());

					int[] attributes = new int[model.getMetaData().getNAttributes()];
					for (int i=0; i<attributes.length; ++i)
						attributes[i]=i;

					GeoTessModelUtils.vtk(model, outputFile.getCanonicalPath(), 0, false, attributes);
				}
		}
		else if (modelFile.isFile() && GeoTessModel.isGeoTessModel(modelFile))
		{
			System.out.println(modelFile.toString());
			GeoTessModel model = new GeoTessModel(modelFile);
			String name = modelFile.getName();
			name = name.substring(0, name.indexOf("."))+".vtk";
			File outputFile = new File(vtkDir, name);
			System.out.println(outputFile.getCanonicalPath());

			int[] attributes = new int[model.getMetaData().getNAttributes()];
			for (int i=0; i<attributes.length; ++i)
				attributes[i]=i;

			GeoTessModelUtils.vtk(model, outputFile.getCanonicalPath(), 0, false, attributes);
		}

	}

}
