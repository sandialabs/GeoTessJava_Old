//- ****************************************************************************
//-
//- Copyright 2009 Sandia Corporation. Under the terms of Contract
//- DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
//- retains certain rights in this software.
//-
//- BSD Open Source License.
//- All rights reserved.
//-
//- Redistribution and use in source and binary forms, with or without
//- modification, are permitted provided that the following conditions are met:
//-
//-    * Redistributions of source code must retain the above copyright notice,
//-      this list of conditions and the following disclaimer.
//-    * Redistributions in binary form must reproduce the above copyright
//-      notice, this list of conditions and the following disclaimer in the
//-      documentation and/or other materials provided with the distribution.
//-    * Neither the name of Sandia National Laboratories nor the names of its
//-      contributors may be used to endorse or promote products derived from
//-      this software without specific prior written permission.
//-
//- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//- AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//- IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//- ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
//- LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
//- CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
//- SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
//- INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
//- CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//- ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
//- POSSIBILITY OF SUCH DAMAGE.
//-
//- ****************************************************************************

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
