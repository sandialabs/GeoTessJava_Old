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

package gov.sandia.geotess.gui.enums;

public enum MethodHelp {

    FILE_LOADER("Model Grid File Loader: loads model and grid files to run functions on"),

    STATISTICS("Statistics: Prints out basic statistics about the data in a GeoTessFile\n\n"),

    TO_STRING("To String: Gives some basic information about a particular GeoTessModel\n\n"),

    EXTRACT_GRID("Extract Grid: This method outputs the grid of an already loaded model to a new file. The " +
            "output file can be output to a variety of file formats: LatLon, GMT, KML/KMZ, VTK, ASCII, Binary.  \n" +
            "     1) LatLon format: Outputs the triangle edges to a file in lat1, lon1, lat2, lon2 format. \n" +
            "     2) GMT format: Outputs the triangle edges in a GMT-compatible format.  \n" +
            "     3) KML/KMZ format: Outputs the edges to a file that can be viewed in Google Earth.  \n" +
            "     4) VTK format: The triangles are written to a vtk file which is viewable with Paraview.  \n" +
            "     5) ASCII format: Outputs the grid to a GeoTessGrid file in ascii format.  \n" +
            "     6) Binary format:  outputs the GeoTessGrid file in binary format with any extension that the user " +
            "wants.  \n\n"),

    EXTRACT_ACTIVE_NODES("Extract Active Nodes: This function extracts the positions of all active " +
            "nodes in a model in \'latitude longitude depth, layer index\' format\n\n"),

    GET_VALUES("Get Values: This function outputs interpolated values at a single point.\n\n"),

    EQUALS("Equals: Takes two GeoTess models as input arguments and checks if they are the same model.\n\n"),

    INTERPOLATE_POINT("Interpolate Point: This function outputs inteprolated values and " +
            "coefficients for a single point. \n\n"),

    BOREHOLE("Borehole: Outputs a two-dimensional array of values representing attribute values on a" +
            " radial profile through the model.\n\n"),

    REPLACE_ATTRIBUTE_VALUES("Replace Attribute Values: Replace Attribute values method help\n\n"),

    GET_VALUES_FILE("Get Values File: The Get Values File function uses a file of the form: \n" +
            "\'latitude, longitude, depth, layer index values\' to output interpolated values at a single point.\n\n"),

    PROFILE("Profile: Outputs a two-dimensional array of values representing attribute values on a radial " +
            "profile through the model.\n\n"),

    REFORMAT("Reformat: Translates a GeoTessModel file from one format to another or changes the grid path " +
            "information in a file.  \n" +
            "If the supplied input model file name is a directory, then the operation is performed on every " +
            "GeoTessModel file in that directory.  \n" +
            "If an attempt is made to make a GeoTessModel reference a GeoTessGrid in another file and the internal " +
            "gridIDs in the model\n" +
            "and grid file are not the same, then the attempt to reformat will fail.  \n\n"),

    FIND_CLOSEST_POINT("Find Closest Point: This function finds the closest point to a supplied geographic " +
            "location \n" +
            "and returns information about the point such as latitude, longitude, depth, vertex index, etc...\n\n"),

    GET_LATITUDES("Get Latitudes: get lats method\n\n"),

    GET_LONGITUDES("Get Longitudes: get lon method \n\n"),

    GET_DISTANCE_DEGREES("Get Distance Degrees: gdd method\n\n"),

    TRANSLATE_POLYGON("Translate Polygon: This function translates a Polygon file Goggle Earth kml/kmz format to ascii format readable " +
            "by GeoTess C++ libraries\n\n"),

    FUNCTION("Function: func method\n\n"),

    //Extract Data Functions
    SLICE("Slice: Outputs a three-dimensional array of values representing attribute values on a vertical " +
            "slice through the model.  \n\n"),

    SLICE_DIST_AZ("Slice Dist Azimuth: Outputs a three-dimensional array of values representing attribute values on" +
            "a vertical slice through the model.  \n\n"),

    MAP_VALUES_DEPTH("Map Values Depth: Outputs a three-dimensional array of values representing attribute " +
            "values at a constant depth on a regular lat, lon grid of points. \n\n"),

    MAP_VALUES_LAYER("Map Values Layer: Outputs a three-dimensional array of values representing attribute " +
            "values at a constant fractional radius in a layer, on a regular lat, lon grid of points.  \n\n"),


    MAP_LAYER_BOUNDARY("Map Layer Boundary: Outputs a two-dimensional array or values representing the depth " +
            "or radius (in km) of the top or bottom of a specified layer on a regular lat, lon grid.  \n\n"),

    MAP_LAYER_THICKNESS("Map Layer Thickness: Output a two-dimensional array of values representing the thickness " +
            "(in km) " +
            "of the specified layers on a regular lat, lon grid.  Thickness values are the sum of the thicknesses " +
            "from the \n" +
            "bottom of the first layer to the top of the last layer. \n\n"),

    VALUES_3D_BLOCK("Values 3D Block: Outputs a 4D array of values representing attribute values on a regular " +
            "lat, lon, radius grid of points. \n\n"),

    //Map Data
    VTK_LAYERS("VTK Layers: Outputs a VTK file (viewable in Paraview) containing file attribute values at the " +
            "top of a specified set of layers.  \n\n"),

    VTK_DEPTHS("VTK Depths: Outputs a VTK file (viewable in Paraview) with attribute values at a specified " +
            "range of depths.  \n\n"),

    VTK_DEPTHS_2("VTK Depths 2: Outputs a VTK file (viewable in Paraview) with attribute values at a specified " +
            "range of depths.  \n\n"),

    VTK_LAYER_THICKNESS("VTK Layer Thickness: Outputs a VTK file (viewable in Paraview) with the combined " +
            "thickness of a range of layers.  \n\n"),

    VTK_LAYER_BOUNDARY("VTK Layer Boundary: Outputs a VTK file (viewable in Paraview) with the depth or " +
            "elevation to the top of each layer boundary. \n\n"),

    VTK_SLICE("VTK Slice: vtk slice method\n\n"),

    VTK_SOLID("VTK Solid: vtk solid method\n\n"),

    VTK_3D_BLOCK("VTK 3D Block: vtk 3d block\n\n"),

    VTK_ROBINSON("VKT Robinson: vtk robinson method\n\n"),

    VTK_ROBINSON_LAYERS("VKT Robinson Layers: vtk robinson layers method\n\n"),

    VTK_ROBINSON_POINTS("VKT Robinson Points: vtk robinson points method help\n\n"),

    VTK_RONINSON_TRIANGLE_SIZE("VTK Triangle Size: vtk triangle size method help\n\n");

    private String methodTip;

    private MethodHelp(String m) {
        this.methodTip = m;
    }

    public String getMethodTip() {
        return methodTip;
    }
}
