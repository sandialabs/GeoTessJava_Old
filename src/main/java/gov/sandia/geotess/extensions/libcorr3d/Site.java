package gov.sandia.geotess.extensions.libcorr3d;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import gov.sandia.geotess.GeoTessUtils;
import gov.sandia.gmp.util.globals.GMTFormat;


/**
 * A representation of a seismic station.  Not used by GeoTess directly but used
 * by several classes that extend GeoTess.
 * 
 * @author jrhipp
 *
 */
public class Site implements Serializable, Comparable<Site> 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Station code. This is the code name of a seismic observatory and
	 * identifies a geographic location recorded in the <B>site</B> table.
	 */
	protected String sta;

	static final public String STA_NA = null;

	/**
	 * Turn on date. Date on which the station, or sensor indicated began
	 * operating. The columns offdate and ondate are not intended to accommodate
	 * temporary downtimes, but rather to indicate the time period for which the
	 * columns of the station (<I>lat</I>, <I>lon</I>, <I>elev</I>,) are valid
	 * for the given station code. Stations are often moved, but with the
	 * station code remaining unchanged.
	 */
	protected long ondate;

	static final public long ONDATE_NA = -1;

	/**
	 * Turn off date. This column is the Julian Date on which the station or
	 * sensor indicated was turned off, dismantled, or moved (see <I>ondate</I>)
	 */
	protected long offdate;

	static final public long OFFDATE_NA = 2286324;

	/**
	 * Geographic latitude. Locations north of equator have positive latitudes.
	 * <p>
	 * Units: degree
	 */
	protected double lat;

	static final public double LAT_NA = -999;

	/**
	 * Geographic longitude. Longitudes are measured positive East of the
	 * Greenwich meridian.
	 * <p>
	 * Units: degree
	 */
	protected double lon;

	static final public double LON_NA = -999;

	/**
	 * Surface elevation. This column is the elevation of the surface of the
	 * earth above the seismic station (<B>site</B>) relative to mean sea level
	 * <p>
	 * Units: km
	 */
	protected double elev;

	static final public double ELEV_NA = -999;

	/**
	 * Station name/Description. This value is the full name of the station
	 * whose code name is in <I>sta</I> [for example, one record in the
	 * <B>site</B> table connects <I>sta</I> = ANMO to staname = ALBUQUERQUE,
	 * NEW MEXICO (SRO)].
	 */
	protected String staname;

	static final public String STANAME_NA = "-";

	/**
	 * Station type; character string specifies the station type. Recommended
	 * entries are single station (ss) or array (ar).
	 */
	protected String statype;

	static final public String STATYPE_NA = "-";

	/**
	 * Reference station. This string specifies the reference station with
	 * respect to which array members are located (see <I>deast</I>,
	 * <I>dnorth</I>).
	 */
	protected String refsta;

	static final public String REFSTA_NA = "-";

	/**
	 * Distance North. This column gives the northing or relative position of
	 * array element North of the array center specified by the value of
	 * <I>refsta</I> (see <I>deast</I>).
	 * <p>
	 * Units: km
	 */
	protected double dnorth;

	static final public double DNORTH_NA = 0.0;

	/**
	 * Distance East. This column gives the easting or the relative position of
	 * an array element East of the location of the array center specified by
	 * the value of <I>refsta</I> (see <I>dnorth</I>).
	 * <p>
	 * Units: km
	 */
	protected double deast;

	static final public double DEAST_NA = 0.0;

	/**
	 * Parameterized constructor. Populates all values with specified values.
	 */
	public Site(String sta, long ondate, long offdate, double lat, double lon,
			double elev, String staname, String statype, String refsta,
			double dnorth, double deast) {
		this.sta = sta;
		this.ondate = ondate;
		this.offdate = offdate;
		this.lat = lat;
		this.lon = lon;
		this.elev = elev;
		this.staname = staname;
		this.statype = statype;
		this.refsta = refsta;
		this.dnorth = dnorth;
		this.deast = deast;
	}

	public Site(String sta, double ontime, double offtime, double lat, double lon,
			double elev, String staname, String statype, String refsta,
			double dnorth, double deast) {
		this.sta = sta;
		setOntime(ontime);
		setOfftime(offtime);
		this.lat = lat;
		this.lon = lon;
		this.elev = elev;
		this.staname = staname;
		this.statype = statype;
		this.refsta = refsta;
		this.dnorth = dnorth;
		this.deast = deast;
	}


	/**
	 * Copy constructor.
	 */
	public Site(Site other) {
		this.sta = other.getSta();
		this.ondate = other.getOndate();
		this.offdate = other.getOffdate();
		this.lat = other.getLat();
		this.lon = other.getLon();
		this.elev = other.getElev();
		this.staname = other.getStaname();
		this.statype = other.getStatype();
		this.refsta = other.getRefsta();
		this.dnorth = other.getDnorth();
		this.deast = other.getDeast();
	}

	/**
	 * Default constructor that populates all values with na_values.
	 */
	public Site() {
		this(STA_NA, ONDATE_NA, OFFDATE_NA, LAT_NA, LON_NA, ELEV_NA,
				STANAME_NA, STATYPE_NA, REFSTA_NA, DNORTH_NA, DEAST_NA);
	}

	/**
	 * Constructor that loads values from a Scanner. It can read the output of
	 * the toString() function.
	 * Note: ondate and offdate are read/written as epoch times because c++ version
	 * requires it.
	 */
	public Site(Scanner input) throws IOException {
		String line = input.nextLine();
		sta = line.substring(line.indexOf(":")+1).trim();

		line = input.nextLine();
		setOntime(Double.parseDouble(line.substring(line.indexOf(":")+1).trim()));

		line = input.nextLine();
		setOfftime(Double.parseDouble(line.substring(line.indexOf(":")+1).trim()));

		line = input.nextLine();
		lat = Double.parseDouble(line.substring(line.indexOf(":")+1).trim());

		line = input.nextLine();
		lon = Double.parseDouble(line.substring(line.indexOf(":")+1).trim());

		line = input.nextLine();
		elev = Double.parseDouble(line.substring(line.indexOf(":")+1).trim());

		line = input.nextLine();
		staname = line.substring(line.indexOf(":")+1).trim();

		line = input.nextLine();
		statype = line.substring(line.indexOf(":")+1).trim();

		line = input.nextLine();
		refsta = line.substring(line.indexOf(":")+1).trim();

		line = input.nextLine();
		dnorth = Double.parseDouble(line.substring(line.indexOf(":")+1).trim());

		line = input.nextLine();
		deast = Double.parseDouble(line.substring(line.indexOf(":")+1).trim());

	}

	/**
	 * Constructor that loads values from a DataInputStream.
	 * Note: ondate and offdate are read/written as epoch times because c++ version
	 * requires it.
	 */
	public Site(DataInputStream input) throws IOException
	{
		this(GeoTessUtils.readString(input), 
				input.readDouble(), 
				input.readDouble(), 
				input.readDouble(), input.readDouble(), input.readDouble(),
				GeoTessUtils.readString(input), GeoTessUtils.readString(input), 
				GeoTessUtils.readString(input), input.readDouble(), input.readDouble());
	}

	/**
	 * Write this row to a DataOutputStream.
	 * Note: ondate and offdate are read/written as epoch times because c++ version
	 * requires it.
	 */
	public void write(DataOutputStream output) throws IOException {
		GeoTessUtils.writeString(output, sta);
		output.writeDouble(getOntime());
		output.writeDouble(getOfftime());
		output.writeDouble(lat);
		output.writeDouble(lon);
		output.writeDouble(elev);
		GeoTessUtils.writeString(output, staname);
		GeoTessUtils.writeString(output, statype);
		GeoTessUtils.writeString(output, refsta);
		output.writeDouble(dnorth);
		output.writeDouble(deast);
	}

	/**
	 * Write this row to an ascii String.
	 */
	@Override
	public String toString() {
		  return String.format(
				"sta:     %s%n"
		      + "ondate:  %d%n"
		      + "offdate: %d%n"
		      + "lat:     %1.6f%n"
		      + "lon:     %1.6f%n"
		      + "elev:    %1.4f%n"
		      + "staname: %s%n"
		      + "statype: %s%n"
		      + "refsta:  %s%n"
		      + "dnorth:  %1.3f%n"
		      + "deast:   %1.3f%n",
		      sta, ondate, offdate, lat, lon, elev, staname, statype, refsta, dnorth, deast);
	}

	/**
	 * Write this row to an ascii String.
	 * Note: ondate and offdate are read/written as epoch times because c++ version
	 * requires it.
	 */
	public String getString() {
		  return String.format(
				"sta:     %s%n"
		      + "ontime:  %1.3f%n"
		      + "offtime: %1.3f%n"
		      + "lat:     %1.6f%n"
		      + "lon:     %1.6f%n"
		      + "elev:    %1.4f%n"
		      + "staname: %s%n"
		      + "statype: %s%n"
		      + "refsta:  %s%n"
		      + "dnorth:  %1.3f%n"
		      + "deast:   %1.3f%n",
		      sta, 
		      getOntime(), getOfftime(), 
		      lat, lon, elev, staname, statype, refsta, dnorth, deast);
	}

	/**
	 * Station code. This is the code name of a seismic observatory and
	 * identifies a geographic location recorded in the <B>site</B> table.
	 * 
	 * @return sta
	 */
	public String getSta() {
		return sta;
	}

	/**
	 * Station code. This is the code name of a seismic observatory and
	 * identifies a geographic location recorded in the <B>site</B> table.
	 * 
	 * @param sta
	 * @return reference to this
	 */
	public Site setSta(String sta) {
		this.sta = sta;
		return this;
	}

	/**
	 * Turn on date. Date on which the station, or sensor indicated began
	 * operating. The columns offdate and ondate are not intended to accommodate
	 * temporary downtimes, but rather to indicate the time period for which the
	 * columns of the station (<I>lat</I>, <I>lon</I>, <I>elev</I>,) are valid
	 * for the given station code. Stations are often moved, but with the
	 * station code remaining unchanged.
	 * 
	 * @return ondate
	 */
	public long getOndate() {
		return ondate;
	}

	/**
	 * Turn on date. Date on which the station, or sensor indicated began
	 * operating. The columns offdate and ondate are not intended to accommodate
	 * temporary downtimes, but rather to indicate the time period for which the
	 * columns of the station (<I>lat</I>, <I>lon</I>, <I>elev</I>,) are valid
	 * for the given station code. Stations are often moved, but with the
	 * station code remaining unchanged.
	 * 
	 * @param ondate
	 * @return reference to this
	 */
	public Site setOndate(long ondate) {
		this.ondate = ondate;
		return this;
	}

	/**
	 * Turn off date. This column is the Julian Date on which the station or
	 * sensor indicated was turned off, dismantled, or moved (see <I>ondate</I>)
	 * 
	 * @return offdate
	 */
	public long getOffdate() {
		return offdate;
	}
	
	/**
	 * Turn off date. This column is the Julian Date on which the station or
	 * sensor indicated was turned off, dismantled, or moved (see <I>ondate</I>)
	 * 
	 * @param offdate
	 * @return reference to this
	 */
	public Site setOffdate(long offdate) {
		this.offdate = offdate;
		return this;
	}

	/**
	 * Turn on time
	 * 
	 * @return ontime (epoch time).  if ondate == -1 returns -1e10
	 */
	public double getOntime() {
		return ondate == Site.ONDATE_NA ? -1e10 : GMTFormat.getEpochTime((int)ondate);
	}

	/**
	 * Turn on date. Date on which the station, or sensor indicated began
	 * operating. The columns offdate and ondate are not intended to accommodate
	 * temporary downtimes, but rather to indicate the time period for which the
	 * columns of the station (<I>lat</I>, <I>lon</I>, <I>elev</I>,) are valid
	 * for the given station code. Stations are often moved, but with the
	 * station code remaining unchanged.
	 * 
	 * @param ontime
	 * @return reference to this
	 */
	public Site setOntime(double ontime) {
		this.ondate = GMTFormat.getJDate(ontime);
		if (this.ondate < 1000000L)
			this.ondate = Site.ONDATE_NA;
		return this;
	}

	/**
	 * Turn off date. This column is the Julian Date on which the station or
	 * sensor indicated was turned off, dismantled, or moved (see <I>ondate</I>)
	 * 
	 * @return offdate
	 */
	public double getOfftime() {
		return GMTFormat.getOffTime((int)offdate);
	}
	
	/**
	 * Turn off date. This column is the Julian Date on which the station or
	 * sensor indicated was turned off, dismantled, or moved (see <I>ondate</I>)
	 * 
	 * @param offtime
	 * @return reference to this
	 */
	public Site setOfftime(double offtime) {
		this.offdate = GMTFormat.getJDate(offtime);
		return this;
	}

	/**
	 * Geographic latitude. Locations north of equator have positive latitudes.
	 * <p>
	 * Units: degree
	 * 
	 * @return lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Geographic latitude. Locations north of equator have positive latitudes.
	 * <p>
	 * Units: degree
	 * 
	 * @param lat
	 * @return reference to this
	 */
	public Site setLat(double lat) {
		this.lat = lat;
		return this;
	}

	/**
	 * Geographic longitude. Longitudes are measured positive East of the
	 * Greenwich meridian.
	 * <p>
	 * Units: degree
	 * 
	 * @return lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * Geographic longitude. Longitudes are measured positive East of the
	 * Greenwich meridian.
	 * <p>
	 * Units: degree
	 * 
	 * @param lon
	 * @return reference to this
	 */
	public Site setLon(double lon) {
		this.lon = lon;
		return this;
	}

	/**
	 * Surface elevation. This column is the elevation of the surface of the
	 * earth above the seismic station (<B>site</B>) relative to mean sea level
	 * <p>
	 * Units: km
	 * 
	 * @return elev
	 */
	public double getElev() {
		return elev;
	}

	/**
	 * Surface elevation. This column is the elevation of the surface of the
	 * earth above the seismic station (<B>site</B>) relative to mean sea level
	 * <p>
	 * Units: km
	 * 
	 * @param elev
	 * @return reference to this
	 */
	public Site setElev(double elev) {
		this.elev = elev;
		return this;
	}

	/**
	 * Station name/Description. This value is the full name of the station
	 * whose code name is in <I>sta</I> [for example, one record in the
	 * <B>site</B> table connects <I>sta</I> = ANMO to staname = ALBUQUERQUE,
	 * NEW MEXICO (SRO)].
	 * 
	 * @return staname
	 */
	public String getStaname() {
		return staname;
	}

	/**
	 * Station name/Description. This value is the full name of the station
	 * whose code name is in <I>sta</I> [for example, one record in the
	 * <B>site</B> table connects <I>sta</I> = ANMO to staname = ALBUQUERQUE,
	 * NEW MEXICO (SRO)].
	 * 
	 * @param staname
	 * @return reference to this
	 */
	public Site setStaname(String staname) {
		this.staname = staname;
		return this;
	}

	/**
	 * Station type; character string specifies the station type. Recommended
	 * entries are single station (ss) or array (ar).
	 * 
	 * @return statype
	 */
	public String getStatype() {
		return statype;
	}

	/**
	 * Station type; character string specifies the station type. Recommended
	 * entries are single station (ss) or array (ar).
	 * 
	 * @param statype
	 * @return reference to this
	 */
	public Site setStatype(String statype) {
		this.statype = statype;
		return this;
	}

	/**
	 * Reference station. This string specifies the reference station with
	 * respect to which array members are located (see <I>deast</I>,
	 * <I>dnorth</I>).
	 * 
	 * @return refsta
	 */
	public String getRefsta() {
		return refsta;
	}

	/**
	 * Reference station. This string specifies the reference station with
	 * respect to which array members are located (see <I>deast</I>,
	 * <I>dnorth</I>).
	 * 
	 * @param refsta
	 * @return reference to this
	 */
	public Site setRefsta(String refsta) {
		this.refsta = refsta;
		return this;
	}

	/**
	 * Distance North. This column gives the northing or relative position of
	 * array element North of the array center specified by the value of
	 * <I>refsta</I> (see <I>deast</I>).
	 * <p>
	 * Units: km
	 * 
	 * @return dnorth
	 */
	public double getDnorth() {
		return dnorth;
	}

	/**
	 * Distance North. This column gives the northing or relative position of
	 * array element North of the array center specified by the value of
	 * <I>refsta</I> (see <I>deast</I>).
	 * <p>
	 * Units: km
	 * 
	 * @param dnorth
	 * @return reference to this
	 */
	public Site setDnorth(double dnorth) {
		this.dnorth = dnorth;
		return this;
	}

	/**
	 * Distance East. This column gives the easting or the relative position of
	 * an array element East of the location of the array center specified by
	 * the value of <I>refsta</I> (see <I>dnorth</I>).
	 * <p>
	 * Units: km
	 * 
	 * @return deast
	 */
	public double getDeast() {
		return deast;
	}

	/**
	 * Distance East. This column gives the easting or the relative position of
	 * an array element East of the location of the array center specified by
	 * the value of <I>refsta</I> (see <I>dnorth</I>).
	 * <p>
	 * Units: km
	 * 
	 * @param deast
	 * @return reference to this
	 */
	public Site setDeast(double deast) {
		this.deast = deast;
		return this;
	}

	/**
	 * Returns a unit vector of the site location.
	 * 
	 * @return A unit vector of the site location.
	 */
	public double[] getUnitVector()
	{
		return GeoTessUtils.getVectorDegrees(lat, lon);
	}

	/**
	 * Returns the site location radius (km).
	 * 
	 * @return The site location radius (km).
	 */
	public double getRadius()
	{
		return GeoTessUtils.getEarthRadius(getUnitVector()) + elev;
	}

	@Override
	public int compareTo(Site o) {
		int x = sta.compareTo(o.sta);
		if (x == 0)
			x = (int) Math.signum(this.ondate-o.ondate);
		return x;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		}
		return (this.ondate == ((Site)o).ondate) && this.sta.equals(((Site)o).sta);
	}

	@Override
	public int hashCode() {
		return ((int)ondate) * sta.hashCode();
	}

}

