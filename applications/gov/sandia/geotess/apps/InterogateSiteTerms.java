package gov.sandia.geotess.apps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import gov.sandia.geotess.AttributeDataDefinitions;
import gov.sandia.geotess.Data;
import gov.sandia.geotess.extensions.siteterms.GeoTessModelSiteData;
import gov.sandia.geotess.extensions.siteterms.SiteData;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.globals.GMTFormat;
import gov.sandia.gmp.util.numerical.vector.EarthShape;

public class InterogateSiteTerms {

  public static void main(String[] args) {

    try {

      // read in all the site terms from the text file retrieved from the sandia geotess
      // website in July 2019.
      HashSet<String> stations = new HashSet<>(15000);
      Scanner input = new Scanner(new File("/Users/sballar/Documents/salsa3d/from_website/SALSA3D_site_terms.txt"));
      while (input.hasNextLine())
      {
        stations.add(input.next());
        input.nextLine();
      }
      input.close();
      System.out.println(stations.size());

      // instantiate a new AttributeDataDefinition for a single site correction.
      // (the model had 2 terms for P and S corrections but all S corrections were 0)
      AttributeDataDefinitions siteTermAttributes = new AttributeDataDefinitions(
          "TT_SITE_CORRECTION_P", "SECONDS", DataType.DOUBLE);

      // read in the original model that contains all site terms, including OUO stations
      GeoTessModelSiteData model = new GeoTessModelSiteData("/Users/sballar/Documents/salsa3d/from_mike_begnaud/salsa3d.3.0.Pphases.start_pred_5-1deg.geotess");
      //System.out.println(model);

      // write out all the site corrections, including OUO stations
      BufferedWriter output = new BufferedWriter(new FileWriter(new File(
          "/Users/sballar/Documents/salsa3d/from_mike_begnaud/SALSA3D_site_terms_OUO.txt")));
      for (Entry<String, ArrayList<SiteData>> e : model.getSiteTermMap().entrySet())
      {
        for (SiteData a : e.getValue())
          output.write(String.format("%-6s %s%n", e.getKey(), a));
      }
      output.close();

      // load the site terms from the original model, but don't include OUO stations
      Map<String, ArrayList<SiteData>> siteTerms = new TreeMap<String, ArrayList<SiteData>>();
      for (Entry<String, ArrayList<SiteData>> e : model.getSiteTermMap().entrySet())
        if (stations.contains(e.getKey()))
          for (SiteData data : e.getValue())
          {
            ArrayList<SiteData> list = siteTerms.get(e.getKey());
            if (list == null)
              siteTerms.put(e.getKey(), list = new ArrayList<SiteData>());

            SiteData newData = new SiteData(data.getStationLocation(), data.getStationRadius(), 
                data.getOnTime(), data.getOffTime(), 
                siteTermAttributes);

            newData.setSiteTerm(0, data.getSiteTerm(0));

            list.add(newData);
          }
      System.out.println(siteTerms.size());

      // look for stations that have more than one site term (for different on-off times)
      for (Entry<String, ArrayList<SiteData>> e : siteTerms.entrySet())
      {
        if (e.getValue().size() > 1)
        {
          System.out.println(e.getKey());
          for (SiteData d : e.getValue())
            System.out.println(d);
          System.out.println();
        }
      }

      // found 5 stations BOSA, DAG, EIL, KSRS and MJAR, but in each case, the
      // the site term values were exactly the same in the two time ranges.
      for (Entry<String, ArrayList<SiteData>> e : siteTerms.entrySet())
      {
        if (e.getValue().size() > 1)
        {
          SiteData data = e.getValue().get(0);
          data.setOnTime(GMTFormat.getEpochTime(1850001));
          data.setOffTime(GMTFormat.getOffTime(2286324));
          e.getValue().clear();
          e.getValue().add(data);
        }
      }

      // clear the site terms in the original model
      model.clearSiteTerms();

      // set a new AttributeDataDefinition that includes only P corrections
      model.setSiteAttributes("TT_SITE_CORRECTION_P", "SECONDS", DataType.DOUBLE);

      // set the site terms in teh model, including only non-OUO stations.
      model.setSiteTerms(siteTerms);

      System.out.println(model.getNSiteTermStations());
      System.out.println(model.getNSiteTerms());

      model.getMetaData().setDescription("This SALSA3D model is an updated version of the model presented in the paper: \n"+
          "Ballard, S., J. R. Hipp, M. L. Begnaud, C. J. Young, A. V. Encarnacao, E. P. \n"+
          "Chael, W. S. Phillips (2016) SALSA3D – A Tomographic Model of Compressional \n"+ 
          "Wave Slowness in the Earth’s Mantle For Improved Travel Time Prediction and \n"+ 
          "Travel Time Prediction Uncertainty, Bulletin of the Seismological Society of \n"+ 
          "America, Vol. 106, No. 6, pp. 2900-2916, December 2016, doi: 10.1785/0120150271. \n"+
          " \n"+
          "This version, developed with all the P data from the published version, \n"+
          "includes the addition of PKPdf, PKPbc, PcP, pP, and PP phases. \n"+
          " \n"+
          "This model can be interrogated for velocity structure by using the \n"+
          "GeoTessModelExplorer software. See Links and Software. \n"+
          " \n"+
          "To calculate a travel time + uncertainty for any source/receiver combination, \n"+
          "use the pCalc software (see Software). Note – Full 3D path-dependent \n"+
          "uncertainty calculations are only available for the pre-calculated Travel Time \n"+
          "Tables. pCalc will allow a calculation of travel time + 1D uncertainty \n"+
          "(uncertainty with distance). The 1D distance-dependent uncertainty will not be \n"+
          "as accurate as the full 3D uncertainty available in the Travel Time Tables. \n"+
          " \n"+
          "The SALSA3D_site_terms.txt file contains site terms (station or static \n"+
          "corrections) for the stations used in the SALSA3D inversion. The value \n"+
          "associated with each station in this file gets added to a travel time computed \n"+
          "through the SALSA3D model from any point to that station. The columns in the \n"+
          "site terms file are, in order: Station_Name Latitude Longitude Elevation(km) \n"+
          "P-Wave_Correction. When travel times are computed with PCalc software, the \n"+
          "site terms stored in this geotess model file are included in the computed \n"+
          "travel times.\n");

      model.writeModel("/Users/sballar/Documents/salsa3d/from_mike_begnaud/SALSA3D_NO_OUO.geotess");

      model = null;

      model = new GeoTessModelSiteData("/Users/sballar/Documents/salsa3d/from_mike_begnaud/SALSA3D_NO_OUO.geotess");
      
      System.out.println(model);

      // write out all the site corrections, including OUO stations
      output = new BufferedWriter(new FileWriter(new File(
          "/Users/sballar/Documents/salsa3d/from_mike_begnaud/SALSA3D_site_terms_NO_OUO.txt")));
      for (Entry<String, ArrayList<SiteData>> e : model.getSiteTermMap().entrySet())
      {
        for (SiteData a : e.getValue())
          output.write(String.format("%-6s %s %11.6f%n", e.getKey(), 
              a.getPositionString(EarthShape.WGS84), a.getSiteTerm(0)));
      }
      output.close();



    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
