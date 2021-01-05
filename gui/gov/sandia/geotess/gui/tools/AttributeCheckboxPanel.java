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
