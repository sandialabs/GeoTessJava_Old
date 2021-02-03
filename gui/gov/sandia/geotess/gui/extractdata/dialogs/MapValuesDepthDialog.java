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

package gov.sandia.geotess.gui.extractdata.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.DeltaOrNComponents;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.RadialInterpolationComponents;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapValuesDepthDialog extends AbstractModelNeededDialog {
    private TitleFieldComponents lat1;
    private TitleFieldComponents lat2;
    private TitleFieldComponents lon1;
    private TitleFieldComponents lon2;

    private DeltaOrNComponents latSpacing;
    private DeltaOrNComponents lonSpacing;

    private TitleFieldComponents depth;
    private TitleFieldComponents layer;

    private HorizontalInterpolationComponents horizontalInterpolation;
    private RadialInterpolationComponents radialInterpolation;

    private ReciprocalComponents reciprocal;
    private AttributeCheckboxPanel attributes;

    private FileIOComponents output;
    
    public MapValuesDepthDialog(GeoTessPresenter presenter, JFrame parent) {
        super(presenter, parent, "Map Values Depth");
    }

    @Override
    public JPanel makeNewDialog(GeoTessModel model) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.output = new FileIOComponents("Output File: ", FileOperation.SAVE, parent, ParameterHelp.OUTPUT);
        this.latSpacing = new DeltaOrNComponents("Latitude Spacing: ");
        this.lonSpacing = new DeltaOrNComponents("Longitude Spacing: ");
        this.depth = new TitleFieldComponents("Depth: ", 7, ParameterHelp.DEPTH);
        this.layer = new TitleFieldComponents("Layer: ", 7, ParameterHelp.LAYER_ID);
        this.horizontalInterpolation = new HorizontalInterpolationComponents();
        this.radialInterpolation = new RadialInterpolationComponents();
        this.reciprocal = new ReciprocalComponents();
        
        panel.add(lats());
        panel.add(latSpacing);
        panel.add(lons());
        panel.add(lonSpacing);

        panel.add(depth.getTitle());
        panel.add(depth.getTextBox());

        panel.add(layer.getTitle());
        panel.add(layer.getTextBox());

        panel.add(horizontalInterpolation.getTitle());
        panel.add(horizontalInterpolation.getButtons());

        panel.add(radialInterpolation.getTitle());
        panel.add(radialInterpolation.getButtons());


        panel.add(reciprocal.getButtons());
        panel.add(attributesPanel(model));

        panel.add(output.getTitle());
        panel.add(output.getTextBox());
        panel.add(output.getButton(), "wrap");
        return panel;
    }

    private JPanel attributesPanel(GeoTessModel model) {
        return attributes = new AttributeCheckboxPanel(model);
    }

    private JPanel lons() {
        JPanel p = new JPanel();
        this.lon1 = new TitleFieldComponents("First Longitude: ", 7, "degrees", ParameterHelp.FIRST_LON);
        this.lon2 = new TitleFieldComponents("Last Longitude: ", 7, "degrees", ParameterHelp.LAST_LON);

        p.add(lon1.getTitle());
        p.add(lon1.getTextBox());

        p.add(lon2.getTitle());
        p.add(lon2.getTextBox());


        return p;
    }

    private JPanel lats() {
        JPanel p = new JPanel();
        this.lat1 = new TitleFieldComponents("First Latitude: ", 7, "degrees", ParameterHelp.FIRST_LAT);
        this.lat2 = new TitleFieldComponents("Last Latitude: ", 7, "degrees", ParameterHelp.LAST_LAT);

        p.add(lat1.getTitle());
        p.add(lat1.getTextBox());


        p.add(lat2.getTitle());
        p.add(lat2.getTextBox());


        return p;
    }

    @Override
    public String methodHelp() {
        return MethodHelp.MAP_VALUES_DEPTH.getMethodTip();
    }

    @Override
    public ActionListener getAcceptButtonListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    presenter.mapValuesDepth(
                            lat1.getFieldValue(),
                            lat2.getFieldValue(),
                            latSpacing.getTextField().getText(),
                            lon1.getFieldValue(),
                            lon2.getFieldValue(),
                            lonSpacing.getTextField().getText(),
                            Integer.parseInt(layer.getFieldValue()), Double.parseDouble(depth.getFieldValue()),
                            horizontalInterpolation.getInterpolation(),
                            radialInterpolation.getInterpolation(),
                            reciprocal.getSelected(), attributes.getCheckedAttributeIndexes(),
                            output.getText());
                    destroy();
                } catch(Exception e)
                {
                	setErrorVisible(true);
                }

            }
        };
    }

}
