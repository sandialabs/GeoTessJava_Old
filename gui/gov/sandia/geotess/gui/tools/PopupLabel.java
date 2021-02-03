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
