package gov.sandia.geotess.examples.customdata;


public class Triple {
	
	String station;
	
	SeismicPhase phase;
	
	GeoAttributes attribute;

	public Triple(String station, SeismicPhase phase, GeoAttributes attribute) {
		this.station = station;
		this.phase = phase;
		this.attribute = attribute;
	}

}
