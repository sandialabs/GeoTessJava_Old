/**
 * Copyright 2009 Sandia Corporation. Under the terms of Contract
 * DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
 * retains certain rights in this software.
 * 
 * BSD Open Source License.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *    * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *    * Neither the name of Sandia National Laboratories nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package gov.sandia.geotess.extensions.libcorr3d;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.gmp.util.containers.arraylist.ArrayListInt;
import gov.sandia.gmp.util.containers.multilevelmap.MultiLevelMap;
import gov.sandia.gmp.util.globals.GMTFormat;
import gov.sandia.gmp.util.globals.Site;
import gov.sandia.gmp.util.logmanager.ScreenWriterOutput;

/**
 * <p>
 * Assumption: stations are unique/equal based on station name and onTime. If
 * two stations have the same onTime they are assumed to be equal and therefore
 * interchangeable. If names or onTimes are different the stations are assumed
 * to be different stations.
 * 
 * <p>
 * This class is thread-safe after the constructor has executed.
 * 
 * @author sballar
 * 
 */
public class LibCorr3DModels
{

  private static int nextIndex;
  public final int index;

  /**
   * Path to directory where model files are stored.
   */
  private final File rootDirectory;

  /**
   * Relative path from lc3dRootPath to the directory where grid files are
   * stored.
   */
  private final String gridRelPath;

  /**
   * Map from Site -&gt; String phase -&gt; String attribute -&gt; index of model in
   * the models array.
   */
  private MultiLevelMap supportMap;

  /**
   * ArrayList of references to models. There will likely be multiple entries
   * for each instance of a LibCorr3DModel that support different phases and
   * attributes. The indexes stored in supportMap point into this array.
   */
  private ArrayList<LibCorr3DModel> models;

  /**
   * Model file names. Names of actual files will likely appear in this list
   * multiple times, once for each station/phase/attribute combination. The
   * indexes correspond to the indexes of the models in the models array. The
   * indexes in supportMap point into this array.
   */
  private ArrayList<File> modelFileNames;

  /**
   * map from a fileName to the indexes in supportMap that link to it.
   */
  private Map<File, ArrayListInt> modelIndexes;;

  /**
   * Map from station name -&gt; array list of Sites that have that name but
   * presumably have different onTimes. In the ArrayList, stations are in
   * order of decreasing onTime.
   */
  private TreeMap<String, ArrayList<Site>> stationNames;

  /**
   * Total number of stations, not unique station names. For a  with
   * entries for multiple on-off times, each entry contributes to nSites.
   */
  private int nSites;

  /**
   * list of unique phases supported by any model.
   */
  private TreeSet<String> supportedPhases = new TreeSet<String>();

  /**
   * list of unique attributes supported by any model.
   */
  private TreeSet<String> supportedAttributes = new TreeSet<String>();

  /**
   * Standard constructor. Visits the rootPath directory and tries to read
   * every file in it to see if the file is a valid LibCorr3DModel. For every
   * file that is a valid model, the station name and all the supported phases
   * and attributes are extracted and stored in a MultiLevelMap. If
   * preloadModels is true, references are stored to all the loaded models. If
   * not, models are released for garbage collection and will have to be
   * reloaded from file if/when they are requested.
   * 
   * @param rootPath
   *            the name of the directory where models are stored.
   * @param relGridPath
   *            the relative path from rootPath to the directory where grid
   *            files are stored.
   * @param preloadModels
   *            if true, all the models in the rootPath are loaded right away.
   *            If false, lazy evaluation is used where models are loaded the
   *            first time they are requested and stored for the duration.
   * @throws IOException
   */
  public LibCorr3DModels(File rootPath, String relGridPath, boolean preloadModels)
      throws IOException
  {
    this(rootPath, relGridPath, preloadModels, null);
  }

