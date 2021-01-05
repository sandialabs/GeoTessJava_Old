package gov.sandia.geotess.extensions.libcorr3d;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.sandia.geotess.GeoTessExplorer;
import gov.sandia.geotess.GeoTessGrid;
import gov.sandia.geotess.GeoTessModel;

public class LibCorr3DModelTests {
	
	File dir = new File("/Users/sballar/git/geo-tess-java/src/test/resources/permanent_files/libcorr3d_files");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		// get one of the models that was posted to the locoo3d public website in July 2020.  
		// It is a binary geotess file, with libcorr3d info stored in file format 1.
		//
		// resample the model onto a 32 degree grid to make it small and fast for testing.
		// Save it as ascii and geotess in formatVersion 1 and 2.
		//
		// In a separate c++ application, these files were read in with LibCorr3D C++ code
		// and written back out as files with names that start with 'cpp_'.
		
//	    LibCorr3DModel model = new LibCorr3DModel("/Users/sballar/Documents/locoo3d/release_2020_07_16/libcorr3d_models_tt_delta_ak135/Pmantle_geotess_tt_delta_ak135/AAK_Pmantle_TT.geotess");
//
//	    GeoTessGrid newGrid = new GeoTessGrid("/Users/sballar/git/geo-tess-java/src/test/resources/permanent_files/geotess_grid_32000.geotess");
//	    
//	    LibCorr3DModel newModel = (LibCorr3DModel) model.resample(newGrid);
//	    
//	    newModel.setFormatVersion(1);   
//	    newModel.writeModel("/Users/sballar/git/geo-tess-java/src/test/resources/permanent_files/libcorr3d_files/java_file_format_1.ascii");
//	    newModel.writeModel("/Users/sballar/git/geo-tess-java/src/test/resources/permanent_files/libcorr3d_files/java_file_format_1.geotess");
//	    newModel.setFormatVersion(2);   
//	    newModel.writeModel("/Users/sballar/git/geo-tess-java/src/test/resources/permanent_files/libcorr3d_files/java_file_format_2.ascii");
//	    newModel.writeModel("/Users/sballar/git/geo-tess-java/src/test/resources/permanent_files/libcorr3d_files/java_file_format_2.geotess");
//		
	    // Load a model from the website that that supports phase PKPdf.
		// Set supported phases to PKP and PKPdf.
		// Write the model out in geotess format, formatVersion 1 and 2.
		// With format version 1, the support for PKP will be lost.
		
//	    model = new LibCorr3DModel("/Users/sballar/Documents/locoo3d/release_2020_07_16/libcorr3d_models_tt_delta_ak135/PKPdf_geotess_tt_delta_ak135/AAK_PKPdf_TT.geotess");
//	    newModel = (LibCorr3DModel) model.resample(newGrid);
//		newModel.setSupportedPhasesString("PKPdf", "PKP, PKPdf");
//	    newModel.setFormatVersion(1);   
//	    newModel.writeModel("/Users/sballar/git/geo-tess-java/src/test/resources/permanent_files/libcorr3d_files/java_file_format_1_PKPdf.geotess");
//	    newModel.setFormatVersion(2);   
//	    newModel.writeModel("/Users/sballar/git/geo-tess-java/src/test/resources/permanent_files/libcorr3d_files/java_file_format_2_PKPdf.geotess");
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
	public void testLibCorr2D() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "libcorr2d.binary"), "..");
		assertEquals("ASAR", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(-1, model.getFormatVersion());
		assertEquals(2005165L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(0.6273, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testLibCorr2DModelTT() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "ASAR_P_TT_2005165_2286324"), "..");
		assertEquals("ASAR", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(0, model.getFormatVersion());
		assertEquals(2005165L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(0.6273, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testLibCorr2DModelAZ() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "ASAR_P_AZ_2005165_2286324"), "..");
		assertEquals("ASAR", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(-1, model.getFormatVersion());
		assertEquals(2005165L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(0.6273, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testLibCorr2DModelSLO() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "ASAR_P_SLO_2005165_2286324"), "..");
		assertEquals("ASAR", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(0, model.getFormatVersion());
		assertEquals(2005165L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(0.6273, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testJavaGeoTess_format1_PKP() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "java_file_format_1_PKPdf.geotess"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("PKPdf", model.getPhase());
		// the fact that the model supports both PKP and PKPdf is lost in formatVersion 1 files.
		assertEquals("[PKPdf]", model.getSupportedPhases().toString());
		assertEquals(1, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testJavaGeoTess_format2_PKP() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "java_file_format_2_PKPdf.geotess"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("PKPdf", model.getPhase());
		assertEquals("[PKP, PKPdf]", model.getSupportedPhases().toString());
		assertEquals(2, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testJavaAscii_format1() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "java_file_format_1.ascii"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(1, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testJavaGeoTess_format1() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "java_file_format_1.geotess"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(1, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testJavaAscii_format2() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "java_file_format_2.ascii"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(2, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testJavaGeoTess_format2() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "java_file_format_2.geotess"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(2, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}
	
	@Test
	public void testCPPAscii_format1() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "cpp_file_format_1.ascii"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(1, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testCPPGeoTess_format1() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "cpp_file_format_1.geotess"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(1, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testCPPAscii_format2() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "cpp_file_format_2.ascii"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(2, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}

	@Test
	public void testCPPGeoTess_format2() throws IOException {
		LibCorr3DModel model = new LibCorr3DModel(new File(dir, "cpp_file_format_2.geotess"), "..");
		assertEquals("AAK", model.getSite().getSta());
		assertEquals("Pmantle", model.getPhase());
		assertEquals("[P, Pn]", model.getSupportedPhases().toString());
		assertEquals(2, model.getFormatVersion());
		assertEquals(2007065L, model.getSite().getOndate());
		assertEquals(2286324L, model.getSite().getOffdate());
        assertEquals(1.645, model.getSite().getElev(), 1e-3);
	}
	
	@Test
	public void testAscii_format1() throws IOException {
		LibCorr3DModel java_model = new LibCorr3DModel(new File(dir, "java_file_format_1.ascii"), "..");
		LibCorr3DModel cpp_model = new LibCorr3DModel(new File(dir, "cpp_file_format_1.ascii"), "..");
		assertEquals(java_model, cpp_model);
	}

	@Test
	public void testAscii_format2() throws IOException {
		LibCorr3DModel java_model = new LibCorr3DModel(new File(dir, "java_file_format_2.ascii"), "..");
		LibCorr3DModel cpp_model = new LibCorr3DModel(new File(dir, "cpp_file_format_2.ascii"), "..");
		assertEquals(java_model, cpp_model);
	}

	@Test
	public void testGeoTess_format1() throws IOException {
		LibCorr3DModel java_model = new LibCorr3DModel(new File(dir, "java_file_format_1.geotess"), "..");
		LibCorr3DModel cpp_model = new LibCorr3DModel(new File(dir, "cpp_file_format_1.geotess"), "..");
		assertEquals(java_model, cpp_model);
	}

	@Test
	public void testGeoTess_format2() throws IOException {
		LibCorr3DModel java_model = new LibCorr3DModel(new File(dir, "java_file_format_2.geotess"), "..");
		LibCorr3DModel cpp_model = new LibCorr3DModel(new File(dir, "cpp_file_format_2.geotess"), "..");
		assertEquals(java_model, cpp_model);
	}

	@Test
	public void testModelIO() throws IOException {
		// load the java format 1 geotess model and compare it to models written with java and cpp code,
		// in formatVersion 1 and 2, and in ascii and geotess format.
		LibCorr3DModel model1 = new LibCorr3DModel(new File(dir, "java_file_format_1.geotess"), "..");
		for (String code : new String[] {"java", "cpp"})
			for (String format : new String[] {"1", "2"})
				for (String ext : new String[] {"ascii", "geotess"})
				{
					String f = String.format("%s_file_format_%s.%s", code, format, ext);
					if (f.equals("java_file_format_2.ascii"))
						System.out.println(f);
					LibCorr3DModel model2 = new LibCorr3DModel(new File(dir, f), "..");
					assertEquals(model1, model2);
				}
	}

}
