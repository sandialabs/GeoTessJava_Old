package geotesstest;


import java.util.Date;

import gov.sandia.geotess.GeoTessMetaData;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessPosition;
import gov.sandia.geotess.examples.customdata.GeoTessModelGA;
import gov.sandia.geotess.examples.customdata.GeoAttributes;
import gov.sandia.geotess.examples.customdata.GridPointData;
import gov.sandia.geotess.examples.customdata.PhaseData;
import gov.sandia.geotess.examples.customdata.SeismicPhase;
import gov.sandia.geotess.examples.customdata.StationData;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.globals.Globals;
import gov.sandia.gmp.util.numerical.vector.VectorGeo;
import gov.sandia.gmp.util.numerical.vector.VectorUnit;

public class PopulateModelGA {

	public PopulateModelGA() {
	}

	public static void main(String[] args) 
	{
		try 
		{
			PopulateModelGA run = new PopulateModelGA();

			// build a GeoTessModel that uses CustomData.
			GeoTessModelGA m = run.buildModel();

			// for every station-phase defined in the model, print out
			// the value of all the GeoAttributes at point index 20
			int pointIndex = 25;
			System.out.printf("Point index %d is located at lat, lon, depth = %1.5f, %1.5f, %1.3f%n", 
					pointIndex,
					m.getPointMap().getPointLatitudeDegrees(pointIndex),
					m.getPointMap().getPointLongitudeDegrees(pointIndex),
					m.getPointMap().getPointDepth(pointIndex));
			
			for (String station : m.getStationNames())
				for (SeismicPhase phase : m.getPhaseNames())
					for (GeoAttributes attribute : m.getAttributes())
					{
						int attributeIndex = m.getAttributeIndex(station, phase, attribute);
						System.out.printf("%6s %6s %6s %10.2f%n", station, phase, attribute.toString(), 
								m.getPointMap().getPointValueDouble(pointIndex, attributeIndex));
					}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Build a geotess model that uses CustomData
	 * @return
	 * @throws Exception
	 */
	protected GeoTessModelGA buildModel() throws Exception
	{
		// Specify all the stations, phases, and GeoAttributes that 
		// will be included in the model.  Only a subset of the stations
		// and phases will actually be defined at each grid point, but
		// these lists include all that will be included.
		String[] stations = new String[] {"ABC", "XYZ"};
		SeismicPhase[] phases = new SeismicPhase[] {SeismicPhase.P, SeismicPhase.S};
		GeoAttributes[] attributes = new GeoAttributes[] 
				{GeoAttributes.TT, GeoAttributes.AZ, GeoAttributes.SH};

		// Create a MetaData object in which we can specify information
		// needed for model construction.
		GeoTessMetaData metaData = new GeoTessMetaData();

		// specify the name of the software that is going to generate
		// the model.  This gets stored in the model for future reference.
		metaData.setModelSoftwareVersion(getClass().getName());

		// specify the date when the model was generated.  This gets 
		// stored in the model for future reference.
		metaData.setModelGenerationDate(new Date().toString());

		// Specify a description of the model. This information is not
		// processed in any way by GeoTess. It is carried around for
		// information purposes.
		metaData.setDescription(String
				.format("Example of a model that uses DataCustom data objects."));

		// Specify a list of layer names. A model could have many layers,
		// e.g., ("core", "mantle", "crust"), specified in order of
		// increasing radius. This simple example has only one layer.
		metaData.setLayerNames("surface");

		// specify the DataType for the data. All attributes, in all
		// profiles, will have the same data type.
		metaData.setDataType(DataType.DOUBLE);

		// specify the names of the attributes and the units of the
		// attributes in two String arrays. This model includes an
		// attribute for each unique station-phase-attribute.
		int nAttributes = stations.length*phases.length*attributes.length;
		String[] names = new String[nAttributes];
		String[] units = new String[nAttributes];
		int n=0;
		for (int i=0; i<stations.length; ++i)
			for (int j=0; j<phases.length; ++j)
				for (int k=0; k<attributes.length; ++k)
				{
					names[n] = String.format("%s_%s_%s", stations[i], phases[j], attributes[k].toString());
					units[n] = attributes[k].getUnits();
					++n;
				}
		metaData.setAttributes(names, units);

		// specify the path to the file containing the grid to be used for
		// this test.  In this demo, use one of the grids supplied in the 
		// resources directory of the project or jar.
		String gridFile = "src/test/resources/permanent_files/geotess_grid_32000.geotess";

		// call a GeoTessModel constructor to build the model. This will
		// load the grid, and initialize all the data structures to null.
		// To be useful, we will have to populate the data structures.
		GeoTessModelGA model = new GeoTessModelGA(gridFile, metaData);

		// the previous statement took care of all the details involved in 
		// constructing a standard geotess model.  This next statement
		// takes care of specifying some stuff that is specific to a 
		// GeoTessModelGA.
		model.setStationsPhasesAttributes(stations, phases, attributes);

		// now we need to populate the model with data structures.

		// First, load the seismicity_depth.geotess model which contains
		// the maximum depth of seismicity everywhere on the globe.
		// This model can be found in the resources directory of the 
		// geo-tess-builder project.

		GeoTessModel seismicityDepth = new GeoTessModel("/Users/sballar/Desktop/seismicity_depth.geotess");

		//System.out.println(seismicityDepth);
		
		// get a GeoTessPosition object from the seismicityDepth model that
		// we can use to interpolate the maximum depth of seismicity
		// anywhere on earth.
		GeoTessPosition depthModel = seismicityDepth.getGeoTessPosition();

		// interate over every vertex in the geotess grid in our new model that we are building.
		for (int vtx = 0; vtx < model.getGrid().getNVertices(); ++vtx)
		{
			// retrieve the unit vector corresponding to the i'th vertex of
			// the grid.
			double[] vertex = model.getGrid().getVertex(vtx);

			// get the radius of the earth at this latitude.
			double earthRadius = VectorGeo.getEarthRadius(vertex);

			// find the maximum depth of seismicity at the geographic
			// position of this vertex
			double bottom = depthModel.set(vertex, earthRadius).getValue(2);

			// this function computes an array values between first and last
			// such that the spacing between adjacent entries is no larger than
			// 50.
			float[] radii = Globals.getArrayFloat(earthRadius-bottom, earthRadius, 50.);

			System.out.println(vtx+"  "+model.getEarthShape().getLatLonString(vertex)+"  "+radii.length);
			
			// instantiate an array of GridPointData objects, one for
			// each radius in this Profile.
			// Note that GridPointData extends CustomData
			GridPointData[] data = new GridPointData[radii.length];

			for (int i=0; i<radii.length; ++i)
			{
				// create a GridPointData object at this radius.
				// GridPointData extends CustomData
				// Real code will need to be much more complicated
				// than this.  It will have to compute predictions.
				double cellRadius = 200.;
				double minDepth = radii[i]+10.;
				double maxDepth = radii[i]-10;

				data[i] = new GridPointData(model, cellRadius, minDepth, maxDepth);

				for (String station : model.getStationNames())
				{
					StationData stationData = new StationData();
					data[i].put(station, stationData);
					for (SeismicPhase phase : model.getPhaseNames())
					{
						PhaseData phaseData = new PhaseData();
						stationData.put(phase, phaseData);

						// this is a very dumb but simple example.  
						// Must compute real predictions here.
						phaseData.put(GeoAttributes.TT, 100.);
						phaseData.put(GeoAttributes.AZ, 90.);
						phaseData.put(GeoAttributes.SH, 1.5);
					}
				}
			}

			// at many places on earth, bottom == 0 and radii.length will 
			// equal 1.  At those locations the line below will create 
			// ProfileThin objects.  At some locations on Earth, bottom is 
			// greater than zero and sometimes as large as 700 km.  At those
			// locations radii.length will be > zero and perhaps as large as
			// 16.  In those places the code below will compute ProfileNPoint
			// objects.

			model.setProfile(vtx, 0, radii, data);
		}

		// We are done!  model is constructed and populated.

		return model;
	}

}