  /**
   * Standard constructor. Visits the rootPath directory and tries to read
   * every file in it to see if the file is a valid LibCorr3DModel. For every
   * file that is a valid model, the station name and all the supported phases
   * and attributes are extracted and stored in a MultiLevelMap. If
   * preloadModels is true, references are stored to all the loaded models. If
   * not, models are released for garbage collection and will have to be
   * reloaded from file if/when they are requested.
   * 
   * @param rootDirectory
   *            the name of the directory where models are stored.
   * @param relGridPath
   *            the relative path from rootPath to the directory where grid
   *            files are stored.
   * @param preloadModels
   *            if true, all the models in the rootPath are loaded right away.
   *            If false, lazy evaluation is used where models are loaded the
   *            first time they are requested and stored for the duration.
   * @throws IOException
   */
  public LibCorr3DModels(File rootDirectory, String relGridPath,
      boolean preloadModels, ScreenWriterOutput logger)
          throws IOException
  {
    if (!rootDirectory.exists())
      throw new IOException(String.format(
          "%nrootPath does not exist%n%s%n", rootDirectory.getPath()));

    this.index = nextIndex++;

    this.rootDirectory = rootDirectory;

    supportMap = new MultiLevelMap();
    models = new ArrayList<LibCorr3DModel>();
    modelFileNames = new ArrayList<File>();

    // map from a fileName to the indexes in supportMap that link to it.
    modelIndexes = new TreeMap<File, ArrayListInt>();

    // set of unique stations loaded from the model files.
    TreeSet<Site> stations = new TreeSet<Site>();

    long timer = System.nanoTime();

    // if rootDirectory has a subdirectory named 'tt' then subdirectories
    // 'tt', 'az', and 'slo' will all be searched for path correction models.
    // Otherwise, rootDirectory itself will be searched.
    File[] directories;
    if (new File(rootDirectory, "tt").exists())
      directories = new File[] { new File(rootDirectory, "tt"),
          new File(rootDirectory, "az"), new File(rootDirectory, "slo") , new File(rootDirectory, "sh")};
    else
      directories = new File[] { rootDirectory };

    // search for the relative grid path if it the specified one does not exist.
    File gridPath = new File(rootDirectory, relGridPath);
    if (relGridPath.endsWith("tess") && !gridPath.exists())
    {
      if (new File(directories[0], "tess").exists())
        relGridPath = "tess";
      else if (new File(directories[0], "../tess").exists())
        relGridPath = "../tess";
      else if (new File(directories[0], "../../tess").exists())
        relGridPath = "../../tess";
    }

    this.gridRelPath = relGridPath;

    for (File directory : directories)
      if (directory.exists())
      {
        Path dirPath = Paths.get(directory.getAbsolutePath());

        File supportFile = new File(directory, "_supportMap.txt");

        if (!supportFile.exists())
        {
        	if (logger != null && logger.getVerbosity() > 0)
        		logger.writeln("LibCorr3DModels is generating default _supportMap.txt file.");

          // load all the files and extract station, phase, attribute
          // information
          ArrayList<File> files = new ArrayList<File>(500);
          discoverFiles(rootDirectory, files);
          for (File modelFile : files)
          {
            try
            {
              LibCorr3DModel model = new LibCorr3DModel(
                  modelFile, relGridPath);

              // ++nModels;

              stations.add(model.getSite());
              supportedPhases.addAll(model.getSupportedPhases());
              model.getMetaData().getAttributeNames(
                  supportedAttributes);

              for (String phase : model.getSupportedPhases())
                for (String attribute : model.getMetaData()
                    .getAttributeNames())
                {
                  int index = supportMap.addEntry(
                      model.getSite(), phase,
                      attribute);
                  while (models.size() <= index)
                  {
                    models.add(null);
                    modelFileNames.add(null);
                  }

                  modelFileNames.set(index, modelFile);

                  ArrayListInt indices = modelIndexes
                      .get(modelFile);
                  if (indices == null)
                  {
                    indices = new ArrayListInt();
                    modelIndexes.put(modelFile, indices);
                  }
                  indices.add(index);

                  if (preloadModels)
                    models.set(index, model);
                }
            }
            catch (IOException e)
            {
              if (!e.getMessage().contains(
                  "Expected file to start with GEOTESSMODEL"))
                throw new IOException(e);
            }
          }

          timer = System.nanoTime() - timer;
          if (logger != null && logger.getVerbosity() > 0)
          {
            if (preloadModels)
              logger.write(String.format("LibCorr3DModels constructor: loaded %d models in %1.3f seconds\n",
                  models.size(), timer * 1e-9));
            else
              logger.write(String.format("LibCorr3DModels constructor: analyzed %d models in %1.3f seconds\n",
                  models.size(), timer * 1e-9));
          }
          // write the supportMap.txt file
          try
          {
            Writer output = new BufferedWriter(new FileWriter(supportFile));
            output.write(String.format("# Generated automatically by LibCorr3DModels.java %s %s\n",
                System.getProperty("user.name", "???"), GMTFormat.getNow()));
            output.write(String.format("# LibCorr3D model directory: %s\n", rootDirectory.getCanonicalPath()));
            output.write("# Every LibCorr3D model is associated only to a station corresponding to the station used to generate the model. \n");
            output.write(String.format("%-29s %-6s %-6s %7s %7s %13s %14s %9s %-9s %s%n",
                "# model", "sta", "refsta", "ondate",
                "offdate", "lat", "lon", "elev", "ph",
                "attribute"));

            ArrayList<String> records = new ArrayList<String>(stations.size()*supportedPhases.size()*supportedAttributes.size());

            for (Site station : stations)
              for (String phase : supportedPhases)
                for (String attribute : supportedAttributes)
                {
                  int index = supportMap.getIndex(station,
                      phase, attribute);
                  if (index >= 0)
                  {
                    Path modelPath = Paths.get(modelFileNames.get(index).getAbsolutePath());

                    records.add(String.format("%-29s %-6s %-6s %7d %7d %13.6f %14.6f %9.3f %-6s %s%n",
                        dirPath.relativize(modelPath), 
                        station.getSta(), station.getRefsta(), station.getOndate(), station.getOffdate(), station.getLat(),
                        station.getLon(), station.getElev(),
                        phase, attribute));
                  }
                }
            Collections.sort(records);

            for (String record : records)
              output.write(record);

            output.close();
            if (logger != null && logger.getVerbosity() > 0)
              logger.writeln(String.format("LibCorr3DModels constructor wrote file _supportMap.txt with %d stations and %d records",
                  stations.size(), records.size()));
          }
          catch (Exception ex)
          {
            System.out.println("LibCorr3DModels constructor: writing file _supportMap.txt failed");

          }
        }
        else
        {
          // supportFile.exists.

          File modelFile;
          Site station;
          String phase, attribute;

          // missing models are models that are specified in the
          // supportMap.txt file
          // but which do not exist in the file system.
          HashSet<File> missingModels = new HashSet<File>();

          Scanner input = new Scanner(supportFile);

          while (input.hasNext())
          {
            String line = input.nextLine().trim();
            if (line.startsWith("#"))
              continue;
            Scanner record = new Scanner(line);
            try
            {
              modelFile = dirPath.resolve(record.next()).toFile();
              
//              # model                       sta    refsta  ondate offdate           lat            lon      elev ph        attribute
//              AAK_P_salsa3_10km.geotess     AAK    AAK       2364 2286324     42.633300      74.494400     1.680 P      TT_DELTA_AK135
//              AAK_P_salsa3_10km.geotess     AAK    AAK       2364 2286324     42.633300      74.494400     1.680 P      TT_MODEL_UNCERTAINTY
              station = new Site();
              station.setSta(record.next());
              station.setRefsta(record.next());
              station.setOndate(record.nextLong());
              station.setOffdate(record.nextLong());
              station.setLat(record.nextDouble());
              station.setLon(record.nextDouble());
              station.setElev(record.nextDouble());

              phase = record.next().trim();

              attribute = record.next().trim();

              // want to add info if the modelFile exists but want
              // to avoid checking for modelFile existence as much
              // as possible
              if (!missingModels.contains(modelFile)
                  && (modelIndexes.keySet().contains(
                      modelFile) || modelFile.exists()))
              {
                stations.add(station);
                supportedPhases.add(phase);
                supportedAttributes.add(attribute);

                int index = supportMap.addEntry(station, phase,
                    attribute);

                ArrayListInt indices = modelIndexes.get(modelFile);
                if (indices == null)
                {
                  indices = new ArrayListInt();
                  modelIndexes.put(modelFile, indices);
                }
                indices.add(index);
              }
              else
                missingModels.add(modelFile);
            }
            catch (Exception ex)
            {
              // simply skip lines that don't parse properly
            }
            record.close();
          }
          input.close();

          // ensure that models and modelFileNames are each of length
          // supportMap.size() and are filled with null.
          models.ensureCapacity(supportMap.size());
          modelFileNames.ensureCapacity(supportMap.size());
          for (int i = 0; i < supportMap.size(); ++i)
          {
            models.add(null);
            modelFileNames.add(null);
          }

          // populate arraylist of model file names. Length is
          // supportMap.size()
          // Each element is the model File that supports that
          // supportMap entry.
          for (Entry<File, ArrayListInt> entry : modelIndexes
              .entrySet())
            // key is a model file name and value is list of indexes
            // associated to that model
            for (int i = 0; i < entry.getValue().size(); ++i)
              modelFileNames.set(entry.getValue().get(i),
                  entry.getKey());

          // populate arraylist of models. Length is supportMap.size()
          // Each element is the model that supports that supportMap
          // entry.
          if (preloadModels)
            for (Entry<File, ArrayListInt> entry : modelIndexes
                .entrySet())
            {
              // key is a model file name and value is list of
              // indexes associated to that model
              LibCorr3DModel model = new LibCorr3DModel(
                  entry.getKey(), relGridPath);
              for (int i = 0; i < entry.getValue().size(); ++i)
                models.set(entry.getValue().get(i), model);
            }

          timer = System.nanoTime() - timer;

          if (missingModels.size() > 0)
          {
            System.out.println();
            System.out.printf("Problem in LibCorr3DModels.  The following LibCorr3D models were specified in %s%nbut do not exist in the file system:%n",
                supportFile.getCanonicalPath());
            for (File f : missingModels)
              System.out.printf("   %s%n", f.getCanonicalPath());
            System.out.println();
            System.out.println();
          }

          if (logger != null && logger.getVerbosity() > 0)
          {
            logger.writef("LibCorr3DModels loaded info about %d models %nand %d station-phase-attribute combinations%n"
                + "from file %s%nin %1.3f seconds%n%n",
                modelIndexes.size(), models.size(),
                supportFile.getCanonicalPath(),
                timer * 1e-9);

            logger.writeln("LibCorr3D model : associated station names:\n" + getModelSiteMapToString());
          }

          // String errors = testSupportMap();
          // if (errors.length() > 0)
          // throw new
          // IOException(String.format("\nERROR in LibCorr3DModels after reading %s%n%s",
          // supportFile, errors));

        }
      }

    // Total number of stations, not unique station names. For
    // a  with entries for multiple on-off times, each
    // entry contributes to nSites.
    nSites = stations.size();

    // build a map from station name -> list of Sites.
    stationNames = new TreeMap<String, ArrayList<Site>>();
    for (Site station : stations)
    {
      ArrayList<Site> staList = stationNames.get(station.getSta());
      if (staList == null)
      {
        staList = new ArrayList<Site>();
        stationNames.put(station.getSta(), staList);
      }
      staList.add(station);
    }

    // sort the station lists by onTime.
    for (ArrayList<Site> staList : stationNames.values())
      Collections.sort(staList);

  }

