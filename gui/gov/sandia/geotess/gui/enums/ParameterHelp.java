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

/**
 * Created by dmdaily on 8/15/2014.
 */
public enum ParameterHelp {

    GEOTESS_MODEL("The GeoTessModel file that you would like to perform functions on"),
    GEOTESS_GRID("The GeoTessGrid for this model. "
    		+ "Can be omitted if the GeoTessModel contains the grid"),

    LAYER_ID("Layer Id Parameter help"),
    FIRST_LAYER_ID("First layer ID"),
    LAST_LAYER_ID("Last layer id "),
    DEPTH("Depth parameter help"),

    FUNCTION("ERRRRR"),
    N_POINTS("n points parameter help"),

    MAX_RADIAL_SPACING("max radial spacing parameter help"),

    LAT("Lat help"),
    LON("Lon help"),
    FIRST_LAT("First lat help"),
    LAST_LAT("Last lat help"),
    FIRST_LON("First lon help"),
    LAST_LON("last lon help"),
    LAT_LON_FILE("File containing lats and lons"),

    FIRST_DEPTH("First depth parameter help"),
    LAST_DEPTH("last depth parameter help"),
    DEPTH_SPACING("depth spacing help"),

    DIST_LAST_POINT("Distance last point help"),
    AZIMUTH_LAST_POINT("Azimuth help"),

    OUTPUT_MODEL("OUTPUT model"),
    OUTPUT_GRID("Output grid"),
    OUTPUT_FORMAT("Format of the output file"),
    DEEPEST_LAYER("Deepest layer"),
    SHALLOWEST_LAYER("Shallowest layer"),

    FRAC_RADIUS("Fractional radius param help"),
    RADIAL_INTERPOLATION("Radial interpolation"),

    DELTA_OR_N("delat or n"),
    RECIPROCAL("Reciprocal"),
    HORIZONTAL_INTERPOLATION("horizontal interpolation help"),
    PATH_TO_POLYGON("Path to polygon file"),
    ATTRIBUTES("Attributes Help"),
    ATTRIBUTES_FILE("File of attributes"),
    OUTPUT("Output help"),

    TOP_OR_BOTTOM("top or bottom"),
    GEOMETRY_MODEL("Geometry model"),
    GEOMETRY_GRID("Geometry Grid"),
    RADIAL_DIMENSION("Radial dimension"),
    GRID_REFERENCE_LABEL("grid reference"),

    DEPTHS("depths"),
    DEPTH_OR_RADIUS("Depth or radius"),
    DEPTH_OR_ELEVATION("Depth or elevation"),
    SHORTEST_PATH("Determines if you want the shortest path to a point, or the path that circles the globe"),
    INPUT_POLYGON("Imput polygon Tooltip"),
    OUTPUT_POLYGON("Output polygon"),
    
    UNITS("units");

    private String tip;

    private ParameterHelp(String tip) {
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }

}
