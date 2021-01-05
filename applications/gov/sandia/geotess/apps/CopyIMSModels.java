package gov.sandia.geotess.apps;

import java.io.File;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.extensions.siteterms.Site;

public class CopyIMSModels {

	public static void main(String[] args) 
	{
		try {
			File inputDir = new File("Z:\\TTLookup\\2013_10_29_TTLookup_130521_SALSA_only_1deg_IMS\\run_damp100_NR0.012\\libcorr3d_models_damp100_nr0.012\\models");
			File outputDir = new File("T:\\salsa3d_website\\models\\130521\\lookup_tables");
			
			outputDir.mkdirs();
			
			File outputGridFile = new File(outputDir, "grid_01000.geotess");
			
			//System.out.println(new GeoTessModel(new File(outputDir, "AFI.geotess")));  System.exit(0);

			int count = 0;
			for (File f : inputDir.listFiles())
				if (GeoTessModel.isGeoTessModel(f))
				{
					GeoTessModel m = new GeoTessModel(f);
					
					Site site = new Site();
					
					for (String s : m.getMetaData().getDescription().split("\n"))
						if (s.startsWith("sta:"))
							site.setSta(s.split(":")[1].trim());
						else if (s.startsWith("staname:"))
							site.setStaname(s.split(":")[1].trim());
						else if (s.startsWith("lat:"))
							site.setLat(Double.parseDouble(s.split(":")[1].trim()));
						else if (s.startsWith("lon:"))
							site.setLon(Double.parseDouble(s.split(":")[1].trim()));
						else if (s.startsWith("elev:"))
							site.setElev(Double.parseDouble(s.split(":")[1].trim()));
						else if (s.startsWith("ondate:"))
							site.setOndate(Integer.parseInt(s.split(":")[1].trim()));
						else if (s.startsWith("offdate:"))
							site.setOffdate(Integer.parseInt(s.split(":")[1].trim()));
						else if (s.startsWith("statype:"))
							site.setStatype(s.split(":")[1].trim());
						else if (s.startsWith("refsta:"))
							site.setRefsta(s.split(":")[1].trim());
						else if (s.startsWith("dnorth:"))
							site.setDnorth(Double.parseDouble(s.split(":")[1].trim()));
						else if (s.startsWith("deast:"))
							site.setDeast(Double.parseDouble(s.split(":")[1].trim()));
					
					System.out.printf("%-6s %11.5f %11.5f %8.3f %7d %7d %s%n",
							site.getSta(),
							site.getLat(),
							site.getLon(),
							site.getElev(),
							site.getOndate(),
							site.getOffdate(),
							site.getStaname()
							); 
					
					m.writeModel(new File(outputDir, f.getName().replace("libcorr3d", "geotess")), outputGridFile);
				}
			
			System.out.println("Done.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
