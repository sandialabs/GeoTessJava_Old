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

import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractNoModelNeeded;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;
import gov.sandia.gmp.util.globals.InterpolatorType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FunctionDialog extends AbstractNoModelNeeded {

    private FileIOComponents inputModel1;
    private FileIOComponents inputGrid1;
    private TitleFieldComponents attributeIndex1;

    private FileIOComponents inputModel2;
    private FileIOComponents inputGrid2;
    private TitleFieldComponents attributeIndex2;

    private FileIOComponents geometryModel;
    private FileIOComponents geometryGrid;

    private FileIOComponents outputFile;

    private FileIOComponents referenceToGrid;

    private TitleFieldComponents functionIndex;
    private TitleFieldComponents newAttributeName;
    private TitleFieldComponents newAttributeUnits;

    private InterpolatorType interpolation;

    public FunctionDialog(GeoTessPresenter presenter, JFrame parent) {
        super(presenter, parent, "Function Dialog");
    }

    @Override
    public JPanel makeMainPanelNoModel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

   /*     JLabel model1Label = new PopupLabel("Model 1:", ParameterHelp.GEOTESS_MODEL);
        JLabel grid1Label = new PopupLabel("Grid 1:", ParameterHelp.GEOTESS_GRID);
        JLabel model2Label = new PopupLabel("Model 2:",  ParameterHelp.GEOTESS_MODEL);
        JLabel grid2Label = new PopupLabel("Grid 2:", ParameterHelp.GEOTESS_GRID);
        JLabel geometryModelLabel = new PopupLabel("Geometry Model: ", ParameterHelp.GEOMETRY_MODEL);
        JLabel geometryGridLabel = new PopupLabel("Geometry Grid: ", ParameterHelp.GEOMETRY_GRID);
        JLabel outputLabel = new PopupLabel("Output File: ", ParameterHelp.OUTPUT);
        JLabel gridReferenceLabel = new PopupLabel("Reference to Grid: ", ParameterHelp.GRID_REFERENCE_LABEL);
        int max = getMaxLabelSize(model1Label.getPreferredSize().getWidth(), grid1Label.getPreferredSize().getWidth(), model2Label.getPreferredSize().getWidth(), grid2Label.getPreferredSize().getWidth(), geometryModelLabel.getPreferredSize().getWidth(), geometryGridLabel.getPreferredSize().getWidth(), outputLabel.getPreferredSize().getWidth(), gridReferenceLabel.getPreferredSize().getWidth());

        this.inputModel1 = loadPanel(model1Label, max);
        this.inputGrid1 = loadPanel(grid1Label, max);
        p.add(inputModel1);
        p.add(inputGrid1);

        this.attributeIndex1 = new TitleFieldComponents("Attribute Index 1: ", 3, ParameterHelp.FUNCTION);
        p.add(attributeIndex1.getTitle());
        p.add(attributeIndex1.getTextBox());
        p.add(attributeIndex1.getUnits());

        this.inputModel2 = loadPanel(model2Label, max);
        this.inputGrid2 = loadPanel(grid2Label, max);
        p.add(inputModel2);
        p.add(inputGrid2);

        this.attributeIndex2 = new TitleFieldComponents("Attribute Index 2: ", 3, ParameterHelp.FUNCTION);
        p.add(attributeIndex2.getTitle());
        p.add(attributeIndex2.getTextBox());
        p.add(attributeIndex2.getUnits());

        this.geometryModel = loadPanel(geometryModelLabel, max);
        p.add(geometryModel);

        this.geometryGrid = loadPanel(geometryGridLabel, max);
        p.add(geometryGrid);

        this.outputFile = loadPanel(outputLabel, max);
        p.add(outputFile);

        this.referenceToGrid = loadPanel(gridReferenceLabel, max);
        p.add(referenceToGrid);

        this.functionIndex = new TitleFieldComponents("Function Index: ", 1, ParameterHelp.FUNCTION);
        p.add(functionIndex.getTitle());
        p.add(functionIndex.getTextBox());
        p.add(functionIndex.getUnits());

        this.newAttributeName = new TitleFieldComponents("New Attribute Name: ",12, ParameterHelp.FUNCTION);
        p.add(newAttributeName.getTitle());
        p.add(newAttributeName.getTextBox());
        p.add(newAttributeName.getUnits());

        this.newAttributeUnits = new TitleFieldComponents("New Attribute Units: ", 12, ParameterHelp.FUNCTION);
        p.add(newAttributeUnits.getTitle());
        p.add(newAttributeUnits.getTextBox());
        p.add(newAttributeUnits.getUnits());


        this.add(interpolationPanel());
*/
        return p;
    }

  /*  private FileIOPanel loadPanel(JLabel label) {
        return new FileIOPanel(label, FileOperation.LOAD, parent);
    }
*/
    private JPanel interpolationPanel() {
        JPanel panel = new JPanel();
        JLabel label = new PopupLabel("Horizontal Interpolation: ", ParameterHelp.HORIZONTAL_INTERPOLATION);
        panel.add(label);

        ButtonGroup bg = new ButtonGroup();
        JRadioButton linear = new JRadioButton("Linear");
        linear.setSelected(true);

        JRadioButton nattyNeighbor = new JRadioButton("Natural Neighbor");
        ActionListener al = new RadioButtonListener();

        linear.addActionListener(al);
        nattyNeighbor.addActionListener(al);

        bg.add(linear);
        bg.add(nattyNeighbor);

        panel.add(linear);
        panel.add(nattyNeighbor);

        return panel;
    }

    @Override
    public ActionListener getAcceptButtonListener() {
        return new SubmitButtonListener();
    }

    @Override
    public String methodHelp() {
        return MethodHelp.FUNCTION.getMethodTip();
    }


    private class RadioButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonClicked = ((JRadioButton) e.getSource()).getText();
            if(buttonClicked.equals("Linear"))
                interpolation = InterpolatorType.LINEAR;
            else interpolation = InterpolatorType.NATURAL_NEIGHBOR;
        }
    }

    private class SubmitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            //performer.function();
        }

    }

}
