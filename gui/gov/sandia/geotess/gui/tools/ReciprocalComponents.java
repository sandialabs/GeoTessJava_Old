package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.gui.enums.ParameterHelp;

import javax.swing.*;

/**
 * Created by dmdaily on 7/29/2014.
 */
public class ReciprocalComponents {

    private JLabel label;
    private RadioButtonPanel buttonPanel;

    public ReciprocalComponents()
    {
        this.label = new PopupLabel("Reciprocal: ", ParameterHelp.RECIPROCAL);
        this.buttonPanel = new RadioButtonPanel("True", "False");
    }

    public boolean getSelected()
    {
        return Boolean.parseBoolean(buttonPanel.getSelected());
    }

    public JPanel getButtons()
    {
        return buttonPanel;
    }

    public JLabel getTitle()
    {
        return label;
    }
}
