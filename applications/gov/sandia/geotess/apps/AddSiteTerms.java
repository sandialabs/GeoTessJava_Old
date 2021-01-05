package gov.sandia.geotess.apps;

import gov.sandia.geotess.AttributeDataDefinitions;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.extensions.siteterms.GeoTessModelSiteData;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.globals.GMTFormat;
import gov.sandia.gmp.util.numerical.vector.Vector3D;
import gov.sandia.gmp.util.numerical.vector.VectorGeo;

import java.io.File;
import java.util.Scanner;

public class AddSiteTerms {

	public static void main(String[] args) {
	
		try {
			
			GeoTessModel baseModel = new GeoTessModel(
					"/Users/sballar/Downloads/SALSA3D_Model/SALSA3D.geotess");
			
			AttributeDataDefinitions siteTermAttributes = new AttributeDataDefinitions(
					"TT_SITE_CORRECTION_P", "SECONDS", DataType.DOUBLE);
			
			GeoTessModelSiteData model = new GeoTessModelSiteData(baseModel, siteTermAttributes);
			
			Scanner st = new Scanner(new File(
					"/Users/sballar/Downloads/SALSA3D_Model/SALSA3D_site_terms.txt"));
			
			double lat, lon, depth=0., ton, toff, st1, st2;
			
			ton = GMTFormat.getEpochTime(-1);
			toff = GMTFormat.getEpochTime(2286324);
			
			System.out.println(ton+"  "+toff);
			
			while (st.hasNext())
			{
				String sta = st.next();
				lat = st.nextDouble();
				lon = st.nextDouble();
//				depth = st.nextDouble();
//				ton = st.nextDouble();
//				st.next();
//				toff = st.nextDouble();
//				st.next();
				st1 = st.nextDouble();
//				st2 = st.nextDouble();
				
				double[] u =  Vector3D.getVectorDegrees(lat, lon);
				double radius = VectorGeo.getEarthRadius(u)-depth;
				
				model.addSiteTerm(sta, u, radius, ton, toff, new double[] {st1});
			}
			
			st.close();
			
			//model.getMetaData().setDescription("PSLOWNESS values come from tomography model 2014_05_09_tomo140317_3D2.  SSLOWNESS values come from model 2014_11_05_tomo141007_1D2");
			
			model.writeModel("/Users/sballar/Downloads/SALSA3D_Model/SALSA3D_with_siteterms.geotess");
			
			System.out.println(model);
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

}
