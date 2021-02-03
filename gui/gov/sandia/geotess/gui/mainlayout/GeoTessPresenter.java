//- ****************************************************************************
//-
//- Copyright 2009 Sandia Corporation. Under the terms of Contract
//- DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
//- retains certain rights in this software.
//-
//- BSD Open Source License.
//- All rights reserved.
//-
//- Redistribution and use in source and binary forms, with or without
//- modification, are permitted provided that the following conditions are met:
//-
//-    * Redistributions of source code must retain the above copyright notice,
//-      this list of conditions and the following disclaimer.
//-    * Redistributions in binary form must reproduce the above copyright
//-      notice, this list of conditions and the following disclaimer in the
//-      documentation and/or other materials provided with the distribution.
//-    * Neither the name of Sandia National Laboratories nor the names of its
//-      contributors may be used to endorse or promote products derived from
//-      this software without specific prior written permission.
//-
//- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//- AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//- IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//- ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
//- LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
//- CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
//- SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
//- INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
//- CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//- ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
//- POSSIBILITY OF SUCH DAMAGE.
//-
//- ****************************************************************************

package gov.sandia.geotess.gui.mainlayout;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessGrid;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.numerical.polygon.GreatCircle.GreatCircleException;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * The GeoTessPresenter is the middle man for communication between the View and the Model.  When a user enters information
 * in the {@link MainFrame}, this view will call the presenter to send the user selected arguments to the model.  When the
 * {@link GeoTessUIModel} gets these arguments, the model will run some calculations and give the output back to this presenter.
 * This presenter will call the view to update with the output from the model.
 *
 * Created by dmdaily on 7/22/2014.
 */
public class GeoTessPresenter {

    private GeoTessUIModel model;
    private MainFrame view;

    /**
     * Called by the {@link GeoTessUserInterface} to create a view and set it visible
     *
     * @param version the current version of GeoTessJava for the header of the {@link MainFrame}
     */
    public void startApplication(String version) {
        this.view = new MainFrame(this, version);
        this.view.setPresenter(this);
        this.view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.view.pack();
        this.view.setVisible(true);
    }

    /**
     * Associates a {@link GeoTessUIModel} with this presenter
     *
     *  @param model the model to assign to this presenter
     */
    public void setModel(GeoTessUIModel model) {
        this.model = model;
    }

    /**
     * Called when the user uploads a new {@link GeoTessModel} or a new {@link GeoTessGrid}.  Calls the model to
     * change the  {@link GeoTessModel} and {@link GeoTessGrid} files that it has saved.
     *
     * @param modelFile the new model to use
     * @param gridDir the new grid to use
     */
    public void updateModelFilePaths(String modelFile, String gridDir) throws IOException{
        view.updateFileDisplayPanel(modelFile, gridDir);
        model.updateModelFilePaths(modelFile, gridDir);
    }

    public String getModel()
    {
        return model.getModelFile();
    }

    public String getGrid()
    {
        return model.getGridDir();
    }

    public boolean hasModel() {
        return model.hasModel();
    }

    public void writeToUtilityPanel(String s) {
        view.updateUtilityText(s);
    }

    public void writeToExtractDataPanel(String s) {
        view.updateExtractDataText(s);
    }

    public void writeToMapDataPanel(String s) {
        view.updateMapDataText(s);
    }

    public void extractGrid(String outputType, String output, int tessID) throws Exception {
        view.updateUtilityText(model.extractGrid(outputType, output, tessID));
    }

    public void extractActiveNodes(int[] attributes, boolean reciprocal, String polygonPath,
                                   String output) throws IOException {
        view.updateUtilityText(model.extractActiveNodes(attributes, reciprocal, polygonPath, output));
    }

    public void makeStatistics() throws IOException {
        writeToUtilityPanel(model.makeStatistics());
    }

    public void updateEquals(String model1, String grid1, String model2, String grid2) throws IOException {
        model.equals(model1, grid1, model2, grid2);
    }

    public void findClosestPoint(double lat, double lon, double depth,
                                 int layerID, List<String> output) throws IOException, GeoTessException {
        System.out.println(lat + ", " + lon + ", " + depth + ", " + layerID + ", " + output);
        String outputToPanel = model.findClosestPoint(lat, lon, depth, layerID, output);
        writeToUtilityPanel(outputToPanel);
    }

    public void borehole(double lat, double lon, double maxRadSpacing,
                         int deepIndex, int shallowIndex, InterpolatorType horizontal,
                         InterpolatorType radial, String depthOrRad, boolean reciprocal,
                         int[] attributes) throws IOException, GeoTessException {
        String output = model.borehole(lat, lon, maxRadSpacing, deepIndex, shallowIndex, horizontal, radial,
                depthOrRad, reciprocal, attributes);
        writeToUtilityPanel(output);
    }

