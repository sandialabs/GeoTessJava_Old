package gov.sandia.geotess.examples.customdata;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import gov.sandia.geotess.GeoTessUtils;

/**
 * Defines information for an event hypothesis at a single grid 
 * point and a single station.
 * @author sandy
 * 
 */
public class StationData extends HashMap<SeismicPhase, PhaseData>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8612644644926375628L;

	/**
	 * Distance from station to grid point, in degrees. 
	 * (Easily computed from grid point and station locations.)
	 */
	double delta;

	/**
	 * Azimuth from station to grid point, in degrees.
	 * (In GMS system is azimuth station-to-event or other way around?)
	 */
	double azimuth;

	/**
	 * Backazimuth from grid point to station, in degrees.
	 * (In GMS system is backAzimuth station-to-event or other way around?)
	 */
	double backAzimuth;

	public StationData() {
	}

	/**
	 * 
	 * @param input
	 * @param attributes A list of all the attributes supported by this model.
	 * All of these attributes will have values in each PhaseData map and will
	 * be read from, and written to the output files.
	 * @throws IOException
	 */
	public StationData(DataInputStream input, GeoAttributes[] attributes) throws IOException
	{
		delta = input.readDouble();
		azimuth = input.readDouble();
		backAzimuth = input.readDouble();

		int nPhases = input.readInt();

		for (int i=0; i<nPhases; ++i)
		{
			SeismicPhase phase = SeismicPhase.valueOf(GeoTessUtils.readString(input));
			put(phase, new PhaseData(input, attributes));
		}
	}

	/**
	 * 
	 * @param output
	 * @param attributes A list of all the attributes supported by this model.
	 * All of these attributes will have values in each PhaseData map and will
	 * be read from, and written to the output files.
	 * @throws IOException
	 */
	public void write(DataOutputStream output, 
			GeoAttributes[] attributes) throws IOException {
		output.writeDouble(delta);
		output.writeDouble(azimuth);
		output.writeDouble(backAzimuth);

		output.writeInt(size()); // number phases
		for (Entry<SeismicPhase, PhaseData> entry : entrySet())
		{
			GeoTessUtils.writeString(output, entry.getKey().toString()); // phase name
			entry.getValue().write(output, attributes); // PhaseData
		}
	}

	/**
	 * 
	 * @param input
	 * @param attributes A list of all the attributes supported by this model.
	 * All of these attributes will have values in each PhaseData map and will
	 * be read from, and written to the output files.
	 */
	public StationData(Scanner input, GeoAttributes[] attributes) 
	{
		delta = input.nextDouble();
		azimuth = input.nextDouble();
		backAzimuth = input.nextDouble();

		int nPhases = input.nextInt();
		for (int i=0; i<nPhases; ++i)
		{
			SeismicPhase phase = SeismicPhase.valueOf(input.next());
			put(phase, new PhaseData(input, attributes));
		}
	}

	/**
	 * 
	 * @param output
	 * @param attributes A list of all the attributes supported by this model.
	 * All of these attributes will have values in each PhaseData map and will
	 * be read from, and written to the output files.
	 */
	public void write(StringBuffer output, 
			GeoAttributes[] attributes)  {
		output.append(Double.toString(delta)).append(" ");
		output.append(Double.toString(azimuth)).append(" ");
		output.append(Double.toString(backAzimuth)).append('\n');

		for (Entry<SeismicPhase, PhaseData> entry : entrySet())
		{
			output.append(entry.getKey().toString()).append('\n'); // phase name
			entry.getValue().write(output, attributes); // PhaseData
		}
	}

}