  /**
   * Retrieve the index of the model that supports the specified station,
   * phase, attribute, or -1.
   * 
   * @param station
   * @param phase
   * @param attribute
   * @return the index of the model that supports the specified station,
   *         phase, attribute, or -1.
   */
  public int getModelIndex(Site station, String phase, String attribute)
  {
    return supportMap.getIndex(station, phase, attribute);
  }

  /**
   * Retrieve a reference to the model that supports the specified station,
   * phase, attribute. Returns null if no such model exists in rootPath.
   * 
   * @param station
   *            name of station
   * @param phase
   *            seismic phase; LibCorr3D supports P and Pn
   * @param attribute
   *            Libcorr3D supports TT_DELTA_AK135 and TT_MODEL_UNCERTAINTY
   * @return a reference to the model, or null
   */
  public LibCorr3DModel getModel(Site station, String phase,
      String attribute)
  {
    int index = supportMap.getIndex(station, phase, attribute);
    if (index < 0)
      return null;

    synchronized (models)
    {
      LibCorr3DModel model = models.get(index);

      if (model == null)
      {
        try
        {
          File f = getModelFile(station, phase, attribute);
          if (f != null)
          {
            long timer = System.nanoTime();
            model = new LibCorr3DModel(f, gridRelPath);
            timer = System.nanoTime() - timer;

            for (String ph : model.getSupportedPhases())
              for (String at : model.getMetaData()
                  .getAttributeNames())
              {
                int i = supportMap.getIndex(model.getSite(),
                    ph, at);
                if (i >= 0)
                  models.set(i, model);
              }
          }
        }
        catch (Exception ex)
        {

          ex.printStackTrace();
          model = null;
        }
      }
      return model;
    }
  }

