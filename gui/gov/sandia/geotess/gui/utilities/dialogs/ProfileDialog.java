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

package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.DeepestShallowestComponents;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.RadioButtonPanel;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ProfileDialog extends AbstractModelNeededDialog {

    private LatLonComponents latlon;
    private DeepestShallowestComponents layers;
    private RadioButtonPanel depthRadius;
    private ReciprocalComponents reciprocal;
    private AttributeCheckboxPanel attributes;
    
    public ProfileDialog(GeoTessPresenter presenter, JFrame parent) {
        super(presenter, parent, "Profile Dialog");
    }

    @Override
    public ActionListener getAcceptButtonListener() {
        return new SubmitButtonListener();
    }

    @Override
    public JPanel makeNewDialog(GeoTessModel model) {
        JPanel p = new JPanel();
        p.setLayout(new MigLayout());

        this.depthRadius = new RadioButtonPanel("Depth", "Radius");
        this.reciprocal = new ReciprocalComponents();
        this.layers = new DeepestShallowestComponents(model);
        this.latlon = new LatLonComponents();
        this.attributes = new AttributeCheckboxPanel(model);
        this.attributes.setPreferredSize(new Dimension(300, 150));
        
        p.add(latlon.getLatTitle());
        p.add(latlon.getLatTextBox(), "split 2");
        p.add(latlon.getLatUnits(), "wrap");
        
        p.add(latlon.getLonTitle());
        p.add(latlon.getLonTextBox(), "split 2");
        p.add(latlon.getLonUnits(), "wrap");
        
        //deepest shallowest
        p.add(layers.getDeepLabel());
        p.add(layers.getDeepBox(), "wrap");
        p.add(layers.getShallowLabel());
        p.add(layers.getShallowBox(), "wrap");

        //depth or radius
        p.add(new PopupLabel("Depth Or Radius: ", ParameterHelp.DEPTH_OR_RADIUS));
        p.add(depthRadius, "wrap");

        //reciprocal
        p.add(reciprocal.getTitle());
        p.add(reciprocal.getButtons(), "wrap");

        p.add(attributes, "spanx 3, wrap");
        
        return p;
    }

    @Override
    public String methodHelp() {
        return MethodHelp.PROFILE.getMethodTip();
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            int[] attributeIndexes = attributes.getCheckedAttributeIndexes();
            try {
                presenter.profile(latlon.getLat(), latlon.getLon(), layers.getDeepestIndex(),
                        layers.getShallowestIndex()
                        , depthRadius.getSelected(), reciprocal.getSelected(), attributeIndexes);
                destroy();
            } catch(IOException | GeoTessException | NumberFormatException e) {
            	setErrorVisible(true);
            }
        }
    }



}
