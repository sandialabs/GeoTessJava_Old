package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.gui.enums.ParameterHelp;

import javax.swing.*;

/**
 * Created by dmdaily on 8/1/2014.
 */
public class DeltaOrNComponents extends JPanel {

	private JLabel title;
    private RadioButtonPanel buttons;
    private JTextField value;

    public DeltaOrNComponents(String label)
    {
    	this.title = new PopupLabel( "Delta or N:", ParameterHelp.DELTA_OR_N);
        this.buttons = new RadioButtonPanel("Delta", "N");
        this.value = new JTextField(4);
    }

    public JLabel getTitle()
    {
    	return title;
    }
    
    public RadioButtonPanel getButtons()
    {
    	return buttons;
    }
    
    public JTextField getTextField()
    {
    	return value;
    }
}