  /**
   * Retrieve a reference to the model with specified index, or null if it
   * does not exist.
   * 
   * @param lookupTableIndex
   *            the index of the model
   * @return a reference to the model, or null
   * @throws IOException
   */
  public LibCorr3DModel getModel(int lookupTableIndex) throws IOException
  {
    if (lookupTableIndex < 0)
      return null;

    LibCorr3DModel model = null;
    synchronized (models)
    {
      model = models.get(lookupTableIndex);
      if (model == null)
      {
        File modelFile = getModelFile(lookupTableIndex);

        //System.out.println("Loading model " + modelFile.getCanonicalPath());

        long timer = System.nanoTime();
        model = new LibCorr3DModel(modelFile, gridRelPath);
        timer = System.nanoTime() - timer;

        ArrayListInt indexes = modelIndexes.get(modelFile);
        if (indexes != null)
          for (int i = 0; i < indexes.size(); ++i)
            models.set(indexes.get(i), model);
      }
    }
    return model;
  }

  /**
   * Clear all the models currently in memory. If a model that has been
   * cleared is requested again, it will be reloaded from file.
   */
  public void clearModels()
  {
    synchronized (models)
    {
      for (int i = 0; i < models.size(); ++i)
        models.set(i, null);
    }
  }

  /**
   * Clear from memory the specified model. If the model is requested again
   * later, it will be reloaded from file.
   * 
   * @param model
   *            LibCorr3DModel model
   */
  synchronized public void clearModel(LibCorr3DModel model)
  {
    synchronized (models)
    {
      ArrayListInt idx = modelIndexes.get(model.getMetaData()
          .getInputModelFile());
      for (int i = 0; i < idx.size(); ++i)
        models.set(idx.get(i), null);
    }
  }

