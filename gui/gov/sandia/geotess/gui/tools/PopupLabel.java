package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.gui.enums.ParameterHelp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopupLabel extends JLabel {

    private JToolTip tip;
    private Popup tooltipContainer;
    private boolean tooltipVisible = false;

    public PopupLabel(String arg, ParameterHelp tooltip) {
        super(arg);
        tip = this.createToolTip();
        tip.setTipText(tooltip.getTip());
        init();
    }

    private void init() {
        this.setForeground(Color.BLACK);
        this.setFont(new Font("Arial", Font.BOLD, 14));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    PopupFactory popupFactory = PopupFactory.getSharedInstance();
                    int x = getLocationOnScreen().x;
                    int y = getLocationOnScreen().y;
                    x += getWidth() / 2;
                    y += getHeight() + 5;  // small offset to avoid covering
                    
                    if (tooltipVisible && tooltipContainer != null)
                    	tooltipContainer.hide();
                    
                    if (!tooltipVisible)
                    {	tooltipContainer = popupFactory.getPopup(PopupLabel.this, tip, x, y);
                    	tooltipContainer.show();
                    }
                    
                    tooltipVisible = !tooltipVisible;
                }
                else if (SwingUtilities.isLeftMouseButton(e))
                {   if (tooltipVisible && tooltipContainer != null)
                		tooltipContainer.hide();
                	tooltipVisible = !tooltipVisible;
                }

            }
        });
    }

}
