package gov.sandia.geotess.gui.mainlayout;

import gov.sandia.geotess.*;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.globals.OptimizationType;
import gov.sandia.gmp.util.numerical.polygon.GreatCircle;
import gov.sandia.gmp.util.numerical.polygon.GreatCircle.GreatCircleException;
import gov.sandia.gmp.util.numerical.polygon.Polygon;
import gov.sandia.gmp.util.numerical.vector.EarthShape;
import gov.sandia.gmp.util.numerical.vector.VectorUnit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * This class is the performer of the Model-View-Performer (MVP) architechture
 * in the GUI. It is the brain behind all calculations and operations. When the
 * user enters information in the GUI, the View (what the user is seeing) sends
 * the user selected input to this performer. Once this class finishes it's
 * computation, it writes output to a file or updates the View with information
 * for the user.
 *
 * @author dmdaily
 */
public class GeoTessUIModel {

    private String modelFile;
    private String gridDir;
    private GeoTessPresenter presenter;

    public void startApplication() {
        presenter.startApplication(getVersion());
    }

    private String getVersion() {
        return GeoTessUtils.getVersion();
    }

    public void setPresenter(GeoTessPresenter presenter) {
        this.presenter = presenter;
    }

    public String getModelFile()
    {
        return modelFile;
    }

    public String getGridDir()
    {
        return gridDir;
    }


    /**
     * When the GUI is run, it has no {@link GeoTessModel} or
     * {@link GeoTessGrid} associated with it. This method is called when a user
     * has selected the model and grid file that they would like to run
     * functions on. It updates the view with the names of the files that the
     * user has entered.
     *
     * @param newModel the path to a GeoTessModel file to use for computation
     * @param newGrid   the directory relative to the location of the above model that
     *                  the GeoTessGrid file lies in
     */
    public void updateModelFilePaths(String newModel, String newGrid) {
        modelFile = newModel;
        gridDir = newGrid;
    }

    /**
     * @return True if a model file has been loaded by a user. False if
     * otherwise.
     */
    public boolean hasModel() {
        return modelFile != null;
    }

    /**
     * Called by the View. This performer will send the currently loaded model
     * and grid file back to the view which will run the toString function of a
     * {@link GeoTessModel}
     *
     * @throws IOException
     */
    public String makeToString() throws IOException {

        GeoTessModel model = new GeoTessModel(modelFile, gridDir);
        return "========== To String Results ==========\n\n" + model.toString() + "\n\n";
    }

    /**
     * Called by the View. This method makes a {@link GeoTessModel} with the
     * currently loaded model and grid file. It then gets the statistics of this
     * model from {@link GeoTessModelUtils} and sends the result to the View.
     * The View will get updated with the statistics of the currently loaded
     * model.
     *
     * @throws IOException
     */
    public String makeStatistics() throws IOException {
        GeoTessModel model = new GeoTessModel(modelFile, gridDir);
        return "========== Statistics ==========\n\n" + GeoTessModelUtils.statistics(model) + "\n\n";
    }

    public void writeToUtilityPanel(String s) {
        presenter.writeToUtilityPanel(s);
    }

    /**
     * Takes two {@link GeoTessModel} file paths and two {@link GeoTessGrid}
     * file paths and simply checks to see if the two models or the two grids
     * are the same. It then calls the View with the result and the View is
     * updated.
     *
     * @param model1Path the first {@link GeoTessModel} path
     * @param model2Path the second {@link GeoTessModel} path
     * @throws IOException
     */
    public void equals(String model1Path, String grid1Path, String model2Path, String grid2Path) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("========== Equals ==========\n\n");

        GeoTessModel model1 = new GeoTessModel(model1Path, grid1Path);
        GeoTessModel model2 = new GeoTessModel(model2Path, grid2Path);
        if(model1.equals(model2))
            sb.append("The Two Uploaded Models Are Equal.\n\n");
        else
            sb.append("The Two Uploaded Models Are NOT Equal.\n\n");