  /**
   * Retrieve the name of the file that contains the model that supports the
   * specified station, phase, attribute. Returns null if the
   * station/phase/attribute is unsupported.
   * 
   * @param pathCorrIndex
   * @return a reference to the model, or null
   */
  public File getModelFile(int pathCorrIndex)
  {
    return pathCorrIndex < 0 ? null : modelFileNames.get(pathCorrIndex);
  }

  /**
   * Retrieve the name of the file that contains the model that supports the
   * specified station, phase, attribute. Returns null if the
   * station/phase/attribute is unsupported.
   * 
   * @param station
   *            name of station
   * @param phase
   *            seismic phase
   * @param attribute
   *            Libcorr3D supports TT_DELTA_AK135 and TT_MODEL_UNCERTAINTY
   * @return a reference to the model, or null
   */
  public File getModelFile(Site station, String phase, String attribute)
  {
    return getModelFile(supportMap.getIndex(station, phase, attribute));

  }

  /**
   * Retrieve the number of unique Site -&gt; phase -&gt; attribute combinations
   * that are supported by this LibCorr3DModels object.
   * 
   * @return
   */
  public int size()
  {
    return supportMap.size();
  }

  /**
   * Retrieve total number of stations, not unique station names. For a 
   * with entries for multiple on-off times, each entry contributes to
   * nSites.
   * 
   * @return total number of Site objects.
   */
  public int getNSites()
  {
    return nSites;
  }

  /**
   * Retrieve the number of unique station names. For a  with entries for
   * multiple on-off times, only 1 entry contributes to nSiteNames.
   * 
   * @return
   */
  public int getNSiteNames()
  {
    return stationNames.size();
  }