    public void getDistanceDegrees(double lat1, double lon1, double lat2,
                                   double lon2, int nx) {
        String output = model.getDistanceDegrees(lat1, lon1, lat2, lon2, nx);
        writeToUtilityPanel(output);
    }

    public void getValues(double lat, double lon, double depth, int layerID,
                          int[] attributes, InterpolatorType horInterpolation,
                          InterpolatorType radialInterpolation, boolean reciprocal)
            throws GeoTessException, IOException {
        String output = model.getValues(lat, lon, depth, layerID, attributes, horInterpolation, radialInterpolation,
                reciprocal);
        writeToUtilityPanel(output);
    }

    public void getValuesFile(String latlonFile, int[] attributes,
                              InterpolatorType horizontalType, InterpolatorType radialType,
                              boolean reciprocal, String outputFile) throws GeoTessException,IOException {
        String output = model.getValuesFile(latlonFile, attributes, horizontalType, radialType, reciprocal, outputFile);
        writeToUtilityPanel(output);
    }

    public void interpolatePoint(double lat, double lon, double depth,
                                 int layerID, InterpolatorType horInterpolation,
                                 InterpolatorType radialInterpolation, boolean reciprocal)
            throws GeoTessException, IOException {
        String output = model.interpolatePoint(lat, lon, depth, layerID, horInterpolation, radialInterpolation,
                reciprocal);
        writeToUtilityPanel(output);
    }

    public void profile(double lat, double lon, int deepest, int shallowest,
                        String depOrRad, boolean reciprocal, int[] attributes)
            throws IOException, GeoTessException {
        String output = model.profile(lat, lon, deepest, shallowest, depOrRad, reciprocal, attributes);
        writeToUtilityPanel(output);
    }

    public void reformat(String outputFile, String outputGridName) throws Exception {
        String outputToPanel = model.reformat(outputFile, outputGridName);
        writeToUtilityPanel(outputToPanel);
    }

    public void translatePolygon(String inputFile, String outputFile, String fileExtension) throws IOException {
        String output = model.translatePolygon(inputFile, outputFile, fileExtension);
        writeToUtilityPanel(output);
    }

    public void replaceAttributeValues(String polygonFileName,
                                       String attributeValues, String outputPath) throws Exception {
        String output = model.replaceAttributeValues(polygonFileName, attributeValues, outputPath);
        writeToUtilityPanel(output);
    }

    public void slice(double lat1, double lon1, double lat2, double lon2,
                      boolean shortestPath, int nx, double rspacing, int firstLayer,
                      int lastLayer, InterpolatorType horizontalType,
                      InterpolatorType radialType, String spatialCoords,
                      boolean reciprocal, int[] attributes, String output)
            throws GeoTessException, IOException {
        model.slice(lat1, lon1, lat2, lon2, shortestPath, nx, rspacing, firstLayer, lastLayer, horizontalType,
                radialType, spatialCoords, reciprocal, attributes, output);
    }

    public void sliceDistAz(double lat, double lon, double dist, double az,
                            int nx, double rspacing, int firstLayer, int lastLayer,
                            InterpolatorType horizontalType, InterpolatorType radialType,
                            String spatialCoordinates, boolean reciprocal, int[] attributes, String outputFile)
            throws IOException, GreatCircleException, GeoTessException {
        model.sliceDistAz(lat, lon, dist, az, nx, rspacing, firstLayer, lastLayer, horizontalType, radialType,
                spatialCoordinates, reciprocal, attributes, outputFile);
    }

    public void mapValuesLayer(String firstLatitude, String lastLatitude, String latSpacing, String firstLongitude,
                               String lastLongitude, String lonSpacing, int layerID, double fractionalRadius,
                               InterpolatorType horizontalType, InterpolatorType radialType, boolean reciprocal,
                               int[] attributes, String outputFile) throws GeoTessException, IOException {
        model.mapValuesLayer(firstLatitude, lastLatitude, latSpacing, firstLongitude, lastLongitude, lonSpacing,
                layerID, fractionalRadius, horizontalType, radialType, reciprocal, attributes, outputFile);
    }

