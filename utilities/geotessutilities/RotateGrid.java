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

import gov.sandia.geotess.GeoTessGrid;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.gmp.util.numerical.vector.VectorGeo;
import gov.sandia.gmp.util.numerical.vector.VectorUnit;

import java.io.File;

public class RotateGrid
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			String inputFile = "H:\\workspace\\GeoTessModels\\geotess_grid_%05d";
			String outputFile = "H:\\workspace\\GeoTessModels\\rotated_grid_%05d";
			int edge = 64000;
			
			for (int i=0; i<8; ++i)
			{
				System.out.println(i);
				
				double angle = edge/2000.;
				GeoTessGrid grid = new GeoTessGrid().loadGrid(new File(String.format(inputFile+".bin", edge)));

				GeoTessModelUtils.vtkGrid(grid, String.format(inputFile+".vtk", edge));

				double[] pole = VectorUnit.center(grid.getTriangleVertices(0));
				
				System.out.println(VectorGeo.getLatLonString(pole));
				
				for (double[] vertex : grid.getVertices())
					VectorUnit.rotate(vertex, pole, Math.toRadians(angle), vertex);
				
				grid.writeGrid(String.format(outputFile+".bin", edge));
				
				GeoTessModelUtils.vtkGrid(grid, String.format(outputFile+".vtk", edge));

				edge /= 2;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