  /**
   * Returns true if a model exists to support the specified station, phase,
   * attribute.
   * 
   * @param station
   *            station
   * @param phase
   *            seismic phase; LibCorr3D supports P and Pn
   * @param attribute
   *            Libcorr3D supports TT_DELTA_AK135 and TT_MODEL_UNCERTAINTY
   * @return
   */
  public boolean isSupported(Site station, String phase, String attribute)
  {
    return supportMap.getIndex(station, phase, attribute) >= 0;
  }

  /**
   * Get vertion number.
   * 
   * @return
   */
  public String getVersion()
  {
    return "1.3.0";
  }

  /**
   * Returns path to directory where models are stored
   */
  public File getRootPath()
  {
    return rootDirectory;
  }

  /**
   * Retrieve the relative path from the directory where models are stored to
   * the directory where grids are stored.
   * 
   * @return
   */
  public String getGridRelativePath()
  {
    return gridRelPath;
  }

  /**
   * Map from Site -&gt; phase -&gt; attribute.
   * 
   * @return
   */
  public MultiLevelMap getSupportMap()
  {
    return supportMap;
  }

  /**
   * Map from station name -&gt; array list of Sites that have that name but
   * have different onTimes. In the ArrayList, stations are in order of
   * decreasing onTime.
   * 
   * @return map from station name -&gt; array list of Sites
   */
  public Map<String, ArrayList<Site>> getSupportedSites()
  {
    return stationNames;
  }

  /**
   * Set of phases supported by any one model in this set of models. Just
   * because a phase is in this list does not guarantee that it is supported
   * by any specific model. Must check isSupported(Site, phase, attribute)
   * to determine that.
   * 
   * @return Set of phases supported by any one model in this set of models.
   */
  public Set<String> getSupportedPhases()
  {
    return supportedPhases;
  }

  /**
   * Set of attributes supported by any one model in this set of models. Just
   * because an attributes is in this list does not guarantee that it is
   * supported by any specific model. Must check isSupported(Site, phase,
   * attribute) to determine that.
   * 
   * @return Set of attributes supported by any one model in this set of
   *         models.
   */
  public Set<String> getSupportedAttributes()
  {
    return supportedAttributes;
  }

  /**
   * Retrieve the Site object with specified name and that was active at
   * specified epochTime. To test for a station that is currently active,
   * specify epochTime = 1e10. Returns null if no station meets the criteria.
   * 
   * @param sta
   *            station name
   * @param epochTime
   *            seconds since 1970
   * @return Site or null
   */
  public Site getSite(String sta, double epochTime)
  {
	long jdate = GMTFormat.getJDate(epochTime);
    ArrayList<Site> staList = stationNames.get(sta);
    if (staList != null)
      for (Site station : staList)
        if (jdate >= station.getOndate() && jdate <= station.getOffdate())
          return station;
    return null;
  }

  /**
   * Retrieve the Site object with specified name and that has the latest
   * onTime. Returns null if sta is not supported.
   * 
   * @param sta
   *            station name
   * @return Site or null
   */
  public Site getSite(String sta)
  {
    ArrayList<Site> staList = stationNames.get(sta);
    if (staList != null)
      return staList.get(0);
    return null;
  }

  /**
   * Return true if model is available for station, phase, attribute, at the
   * specified epochTime. To test for a station that is currently active,
   * specify epochTime = 1e10.
   * 
   * @param sta
   * @param phase
   * @param attribute
   * @param epochTime
   *            seconds since 1970
   * @return true if model is available for station, phase, attribute, at the
   *         specified epochTime.
   */
  public boolean isSupported(String sta, String phase, String attribute,
      double epochTime)
  {
    Site station = getSite(sta, epochTime);
    return station == null ? false : isSupported(station, phase, attribute);
  }

  /**
   * Return true if there is a model available for station, phase, attribute,
   * independent of on/off time.
   * 
   * @param sta
   * @param phase
   * @param attribute
   * @return
   */
  public boolean isSupported(String sta, String phase, String attribute)
  {
    Site station = getSite(sta);
    return station == null ? false : isSupported(station, phase, attribute);
  }

