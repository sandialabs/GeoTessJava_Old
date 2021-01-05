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
