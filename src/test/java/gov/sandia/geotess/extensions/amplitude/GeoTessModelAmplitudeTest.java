//- ****************************************************************************************
//- Notice: This computer software was prepared by Sandia Corporation, hereinafter 
//- the Contractor, under Contract DE-AC04-94AL85000 with the Department of Energy (DOE). 
//- All rights in the computer software are reserved by DOE on behalf of the United 
//- States Government and the Contractor as provided in the Contract. You are authorized 
//- to use this computer software for Governmental purposes but it is not to be released 
//- or distributed to the public.  
//- NEITHER THE U.S. GOVERNMENT NOR THE CONTRACTOR MAKES ANY WARRANTY, EXPRESS OR IMPLIED, 
//- OR ASSUMES ANY LIABILITY FOR THE USE OF THIS SOFTWARE.
//- This notice including this sentence must appear on any copies of this computer software.
//- ****************************************************************************************

package gov.sandia.geotess.extensions.amplitude;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.sandia.geotess.GeoTessException;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.numerical.polygon.GreatCircle;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GeoTessModelAmplitudeTest {

	private static GeoTessModelAmplitude qfModel;
	private static GeoTessModelAmplitude qetaModel;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		File dir = null;
		///Users/sballar/Documents/amplitude/LANL_Models/AMP1.1_4AFTAC_090215/Run3/AMP1.1_Run3.Pn.0.5.geotess
		///Users/sballar/Documents/amplitude/LANL_Models/AMP1.1_4AFTAC_090215/Run3/AMP1.1_Run3.Pn.0.5.geotess

		if (System.getProperty("os.name").toLowerCase().contains("mac"))
			dir = new File("/Users/sballar/git/geo-tess-java/src/test/resources/amplitude_files");
		else if (System.getProperty("os.name").toLowerCase().contains("windows"))
			dir = new File("\\\\tonto2\\GNEM\\devlpool\\sballar\\amplitude\\LANL_Models");
		else
			System.out.println("os.name = "+System.getProperty("os.name"));

		qfModel = new GeoTessModelAmplitude(new File(dir,"AMP1.1_4AFTAC_090215/Run3/AMP1.1_Run3.Pn.0.5.geotess"), "..");

		qetaModel = new GeoTessModelAmplitude(new File(dir,"AMP1.1_4AFTAC_090215/Run4/AMP1.1_Run4.Pn.0.5.geotess"), "..");

		//System.out.println(qfModel); System.out.println();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPathIntegral_Q_ETA() throws GeoTessException 
	{
		GreatCircle path = new GreatCircle(
				qetaModel.getEarthShape().getVectorDegrees(42.8900, 126.1000),
				qetaModel.getEarthShape().getVectorDegrees(44.6164, 129.5920)
				);

		double qeff = qetaModel.getPathQ(path, "1.0_2.0", InterpolatorType.LINEAR);

		//System.out.printf("Q0-ETA = %f%n", qeff);

		assertEquals(3398.31697, qeff, 1e-3);

	}

	@Test
	public void testGetPathIntegral_QF() throws Exception 
	{
		GreatCircle path = new GreatCircle(
				qfModel.getEarthShape().getVectorDegrees(42.8900, 126.1000),
				qfModel.getEarthShape().getVectorDegrees(44.6164, 129.5920)
				);

		double qeff = qfModel.getPathQ(path, "1.0_2.0", InterpolatorType.LINEAR);

		assertEquals(3166.188518, qeff, 1e-3);

		qeff = qfModel.getPathQ(path, "1.0_2.0", InterpolatorType.NATURAL_NEIGHBOR);

		assertEquals(3166.86389, qeff, 1e-3);
	}

	@Test
	public void testGetPathIntegral_latlon() throws Exception 
	{
		assertEquals(3166.188518, qfModel.getPathQ(42.8900, 126.1000, 44.6164, 129.5920, true, "1.0_2.0"), 1e-3);
	}

	@Test
	public void testGetPhase() {
		assertEquals("Pn", qfModel.getPhase());
	}

	@Test
	public void testSetPhase() {
		String phase = qfModel.getPhase();
		assertEquals("Pn", phase);
		qfModel.setPhase("xxxx");
		assertEquals("xxxx", qfModel.getPhase());
		qfModel.setPhase(phase);
		assertEquals(phase, qfModel.getPhase());

	}

	@Test
	public void testSetSiteTerms() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetSiteTerms() {
		assertEquals(3667, qfModel.getSiteTrans().size());
	}

	@Test
	public void testGetSiteTerm() {
		float siteTerm = qfModel.getSiteTrans("MDJ", "BHZ", "1.0_2.0");
		assertEquals(-19.44614, siteTerm, 0.0001);		

		siteTerm = qfModel.getSiteTrans("xxx", "BHZ", "1.0_2.0");
		assertTrue(Float.isNaN(siteTerm));		

		siteTerm = qfModel.getSiteTrans("MDJ", "xxx", "1.0_2.0");
		assertTrue(Float.isNaN(siteTerm));		

		siteTerm = qfModel.getSiteTrans("MDJ", "BHZ", "xxx");
		assertTrue(Float.isNaN(siteTerm));		
	}
	
	@Test
	public void testGetNStations()
	{
		assertEquals(3667, qfModel.getNStations());
	}
	
	@Test
	public void testGetStations()
	{
		Set<String> stations = qfModel.getStations();
		
		//for (String s : stations) System.out.println("assertTrue(stations.contains(\""+s+"\"));");

		assertEquals(3667, stations.size());
		
		assertTrue(stations.contains("EPU"));
		assertTrue(stations.contains("DXIAN"));
		assertTrue(stations.contains("N20A"));
		assertTrue(stations.contains("A05A"));
		assertTrue(stations.contains("M30A"));
		assertTrue(stations.contains("E15A"));
		assertTrue(stations.contains("R47A"));
		assertTrue(stations.contains("BOSA"));
		assertTrue(stations.contains("C14A"));
		assertTrue(stations.contains("546A"));
		assertTrue(stations.contains("S09A"));
		assertTrue(stations.contains("JOHN"));
		assertTrue(stations.contains("VI01"));
		assertTrue(stations.contains("P13A1"));
		assertTrue(stations.contains("VI03"));
		assertTrue(stations.contains("GEC2A"));
		assertTrue(stations.contains("GEC2B"));
		assertTrue(stations.contains("BORG"));

	}

	@Test
	public void testIsSupportedStation() {
		assertTrue(qfModel.isSupportedStation("MDJ"));
		assertTrue(!qfModel.isSupportedStation("---"));
	}

	@Test
	public void testGetNChannels() {
		assertEquals(7, qfModel.getNChannels("MDJ"));
		assertEquals(0, qfModel.getNChannels("---"));
	}

	@Test
	public void testGetChannels() {
		Set<String> channels = qfModel.getChannels("MDJ");

		assertEquals(7, channels.size());

		//for (String ch : channels) System.out.println("assertTrue(channels.contains(\""+ch+"\"));");

		assertTrue(channels.contains("BHZ"));
		assertTrue(channels.contains("BHZ00"));
		assertTrue(channels.contains("BHZ10"));
		assertTrue(channels.contains("HHZ"));
		assertTrue(channels.contains("HHZ10"));
		assertTrue(channels.contains("SHZ"));
		assertTrue(channels.contains("SHZ00C2"));
	}

	@Test
	public void testIsSupportedChannel() {
		assertTrue(qfModel.isSupportedChannel("MDJ", "BHZ"));
		assertTrue(!qfModel.isSupportedChannel("MDJ", "---"));
		assertTrue(!qfModel.isSupportedChannel("---", "BHZ"));
		assertTrue(!qfModel.isSupportedChannel("---", "---"));
	}

	@Test
	public void testGetNBands() {
		assertEquals(16, qfModel.getNBands("MDJ", "BHZ"));
		assertEquals(0, qfModel.getNBands("MDJ", "---"));
		assertEquals(0, qfModel.getNBands("---", "BHZ"));
		assertEquals(0, qfModel.getNBands("---", "---"));
	}

	@Test
	public void testGetBands() {
		Set<String> bands = qfModel.getBands("MDJ", "BHZ");
		
		assertEquals(16, bands.size());
		
		//for (String b : bands) System.out.printf("assertTrue(bands.contains(\"%s\"));%n", b);
		
		assertTrue(bands.contains("0.25_0.5"));
		assertTrue(bands.contains("0.375_0.75"));
		assertTrue(bands.contains("0.5_1.0"));
		assertTrue(bands.contains("0.75_1.5"));
		assertTrue(bands.contains("1.0_2.0"));
		assertTrue(bands.contains("1.25_2.5"));
		assertTrue(bands.contains("1.5_3.0"));
		assertTrue(bands.contains("2.0_4.0"));
		assertTrue(bands.contains("2.5_5.0"));
		assertTrue(bands.contains("3.0_6.0"));
		assertTrue(bands.contains("4.0_6.0"));
		assertTrue(bands.contains("4.0_8.0"));
		assertTrue(bands.contains("6.0_12.0"));
		assertTrue(bands.contains("6.0_8.0"));
		assertTrue(bands.contains("8.0_10.0"));
		assertTrue(bands.contains("8.0_16.0"));

	}

	@Test
	public void testIsSupportedBand() {
		assertTrue(qfModel.isSupportedBand("MDJ", "BHZ", "1.0_2.0"));
		assertTrue(!qfModel.isSupportedBand("MDJ", "BHZ", "---"));
		assertTrue(!qfModel.isSupportedBand("MDJ", "---", "1.0_2.0"));
		assertTrue(!qfModel.isSupportedBand("---", "BHZ", "1.0_2.0"));
	}

	@Test
	public void testGetFrequencyMap()
	{
		LinkedHashMap<String, float[]> fmap = qfModel.getFrequencyMap();

//		for (String band : fmap.keySet())
//			System.out.printf("assertTrue(fmap.containsKey(\"%s\"));%n", band);
//
//		for (String band : fmap.keySet())
//		{
//			System.out.printf("assertEquals(%1.3f, fmap.get(\"%s\")[0], 1e-3);%n", fmap.get(band)[0], band);
//			System.out.printf("assertEquals(%1.3f, fmap.get(\"%s\")[1], 1e-3);%n", fmap.get(band)[1], band);
//		}
		
		assertEquals(16, fmap.size());

		assertTrue(fmap.containsKey("0.25_0.5"));
		assertTrue(fmap.containsKey("0.375_0.75"));
		assertTrue(fmap.containsKey("0.5_1.0"));
		assertTrue(fmap.containsKey("0.75_1.5"));
		assertTrue(fmap.containsKey("1.0_2.0"));
		assertTrue(fmap.containsKey("1.25_2.5"));
		assertTrue(fmap.containsKey("1.5_3.0"));
		assertTrue(fmap.containsKey("2.0_4.0"));
		assertTrue(fmap.containsKey("2.5_5.0"));
		assertTrue(fmap.containsKey("3.0_6.0"));
		assertTrue(fmap.containsKey("4.0_6.0"));
		assertTrue(fmap.containsKey("4.0_8.0"));
		assertTrue(fmap.containsKey("6.0_12.0"));
		assertTrue(fmap.containsKey("6.0_8.0"));
		assertTrue(fmap.containsKey("8.0_10.0"));
		assertTrue(fmap.containsKey("8.0_16.0"));
		
		assertEquals(0.250, fmap.get("0.25_0.5")[0], 1e-3);
		assertEquals(0.500, fmap.get("0.25_0.5")[1], 1e-3);
		assertEquals(0.375, fmap.get("0.375_0.75")[0], 1e-3);
		assertEquals(0.750, fmap.get("0.375_0.75")[1], 1e-3);
		assertEquals(0.500, fmap.get("0.5_1.0")[0], 1e-3);
		assertEquals(1.000, fmap.get("0.5_1.0")[1], 1e-3);
		assertEquals(0.750, fmap.get("0.75_1.5")[0], 1e-3);
		assertEquals(1.500, fmap.get("0.75_1.5")[1], 1e-3);
		assertEquals(1.000, fmap.get("1.0_2.0")[0], 1e-3);
		assertEquals(2.000, fmap.get("1.0_2.0")[1], 1e-3);
		assertEquals(1.250, fmap.get("1.25_2.5")[0], 1e-3);
		assertEquals(2.500, fmap.get("1.25_2.5")[1], 1e-3);
		assertEquals(1.500, fmap.get("1.5_3.0")[0], 1e-3);
		assertEquals(3.000, fmap.get("1.5_3.0")[1], 1e-3);
		assertEquals(2.000, fmap.get("2.0_4.0")[0], 1e-3);
		assertEquals(4.000, fmap.get("2.0_4.0")[1], 1e-3);
		assertEquals(2.500, fmap.get("2.5_5.0")[0], 1e-3);
		assertEquals(5.000, fmap.get("2.5_5.0")[1], 1e-3);
		assertEquals(3.000, fmap.get("3.0_6.0")[0], 1e-3);
		assertEquals(6.000, fmap.get("3.0_6.0")[1], 1e-3);
		assertEquals(4.000, fmap.get("4.0_6.0")[0], 1e-3);
		assertEquals(6.000, fmap.get("4.0_6.0")[1], 1e-3);
		assertEquals(4.000, fmap.get("4.0_8.0")[0], 1e-3);
		assertEquals(8.000, fmap.get("4.0_8.0")[1], 1e-3);
		assertEquals(6.000, fmap.get("6.0_12.0")[0], 1e-3);
		assertEquals(12.000, fmap.get("6.0_12.0")[1], 1e-3);
		assertEquals(6.000, fmap.get("6.0_8.0")[0], 1e-3);
		assertEquals(8.000, fmap.get("6.0_8.0")[1], 1e-3);
		assertEquals(8.000, fmap.get("8.0_10.0")[0], 1e-3);
		assertEquals(10.000, fmap.get("8.0_10.0")[1], 1e-3);
		assertEquals(8.000, fmap.get("8.0_16.0")[0], 1e-3);
		assertEquals(16.000, fmap.get("8.0_16.0")[1], 1e-3);
		
		
		fmap = qetaModel.getFrequencyMap();

		assertEquals(16, fmap.size());

		assertTrue(fmap.containsKey("0.25_0.5"));
		assertTrue(fmap.containsKey("0.375_0.75"));
		assertTrue(fmap.containsKey("0.5_1.0"));
		assertTrue(fmap.containsKey("0.75_1.5"));
		assertTrue(fmap.containsKey("1.0_2.0"));
		assertTrue(fmap.containsKey("1.25_2.5"));
		assertTrue(fmap.containsKey("1.5_3.0"));
		assertTrue(fmap.containsKey("2.0_4.0"));
		assertTrue(fmap.containsKey("2.5_5.0"));
		assertTrue(fmap.containsKey("3.0_6.0"));
		assertTrue(fmap.containsKey("4.0_6.0"));
		assertTrue(fmap.containsKey("4.0_8.0"));
		assertTrue(fmap.containsKey("6.0_12.0"));
		assertTrue(fmap.containsKey("6.0_8.0"));
		assertTrue(fmap.containsKey("8.0_10.0"));
		assertTrue(fmap.containsKey("8.0_16.0"));
		
		assertEquals(0.250, fmap.get("0.25_0.5")[0], 1e-3);
		assertEquals(0.500, fmap.get("0.25_0.5")[1], 1e-3);
		assertEquals(0.375, fmap.get("0.375_0.75")[0], 1e-3);
		assertEquals(0.750, fmap.get("0.375_0.75")[1], 1e-3);
		assertEquals(0.500, fmap.get("0.5_1.0")[0], 1e-3);
		assertEquals(1.000, fmap.get("0.5_1.0")[1], 1e-3);
		assertEquals(0.750, fmap.get("0.75_1.5")[0], 1e-3);
		assertEquals(1.500, fmap.get("0.75_1.5")[1], 1e-3);
		assertEquals(1.000, fmap.get("1.0_2.0")[0], 1e-3);
		assertEquals(2.000, fmap.get("1.0_2.0")[1], 1e-3);
		assertEquals(1.250, fmap.get("1.25_2.5")[0], 1e-3);
		assertEquals(2.500, fmap.get("1.25_2.5")[1], 1e-3);
		assertEquals(1.500, fmap.get("1.5_3.0")[0], 1e-3);
		assertEquals(3.000, fmap.get("1.5_3.0")[1], 1e-3);
		assertEquals(2.000, fmap.get("2.0_4.0")[0], 1e-3);
		assertEquals(4.000, fmap.get("2.0_4.0")[1], 1e-3);
		assertEquals(2.500, fmap.get("2.5_5.0")[0], 1e-3);
		assertEquals(5.000, fmap.get("2.5_5.0")[1], 1e-3);
		assertEquals(3.000, fmap.get("3.0_6.0")[0], 1e-3);
		assertEquals(6.000, fmap.get("3.0_6.0")[1], 1e-3);
		assertEquals(4.000, fmap.get("4.0_6.0")[0], 1e-3);
		assertEquals(6.000, fmap.get("4.0_6.0")[1], 1e-3);
		assertEquals(4.000, fmap.get("4.0_8.0")[0], 1e-3);
		assertEquals(8.000, fmap.get("4.0_8.0")[1], 1e-3);
		assertEquals(6.000, fmap.get("6.0_12.0")[0], 1e-3);
		assertEquals(12.000, fmap.get("6.0_12.0")[1], 1e-3);
		assertEquals(6.000, fmap.get("6.0_8.0")[0], 1e-3);
		assertEquals(8.000, fmap.get("6.0_8.0")[1], 1e-3);
		assertEquals(8.000, fmap.get("8.0_10.0")[0], 1e-3);
		assertEquals(10.000, fmap.get("8.0_10.0")[1], 1e-3);
		assertEquals(8.000, fmap.get("8.0_16.0")[0], 1e-3);
		assertEquals(16.000, fmap.get("8.0_16.0")[1], 1e-3);
	}


	@Test
	public void testRefreshFrequencyMap()
	{
		LinkedHashMap<String, float[]> fmap = qfModel.refreshFrequencyMap();

//		for (String band : fmap.keySet())
//			System.out.printf("assertTrue(fmap.containsKey(\"%s\"));%n", band);
//
//		for (String band : fmap.keySet())
//		{
//			System.out.printf("assertEquals(%1.3f, fmap.get(\"%s\")[0], 1e-3);%n", fmap.get(band)[0], band);
//			System.out.printf("assertEquals(%1.3f, fmap.get(\"%s\")[1], 1e-3);%n", fmap.get(band)[1], band);
//		}

		assertEquals(16, fmap.size());

		assertTrue(fmap.containsKey("0.25_0.5"));
		assertTrue(fmap.containsKey("0.375_0.75"));
		assertTrue(fmap.containsKey("0.5_1.0"));
		assertTrue(fmap.containsKey("0.75_1.5"));
		assertTrue(fmap.containsKey("1.0_2.0"));
		assertTrue(fmap.containsKey("1.25_2.5"));
		assertTrue(fmap.containsKey("1.5_3.0"));
		assertTrue(fmap.containsKey("2.0_4.0"));
		assertTrue(fmap.containsKey("2.5_5.0"));
		assertTrue(fmap.containsKey("3.0_6.0"));
		assertTrue(fmap.containsKey("4.0_6.0"));
		assertTrue(fmap.containsKey("4.0_8.0"));
		assertTrue(fmap.containsKey("6.0_12.0"));
		assertTrue(fmap.containsKey("6.0_8.0"));
		assertTrue(fmap.containsKey("8.0_10.0"));
		assertTrue(fmap.containsKey("8.0_16.0"));
		
		assertEquals(0.250, fmap.get("0.25_0.5")[0], 1e-3);
		assertEquals(0.500, fmap.get("0.25_0.5")[1], 1e-3);
		assertEquals(0.375, fmap.get("0.375_0.75")[0], 1e-3);
		assertEquals(0.750, fmap.get("0.375_0.75")[1], 1e-3);
		assertEquals(0.500, fmap.get("0.5_1.0")[0], 1e-3);
		assertEquals(1.000, fmap.get("0.5_1.0")[1], 1e-3);
		assertEquals(0.750, fmap.get("0.75_1.5")[0], 1e-3);
		assertEquals(1.500, fmap.get("0.75_1.5")[1], 1e-3);
		assertEquals(1.000, fmap.get("1.0_2.0")[0], 1e-3);
		assertEquals(2.000, fmap.get("1.0_2.0")[1], 1e-3);
		assertEquals(1.250, fmap.get("1.25_2.5")[0], 1e-3);
		assertEquals(2.500, fmap.get("1.25_2.5")[1], 1e-3);
		assertEquals(1.500, fmap.get("1.5_3.0")[0], 1e-3);
		assertEquals(3.000, fmap.get("1.5_3.0")[1], 1e-3);
		assertEquals(2.000, fmap.get("2.0_4.0")[0], 1e-3);
		assertEquals(4.000, fmap.get("2.0_4.0")[1], 1e-3);
		assertEquals(2.500, fmap.get("2.5_5.0")[0], 1e-3);
		assertEquals(5.000, fmap.get("2.5_5.0")[1], 1e-3);
		assertEquals(3.000, fmap.get("3.0_6.0")[0], 1e-3);
		assertEquals(6.000, fmap.get("3.0_6.0")[1], 1e-3);
		assertEquals(4.000, fmap.get("4.0_6.0")[0], 1e-3);
		assertEquals(6.000, fmap.get("4.0_6.0")[1], 1e-3);
		assertEquals(4.000, fmap.get("4.0_8.0")[0], 1e-3);
		assertEquals(8.000, fmap.get("4.0_8.0")[1], 1e-3);
		assertEquals(6.000, fmap.get("6.0_12.0")[0], 1e-3);
		assertEquals(12.000, fmap.get("6.0_12.0")[1], 1e-3);
		assertEquals(6.000, fmap.get("6.0_8.0")[0], 1e-3);
		assertEquals(8.000, fmap.get("6.0_8.0")[1], 1e-3);
		assertEquals(8.000, fmap.get("8.0_10.0")[0], 1e-3);
		assertEquals(10.000, fmap.get("8.0_10.0")[1], 1e-3);
		assertEquals(8.000, fmap.get("8.0_16.0")[0], 1e-3);
		assertEquals(16.000, fmap.get("8.0_16.0")[1], 1e-3);
		
		
		fmap = qetaModel.refreshFrequencyMap();

		assertEquals(16, fmap.size());

		assertTrue(fmap.containsKey("0.25_0.5"));
		assertTrue(fmap.containsKey("0.375_0.75"));
		assertTrue(fmap.containsKey("0.5_1.0"));
		assertTrue(fmap.containsKey("0.75_1.5"));
		assertTrue(fmap.containsKey("1.0_2.0"));
		assertTrue(fmap.containsKey("1.25_2.5"));
		assertTrue(fmap.containsKey("1.5_3.0"));
		assertTrue(fmap.containsKey("2.0_4.0"));
		assertTrue(fmap.containsKey("2.5_5.0"));
		assertTrue(fmap.containsKey("3.0_6.0"));
		assertTrue(fmap.containsKey("4.0_6.0"));
		assertTrue(fmap.containsKey("4.0_8.0"));
		assertTrue(fmap.containsKey("6.0_12.0"));
		assertTrue(fmap.containsKey("6.0_8.0"));
		assertTrue(fmap.containsKey("8.0_10.0"));
		assertTrue(fmap.containsKey("8.0_16.0"));
		
		assertEquals(0.250, fmap.get("0.25_0.5")[0], 1e-3);
		assertEquals(0.500, fmap.get("0.25_0.5")[1], 1e-3);
		assertEquals(0.375, fmap.get("0.375_0.75")[0], 1e-3);
		assertEquals(0.750, fmap.get("0.375_0.75")[1], 1e-3);
		assertEquals(0.500, fmap.get("0.5_1.0")[0], 1e-3);
		assertEquals(1.000, fmap.get("0.5_1.0")[1], 1e-3);
		assertEquals(0.750, fmap.get("0.75_1.5")[0], 1e-3);
		assertEquals(1.500, fmap.get("0.75_1.5")[1], 1e-3);
		assertEquals(1.000, fmap.get("1.0_2.0")[0], 1e-3);
		assertEquals(2.000, fmap.get("1.0_2.0")[1], 1e-3);
		assertEquals(1.250, fmap.get("1.25_2.5")[0], 1e-3);
		assertEquals(2.500, fmap.get("1.25_2.5")[1], 1e-3);
		assertEquals(1.500, fmap.get("1.5_3.0")[0], 1e-3);
		assertEquals(3.000, fmap.get("1.5_3.0")[1], 1e-3);
		assertEquals(2.000, fmap.get("2.0_4.0")[0], 1e-3);
		assertEquals(4.000, fmap.get("2.0_4.0")[1], 1e-3);
		assertEquals(2.500, fmap.get("2.5_5.0")[0], 1e-3);
		assertEquals(5.000, fmap.get("2.5_5.0")[1], 1e-3);
		assertEquals(3.000, fmap.get("3.0_6.0")[0], 1e-3);
		assertEquals(6.000, fmap.get("3.0_6.0")[1], 1e-3);
		assertEquals(4.000, fmap.get("4.0_6.0")[0], 1e-3);
		assertEquals(6.000, fmap.get("4.0_6.0")[1], 1e-3);
		assertEquals(4.000, fmap.get("4.0_8.0")[0], 1e-3);
		assertEquals(8.000, fmap.get("4.0_8.0")[1], 1e-3);
		assertEquals(6.000, fmap.get("6.0_12.0")[0], 1e-3);
		assertEquals(12.000, fmap.get("6.0_12.0")[1], 1e-3);
		assertEquals(6.000, fmap.get("6.0_8.0")[0], 1e-3);
		assertEquals(8.000, fmap.get("6.0_8.0")[1], 1e-3);
		assertEquals(8.000, fmap.get("8.0_10.0")[0], 1e-3);
		assertEquals(10.000, fmap.get("8.0_10.0")[1], 1e-3);
		assertEquals(8.000, fmap.get("8.0_16.0")[0], 1e-3);
		assertEquals(16.000, fmap.get("8.0_16.0")[1], 1e-3);
	}

}
