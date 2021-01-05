package gov.sandia.geotess.apps;

import java.io.IOException;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessPosition;
import gov.sandia.geotess.GeoTessUtils;
import gov.sandia.geotess.Profile;
import gov.sandia.gmp.util.globals.InterpolatorType;

public class ExtractProfile
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		ExtractProfile ep = new ExtractProfile();
		
		try
		{
			ep.run(args);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	protected void run(String[] args) throws IOException, GeoTessException
	{
		GeoTessModel tomo = new GeoTessModel(
				"/Users/sballar/work/models/2012_09_05_tomo120808_3AR/start_tomo_1_6.geotess");
		
		GeoTessModel pred = new GeoTessModel(
				"/Users/sballar/work/models/2012_09_05_tomo120808_3AR/start_pred_1_6.geotess");
		
		GeoTessPosition xtomo = tomo.getGeoTessPosition(InterpolatorType.LINEAR);
		xtomo.set(40, -180, 0);
		
		int vertex = xtomo.getIndexOfClosestVertex();
		int layer = 4;
		double lat = tomo.getEarthShape().getLatDegrees(tomo.getGrid().getVertex(vertex));
		double lon = tomo.getEarthShape().getLonDegrees(tomo.getGrid().getVertex(vertex));
		
		double R = tomo.getEarthShape().getEarthRadius(tomo.getGrid().getVertex(vertex));
		
		
		Profile ptomo = tomo.getProfile(vertex, layer);
		
		for (int i=0; i<ptomo.getNRadii(); ++i)
			System.out.printf("%10.6f %10.6f%n", R-ptomo.getRadius(i), 1./ptomo.getData(i).getDouble(0));
		
		
		GeoTessPosition xpred = pred.getGeoTessPosition(InterpolatorType.LINEAR);
		xpred.set(lat, lon, 0.);
		vertex = xpred.getIndexOfClosestVertex();
		double[] v = pred.getGrid().getVertex(vertex);
		System.out.printf("%10.6f %10.6f %10.6f %10.6f%n", lat, lon, 
				tomo.getEarthShape().getLatDegrees(v), tomo.getEarthShape().getLonDegrees(v));
		
		Profile ppred = pred.getProfile(vertex, layer);
		
		for (int i=0; i<ppred.getNRadii(); ++i)
			System.out.printf("%10.6f %10.6f%n", R-ppred.getRadius(i), 1./ppred.getData(i).getDouble(0));
		

	}

}
