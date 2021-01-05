package gov.sandia.geotess.apps;

import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.gmp.util.numerical.vector.Vector3D;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GreatCirclesVTK
{

	static double centerLon;
	
	public static void main(String[] args)
	{
		try
		{
			centerLon = -100; //Double.parseDouble(args[1]);
			File inputFile, outputFile, dir = new File(args[0]);
			
			inputFile = new File(dir, "raypaths.dat");
			outputFile = new File(dir, "raypaths.vtk");
			raypaths(inputFile, outputFile);
			
			inputFile = new File(dir, "stations.dat");
			outputFile = new File(dir, "stations.vtk");
			GeoTessModelUtils.vtkRobinsonPoints(outputFile, inputFile, centerLon);

			inputFile = new File(dir, "events.dat");
			outputFile = new File(dir, "events.vtk");
			points(inputFile, outputFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void points(File inputFile, File outputFile)
	{
		try
		{
			Scanner input = new Scanner(inputFile);
			ArrayList<double[]> points = new ArrayList<double[]>();
			while (input.hasNext())
			{
				Scanner line = new Scanner(input.nextLine());
				try
				{
					double[] u = Vector3D.getVectorDegrees(line.nextDouble(), line.nextDouble());
					points.add(u);
				} 
				catch(Exception ex) { }
			}
			
			System.out.println("n stations = "+points.size());
			GeoTessModelUtils.vtkRobinsonPoints(centerLon, points, outputFile);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void raypaths(File inputFile, File outputFile)
	{
		try
		{
			Scanner input = new Scanner(inputFile);
			ArrayList<double[]> greatCircles = new ArrayList<double[]>();
			while (input.hasNext())
			{
				Scanner line = new Scanner(input.nextLine());
				try
				{
					double[] u = Vector3D.getVectorDegrees(line.nextDouble(), line.nextDouble());
					double[] v = Vector3D.getVectorDegrees(line.nextDouble(), line.nextDouble());
					greatCircles.add(u);
					greatCircles.add(v);
				} 
				catch(Exception ex) { }
			}
			
			System.out.println("count = "+greatCircles.size());
			GeoTessModelUtils.vtkRobinsonGreatCircle(outputFile, centerLon, greatCircles);
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
