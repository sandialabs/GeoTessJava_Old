package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.gui.enums.ParameterHelp;

import javax.swing.*;

public class LatLonComponents {
    private TitleFieldComponents lat;
    private TitleFieldComponents lon;

    public LatLonComponents() {
        lat = new TitleFieldComponents("Latitude: ", 7, "\u00b0", ParameterHelp.LAT);
        lon = new TitleFieldComponents("Longitude: ", 7, "\u00b0", ParameterHelp.LON);
    }

    public LatLonComponents(String latTitle, String lonTitle)
    {
        lat = new TitleFieldComponents(latTitle, 7, "\u00b0", ParameterHelp.LAT);
        lon = new TitleFieldComponents(lonTitle, 7, "\u00b0", ParameterHelp.LON);
    }

    public double getLat() {
        return Double.parseDouble(lat.getFieldValue());
    }

    public double getLon() {
        return Double.parseDouble(lon.getFieldValue());
    }

    public JLabel getLatTitle()
    {
        return lat.getTitle();
    }

    public JLabel getLonTitle()
    {
        return lon.getTitle();
    }

    public JLabel getLatUnits()
    {
    	return lat.getUnits();
    }
    
    public JLabel getLonUnits()
    {
    	return lon.getUnits();
    }
    
    public JTextField getLatTextBox()
    {
        return lat.getTextBox();
    }

    public JTextField getLonTextBox()
    {
        return lon.getTextBox();
    }

}
