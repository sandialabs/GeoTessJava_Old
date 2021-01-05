package gov.sandia.geotess.extensions.rstt;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GeoTessModelSLBMTests {

	static private File homeDir, deletemeDir;

	static private ArrayList<File> temporaryFiles;
	
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  homeDir = new File("src/test/resources/rstt");
	  if (!homeDir.exists())
		  throw new Exception("homeDir = " + homeDir + " doesn't exist.");
	  deletemeDir = new File(homeDir, "deleteme");
	  deletemeDir.mkdir();

	  temporaryFiles = new ArrayList<File>();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
	  for (File f : temporaryFiles) 
		  f.delete();
	  deletemeDir.delete();
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAscii() throws Exception {
	  // Read SLBM model, write ascii, read it back in, assertEquals.
	  File originalFile = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess");
	  GeoTessModelSLBM model = new GeoTessModelSLBM(originalFile);
	  
	  File fout = new File(deletemeDir, "slbm_deleteme.ascii");
	  model.writeModel(fout);
	  temporaryFiles.add(fout);
	  
	  GeoTessModelSLBM model2 = new GeoTessModelSLBM(fout);
	  
	  assertEquals(model, model2);
  }

  @Test
  public void testBinary() throws Exception {
	  // Read SLBM model, write ascii, read it back in, assertEquals.
	  File originalFile = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess");
	  GeoTessModelSLBM model1 = new GeoTessModelSLBM(originalFile);
	  
	  File fout = new File(deletemeDir, "slbm_deleteme.geotess");
	  model1.writeModel(fout);
	  temporaryFiles.add(fout);
	  
	  GeoTessModelSLBM model2 = new GeoTessModelSLBM(fout);
	  
	  for (int p=0; p<4; ++p)
		  for (int a=0; a<3; ++a)
			  assertEquals(model1.getPathIndependentUncertainty()[p][a],
					  model2.getPathIndependentUncertainty()[p][a]);
	  
	  assertEquals(model1, model2);
  }

  @Test
  public void testAsciiNoPDU() throws Exception {
	  // Read SLBM model, write ascii, read it back in, assertEquals.
	  File originalFile = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess");
	  GeoTessModelSLBM model0 = new GeoTessModelSLBM(originalFile);
	  
	  // read model again.
	  GeoTessModelSLBM model1 = new GeoTessModelSLBM(originalFile);
	  
	  // they had better be equal!
	  assertTrue(model0.equals(model1));
	  
	  // clear the pdu from model1
	  model1.clearPathDependentUncertainty();
	  
	  // now they are not equal
	  assertFalse(model0.equals(model1));

	  // write out the no pdu model
	  File fout = new File(deletemeDir, "slbm_no_pdu.ascii");
	  model1.writeModel(fout);
	  temporaryFiles.add(fout);
	  
	  // read the no pdu model back in.
	  GeoTessModelSLBM model2 = new GeoTessModelSLBM(fout);
	  
	  for (int p=0; p<4; ++p)
		  for (int a=0; a<3; ++a)
			  assertEquals(model1.getPathIndependentUncertainty()[p][a],
					  model2.getPathIndependentUncertainty()[p][a]);
	  
	  // should be equal
	  assertEquals(model1, model2);
  }

  @Test
  public void testBinaryNoPDU() throws Exception {
	  // Read SLBM model, write ascii, read it back in, assertEquals.
	  File originalFile = new File(homeDir, "geotess_model_slbm_with_pdu_very_small.geotess");
	  GeoTessModelSLBM model0 = new GeoTessModelSLBM(originalFile);
	  
	  // read model again.
	  GeoTessModelSLBM model1 = new GeoTessModelSLBM(originalFile);
	  
	  // they had better be equal!
	  assertTrue(model0.equals(model1));
	  
	  // clear the pdu from model1
	  model1.clearPathDependentUncertainty();
	  
	  // now they are not equal
	  assertFalse(model0.equals(model1));

	  // write out the no pdu model in geotess binary format
	  File fout = new File(deletemeDir, "slbm_no_pdu.geotess");
	  model1.writeModel(fout);
	  temporaryFiles.add(fout);
	  
	  // read the no pdu model back in.
	  GeoTessModelSLBM model2 = new GeoTessModelSLBM(fout);
	  
	  // should be equal
	  assertEquals(model1, model2);
  }
  
  @Test
  public void testCheckMiddleCrustLayers() throws Exception {
	  File originalFile = new File(homeDir, "rstt201404um.geotess");
	  GeoTessModelSLBM model = new GeoTessModelSLBM(originalFile);
	  System.out.println(model);
	  
	  // the model loaded suffered from the issue of having layers
	  // middle_crust_N and middle_crust_G in the wrong order.
	  // Current code reverses the order so it is now correct.
	  //
	  // wrong: layer 3 = middle_crust_N and layer 4 = middle_crust_G.
	  // right: layer 3 = middle_crust_G and layer 4 = middle_crust_N.
	  
	  // the order of the layers was corrected when the model was loaded.
	  assertEquals("middle_crust_G", model.getMetaData().getLayerName(3));
	  assertEquals("middle_crust_N", model.getMetaData().getLayerName(4));
	  
	  assertEquals(6.199713f, model.getValueFloat(0, 3, 0, 0), 1e-4);
	  assertEquals(6.6f,      model.getValueFloat(0, 4, 0, 0), 1e-4);
	  
  }
}
