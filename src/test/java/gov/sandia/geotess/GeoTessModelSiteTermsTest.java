package gov.sandia.geotess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

import gov.sandia.geotess.AttributeDataDefinitions;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.extensions.siteterms.GeoTessModelSiteData;
import gov.sandia.gmp.util.globals.DataType;

/**
 * Tests site term extensions to GeoTessModel.
 * 
 * @author jrhipp
 *
 */
public class GeoTessModelSiteTermsTest
{

	@Test
	public void testSiteTermsModel() throws Exception
	{
			String resource = "/permanent_files/unified_crust20_ak135.geotess";

			InputStream inp = GeoTessModelExtendedTest.class.getResourceAsStream(resource);

			if (inp == null)
				throw new IOException("Cannot find resource "+resource);

			GeoTessModel baseModel = new GeoTessModel(new DataInputStream(inp));

			AttributeDataDefinitions siteTermAttributes = new AttributeDataDefinitions(
					"PSLOWNESS", "sec/km", DataType.DOUBLE);
			
			// create site term model from base model
			GeoTessModelSiteData model = new GeoTessModelSiteData(baseModel, siteTermAttributes);

			// read in the site terms
			InputStream siteTermResource = GeoTessModelSiteData.class.getResourceAsStream(
					"/permanent_files/siteTerms.ascii");
			model.setSiteTerms(new Scanner(siteTermResource));

			//MJAR  36.54270000000001  138.207  0.40599999999994907  -3.7868256E9  1.109635199999E9  -0.07677902432915866
			//MJAR  36.524717  138.24718  0.6616999999996551  1.1096352E9  1.0000022399999E10  -0.07677902432915866
			//BOSA  -28.614066000000008  25.255484  1.3058000000000902  9.451296E8  1.0000022399999E10  -0.0490978155146058
			//BOSA  -28.6131  25.415600000000005  1.2020000000002256  -3.7868256E9  9.45129599999E8  -0.0490978155146058

			//XYZ  39.279999999999994  46.37999999999999  1.074760000000424  -3.7868256E9  1.0000022399999E10  1.2

			// add one more site term
			String sta = "XYZ";                    //**** set a name
			double[] staUnitVector = baseModel.getMetaData().getEarthShape().getVectorDegrees(39.279999999999994, 46.37999999999999);
			double   staRadius     = baseModel.getMetaData().getEarthShape().getEarthRadius(staUnitVector) - 1.074760000000424; 
			double   staOnTime     = -3.7868256E9;
			double   staOffTime    = 1.0000022399999E10;
			double[]  siteTerm      = {1.2};
			model.addSiteTerm(sta, staUnitVector, staRadius, staOnTime, staOffTime, siteTerm);

			// output site term history
			//System.out.println(model.getSiteTermHistory());
			//System.out.println(model.getSiteTermHistory(sta));

			// check get site term validity
			double epochTime  = 1.0;                   //**** set epoch  time
			double originTime = 0.5;                   //**** set origin time
			double tt         = 0.5;                   //**** set travel time
			double st0 = model.getSiteTerm(0, sta, epochTime);
			double st1 = model.getSiteTerm(0, sta, tt, originTime);
			assertEquals(st0, 1.2, 0.0);
			assertEquals(st1, 1.2, 0.0);

			// write model to byte array in binary format
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			model.writeModelBinary(baos);
			
			GeoTessModelSiteData newModel = new GeoTessModelSiteData(new DataInputStream(
					new ByteArrayInputStream(baos.toByteArray())));
			
			//assertTrue(newModel.equals(model));
			
			// now test the ascii version
			baos = new ByteArrayOutputStream();
			model.writeModelAscii(baos);
			
			//model.writeModelAscii(new FileOutputStream(new File("C:\\Users\\sballar\\deleteme.ascii")));
			
			newModel = new GeoTessModelSiteData(new Scanner(new ByteArrayInputStream(baos.toByteArray())));
			
			assertTrue(newModel.equals(model));
	}

}
