package gov.sandia.geotess.apps;

import gov.sandia.geotess.Data;
import gov.sandia.geotess.GeoTessMetaData;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.geotess.GeoTessUtils;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.numerical.vector.EarthShape;

import java.io.File;
import java.util.Date;

public class Uncertainty_1D
{
	

	/**
	 * Generate a 2D geotessmodel and a vtk map of 1D distant dependent uncertainty
	 * for a single station.
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Start simple example");
			System.out.println();

			// Create a MetaData object in which we can specify information
			// needed for model contruction.
			GeoTessMetaData metaData = new GeoTessMetaData();
			
			// ILAR is located at 64.771446,-146.886454
			// MJAR is located at 36.524717,138.24718
			double[] station = EarthShape.WGS84.getVectorDegrees(36.524717,138.24718);

			// Specify a description of the model. This information is not
			// processed in any way by GeoTess. It is carried around for
			// information purposes.
			metaData.setDescription(String .format(
					"Travel time uncertainty for station MJAR (lat, lon = 36.524717,138.24718) from " +
					"1D distance dependent uncertainty model ak135 at 0 and 200 km depth%n"));

			// Specify a list of layer names. A model could have many layers,
			// e.g., ("core", "mantle", "crust"), specified in order of
			// increasing radius. This simple example has only one layer.
			metaData.setLayerNames("surface");
			
			// specify the names of the attributes and the units of the
			// attributes in two String arrays. This model only includes
			// one attribute.
			// If this model had two attributes, they would be specified 
			// like this: setAttributes("Distance; Depth", "degrees; km");
			metaData.setAttributes("TT_UNCERTAINTY_0_km; TT_UNCERTAINTY_200_km", "seconds; seconds");

			// specify the DataType for the data. All attributes, in all
			// profiles, will have the same data type.
			metaData.setDataType(DataType.FLOAT);
			
			// specify the name of the software that is going to generate
			// the model.  This gets stored in the model for future reference.
			metaData.setModelSoftwareVersion("gov.sandia.geotess.apps.Uncertainty_1D 1.0.0");
			
			// specify the date when the model was generated.  This gets 
			// stored in the model for future reference.
			metaData.setModelGenerationDate(new Date().toString());

			// specify the path to the file containing the grid to be used for
			// this test.  This information was passed in as a command line
			// argument.  Grids were included in the software delivery and
			// are available from the GeoTess website.
			String gridFile = "C:\\Users\\sballar\\git\\geo-tess-java\\resources\\permanent_files\\geotess_grid_01000.geotess";

			// call a GeoTessModel constructor to build the model. This will
			// load the grid, and initialize all the data structures to null.
			// To be useful, we will have to populate the data structures.
			GeoTessModel model = new GeoTessModel(gridFile, metaData);

			// generate some data and store it in the model. The data consists
			// of the angular distance in degrees from each vertex of the model
			// grid to station ANMO near Albuquerque, NM, USA.
			for (int vtx = 0; vtx < model.getGrid().getNVertices(); ++vtx)
			{
				// retrieve the unit vector corresponding to the i'th vertex of
				// the grid.
				double[] vertex = model.getGrid().getVertex(vtx);

//				Data data = Data.getDataFloat((float)getUncertainty(
//						        GeoTessUtils.angleDegrees(station, vertex)));
				
				// associate the Data object with the specified vertex of the model.  
				// This instance of setProfile always creates a ProfileSurface object.
				model.setProfile(vtx, Data.getDataFloat(getUncertainty_ak135(GeoTessUtils.angleDegrees(station, vertex))));
			}

			// At this point, we have a fully functional GeoTessModel object
			// that we can work with.

			// print a bunch of information about the model to the screen.
			System.out.println(model.toString());
			
			model.writeModel("uncertainty_ak135.geotess");
			
			GeoTessModelUtils.vtkRobinson(model, new File("uncertainty_ak135.vtk"), 
					model.getEarthShape().getLonDegrees(station), 0, 0, true, InterpolatorType.LINEAR, false, null);

			System.out.printf("%nDone%n%n");

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}
	
	static protected float[] getUncertainty_ak135(double xdeg)
	{
		float[] u = new float[2];
		if (xdeg > 95) // ak135[ak135.length-1][0])
			return new float[] {Float.NaN, Float.NaN};
		
		for (int i=0; i<ak135.length-1; ++i)
			if (xdeg >= ak135[i][0] && xdeg <= ak135[i+1][0])
			{
				u[0] = (float) (ak135[i][1] + (ak135[i+1][1]-ak135[i][1])
						* (xdeg-ak135[i][0])/(ak135[i+1][0]-ak135[i][0]));
				u[1] = (float) (ak135[i][2] + (ak135[i+1][2]-ak135[i][2])
						* (xdeg-ak135[i][0])/(ak135[i+1][0]-ak135[i][0]));
			}
		
		return u;
	}
	
	static protected double getUncertainty_rstt(double xdeg)
	{
		if (xdeg > distance[distance.length-1])
			return Double.NaN;
		
		for (int i=0; i<distance.length-1; ++i)
			if (xdeg >= distance[i] && xdeg <= distance[i+1])
				return uncertainty[i] + (uncertainty[i+1]-uncertainty[i])
						* (xdeg-distance[i])/(distance[i+1]-distance[i]);
		
		return Double.NaN;
	}
	
	// 1D distance dependent uncertainty for model rstt.2.3 TT Pn.
	static final double[][] ak135 = new double[][]
	{
		{0, 0.1, 0.7}, 
		{0.2, 0.5, 0.7}, 
		{0.5, 1, 0.7}, 
		{1, 1.5, 1}, 
		{2, 2.1, 1.5}, 
		{3, 2.4, 1.7}, 
		{4, 2.6, 1.8}, 
		{8, 2.7, 1.9}, 
		{12, 2.9, 2}, 
		{15, 3.6, 2.5}, 
		{17, 3, 2.1}, 
		{19, 2.1, 1.4}, 
		{25, 1.4, 1}, 
		{33, 1.4, 1}, 
		{37, 1.3, 0.9}, 
		{49, 1.3, 0.9}, 
		{57, 1.2, 0.8}, 
		{65, 1.1, 0.8}, 
		{79, 1.1, 0.8}, 
		{83, 1.2, 0.9}, 
		{91, 1.3, 1.1}, 
		{93, 1.4, 1.2}, 
		{97, 1.4, 1.2}, 
		{101, 2, 1.9}, 
		{104, 2.3, 2.2}, 
		{109, 2.6, 2.4}, 
		{114, 1.4, 1.2}, 
		{119, 1.4, 1.2}, 
		{121, 1.3, 1.1}, 
		{129, 1.3, 1.1}, 
		{137, 2.3, 2.2}, 
		{139, 3.3, 3.3}, 
		{142, 3.7, 3.7}, 
		{145, 1.8, 1.5}, 
		{180, 1.8, 1.5}
	};

	// 1D distance dependent uncertainty for model rstt.2.3 TT Pn.
	static final double[] distance = new double[]
	{
		0,
		0.2,
		0.5,
		1.5,
		2.5,
		3.5,
		4.5,
		5.5,
		6.5,
		7.5,
		8.5,
		9.5,
		10.5,
		11.5,
		12.5,
		13.5,
		14.5,
		17,
		19,
		25,
		33,
		37,
		49,
		57,
		65,
		79,
		83,
		91,
		93,
		97
		};
	
	static final double[] uncertainty = new double[]
		{
		1.3,
		1.3,
		1.3,
		1.666,
		1.319,
		1.197,
		1.142,
		1.253,
		1.246,
		1.363,
		1.299,
		1.274,
		1.188,
		1.182,
		1.125,
		1.271,
		1.471,
		1.448,
		1.424,
		1.4,
		1.4,
		1.3,
		1.3,
		1.2,
		1.1,
		1.1,
		1.2,
		1.3,
		1.4,
		1.4
		};
}
