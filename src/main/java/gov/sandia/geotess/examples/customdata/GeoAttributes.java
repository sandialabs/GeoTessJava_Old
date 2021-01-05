package gov.sandia.geotess.examples.customdata;

public enum GeoAttributes {
	/**
	 * Travel time in seconds
	 */
	TT("seconds"), 
	
	/**
	 * Minimum travel time from a source anywhere in a cell, in seconds
	 */
	TTMIN("seconds"), 
	
	/**
	 * Maximum travel time from a source anywhere in a cell, in seconds
	 */
	TTMAX("seconds"),
	
	/**
	 * Receiver-source azimuth, in degrees
	 */
	AZ("degrees"), 
	
	/**
	 * Minimum receiver-source azimuth from a source anywhere in a cell, in degrees
	 */
	AZMIN("degrees"), 
	
	/**
	 * Maximum receiver-source azimuth from a source anywhere in a cell, in degrees
	 */
	AZMAX("degrees"),
	
	/**
	 * Horizontal slowness in seconds/degree
	 */
	SH("seconds/degree"), 
	
	/**
	 * Minimum horizontal slowness from a source anywhere in a cell, in sec/deg
	 */
	SHMIN("seconds/degree"), 
	
	/**
	 * Maximum horizontal slowness from a source anywhere in a cell, in sec/deg
	 */
	SHMAX("seconds/degree");
	
	private String units;
	
	GeoAttributes(String units) { this.units = units;}
	
	public String getUnits() { return units; }
}
