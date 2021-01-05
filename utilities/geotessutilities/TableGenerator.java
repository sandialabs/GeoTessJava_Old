package geotessutilities;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.tan;

public class TableGenerator
{

	double xmin, xmax, dx;
	
	double[] table;
	
	public static void main(String[] args)
	{
		new TableGenerator().generateTable();
	}
	
	public void generateTable()
	{
		this.xmin = 0.;
		this.xmax = PI/2;
		
		double tolerance = 1e-8;
		
		boolean printTable = false;
		
		
		table = new double[] { function(xmin), function(xmax) };
		
		double error, maxError = Double.POSITIVE_INFINITY;
		
		dx = (xmax - xmin);

		while (maxError > tolerance)
		{
			maxError = 0;
			
			for (int i = 0; i < table.length-1; ++i)
			{
				error = abs(interpolate(i) - function((i + 0.5)*dx));
				maxError = error > maxError ? error : maxError;
			}

			if (!printTable)
				System.out.printf("%10d   %1.4e%n", table.length, maxError);

			if (maxError > tolerance)
			{
				table = new double[2 * table.length - 1];
				dx /= 2;
				for (int i = 0; i < table.length; ++i)
					table[i] = function(i * dx);
			}
		}
		
		if (printTable)
			outDouble(table);
		else
			System.out.println("\nDone.");
		
	}
	
	public double function(double x)
	{
		return atan(0.9933056199770992 * tan(x)); // geocentric
		//return atan(tan(x) / 0.9933056199770992); // geographic
		//return sin(x);
	}
	
	/**
	 * Interpolate a value from the table at index i + 0.5, i.e.,
	 * at a position halfway between x[i] and x[i+1];
	 * @param i
	 * @return
	 */
	public double interpolate(int i)
	{
		return table[i] + 0.5*(table[i+1]-table[i]);
	}
	
	@SuppressWarnings("unused")
	private static void outFloat(double[] x)
	{
		System.out.print("private static float[] table = new float[] {");
		for (int i = 0; i < x.length; ++i)
		{
			if (i % 8 == 0)
				System.out.println();
			System.out.printf("(float)%s%s", Float.toString((float) x[i]),
					i == x.length - 1 ? "};\n" : ", ");
		}
	}
	
	private static void outDouble(double[] x)
	{
		System.out.print("private static double[] table = new double[] {");
		for (int i = 0; i < x.length; ++i)
		{
			if (i % 5 == 0)
				System.out.println();
			System.out.printf("%s%s", Double.toString(x[i]),
					i == x.length - 1 ? "};\n" : ", ");
		}
	}
	
}
