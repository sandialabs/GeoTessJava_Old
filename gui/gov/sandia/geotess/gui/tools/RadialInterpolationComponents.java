package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.gmp.util.globals.InterpolatorType;

import javax.swing.*;

/**
 * Created by dmdaily on 8/18/2014.
 */
public class RadialInterpolationComponents{

    private JLabel title;
    private RadioButtonPanel buttons;

    public RadialInterpolationComponents()
    {
        this.title = new PopupLabel("Radial Interpolation: ", ParameterHelp.RADIAL_INTERPOLATION);
        this.buttons = new RadioButtonPanel("Linear", "Cubic Spline");
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
        return InterpolatorType.CUBIC_SPLINE;
    }
}