    public void mapValuesDepth(String firstLatitude, String lastLatitude, String latSpacing, String firstLongitude,
                               String lastLongitude, String lonSpacing, int layerID, double depth,
                               InterpolatorType horizontalType, InterpolatorType radialType, boolean reciprocal,
                               int[] attributes, String outputFile) throws Exception {
        model.mapValuesDepth(firstLatitude, lastLatitude, latSpacing, firstLongitude, lastLongitude, lonSpacing,
                layerID, depth, horizontalType, radialType, reciprocal, attributes, outputFile);
    }

    public void vtkDepths(String outputFile, int layerID, double firstDepth,
                          double lastDepth, double spacing, boolean reciprocal,
                          int[] attributes) throws Exception {
        model.vtkDepths(outputFile, layerID, firstDepth, lastDepth, spacing, reciprocal, attributes);
    }

    public void vtkDepths2(String outputFile, int layerID, double[] depths,
                           boolean reciprocal, int[] attributes) throws Exception {
        model.vtkDepths2(outputFile, layerID, depths, reciprocal, attributes);
    }

    public void vtkLayerBoundary(String outputFile, String z, InterpolatorType horizontalType) throws IOException,
            GeoTessException {
        model.vtkLayerBoundary(outputFile, z, horizontalType);
    }

    public void vtkLayerThickness(String outputFile, int firstLayer, int lastLayer,
                                  InterpolatorType horizontalType) throws IOException, GeoTessException {
        model.vtkLayerThickness(outputFile, firstLayer, lastLayer, horizontalType);
    }

    public void vtkLayers(String outputFile, int[] layids, boolean reciprocal,
                          int[] attributes) throws Exception {
        model.vtkLayers(outputFile, layids, reciprocal, attributes);
    }

    public void vtkSlice(String output, double lat1, double lon1, double lat2, double lon2, boolean shortestPath,
                         int nPoints, double maxRadSpacing, int deepest, int shallowest, InterpolatorType horizontal,
                         InterpolatorType radial, boolean reciprocal, int[] attributes) throws IOException,
            GeoTessException {
        model.vtkSlice(output, lat1, lon1, lat2, lon2, shortestPath, nPoints, maxRadSpacing, deepest, shallowest,
                horizontal, radial, reciprocal, attributes);
    }

    public void vtkSolid(String output, double rspacing, int deepest, int shallowest,
                         InterpolatorType horizontalType, InterpolatorType radialType, boolean reciprocal,
                         int[] attributes) throws IOException, GeoTessException {
        model.vtkSolid(output, rspacing, deepest, shallowest, horizontalType, radialType, reciprocal, attributes);
    }

    public void mapLayerBoundaries(String lat1, String lat2, String latSpacing, String lon1, String lon2,
                                   String lonSpacing, int layerID, String top, String depth,
                                   InterpolatorType horizontal, String output) throws IOException, GeoTessException {
        model.mapLayerBoundaries(lat1, lat2, latSpacing, lon1, lon2, lonSpacing, layerID, top, depth, horizontal,
                output);
    }

    public void mapLayerThickness(String lat1, String lat2, String latSpacing, String lon1, String lon2,
                                  String lonSpacing, int firstLayerID, int lastLayerID, InterpolatorType horizontal,
                                  String output) throws IOException, GeoTessException {
        model.mapLayerThickness(lat1, lat2, latSpacing, lon1, lon2, lonSpacing, firstLayerID, lastLayerID,
                horizontal, output);
    }

    public void values3dBlock(String lat1, String lat2, String latSpacing, String lon1, String lon2,
                              String lonSpacing, int firstLayer, int lastLayer, String radialDimension,
                              double maxRadialSpacing, InterpolatorType horizontalType, InterpolatorType radialType,
                              boolean reciprocal, int[] attributes, String output) throws Exception {
        model.values3dBlock(lat1, lat2, latSpacing, lon1, lon2, lonSpacing, firstLayer, lastLayer, radialDimension,
                maxRadialSpacing, horizontalType, radialType, reciprocal, attributes, output);
    }

    public void clearUtilityText() {
        view.clearUtilityText();
    }

    public void makeToString() {
        try {
            view.updateUtilityText(model.makeToString());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void vtk3dBlock(String lat1, String lat2, String latSpacing, String lon1,
                           String lon2, String lonSpacing, int deepest, int shallowest, String radialDimension,
                           double rspacing, InterpolatorType horizontalInterpolator,
                           InterpolatorType radialInterpolator,
                           boolean reciprocal, int[] checkedAttributeIndexes, String output) throws Exception {
        model.vtk3dBlock(lat1, lat2, latSpacing, lon1, lon2, lonSpacing, deepest, shallowest, radialDimension,
                rspacing, horizontalInterpolator, radialInterpolator, reciprocal, checkedAttributeIndexes, output);
    }
}
