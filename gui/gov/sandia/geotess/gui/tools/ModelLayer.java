package gov.sandia.geotess.gui.tools;

/**
 * Created by dmdaily on 8/1/2014.
 */
public class ModelLayer {

    private int index;
    private String layerName;

    public ModelLayer(int index, String layerName)
    {
        this.index = index;
        this.layerName = layerName;
    }

    /**
     * This method takes the layer name from the model without being formatted.  For example upper_mantle would be
     * the name of a layer. This method capitalizes each word of the layer name and replaces the '_' with ' '
     * so that the name is displayed in a friendlier way.
     *
     * @param name the non formatted layer name
     * @return a readable formatted version of the layer name
     */
    private String makePretty(String name)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(name.charAt(0)));

        //Starts at 1 since the first letter has already been modified
        for(int i = 1; i < name.length(); i++)
        {
            if(name.charAt(i) == '_') {
                sb.append(" ");
                sb.append(Character.toUpperCase(name.charAt(++i)));
            }
            else sb.append(name.charAt(i));
        }
        return sb.toString();
    }

    public int getIndex()
    {
        return index;
    }

    @Override
    public String toString()
    {
        return index + " - " + makePretty(layerName);
    }
}
