package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.gmp.util.globals.InterpolatorType;

import javax.swing.*;

/**
 * Created by dmdaily on 8/18/2014.
 */
public class HorizontalInterpolationComponents {

    private JLabel title;
    private RadioButtonPanel buttons;

    public HorizontalInterpolationComponents()
    {
        this.title = new PopupLabel("Horizontal Interpolation: ", ParameterHelp.HORIZONTAL_INTERPOLATION);
        this.buttons = new RadioButtonPanel("Linear", "Natural Neighbor");
    }

    public JLabel getTitle()
    {
        return title;
    }

    public RadioButtonPanel getButtons()
    {
        return buttons;
    }

    public InterpolatorType getInterpolation()
    {
        if(buttons.getSelected().equalsIgnoreCase("linear"))
        return InterpolatorType.LINEAR;
        return InterpolatorType.NATURAL_NEIGHBOR;
    }
}
