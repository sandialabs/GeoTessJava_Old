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
