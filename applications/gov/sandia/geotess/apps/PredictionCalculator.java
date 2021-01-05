package gov.sandia.geotess.apps;

import gov.sandia.geotess.Data;
import gov.sandia.geotess.DataArrayOfFloats;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.geotess.GeoTessUtils;
import gov.sandia.geotess.PointMap;
import gov.sandia.geotess.Profile;
import gov.sandia.geotess.ProfileNPoint;
import gov.sandia.geotess.ProfileThin;
import gov.sandia.gmp.util.globals.DataType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class PredictionCalculator
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			new PredictionCalculator().outputLocations();
			
			new PredictionCalculator().populateModel(args);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void outputLocations() throws Exception
	{
		GeoTessModel model = new GeoTessModel(
				"T:\\LibCorr3D\\depth_model\\pedal_depth_model.bin");
		
		PointMap pointMap = model.getPointMap();
		
		BufferedWriter output = new BufferedWriter(new FileWriter(new File(
				"T:\\LibCorr3D\\depth_model\\pedal_depth_model_all_points.dat")));
		
		output.append("point_index  origin_lat origin_lon origin_depth phase\n");
		// ASAR = -23.66513,133.90526,0.6273
		double[] asar = model.getEarthShape().getVectorDegrees(-23.66513,133.90526);
		
//		for (int vertex=0; vertex < model.getNVertices(); ++vertex)
//		{
//			int point = pointMap.getPointIndexLast(vertex, 0);
//			double distance = GeoTessUtils.angleDegrees(asar, pointMap.getPointUnitVector(point));
//			if (distance < 96)
//			output.write(String.format("%d %s %1.6f %s%n",
//			 point, pointMap.getPointLatLonString(point), pointMap.getPointDepth(point),
//			 	distance <= 15 ? "Pn" : "P"));
//		}
		
		for (int point=0; point<pointMap.size(); ++point)
		{
			double distance = GeoTessUtils.angleDegrees(asar, pointMap.getPointUnitVector(point));
			//if (distance < 96)
			output.write(String.format("%d %s %1.6f %s%n",
			 point, pointMap.getPointLatLonString(point), pointMap.getPointDepth(point),
			 	distance <= 15 ? "Pn" : "P"));
		}
		output.close();

	}

	protected void populateModel(String[] args) throws Exception
	{
		GeoTessModel model = new GeoTessModel(
				"T:\\LibCorr3D\\depth_model\\pedal_depth_model.bin");
		
		PointMap pmap = model.getPointMap();
		
		model.getMetaData().setDescription(
				"This model represents the seismicity layer of the Earth. \n"
						+"The horizontal grid is uniform 0.5 degree triangles. \n"
						+"The top of the model is the topographic/bathymetric surface from ETOPO1. \n"
						+"The bottom of the model is deeper than the deepest seismic event \n"
						+"recorded in the REB catalog.  In aseismic areas and areas where only  \n"
						+"surface events have been recorded, the model is comprised of ProfileThin \n"
						+"profiles, characterized by a single radius and singe data object.  In \n"
						+"areas where deep seismicity has been recorded, the model is comprised \n"
						+"of ProfileNPoint profiles that extend 50 km deeper than the deepest  \n"
						+"event ever recorded. \n"
						+" \n"
						+"SALSA3D travel times were computed with salsa3d.1.8.bin."
				);
		
		model.getMetaData().setDataType(DataType.FLOAT);
		model.getMetaData().setAttributes("TT_SALSA; TT_AK135; TT_DELTA_AK135", "seconds; seconds; seconds");
		
		for (int vertex=0; vertex<model.getNVertices(); ++vertex)
		{
			Profile p = model.getProfile(vertex, 0);
			float[] radii = p.getRadii();
			Data[] data = new Data[radii.length];
			for (int i=0; i<radii.length; ++i)
				data[i] = new DataArrayOfFloats(Float.NaN, Float.NaN, Float.NaN);
			
			if (radii.length == 1)
				p = new ProfileThin(radii[0], data[0]);
			else 
				p = new ProfileNPoint(radii, data);
			
			model.setProfile(vertex, 0, p);
		}
			
		
		Scanner in = new Scanner(new File("T:\\LibCorr3D\\depth_model\\pedal_depth_model_pcalc_ilar_all_ak135.out"));
		
		in.nextLine(); // header;
		
		while (in.hasNext())
		{
			int index = in.nextInt();
			in.nextDouble();
			in.nextDouble();
			in.nextDouble();
			in.next();
			double tt = in.nextDouble();
			double slow = in.nextDouble();
			pmap.setPointValue(index, 1, tt < -1e3 ? Float.NaN : tt);
		}
		in.close();
		
		in = new Scanner(new File("T:\\LibCorr3D\\depth_model\\pedal_depth_model_pcalc_ilar_all_salsa3d.out"));
		
		in.nextLine(); // header;
		
		while (in.hasNext())
		{
			int index = in.nextInt();
			in.nextDouble();
			in.nextDouble();
			in.nextDouble();
			in.next();
			double tt = in.nextDouble();
			if (tt < 0) tt = Double.NaN;
			double slow = in.nextDouble();
			pmap.setPointValue(index, 0, tt);
			pmap.setPointValue(index, 2, tt-pmap.getPointValue(index, 1));
		}
		in.close();
		
		model.writeModel("T:\\LibCorr3D\\depth_model\\pedal_depth_model_ilar.bin", "*");
		
		GeoTessModelUtils.vtk(model, "T:\\LibCorr3D\\depth_model\\pedal_depth_model_ilar.vtk", 0, false, null);
		
		System.out.println("Done.");
	}

}
