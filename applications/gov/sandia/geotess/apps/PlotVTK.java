package gov.sandia.geotess.apps;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.numerical.polygon.GreatCircle;

import java.io.File;

public class PlotVTK {

	public static void main(String[] args) 
	{
		try 
		{
			File outputDir = new File("\\\\tonto2\\GNEM\\devlpool\\sballar\\GMP_testing\\models\\smoothingMatrix");
			
			File inputFile = new File("\\\\tonto2\\gnem\\devlpool\\jrhipp\\TestModelCorrelation\\ModelCorrelation\\2015_08_11_covarianceConstraint_Test_A\\smoothing\\aPrioriRowDiagResultsModel.geotess");
			
			File vtkFile = new File(outputDir, "equator.vtk");
			
			GeoTessModel model = new GeoTessModel(inputFile);
			
			System.out.println(model);
			System.out.println(GeoTessModelUtils.statistics(model));
			
			GreatCircle gc = new GreatCircle(new double[] {1,0,0}, 2*Math.PI,  Math.PI/2);
			
			GeoTessModelUtils.vtkSlice(model, vtkFile.getAbsolutePath(), gc, 360, 50, 
					2, 4, InterpolatorType.LINEAR, InterpolatorType.LINEAR, false, new int[] {0});
			
			vtkFile = new File(outputDir, "map_upper_mantle.vtk");
			
			GeoTessModelUtils.vtkRobinson(model, vtkFile, 12, 100, 4, 99, false, InterpolatorType.LINEAR, false, null);
			
			vtkFile = new File(outputDir, "map_transition_zone.vtk");
			
			GeoTessModelUtils.vtkRobinson(model, vtkFile, 12, 410+125, 3, 99, false, InterpolatorType.LINEAR, false, null);
			
			vtkFile = new File(outputDir, "map_lower_mantle.vtk");
			
			GeoTessModelUtils.vtkRobinson(model, vtkFile, 12, 800, 2, 99, false, InterpolatorType.LINEAR, false, null);
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
