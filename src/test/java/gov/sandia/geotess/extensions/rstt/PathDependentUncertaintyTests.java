package gov.sandia.geotess.extensions.rstt;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.sandia.geotess.GeoTessGrid;

public class PathDependentUncertaintyTests {

	static File homeDir, deletemeDir;

	ArrayList<File> temporaryFiles;
	
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  homeDir = new File("src/test/resources/rstt");
	  if (!homeDir.exists())
		  throw new Exception("homeDir = " + homeDir + " doesn't exist.");
	  deletemeDir = new File(homeDir, "deleteme");
	  deletemeDir.mkdir();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
	  deletemeDir.delete();
  }

  @Before
  public void setUp() throws Exception {
	  temporaryFiles = new ArrayList<File>();
  }

  @After
  public void tearDown() throws Exception {
	  for (File f : temporaryFiles) f.delete();
  }

//  @Test
//  public void testReadSLBMGeoTess() throws Exception {
//	  File f = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.ascii");
//	  System.out.println(f);
//	  GeoTessModelSLBM model = new GeoTessModelSLBM(f);
//	  model.getMetaData().setModelFileFormat(3);
//	  model.writeModel(new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess"));
//	  
//	  GeoTessModelSLBM model2 = new GeoTessModelSLBM(new File(homeDir, 
//			  "geotess_model_slbm_with_pdu_very_small.geotess"));
//	  
//	  assertEquals(model, model2);
//  }

  @Test
  public void testAscii() throws Exception {
	  // Read SLBM model, write PDU ascii, read it back in, assertEquals.
	  File originalFile = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess");
	  GeoTessModelSLBM model = new GeoTessModelSLBM(originalFile);
	  
	  UncertaintyPDU pdu = model.getPathDependentUncertainty("Pg");
	  File fout = new File(deletemeDir, "pdu.txt");
	  pdu.writeFileAscii(fout);
	  temporaryFiles.add(fout);
	  
	  UncertaintyPDU pdu2 = new UncertaintyPDU(fout);
	  
	  assertEquals(pdu, pdu2);
  }

  @Test
  public void testBinary() throws Exception {
	  // Read SLBM model, write PDU binary, read it back in, assertEquals.
	  File originalFile = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess");
	  GeoTessModelSLBM model = new GeoTessModelSLBM(originalFile);
	  
	  UncertaintyPDU pdu = model.getPathDependentUncertainty("Pg");
	  File fout = new File(deletemeDir, "pdu.binary");
	  pdu.writeFileBinary(fout);
	  temporaryFiles.add(fout);
	  
	  UncertaintyPDU pdu2 = new UncertaintyPDU(fout);
	  
	  assertEquals(pdu, pdu2);
  }

  @Test
  public void testGeoTessBinary() throws Exception {
	  // Read SLBM model, GeoTessSLBMPDU binary, read it back in, assertEquals.
	  File originalFile = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess");
	  GeoTessModelSLBM model = new GeoTessModelSLBM(originalFile);
	  
	  UncertaintyPDU pdu = model.getPathDependentUncertainty("Pg");
	  File fout = new File(deletemeDir, "pdu.geotess");
	  pdu.writeFileGeoTess(fout);
	  temporaryFiles.add(fout);
	  
	  UncertaintyPDU pdu2 = new UncertaintyPDU(fout);
	  
	  assertEquals(pdu, pdu2);
  }

  @Test
  public void testGeoTessAscii() throws Exception {
	  // Read SLBM model, write GeoTessSLBMPDU ascii, read it back in, assertEquals.
	  File originalFile = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess");
	  GeoTessModelSLBM model = new GeoTessModelSLBM(originalFile);
	  
	  UncertaintyPDU pdu = model.getPathDependentUncertainty("Pg");
	  File fout = new File(deletemeDir, "pdu_geotess.ascii");
	  pdu.writeFileGeoTess(fout);
	  temporaryFiles.add(fout);
	  
	  UncertaintyPDU pdu2 = new UncertaintyPDU(fout);
	  
	  assertEquals(pdu, pdu2);
  }

  @Test
  public void testResample() throws Exception {
	  // Read SLBM model, which has a 32 degree grid.
	  File originalFile = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess");
	  GeoTessModelSLBM model32 = new GeoTessModelSLBM(originalFile);
	  
	  assertEquals(42, model32.getNVertices());
	  assertEquals(42, model32.getPathDependentUncertainty()[0].getNVertices());
	  
	  GeoTessGrid grid16 = new GeoTessGrid(new File(homeDir, "../permanent_files/geotess_grid_16000.geotess"));
	  
	  GeoTessModelSLBM model16 = (GeoTessModelSLBM) model32.resample(grid16);
	  
	  assertEquals(162, model16.getNVertices());
	  
	  assertEquals(model16.getNVertices(), model16.getPathDependentUncertainty()[0].getNVertices());
	  assertEquals(model16.getNVertices(), model16.getPathDependentUncertainty()[1].getNVertices());
	  assertEquals(model16.getNVertices(), model16.getPathDependentUncertainty()[2].getNVertices());
	  assertEquals(model16.getNVertices(), model16.getPathDependentUncertainty()[3].getNVertices());
	  
	  //System.out.println(model16);
	  

	  
  }

}