        writeToUtilityPanel(sb.toString());
    }

    public String extractGrid(String type, String output, int tessID)
            throws Exception {
        GeoTessModel model = null;
        GeoTessGrid grid;

        StringBuilder fileOutputBuilder = new StringBuilder();
        if(GeoTessModel.isGeoTessModel(new File(modelFile))) {
            model = new GeoTessModel(modelFile, gridDir);
            grid = model.getGrid();
            if(tessID >= 0)
                tessID = model.getMetaData().getTessellation(tessID);
        } else
            grid = new GeoTessGrid().loadGrid(new File(modelFile));
        // This block occurs if the output type is stdout. Accepts any extension
        if(type.equalsIgnoreCase("stdout")) {
            if(tessID < 0 && grid.getNTessellations() == 1)
                tessID = 0;
            if(tessID < 0) {
                if(model == null)
                    throw new Exception(
                            "\nMust specify a 5th argument that specifies a tessellation index in range 0 to "
                                    + (grid.getNTessellations() - 1));
                else
                    throw new Exception(
                            "\nMust specify a 5th argument that specifies a layer index in range 0 to "
                                    + (model.getNLayers() - 1));
            }

            for(int[] edge : grid.getEdges(tessID)) {
                fileOutputBuilder.append(String.format(
                        "%1.6f %1.6f %1.6f %1.6f%n",
                        model.getEarthShape().getLatDegrees(
                                grid.getVertex(edge[0])),
                        model.getEarthShape().getLonDegrees(
                                grid.getVertex(edge[0])),
                        model.getEarthShape().getLatDegrees(
                                grid.getVertex(edge[1])),
                        model.getEarthShape().getLonDegrees(
                                grid.getVertex(edge[1]))));
            }
            writeToFile(fileOutputBuilder.toString(), new File(output));
        }
        // end stdout block

        // Begin output as a GMT File. Accepts any extension
        else if(type.equalsIgnoreCase("gmt")) {
            if(tessID < 0 && grid.getNTessellations() == 1)
                tessID = 0;
            if(tessID < 0) {
                if(model == null)
                    throw new Exception(
                            "\nMust specify a 5th argument that specifies a tessellation index in range 0 to "
                                    + (grid.getNTessellations() - 1));
                else
                    throw new Exception(
                            "\nMust specify a 5th argument that specifies a layer index in range 0 to "
                                    + (model.getNLayers() - 1));
            }
            for(int[] edge : grid.getEdges(tessID))
                fileOutputBuilder.append(String.format(
                        ">%n%1.6f %1.6f%n%1.6f %1.6f%n",
                        model.getEarthShape().getLonDegrees(
                                grid.getVertex(edge[0])),
                        model.getEarthShape().getLatDegrees(
                                grid.getVertex(edge[0])),
                        model.getEarthShape().getLonDegrees(
                                grid.getVertex(edge[1])),
                        model.getEarthShape().getLatDegrees(
                                grid.getVertex(edge[1]))));

            writeToFile(fileOutputBuilder.toString(), new File(output));
        }
        // End GMT Block

        // Beginning of the KML/KMZ output
        else if(type.equalsIgnoreCase("kml") || type.equalsIgnoreCase("kmz")) {
            if(tessID < 0 && grid.getNTessellations() == 1)
                tessID = 0;
            if(tessID < 0) {
                if(!output.contains("%"))
                    output = GeoTessModelUtils.expandFileName(output,
                            "_tess_%d");

                for(int i = 0; i < grid.getNTessellations(); ++i) {
                    File fout = new File(String.format(output, i));
                    grid.writeGridKML(fout, i);
                }
            } else
                grid.writeGridKML(new File(output), tessID);
        }
        // End of KML/KMZ output

        // Begin the VTK output section
        else if(type.equalsIgnoreCase("vtk")) {
            if(tessID < 0 && grid.getNTessellations() == 1)
                tessID = 0;
            if(tessID < 0) {
                if(!output.contains("%"))
                    output = GeoTessModelUtils.expandFileName(output,
                            "_tess_%d");

                for(int i = 0; i < grid.getNTessellations(); ++i) {
                    File fout = new File(String.format(output, i));
                    GeoTessModelUtils.vtkTriangleSize(grid, fout, i);
                }
            } else
                GeoTessModelUtils.vtkTriangleSize(grid, new File(output),
                        tessID);

            File outputDirectory = new File(output).getParentFile();
            if(!(new File(outputDirectory, "continent_boundaries.vtk"))
                    .exists())
                GeoTessModelUtils.copyContinentBoundaries(outputDirectory);
        } else {
            grid.writeGrid(output);
        }
        return "========== Extract Grid Results ==========\n\n Finished writing to " + output + "\n\n";
    }

    public String extractActiveNodes(int[] indexes, boolean reciprocal,
                                     String polygonFileName, String outputName) throws IOException {

        GeoTessModel model = new GeoTessModel(modelFile, gridDir);
        File outputFile = new File(outputName);

        if(!polygonFileName.isEmpty())
            model.setActiveRegion(polygonFileName);

        StringBuilder fileOutputBuilder = new StringBuilder();
        PointMap pm = model.getPointMap();
        for(int pointIndex = 0; pointIndex < pm.size(); ++pointIndex) {
            fileOutputBuilder.append(model.getEarthShape().getLatLonString(
                    pm.getPointUnitVector(pointIndex), "%10.6f %11.6f"));

            if(model.is3D())
                fileOutputBuilder.append(String.format(" %1.3f %d",
                        pm.getPointDepth(pointIndex),
                        pm.getLayerIndex(pointIndex)));

            for(Integer i : indexes) {
                Data data = pm.getPointData(pointIndex);

                fileOutputBuilder.append(' ');
                switch(data.getDataType()) {
                    case DOUBLE:
                        fileOutputBuilder.append(Double
                                .toString(reciprocal ? 1. / data.getDouble(i)
                                        : data.getDouble(i)));
                        break;
                    case FLOAT:
                        fileOutputBuilder.append(Float
                                .toString(reciprocal ? 1.F / data.getFloat(i)
                                        : data.getFloat(i)));
                        break;
                    default:
                        fileOutputBuilder.append(data.getLong(i));
                        break;
                }
            }
            fileOutputBuilder.append("\n");
        }
        writeToFile(fileOutputBuilder.toString(), outputFile);
        return "========== Extract Active Nodes Results ==========\n\n Finished writing to " + outputFile.getName() +
                "\n\n";
    }

    public String replaceAttributeValues(String polygonFileName,
                                         String attributeValues, String outputPath) throws Exception {

        GeoTessModel model = new GeoTessModel(modelFile, gridDir);
        if(!polygonFileName.isEmpty())
            model.setActiveRegion(polygonFileName);

        Scanner input = new Scanner(new File(attributeValues));
        File outputFile = new File(outputPath);

        PointMap pm = model.getPointMap();
        int pointIndex = 0;

        while(input.hasNext()) {
            if(pointIndex >= pm.size()) {
                while(input.hasNext())
                    ++pointIndex;
                input.close();
                throw new IOException(
                        String.format(
                                "%nInput file has too many records.%n"
                                        + "There are %d records in the input file and %d active nodes in the model.%n",
                                pointIndex, pm.size()));
            }

            Scanner record = new Scanner(input.nextLine());
            double[] u = model.getEarthShape().getVectorDegrees(
                    record.nextDouble(), record.nextDouble());
            if(VectorUnit.dot(u, pm.getPointUnitVector(pointIndex)) < Math
                    .cos(1e-7)) {
                record.close();
                input.close();
                throw new IOException(String.format(
                        "%nPoints at record %d don't match.%n"
                                + "Input file location: %s%n"
                                + "Model location     : %s%n",
                        pointIndex,
                        model.getEarthShape().getLatLonString(u),
                        model.getEarthShape().getLatLonString(
                                pm.getPointUnitVector(pointIndex))));
            }

            if(model.is3D()) {
                double depth = record.nextDouble();
                if(Math.abs(depth - pm.getPointDepth(pointIndex)) > 1e-3) {
                    record.close();
                    input.close();
                    throw new IOException(String.format(
                            "%nPoints at record %d don't match.%n"
                                    + "Input file location: %s %9.4f%n"
                                    + "Model location     : %s %9.4f%n",
                            pointIndex,
                            model.getEarthShape().getLatLonString(u),
                            depth,
                            model.getEarthShape().getLatLonString(
                                    pm.getPointUnitVector(pointIndex)), pm
                                    .getPointDepth(pointIndex)));
                }

                int layerIndex = record.nextInt();
                if(layerIndex != pm.getLayerIndex(pointIndex)) {
                    record.close();
                    input.close();
                    throw new IOException(String.format(
                            "%nPoints at record %d have different layerIndexes.%n"
                                    + "Input file location: %s %9.4f %d%n"
                                    + "Model location     : %s %9.4f% d%n",
                            pointIndex,
                            model.getEarthShape().getLatLonString(u),
                            depth,
                            layerIndex,
                            model.getEarthShape().getLatLonString(
                                    pm.getPointUnitVector(pointIndex)), pm
                                    .getPointDepth(pointIndex), pm
                                    .getLayerIndex(pointIndex)));
                }
            }

            int attributeIndex = 0;
            while(record.hasNext()) {
                if(attributeIndex == model.getNAttributes()) {
                    while(record.hasNext())
                        ++attributeIndex;
                    record.close();
                    input.close();
                    throw new IOException(
                            String.format(
                                    "%nInput file has too many attributes on record %d.%n"
                                            + "There are %d attributes in the input file and %d attributes in the " +
                                            "model.%n",
                                    pointIndex, attributeIndex,
                                    model.getNAttributes()));
                }
                pm.setPointValue(pointIndex, attributeIndex++,
                        record.nextDouble());
            }

            if(attributeIndex < model.getNAttributes()) {
                record.close();
                input.close();
                record.close();
                input.close();
                throw new IOException(
                        String.format(
                                "%nInput file does not have enough attributes on record %d.%n"
                                        + "There are %d attributes in the input file and %d attributes in the model.%n",
                                pointIndex, attributeIndex,
                                model.getNAttributes()));
            }

            record.close();
            ++pointIndex;
        }
        input.close();

        if(pointIndex < pm.size()) {
            throw new IOException(
                    String.format(
                            "%nInput file does not have enough records.%n"
                                    + "There are %d records in the input file and %d active nodes in the model.%n",
                            pointIndex, pm.size()));
        }
        model.writeModel(outputFile);
        return "========== Replace Attribute Values Results ==========\n\n Finished writing to " + outputPath + "\n\n";
    }


    //TODO NEED TO FIGURE OUT HOW TO RETURN OUTPUT
    public String reformat(String outputFile, String outputGridName)
            throws Exception {
        File inputModelFile = new File(modelFile);
        File outputModelFile = new File(outputFile);

        // if outputGrid is null, the ouputModel gets the same grid name as
        // input model
        // if outputGrid is '*', then grid is written into the output model.
        // if outputGrid is name of file that does not exist , then write the
        // grid file (gridid is null).
        // if outputGrid is name of file that does exist, and gridIDs are ==,
        // use the grid name.
        // if outputGrid is name of file that does exist, and gridIDs are !=,
        // throw exception.

        GeoTessGrid outputGridFile = null;
        if(!outputGridName.isEmpty()) {
            if(!outputGridName.equals("*")) {
                File f = new File(outputGridName);
                if(f.exists())
                    outputGridFile = new GeoTessGrid().loadGrid(outputGridName);
            }
        }

        // at this point, outputGrid is either null, '*', or a grid file name
        // If outputGrid is a file name, the file exists and is a grid file then
        // grid is that grid
        // If outputGrid is a file name and file does not exist, then grid is
        // null and
        // the grid from the old model will be written to the new file.
        // If outputGrid is a file name and file exists, but can't get a valid
        // gridId
        // an exception would have been thrown.

        if(inputModelFile.isFile()) {
            writeModel(inputModelFile, gridDir, outputModelFile, outputGridName, outputGridFile);
        } else {
            if(!outputModelFile.getName().contains("%s"))
                throw new Exception(
                        "\noutput file name must contain substring '%s', \n"
                                + "which will be replaced with the name of the input model\n");

            for(File f : inputModelFile.listFiles())
                if(f.isFile() && GeoTessModel.isGeoTessModel(f)) {
                    String name = f.getName();
                    int index = name.lastIndexOf(".");
                    if(index > 0)
                        name = name.substring(0, index);

                    File outfile = new File(String.format(outputFile, name));
                    outputGridFile = writeModel(f, gridDir, outfile,
                            outputGridName, outputGridFile);
                }
        }

        return "Reformat Completed\n\n";
    }

    public String getValues(double lat, double lon, double depth, int layerID,
                            int[] attributes, InterpolatorType horInterpolation,
                            InterpolatorType radialInterpolation, boolean reciprocal)
            throws GeoTessException, IOException {

        GeoTessModel model = new GeoTessModel(modelFile, gridDir);
        GeoTessPosition pos = GeoTessPosition.getGeoTessPosition(model,
                horInterpolation, radialInterpolation);
        if(layerID < 0)
            pos.set(lat, lon, depth);
        else
            pos.set(layerID, lat, lon, depth);

        StringBuilder sb = new StringBuilder();
        sb.append("========== Get Values Results ==========\n\n");
        switch(model.getMetaData().getDataType()) {
            case DOUBLE:
                for(Integer i : attributes)
                    sb.append(String.format("%1.16g",
                            reciprocal ? 1. / pos.getValue(i) : pos.getValue(i)));
                break;
            case FLOAT:
                for(Integer i : attributes)
                    sb.append(String.format("%1.7g ",
                            reciprocal ? 1. / pos.getValue(i) : pos.getValue(i)));
                break;
            default:
                for(Integer i : attributes)
                    sb.append(String.format(
                            "%d ",
                            reciprocal ? Math.round(1. / pos.getValue(i)) : Math
                                    .round(pos.getValue(i))));
                break;
        }
        sb.append("\n\n");
        return sb.toString();
    }

    public String getValuesFile(String latlonFile, int[] attributes,
                                InterpolatorType horizontalType, InterpolatorType radialType,
                                boolean reciprocal, String outputFile) throws GeoTessException,
            IOException {

        File latlondepthlayerFile = new File(latlonFile);
        if(!latlondepthlayerFile.exists())
            throw new IOException(String.format(
                    "%nInput file %s does not exist.%n",
                    latlondepthlayerFile.getCanonicalPath()));

        GeoTessModel model = new GeoTessModel(modelFile, gridDir,
                OptimizationType.MEMORY);
        DataType dataType = model.getMetaData().getDataType();
        Scanner input = new Scanner(latlondepthlayerFile);
        GeoTessPosition pos = GeoTessPosition.getGeoTessPosition(model,
                horizontalType, radialType);

        int layerIndex;
        double lat, lon, depth;
        StringBuilder fileOutputBuilder = new StringBuilder();
        String record, r, separator;
        String[] ss;
        while(input.hasNext()) {
            record = input.nextLine();
            r = record.replaceAll(",", " ").trim();
            while(r.contains("  "))
                r = r.replaceAll("  ", " ");
            ss = r.split(" ");
            r = record;
            separator = record.contains(",") ? ", " : " ";
            if(ss.length >= 4) {
                try {
                    lat = Double.parseDouble(ss[0]);
                    lon = Double.parseDouble(ss[1]);
                    depth = Double.parseDouble(ss[2]);
                    layerIndex = Integer.parseInt(ss[3]);

                    pos.set(layerIndex, lat, lon, depth);
                    switch(dataType) {
                        case DOUBLE:
                            for(int j : attributes)
                                r += String.format(
                                        "%s%1.16g",
                                        separator,
                                        reciprocal ? 1. / pos.getValue(j) : pos
                                                .getValue(j));
                            break;
                        case FLOAT:
                            for(int j : attributes)
                                r += String.format(
                                        "%s%1.7g",
                                        separator,
                                        reciprocal ? 1. / pos.getValue(j) : pos
                                                .getValue(j));
                            break;
                        default:
                            for(int j : attributes)
                                r += String.format(
                                        "%s%d",
                                        separator,
                                        reciprocal ? Math.round(1. / pos
                                                .getValue(j)) : Math.round(pos
                                                .getValue(j)));
                            break;
                    }
                    record = r;
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            fileOutputBuilder.append(record).append("\n");
        }
        writeToFile(fileOutputBuilder.toString(), new File(outputFile));
        input.close();
        return "========== Get Values File Results =========\n\n" + fileOutputBuilder.toString() + "\n\n";
    }

    public void function(String model0FilePath, String grid0Directory, int attribute0, String model1FilePath,
                         String grid1Directory, int attribute1, String geometryFilePath,
                         String geometryGridDirectory, String outputFile, String outputGridRef, int function,
                         String newAttributeName, String newAttributeUnits, InterpolatorType horizontalType) throws
            IOException, GeoTessException {
        File model0FileName = new File(model0FilePath);
        File model1FileName = new File(model1FilePath);
        File geometryFileName = new File(geometryFilePath);

        if(outputGridRef.equals(".") || outputGridRef.equalsIgnoreCase("null"))
            outputGridRef = "*";

        InterpolatorType radialType = horizontalType == InterpolatorType.LINEAR
                ? InterpolatorType.LINEAR : InterpolatorType.CUBIC_SPLINE;

        GeoTessModel model0 = new GeoTessModel(model0FileName, grid0Directory, OptimizationType.MEMORY);
        GeoTessModel model1 = new GeoTessModel(model1FileName, grid1Directory, OptimizationType.MEMORY);

        System.out.printf("Applying function %d to attributes %s and %s%n%n", function,
                model0.getMetaData().getAttributeName(attribute0),
                model1.getMetaData().getAttributeName(attribute1));

        GeoTessModel geometryModel;
        if(geometryFileName.equals(model0FileName))
            geometryModel = model0;
        else if(geometryFileName.equals(model1FileName))
            geometryModel = model1;
        else
            geometryModel = new GeoTessModel(geometryFileName, geometryGridDirectory);

        GeoTessModel newModel = GeoTessModelUtils.function(function,
                model0, attribute0, model1, attribute1, geometryModel,
                newAttributeName, newAttributeUnits, horizontalType, radialType);

        newModel.writeModel(outputFile, outputGridRef);
        String outputString = "========== Function Output ===========\n\n" + newModel + "\n\n";
        writeToUtilityPanel(outputString);
    }


    public String interpolatePoint(double lat, double lon, double depth,
                                   int layerID, InterpolatorType horInterpolation,
                                   InterpolatorType radialInterpolation, boolean reciprocal)
            throws GeoTessException, IOException {

        GeoTessModel model = new GeoTessModel(modelFile, gridDir);
        GeoTessPosition pos = GeoTessPosition.getGeoTessPosition(model,
                horInterpolation, radialInterpolation);

        if(layerID < 0)
            pos.set(lat, lon, depth);
        else
            pos.set(layerID, lat, lon, depth);

        StringBuilder sb = new StringBuilder();
        sb.append("========== Interpolate Points Results ===========\n\n");
        sb.append(String.format("Lat, lon, depth = %1.6f, %1.6f, %1.3f%n%n",
                lat, lon, depth));
        sb.append(String.format("Layer %2d - %s%n%n", pos.getLayerId(), model
                .getMetaData().getLayerNames()[pos.getLayerId()]));

        for(int i = 0; i < model.getMetaData().getNAttributes(); ++i)
            sb.append(String.format("%-30s %10.3f%s%n", String.format(
                            "%s (%s)", model.getMetaData().getAttributeNames()[i],
                            model.getMetaData().getAttributeUnits()[i]),
                    reciprocal ? 1. / pos.getValue(i) : pos.getValue(i),
                    reciprocal ? " (inverse)" : ""));

        sb.append(String
                .format("\n   Point       Lat        Lon    Depth  Dist(deg)  Coeff "));
        for(int atrib = 0; atrib < model.getMetaData().getNAttributes(); ++atrib)
            sb.append(String.format(" %10s", model.getMetaData()
                    .getAttributeName(atrib)));
        sb.append("\n");

        HashMap<Integer, Double> coeff = pos.getCoefficients();
        for(Integer pt : coeff.keySet()) {
            double[] v = model.getPointMap().getPointUnitVector(pt);
            sb.append(String.format("%8d %9.5f %10.5f %9.3f %7.3f %9.6f", pt,
                    model.getEarthShape().getLatDegrees(v), model
                            .getEarthShape().getLonDegrees(v), model
                            .getPointMap().getPointDepth(pt), VectorUnit
                            .angleDegrees(pos.getVector(), v), coeff.get(pt)));
            for(int atrib = 0; atrib < model.getMetaData().getNAttributes(); ++atrib)
                sb.append(String.format(" %10.3f%s", reciprocal ? 1. / model
                                .getPointMap().getPointValue(pt, atrib) : model
                                .getPointMap().getPointValue(pt, atrib),
                        reciprocal ? " (inverse)" : ""));
            sb.append("\n");
        }

        sb.append("\n\n");
        return sb.toString();
    }

    public String borehole(double lat, double lon, double maxRadSpacing,
                           int deepIndex, int shallowIndex, InterpolatorType horizontal,
                           InterpolatorType radial, String depthOrRad, boolean reciprocal,
                           int[] attributes) throws GeoTessException, IOException {

        GeoTessModel model = new GeoTessModel(modelFile, gridDir,
                OptimizationType.MEMORY);
        GeoTessPosition pos = GeoTessPosition.getGeoTessPosition(model,
                horizontal, radial);

        StringBuilder sb = new StringBuilder();
        sb.append("========== Borehole Results ==========\n\n");
        shallowIndex = Math.min(shallowIndex,
                model.getMetaData().getNLayers() - 1);
        boolean convertToDepth = depthOrRad.toLowerCase().startsWith("d");
        pos.set(shallowIndex, lat, lon, 0.);
        sb.append(GeoTessModelUtils
                .getBoreholeString(pos, maxRadSpacing, deepIndex, shallowIndex,
                        convertToDepth, reciprocal, attributes));
        sb.append("\n\n");
        return sb.toString();
    }

    public String profile(double lat, double lon, int deepest, int shallowest,
                          String depOrRad, boolean reciprocal, int[] attributes)
            throws IOException, GeoTessException {

        boolean convertToDepth = depOrRad.toLowerCase().startsWith("d");
        GeoTessModel model = new GeoTessModel(modelFile, gridDir,
                OptimizationType.MEMORY);
        GeoTessPosition pos = GeoTessPosition.getGeoTessPosition(model);
        pos.set(lat, lon, 1000);

        int vertex = pos.getIndexOfClosestVertex();
        double earthRadius = model.getEarthShape().getEarthRadius(
                model.getGrid().getVertex(vertex));
        shallowest = Math.min(shallowest, model.getMetaData().getNLayers() - 1);

        StringBuilder sb = new StringBuilder();
        sb.append("========== Profile Results ===========\n\n");
        for(int layer = shallowest; layer >= deepest; --layer) {
            Profile p = model.getProfile(vertex, layer);
            if(p.getType() == ProfileType.NPOINT) {
                for(int i = p.getNRadii() - 1; i >= 0; --i) {
                    sb.append(String.format(
                            "%9.3f",
                            convertToDepth ? earthRadius - p.getRadius(i) : p
                                    .getRadius(i)));
                    for(int j = 0; j < attributes.length; ++j)
                        sb.append(String.format(" %12.6f",
                                reciprocal ? 1. / p.getValue(attributes[j], i)
                                        : p.getValue(attributes[j], i)));
                    sb.append("\n");
                }
            } else {
                sb.append(String.format("%9.3f", convertToDepth ? earthRadius
                        - p.getRadiusTop() : p.getRadiusTop()));

                for(int j = 0; j < attributes.length; ++j)
                    sb.append(String.format(
                            " %12.6f",
                            reciprocal ? 1. / p.getValueTop(attributes[j]) : p
                                    .getValueTop(attributes[j])));
                sb.append("\n");
                sb.append(String.format("%9.3f", convertToDepth ? earthRadius
                        - p.getRadiusBottom() : p.getRadiusBottom()));
                for(int j = 0; j < attributes.length; ++j)
                    sb.append(String.format(" %12.6f",
                            reciprocal ? 1. / p.getValueBottom(attributes[j])
                                    : p.getValueBottom(attributes[j])));
                sb.append("\n");
            }
            sb.append("\n");
        }
        sb.append("\n\n");
        return sb.toString();
    }

    public String findClosestPoint(double lat, double lon, double depth,
                                   int layerID, List<String> output) throws IOException, GeoTessException {

        StringBuilder sb = new StringBuilder();
        sb.append("========== Find Closest Point Results ==========\n\n");
        System.out.println(output);

        GeoTessModel model = new GeoTessModel(modelFile, gridDir,
                OptimizationType.MEMORY);
        GeoTessPosition pos = GeoTessPosition.getGeoTessPosition(model);

        if(layerID < 0)
            pos.set(lat, lon, depth);
        else
            pos.set(layerID, lat, lon, depth);

        Iterator<Entry<Integer, Double>> it = pos.getCoefficients().entrySet()
                .iterator();
        int pointIndex = -1;
        Double cmax = 0.;
        while(it.hasNext()) {
            Entry<Integer, Double> e = it.next();
            if(e.getValue() > cmax) {
                pointIndex = e.getKey();
                cmax = e.getValue();
            }
        }
        PointMap pm = model.getPointMap();
        int[] map = pm.getPointIndices(pointIndex);

        for(String out : output) {
            if(out.startsWith("lat"))
                sb.append(String.format(" %1.6f", model.getEarthShape()
                        .getLatDegrees(pm.getPointUnitVector(pointIndex))));
            else if(out.startsWith("lon"))
                sb.append(String.format(" %1.6f", model.getEarthShape()
                        .getLonDegrees(pm.getPointUnitVector(pointIndex))));
            else if(out.equals("depth"))
                sb.append(String.format(" %1.3f", pm.getPointDepth(pointIndex)));
            else if(out.equals("radius"))
                sb.append(String.format(" %1.3f", pm.getPointRadius(pointIndex)));
            else if(out.startsWith("vertex"))
                sb.append(String.format(" %d", map[0]));
            else if(out.startsWith("layer"))
                sb.append(String.format(" %d", map[1]));
            else if(out.startsWith("node"))
                sb.append(String.format(" %d", map[2]));
            else if(out.startsWith("point"))
                sb.append(String.format(" %d", pointIndex));
        }
        sb.append("\n\n");
        return sb.toString();
    }

    public String getDistanceDegrees(double lat1, double lon1, double lat2,
                                     double lon2, int nx) {

        StringBuilder sb = new StringBuilder();
        sb.append("========== Get Distance Degrees Results ==========\n\n");
        double dx = VectorUnit.angleDegrees(
                EarthShape.WGS84.getVectorDegrees(lat1, lon1),
                EarthShape.WGS84.getVectorDegrees(lat2, lon2))
                / (nx - 1);

        for(int i = 0; i < nx; ++i)
            sb.append(String.format(" %1.7g%n", i * dx));
        return sb.toString();
    }

    public String translatePolygon(String inputFile, String outputFile, String fileExtension) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("========== Translate Polygon Results ==========\n\n");
        Polygon polygon = new Polygon(new File(inputFile));
        polygon.write(new File(outputFile));
        sb.append("Successfully wrote polygon to ").append(outputFile)
                .append("\n\n");
        return sb.toString();
    }

    public void slice(double lat1, double lon1, double lat2, double lon2,
                      boolean shortestPath, int nx, double rspacing, int firstLayer,
                      int lastLayer, InterpolatorType horizontalType,
                      InterpolatorType radialType, String spatialCoords,
                      boolean reciprocal, int[] attributes, String output)
            throws GeoTessException, IOException {
        GeoTessModel model = new GeoTessModel(modelFile, gridDir,
                OptimizationType.MEMORY);
        lastLayer = Math.min(lastLayer, model.getMetaData().getNLayers() - 1);
        GreatCircle greatCircle = new GreatCircle(model.getEarthShape()
                .getVectorDegrees(lat1, lon1), model.getEarthShape()
                .getVectorDegrees(lat2, lon2), shortestPath);

        double[][][] results = GeoTessModelUtils.getSlice(model, greatCircle,
                nx, rspacing, firstLayer, lastLayer, horizontalType,
                radialType, spatialCoords, reciprocal, attributes);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < results.length; ++i)
            for(int j = 0; j < results[i].length; ++j) {
                for(int k = 0; k < results[i][j].length; ++k)
                    sb.append(String.format(" %1.7g", results[i][j][k]));
                sb.append("\n");
            }

        sb.append("\n");
        writeToFile(sb.toString(), new File(output));
        presenter.writeToExtractDataPanel("Finished writing Slice Function output to " + output);
    }

    public void sliceDistAz(double lat, double lon, double dist, double az,
                            int nx, double rspacing, int firstLayer, int lastLayer,
                            InterpolatorType horizontalType, InterpolatorType radialType,
                            String spatialCoordinates, boolean reciprocal, int[] attributes, String outputFile)
            throws IOException, GreatCircleException, GeoTessException {

        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        GreatCircle greatCircle = new GreatCircle(model.getEarthShape().getVectorDegrees(lat, lon),
                Math.toRadians(dist), Math.toRadians(az));

        double[][][] results = GeoTessModelUtils.getSlice(model, greatCircle,
                nx, rspacing, firstLayer, lastLayer, horizontalType,
                radialType, spatialCoordinates, reciprocal, attributes);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < results.length; ++i)
            for(int j = 0; j < results[i].length; ++j) {
                for(int k = 0; k < results[i][j].length; ++k)
                    sb.append(String.format(" %1.7g", results[i][j][k]));
                sb.append("\n");
            }

        writeToFile(sb.toString(), new File(outputFile));
        presenter.writeToExtractDataPanel("Finished writing to " + outputFile);
    }

    public void mapValuesDepth(String firstLatitude, String lastLatitude, String latSpacing, String firstLongitude,
                               String lastLongitude, String lonSpacing, int layerID, double depth,
                               InterpolatorType horizontalType, InterpolatorType radialType, boolean reciprocal,
                               int[] attributes, String outputFile) throws Exception {
        StringBuilder sb = new StringBuilder();
        double[] latitudes = GeoTessModelUtils.getLatitudes(firstLatitude, lastLatitude, latSpacing);
        double[] longitudes = GeoTessModelUtils.getLongitudes(firstLongitude, lastLongitude, lonSpacing, "true");


        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        double[][][] results = GeoTessModelUtils.getMapValuesDepth(model,
                latitudes, longitudes, layerID, depth, horizontalType, radialType, reciprocal,
                attributes);

        for(int i = 0; i < results.length; ++i)
            for(int j = 0; j < results[i].length; ++j) {
                sb.append(String.format("%10.6f %11.6f", latitudes[i], longitudes[j]));
                for(int k = 0; k < results[i][j].length; ++k)
                    sb.append(String.format(" %1.7g", results[i][j][k]));
                sb.append("\n");
            }

        writeToFile(sb.toString(), new File(outputFile));
        presenter.writeToExtractDataPanel("Finished writing to " + outputFile);
    }

    public void mapValuesLayer(String firstLatitude, String lastLatitude, String latSpacing, String firstLongitude,
                               String lastLongitude, String lonSpacing, int layerID, double fractionalRadius,
                               InterpolatorType horizontalType, InterpolatorType radialType, boolean reciprocal,
                               int[] attributes, String outputFile) throws GeoTessException, IOException {
        double[] latitudes = GeoTessModelUtils.getLatitudes(firstLatitude, lastLatitude, latSpacing);
        double[] longitudes = GeoTessModelUtils.getLongitudes(firstLongitude, lastLongitude, lonSpacing, "true");
        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);

        double[][][] results = GeoTessModelUtils.getMapValuesLayer(model,
                latitudes, longitudes, layerID, fractionalRadius, horizontalType, radialType,
                reciprocal, attributes);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < results.length; ++i)
            for(int j = 0; j < results[i].length; ++j) {
                sb.append(String.format("%10.6f %11.6f", latitudes[i], longitudes[j]));
                for(int k = 0; k < results[i][j].length; ++k)
                    sb.append(String.format(" %1.7g", results[i][j][k]));
                sb.append("\n");
            }
        writeToFile(sb.toString(), new File(outputFile));
        presenter.writeToExtractDataPanel("Finished Writing to " + outputFile);
    }

    public void mapLayerBoundaries(String lat1, String lat2, String latSpacing, String lon1, String lon2,
                                   String lonSpacing, int layerID, String topOrBottom, String depthOrRadius,
                                   InterpolatorType horizontalType, String output) throws IOException,
            GeoTessException {
    	
    	boolean top = topOrBottom.toLowerCase().startsWith("t");
    	boolean depth = depthOrRadius.toLowerCase().startsWith("d");
    	
        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        double[] latitudes = GeoTessModelUtils.getLatitudes(lat1, lat2, latSpacing);
        double[] longitudes = GeoTessModelUtils.getLongitudes(lon1, lon2, lonSpacing, "true");
        double[][] results = GeoTessModelUtils.getMapLayerBoundary(model, latitudes, longitudes, layerID, top, depth,
                horizontalType);

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < results.length; ++i)
            for(int j = 0; j < results[i].length; ++j)
                builder.append(String.format("%1.7g %1.7g %1.7g%n", latitudes[i], longitudes[j], results[i][j]));

        writeToFile(builder.toString(), new File(output));
        presenter.writeToExtractDataPanel("Finished Writing to " + output);
    }

    public void mapLayerThickness(String lat1, String lat2, String latSpacing, String lon1, String lon2,
                                  String lonSpacing, int firstLayerID, int lastLayerID,
                                  InterpolatorType horizontalType, String output) throws IOException, GeoTessException {
        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.SPEED);
        double[] latitudes = GeoTessModelUtils.getLatitudes(lat1, lat2, latSpacing);
        double[] longitudes = GeoTessModelUtils.getLongitudes(lon1, lon2, lonSpacing, "true");
        double[][] results = GeoTessModelUtils.getMapLayerThickness(model, latitudes, longitudes, firstLayerID,
                lastLayerID, horizontalType);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < results.length; ++i)
            for(int j = 0; j < results[i].length; ++j)
                sb.append(String.format("%1.7g %1.7g %1.7g%n", latitudes[i], longitudes[j], results[i][j]));

        writeToFile(sb.toString(), new File(output));
        presenter.writeToExtractDataPanel("Finished Writing to " + output);
    }

    public void values3dBlock(String lat1, String lat2, String latSpacing, String lon1, String lon2,
                              String lonSpacing, int firstLayer, int lastLayer, String radialDimension,
                              double maxRadialSpacing, InterpolatorType horizontalType, InterpolatorType radialType,
                              boolean reciprocal, int[] attributes, String output) throws Exception {
        double[] latitudes = GeoTessModelUtils.getLatitudes(lat1, lat2, latSpacing);
        double[] longitudes = GeoTessModelUtils.getLongitudes(lon1, lon2, lonSpacing, "true");

        if(firstLayer < 0)
            throw new Exception("index of firstLayer cannot be " + firstLayer);

        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        if(lastLayer >= model.getNLayers())
            lastLayer = model.getNLayers() - 1;

        double[][][][] values3D = GeoTessModelUtils.getValues3D(model,
                latitudes, longitudes, firstLayer, lastLayer, radialDimension, maxRadialSpacing,
                horizontalType, radialType, reciprocal, attributes);

        // output lat, lon, radius, value on separate records.
        String format = "%1.5f %1.5f %1.3f";
        if(model.getMetaData().getDataType() == DataType.DOUBLE)
            format = format + " %1.16g%n";
        else
            format = format + " %1.7g%n";

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < values3D.length; ++i) {
            double[][][] vlat = values3D[i];
            for(int j = 0; j < vlat.length; ++j) {
                double[][] vr = vlat[j];
                for(int k = 0; k < vr.length; ++k) {
                    double[] va = vr[k];
                    for(int a = 1; a < va.length; ++a)
                        sb.append(String.format(format, longitudes[i], latitudes[j], va[0], va[a]));
                }
            }
        }

        writeToFile(sb.toString(), new File(output));
        presenter.writeToExtractDataPanel("Finished Writing to " + output);
    }

    public void vtkLayers(String outputFile, int[] layids, boolean reciprocal,
                          int[] attributes) throws Exception {
        // vtk model fileName layerId reciprocal attributes

        StringBuilder sb = new StringBuilder();
        GeoTessModel model = new GeoTessModel(modelFile, gridDir,
                OptimizationType.MEMORY);
        if(layids.length == 1) {
            GeoTessModelUtils.vtk(model, outputFile, layids[0], reciprocal,
                    attributes);
            presenter.writeToMapDataPanel("VTK output successfully written to " + outputFile);

        } else {
            for(int lid : layids) {
                if(outputFile.contains("%s"))
                    GeoTessModelUtils.vtk(model, String.format(outputFile,
                                    model.getMetaData().getLayerNames()[lid]), lid,
                            reciprocal, attributes);
                else if(outputFile.contains("%d"))
                    GeoTessModelUtils.vtk(model,
                            String.format(outputFile, lid), lid, reciprocal,
                            attributes);
                else
                    throw new Exception(
                            "\noutput file name must contain substring '%s' or '%d'");

                presenter.writeToMapDataPanel("VTK output successfully written to " + outputFile);
            }
        }
    }

    public void vtkDepths(String outputFile, int layerID, double firstDepth,
                          double lastDepth, double spacing, boolean reciprocal,
                          int[] attributes) throws Exception {
        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        GeoTessModelUtils.vtkDepths(model, outputFile, InterpolatorType.LINEAR,
                InterpolatorType.LINEAR, layerID, firstDepth, lastDepth,
                spacing, reciprocal, attributes);

        presenter.writeToMapDataPanel("VTK output successfully written to " + outputFile);
    }

    public void vtkDepths2(String outputFile, int layerID, double[] depths,
                           boolean reciprocal, int[] attributes) throws Exception {
        GeoTessModel model = new GeoTessModel(modelFile, gridDir,
                OptimizationType.MEMORY);

        GeoTessModelUtils.vtkDepths(model, outputFile, InterpolatorType.LINEAR,
                InterpolatorType.LINEAR, layerID, depths, reciprocal,
                attributes);

        presenter.writeToMapDataPanel("VTK output successfully written to " + outputFile);
    }

    public void vtkLayerThickness(String outputFile, int firstLayer, int lastLayer,
                                  InterpolatorType horizontalType) throws IOException, GeoTessException {
        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        GeoTessModelUtils.vtkLayerThickness(model, outputFile, firstLayer, lastLayer, horizontalType);
        presenter.writeToMapDataPanel("VTK output successfully written to " + outputFile);
    }

    public void vtkLayerBoundary(String outputFile, String z, InterpolatorType horizontalType) throws IOException,
            GeoTessException {
        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        GeoTessModelUtils.vtkLayerBoundary(model, outputFile, z, horizontalType);
        presenter.writeToMapDataPanel("VTK output successfully written to " + outputFile);
    }

    public void vtkSlice(String output, double lat1, double lon1, double lat2, double lon2, boolean shortestPath,
                         int nx, double maxRadSpacing, int deepest, int shallowest, InterpolatorType horizontal,
                         InterpolatorType radial, boolean reciprocal, int[] attributes) throws IOException , GeoTessException {
        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        shallowest = Math.min(shallowest, model.getMetaData().getNLayers() - 1);
        GreatCircle greatCircle = new GreatCircle(model.getEarthShape().getVectorDegrees(lat1, lon1),
                model.getEarthShape().getVectorDegrees(lat2, lon2), shortestPath);

        StringBuilder sb = new StringBuilder();
        sb.append("X direction (lat, lon) = ").append(model.getEarthShape().getLatLonString(greatCircle.getTransform()[0])).append("\n");
        sb.append("Y direction (lat, lon) = ").append(model.getEarthShape().getLatLonString(greatCircle.getTransform()[1])).append("\n");
        sb.append("Z direction (lat, lon) = ").append(model.getEarthShape().getLatLonString(greatCircle.getTransform()[2])).append("\n");
        GeoTessModelUtils.vtkSlice(model, output, greatCircle, nx, maxRadSpacing,deepest, shallowest, horizontal, radial, reciprocal, attributes);
        presenter.writeToMapDataPanel(sb.toString() + "File Written to " + output);
    }

    public void vtkSolid(String output, double rspacing, int deepest, int shallowest, InterpolatorType horizontalType, InterpolatorType radialType, boolean reciprocal, int[] attributes) throws IOException, GeoTessException
    {
        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        new File(output).getParentFile().mkdirs();
        if (shallowest >= model.getNLayers())
            shallowest = model.getNLayers()-1;

        GeoTessModelUtils.vtkSolid(model, output, rspacing,
               deepest, shallowest, horizontalType, radialType, reciprocal, attributes);
        presenter.writeToMapDataPanel("File Written to " + output);
    }

    public void vtk3dBlock(String lat1, String lat2, String latSpacing, String lon1,
                           String lon2, String lonSpacing, int deepest, int shallowest, String radialDimension,
                           double rspacing, InterpolatorType horizontalInterpolator,
                           InterpolatorType radialInterpolator,
                           boolean reciprocal, int[] attributes, String output) throws Exception {

        double[] latitudes = GeoTessModelUtils.getLatitudes(lat1, lat2, latSpacing);
        double[] longitudes = GeoTessModelUtils.getLongitudes(lon1, lon2, lonSpacing, "true");
        if (deepest < 0)
            throw new Exception("index of firstLayer cannot be " + deepest);

        GeoTessModel model = new GeoTessModel(modelFile, gridDir, OptimizationType.MEMORY);
        if (shallowest >= model.getNLayers())
            shallowest = model.getNLayers()-1;

        GeoTessModelUtils.vtk3DBlock(model, output, latitudes, longitudes,
                deepest, shallowest, radialDimension, rspacing,
                horizontalInterpolator, radialInterpolator, reciprocal, attributes);

        presenter.writeToMapDataPanel("VTK output written to " + output);
    }

    private void writeToFile(String content, File outputFile) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    outputFile.getAbsoluteFile()));
            writer.write(content);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private GeoTessGrid writeModel(File inputModelPath, String inputGridDir,
                                   File outputModelFile, String outputGridName, GeoTessGrid outputGrid)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("========== Reformat Results ==========\n\n");
        sb.append(String.format("in  " + inputModelPath.getCanonicalPath()));
        GeoTessModel model = new GeoTessModel(inputModelPath, inputGridDir,
                OptimizationType.MEMORY);
        sb.append(String.format(" %1.3f MB in %1.3f sec%n",
                inputModelPath.length() / (1024. * 1024.), model.getMetaData()
                        .getLoadTimeModel()));
        sb.append(String.format("out " + outputModelFile.getCanonicalPath()));
        return writeModel(model, outputModelFile, outputGridName, outputGrid,
                sb);
    }

    private GeoTessGrid writeModel(GeoTessModel model, File outputModelFile,
                                   String outputGridName, GeoTessGrid outputGrid, StringBuilder sb)
            throws IOException {
        if(outputGridName.isEmpty()) {
            // the user did not specify an output grid file name.
            // If the model that was loaded contained its grid in the same file
            // then the output model will similarly contain its grid in the same
            // file.
            // Otherwise, the output model will have a reference to the same
            // grid file as the input model.
            model.writeModel(outputModelFile);
        } else if(outputGridName.equals("*")) {
            // user wants grid written to same file as the output model.
            model.writeModel(outputModelFile, "*");
        } else if(outputGrid == null) {
            // outputGridName is the name of a file and that file does not
            // currently exist. Write the grid from the input model out to
            // a new grid file.

            File f = new File(outputGridName);

            // should not have to do this check, but it won't hurt.
            if(f.exists())
                throw new IOException(String.format(
                        "\nCannot copy the grid from the input model to the new output grid file\n"
                                + "%s%nbecause the file already exists.",
                        outputGridName));

            outputGrid = model.getGrid();
            outputGrid.setGridInputFile(f);
            outputGrid.writeGrid(f);
            model.writeModel(outputModelFile, f.getName());
        } else {
            // the user specified the name of an output grid file, and we have a
            // grid in memory.
            // If the model's gridID and the grid's gridID are the same, then we
            // can write the
            // model with a reference to the grid
            if(model.getGrid().getGridID().equals(outputGrid.getGridID()))
                model.writeModel(outputModelFile, outputGrid.getGridInputFile()
                        .getName());

            model.writeModel(outputModelFile, outputGrid.getGridInputFile()
                    .getName());
        }

        sb.append(String.format("%1.3f MB in %1.3f sec%n",
                outputModelFile.length() / (1024. * 1024.), model.getMetaData()
                        .getWriteTimeModel()));
        sb.append("\n\n");
        GeoTessModel.clearReuseGridMap();
        writeToUtilityPanel(sb.toString());
        return outputGrid;
    }

}
