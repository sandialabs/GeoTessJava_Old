package gov.sandia.geotess.extensions.siteterms;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import gov.sandia.geotess.AttributeDataDefinitions;
import gov.sandia.geotess.GeoTessModel;

public class TestSiteTerms {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void test() throws Exception {
    File modelFile = new File("/Users/sballar/Documents/locoo3d/release_2020_07_16/SALSA3D_Model/SALSA3D.geotess");
    
    assertEquals("GeoTessModelSiteData", GeoTessModel.getClassName(modelFile));
    
    GeoTessModelSiteData model = new GeoTessModelSiteData(modelFile);
    
    File deletemeDir = new File("/Users/sballar/Documents/deleteme");
    
    File siteTermFile = new File(deletemeDir, "siteterms.txt");
    FileWriter output = new FileWriter(siteTermFile);
    output.write(String.format("attributes: %s%n", model.getSiteTermAttributes().getAttributeNamesString()));
    output.write(String.format("units: %s%n", model.getSiteTermAttributes().getAttributeUnitsString()));
    output.write(model.getSiteTermAttributes().getDataType().toString()+"\n");
    output.write(model.getSiteTermHistory());
    output.close();
    

    // load the input model, ignoring any site terms it may contain.
    GeoTessModel inputModel = new GeoTessModel(modelFile);
    
    inputModel.writeModel(new File(deletemeDir, "no_site_terms.geotess"));
    
    assertEquals("GeoTessModel", GeoTessModel.getClassName(new File(deletemeDir, "no_site_terms.geotess")));
    
    inputModel = new GeoTessModel(new File(deletemeDir, "no_site_terms.geotess"));

    Scanner siteTermScanner = new Scanner(siteTermFile);

    // read the siteterm attribute definitions from the site term file.
    AttributeDataDefinitions attributes = new AttributeDataDefinitions(siteTermScanner);
    attributes.setDataType(siteTermScanner.nextLine());

    // make a shallow copy of the input model.
    GeoTessModelSiteData outputModel = new GeoTessModelSiteData(inputModel, attributes);

    // read site term info from file and set the values in the output model.
    outputModel.setSiteTerms(siteTermScanner);
    siteTermScanner.close();
    
    outputModel.writeModel(new File(deletemeDir, "output_model.geotess"));
    
    GeoTessModelSiteData newModel = new GeoTessModelSiteData(new File(deletemeDir, "output_model.geotess"));
    
    System.out.println(newModel);

}

}
