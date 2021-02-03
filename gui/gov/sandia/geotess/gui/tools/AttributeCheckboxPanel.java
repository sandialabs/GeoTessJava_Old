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


import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.ParameterHelp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AttributeCheckboxPanel extends JPanel {

    private GeoTessModel model;
    private CheckBoxList checkBoxes;

    public AttributeCheckboxPanel(GeoTessModel model) {
        this.model = model;
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.checkBoxes = new CheckBoxList();
        this.checkBoxes.setListData(constructCheckBoxes());
        
        JPanel selectCancel = makeSelectAllPanel();
        JScrollPane scrollPanel = new JScrollPane(checkBoxes);
        
        this.add(scrollPanel, BorderLayout.CENTER);
        this.add(selectCancel, BorderLayout.NORTH);
    }

    private JPanel makeSelectAllPanel() {
        JPanel selectAllPanel = new JPanel();
        selectAllPanel.add(new PopupLabel("Attributes", ParameterHelp.ATTRIBUTES));
        ButtonGroup bg = new ButtonGroup();

        JCheckBox selectAll = new JCheckBox("Select All");
        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < checkBoxes.getModel().getSize(); i++)
                    checkBoxes.getModel().getElementAt(i).setSelected(true);
                repaint();
            }
        });

        JCheckBox cancelAll = new JCheckBox("Unselect All");
        cancelAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < checkBoxes.getModel().getSize(); i++)
                    checkBoxes.getModel().getElementAt(i).setSelected(false);
                repaint();
            }
        });

        bg.add(selectAll);
        bg.add(cancelAll);
        selectAllPanel.add(selectAll);
        selectAllPanel.add(cancelAll);
        return selectAllPanel;
    }

    private JCheckBox[] constructCheckBoxes() {
        List<JCheckBox> boxes = new ArrayList<JCheckBox>();
        for(String s : model.getMetaData().getAttributeNames()) {
            String units = model.getMetaData().getAttributeUnit(model.getMetaData().getAttributeIndex(s));
            JCheckBox currentBox = new JCheckBox(s + " (" + units + ")");
            currentBox.setName(s);
            boxes.add(currentBox);
        }
        return boxes.toArray(new JCheckBox[boxes.size()]);
    }

    public int[] getCheckedAttributeIndexes() {
        List<Integer> indexes = new ArrayList<Integer>();
        for(int i = 0; i < checkBoxes.getModel().getSize(); i++) {
            if(checkBoxes.getModel().getElementAt(i).isSelected()) {
                // gets the index of the attribute associated with a checkbox
                // and adds it to a list
                // of attribute indices to be returned
                indexes.add(model.getMetaData().getAttributeIndex(checkBoxes.getModel().getElementAt(i).getName()));
            }
        }
        return convertToArray(indexes);
    }

    private int[] convertToArray(List<Integer> list) {
        int[] listAsArray = new int[list.size()];
        for(int i = 0; i < list.size(); i++) {
            listAsArray[i] = list.get(i);
        }
        return listAsArray;
    }   
}
