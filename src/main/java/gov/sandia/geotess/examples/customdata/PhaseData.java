package gov.sandia.geotess.examples.customdata;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Defines travel time information for an event hypothesis 
 * at a single grid point and single station - phase.
 * @author sandy
 *
 */
public class PhaseData extends HashMap<GeoAttributes, Double>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7339325174314271494L;

	/**
	 * Whether or not this is a primary phase.  Need more information about this.
	 */
	boolean primary;
	
	public PhaseData() {
	}

	/**
	 * 
	 * @param input
	 * @param attributes A list of all the attributes supported by this model.
	 * All of these attributes will have values in each PhaseData map and will
	 * be read from, and written to the output files.
	 * @throws IOException
	 */
	public PhaseData(DataInputStream input, GeoAttributes[] attributes) throws IOException
	{
		primary = input.readBoolean();
		for (GeoAttributes a : attributes)
			put(a, input.readDouble());
	}
	
	/**
	 * 
	 * @param output
	 * @param attributes A list of all the attributes supported by this model.
	 * All of these attributes will have values in each PhaseData map and will
	 * be read from, and written to the output files.
	 * @throws IOException
	 */
	public void write(DataOutputStream output, GeoAttributes[] attributes) throws IOException 
	{
		output.writeBoolean(primary);
		for (GeoAttributes a : attributes)
			output.writeDouble(get(a));
	}
	
	/**
	 * 
	 * @param input
	 * @param attributes A list of all the attributes supported by this model.
	 * All of these attributes will have values in each PhaseData map and will
	 * be read from, and written to the output files.
	 */
	public PhaseData(Scanner input, GeoAttributes[] attributes) 
	{
		primary = input.nextBoolean();
		for (GeoAttributes a : attributes)
			put(a, input.nextDouble());
	}
	
	/**
	 * 
	 * @param output A list of all the attributes supported by this model.
	 * All of these attributes will have values in each PhaseData map and will
	 * be read from, and written to the output files.
	 * @param attributes
	 */
	public void write(StringBuffer output, GeoAttributes[] attributes) {
		output.append(primary);
		for (GeoAttributes a : attributes)
		{
			Double value = get(a);
			output.append(' ');
			output.append(value == null ? "NaN" 
					: Double.toString(value));
		}
	}
	
}
