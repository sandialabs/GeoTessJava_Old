package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.gui.enums.ParameterHelp;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class TitleFieldComponents {

    private JLabel label;
    private JTextField textBox;
    private JLabel units;
    public TitleFieldComponents(String labelText, int textAreaSize, String units, ParameterHelp help) {
        init(labelText, textAreaSize, units, help);
    }

    public TitleFieldComponents(String label, int textAreaSize, ParameterHelp help) {
        init(label, textAreaSize, "", help);
    }

    private void init(String labelText, int textAreaSize, final String units, ParameterHelp help) {
        this.label = new PopupLabel(labelText, help);
        this.textBox = new JTextField();
        this.textBox.setColumns(textAreaSize);
        this.units = new PopupLabel(units, ParameterHelp.UNITS);        
    }
    
    public JLabel getTitle()
    {
        return label;
    }

    public JTextField getTextBox()
    {
        return textBox;
    }

    public JLabel getUnits()
    {
    	return units;
    }
    
    public String getFieldValue() {
        return textBox.getText();
    }
}