  /**
   * Find all the Files in the specified directory, and all of its
   * subdirectories. Return all the Files in the supplied array of Files.
   * 
   * @param directory
   * @param files
   * @throws IOException 
   */
  private void discoverFiles(File directory, ArrayList<File> files) throws IOException
  {
    if (!directory.exists())
      throw new IOException(directory.getAbsolutePath()+" does not exist.");

    if (!directory.isDirectory())
      throw new IOException(directory.getAbsolutePath()+" is not a directory.");

    for (File file : directory.listFiles())
      if (file.isDirectory())
        discoverFiles(file, files);
      else if (file.isFile() && GeoTessModel.isGeoTessModel(file))
        files.add(file);
  }

  /**
   * Retrieve a map from a model File to the set of Sites that are
   * associated with that File.
   * 
   * @return a map from a model File to the set of Sites that are
   *         associated with that File.
   */
  public TreeMap<File, TreeSet<Site>> getModelSiteMap()
  {
    TreeMap<File, TreeSet<Site>> map = new TreeMap<File, TreeSet<Site>>();
    for (Entry<File, ArrayListInt> entry : modelIndexes.entrySet())
    {
      TreeSet<Site> stations = new TreeSet<Site>();
      map.put(entry.getKey(), stations);
      for (int i = 0; i < entry.getValue().size(); ++i)
        stations.add((Site) supportMap.getKeys(entry.getValue().get(
            i))[0]);
    }

    return map;
  }

  /**
   * Retrieve a map from a model file name to the set of stations that are
   * associated with that model.
   * 
   * @return a map from a model file name to the set of stations that are
   *         associated with that model.
   */
  public String getModelSiteMapToString()
  {
    StringBuffer buf = new StringBuffer();
    TreeMap<File, TreeSet<Site>> map = getModelSiteMap();

    int maxLength = 0;
    for (File f : map.keySet())
      if (f.getName().length() > maxLength)
        maxLength = f.getName().length();
    String format = String.format("%%-%ds : ", maxLength);

    TreeSet<String> stations = new TreeSet<String>();
    for (Entry<File, TreeSet<Site>> entry : map.entrySet())
    {
      buf.append(String.format(format, entry.getKey().getName()));

      for (Site station : entry.getValue())
        stations.add(station.getSta());
      for (String sta : stations)
        buf.append(String.format(" %-6s", sta));
      buf.append('\n');
      stations.clear();
    }

    return buf.toString();
  }

  // /**
  // * Test the support map to ensure that each station is associated with
  // * only one LibCorr3DModel of each type (tt, az, slo).
  // * @return errors. If this String is empty, then no errors were
  // encountered.
  // * If not empty, then the String contains information about the errors
  // detected.
  // */
  // public String testSupportMap()
  // {
  // StringBuffer errors = new StringBuffer();
  //
  // // build a map from Site -> list of model files. List should have
  // // exactly one entry.
  // TreeMap<Site, TreeSet<File>> map = new TreeMap<Site,
  // TreeSet<File>>();
  // for (Entry<File, ArrayListInt> entry : modelIndexes.entrySet())
  // // key is a model file name and value is list of indexes associated to
  // that model
  // for (int i=0; i< entry.getValue().size(); ++i)
  // {
  // Site station =
  // (Site)supportMap.getKeys(entry.getValue().get(i))[0];
  // TreeSet<File> files = map.get(station);
  // if (files == null)
  // {
  // files = new TreeSet<File>();
  // map.put(station, files);
  // }
  // files.add(entry.getKey());
  // }
  //
  // // check for entries where a station is associated with more than 1 model
  // file.
  // for (Entry<Site, TreeSet<File>> entry : map.entrySet())
  // {
  // Site station = entry.getKey();
  // if (entry.getValue().size() > 1)
  // {
  // errors.append(String.format(
  // "Site %s is associated with %d LibCorr3DModels:%n",
  // station.getName(), entry.getValue().size()));
  // for (File modelFile : entry.getValue())
  // errors.append(String.format("   %s : %s%n", modelFile, station));
  // }
  // }
  //
  // return errors.toString();
  // }

}
