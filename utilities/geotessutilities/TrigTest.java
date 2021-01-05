package geotessutilities;

import gov.sandia.gmp.util.numerical.vector.VectorGeo;
import gov.sandia.gmp.util.numerical.vector.VectorUnit;

import java.util.Random;

public class TrigTest
{
	/**
	 * Some code to test this class by comparing sin and cos to the versions in
	 * java.lang.Math.
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.print("Sin Cos Test -- ");
		 sincosTest();
		
		System.out.print("\ngetVector Test -- ");
		for (int i=0; i<10; ++i)
		getVectorTest(args);
	}
	
	/**
	 * call GeoTessUtils.getVector and Trig.getVector on a bunch of random 
	 * lat, lon values and find the maximum differenct in km at earth surface.
	 * 
	 *  The compute getVector a whole bunch of times and measure the time difference.
	 *  
	 * @param args 0: number of times to call getVector.
	 */
	protected static void getVectorTest(String[] args)
	{
		Random rand = new Random();
		
		double lat=1, lon=1, err, maxerr=0;
		double[] v1 = new double[3];
		double[] v2 = new double[3];
		
		for (int i=0; i<1000000; ++i)
		{
			lat = Math.PI * (rand.nextDouble()-0.5);
			lon = 2*Math.PI * (rand.nextDouble()-0.5);
			
			//System.out.printf("%14.8f %14.8f%n", Math.toDegrees(lat), Math.toDegrees(lon));
			
			VectorGeo.getVector(lat, lon, v1);
			Trig.getVector(lat, lon, v2);
			
			err = VectorUnit.angle(v1, v2) * 6371.;
			if (err > maxerr) maxerr = err;
		}
		
		//System.out.printf("Maximum error is %1.2e km at surface of the earth%n%n", maxerr);
		
		int n = args.length == 0 ? 10000000 : Integer.parseInt(args[0]);
		
		long timer1 = System.nanoTime();
		
		for (int i=0; i<n; ++i) 
			VectorGeo.getVector(lat, lon, v1);
		timer1 = System.nanoTime()-timer1;
		
		long timer2 = System.nanoTime();
		
		for (int i=0; i<n; ++i) 
			Trig.getVector(lat, lon, v1);
		
		timer2 = System.nanoTime()-timer2;
		System.out.println("GeoTessUtils (sec)      Trig (sec)    Ratio");
		System.out.printf("  %12.6f       %12.6f   %8.6f%n", 
				timer1*1e-9, timer2*1e-9, ((double)timer1)/timer2);
		
	}
	
	
	/**
	 * Compaere Trig.sin and Trig.cos to Math.sin and Math.cos
	 */
	protected static void sincosTest()
	{
		double err, maxErr = 0;
		
		int di = 1;
		
		double one = 1-1e-15;
		
		
		for (int i=-720; i<=1440; i+=di)
		{	
			double a = Math.toRadians(i) * one;
			
			err = Math.abs(Math.sin(a) - Trig.sin(a));
			
			if (err > maxErr) maxErr = err;
			
			if (err > 1e-6)
				System.out.printf("sin %4d  %8.2e%n", i, err);
		}	
		
		for (int i=-720; i<=1440; i+=di)
		{	
			double a = Math.toRadians(i) * one;
			
			err = Math.abs(Math.cos(a) - Trig.cos(a));
			
			if (err > maxErr) maxErr = err;
			
			//System.out.printf("cos %4d  %8.2e%n", i, err);

			if (err > 1e-6)
				System.out.printf("cos %4d  %8.2e%n", i, err);
		}					
		
		System.out.printf("maxError = %1.2e%n", maxErr);
		
	}
	

}
