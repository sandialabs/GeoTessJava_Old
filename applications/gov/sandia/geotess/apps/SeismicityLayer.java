package gov.sandia.geotess.apps;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessPosition;

public class SeismicityLayer 
{
	private static GeoTessPosition model;

	/**
	 * expects 4 arguments:
	 * <ol>
	 * <li>name of geotess model
	 * <li>longitude of origin in degrees
	 * <li>latitude of origin in degrees
	 * <li>depth of origin in km.
	 * </ol>
	 * Prints out the point index of the point in the model
	 * that is closest to the origin location.
	 * @param args
	 * @throws Exception
	 */
	public static void computePointIndex(String[] args) throws Exception 
	{
		try 
		{
			SeismicityLayer seismicityLayer = new SeismicityLayer(args[0]);
			
			double lat = Double.parseDouble(args[1]);
			double lon = Double.parseDouble(args[2]);
			double depth = Double.parseDouble(args[3]);
			
			int pointIndex = seismicityLayer.getPointIndex(lat, lon, depth);
			
			System.out.println(pointIndex);
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor loads a model into memory from specified 
	 * model file name.
	 * @param modelName
	 * @throws Exception
	 */
	public SeismicityLayer(String modelName) throws Exception
	{
		model = new GeoTessModel(modelName).getGeoTessPosition();
	}
	
	/**
	 * Find the index of the model point that is closest to the specified
	 * lat, lon, depth position.
	 * @param lat geographic latitude in degrees
	 * @param lon geographic longitude in degrees
	 * @param depth depth below WGS84 ellipsoid in km.
	 * @return point index.
	 * @throws Exception
	 */
	public int getPointIndex(double lat, double lon, double depth) throws Exception
	{
		return model.set(lat, lon, depth).getClosestPoint();
	}

}
