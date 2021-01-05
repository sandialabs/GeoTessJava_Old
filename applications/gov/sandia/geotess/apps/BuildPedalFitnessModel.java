package gov.sandia.geotess.apps;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import gov.sandia.geotess.Data;
import gov.sandia.geotess.GeoTessMetaData;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessModelUtils;
import gov.sandia.gmp.util.globals.DataType;

public class BuildPedalFitnessModel {

	public static void main(String[] args) 
	{
		try 
		{
			new BuildPedalFitnessModel().run();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected void run() throws Exception
	{
		File dir = new File("/Users/sballar/work/Pedal/pedalModels");
		
		DataInputStream input = new DataInputStream(new BufferedInputStream(
				new FileInputStream(new File(dir, "spatial_fitness_thresh_2Deg_IMS_iasp91_Emp_Pdet"))));
		int nNodes = input.readInt();
		float[] spatial_fitness_thresh = new float[nNodes];
		double min_sf = 10000;
		double max_sf = 0;			
		for (int n=0; n < nNodes; ++n)
		{
			spatial_fitness_thresh[n] = input.readFloat();
			//spatial_fitness_thresh[n] = (float) (spatialFitnessThresholdSlope * input.readFloat() + spatialFitnessThresholdBias);
			if (spatial_fitness_thresh[n] < min_sf)
				min_sf = spatial_fitness_thresh[n];
			if (spatial_fitness_thresh[n] > max_sf)
				max_sf = spatial_fitness_thresh[n];
		}
		input.close();

		GeoTessModel model = new GeoTessModel(new File(dir, "ASAR_2Deg.geotess"));
		
		if (nNodes != model.getNPoints())
			throw new Exception(String.format("nNodes != model.getNPoints()  %d !=%d", nNodes, model.getNPoints()));
		
		// Create a MetaData object in which we can specify information
		// needed for model contruction.
		GeoTessMetaData metaData = model.getMetaData();

		// Specify a description of the model. This information is not
		// processed in any way by GeoTess. It is carried around for
		// information purposes.
		metaData.setDescription(String.format(
				"Pedal spatial fitness model.  Attribute 0 is the spatial fitness threshold value at each grid point.%n"
				+ "Attribute 1 is the computed spatial fitness as a function of the current set of unassociated arrivals.%n"
				+ "Attribute 0 is fixed in the model and should not be changed.  Attribute 1 is initially NaN and is modified%n"
				+ "continuously throughout a Pedal run.%n"));

		// Specify a list of layer names. A model could have many layers,
		// e.g., ("core", "mantle", "crust"), specified in order of
		// increasing radius. This simple example has only one layer.
		metaData.setLayerNames("seismicity");
		
		// Set layerID equal to the index of the one-and-only layer 
		// in this model.
		int layerID = 0;

		// specify the names of the attributes and the units of the
		// attributes in two String arrays. This model only includes
		// one attribute.
		// If this model had two attributes, they would be specified 
		// like this: setAttributes("Distance; Depth", "degrees; km");
		metaData.setAttributes("spatial_fitness_threshold; spatial_fitness", " ; ");

		// specify the DataType for the data. All attributes, in all
		// profiles, will have the same data type.
		metaData.setDataType(DataType.FLOAT);
		
		// specify the name of the software that is going to generate
		// the model.  This gets stored in the model for future reference.
		metaData.setModelSoftwareVersion(this.getClass().getCanonicalName());
		
		// specify the date when the model was generated.  This gets 
		// stored in the model for future reference.
		metaData.setModelGenerationDate(new Date().toString());
		
		for (int pt=0; pt < nNodes; ++pt)
		{
			Data data = Data.getDataFloat(spatial_fitness_thresh[pt], Float.NaN);
			
			model.getPointMap().setPointData(pt, data);
		}

		System.out.println(model);
		
		System.out.println(GeoTessModelUtils.statistics(model));
		
		model.writeModel(new File(dir, "pedalSpatialFitnessModel_02000.geotess"));
	
	}

}
