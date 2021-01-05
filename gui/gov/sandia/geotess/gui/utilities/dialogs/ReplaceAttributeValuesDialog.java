package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ReplaceAttributeValuesDialog extends AbstractModelNeededDialog {
    private FileIOComponents output;
    private FileIOComponents polygonPath;
    private FileIOComponents fileOfAttributes;
    
    public ReplaceAttributeValuesDialog(GeoTessPresenter presenter, JFrame parent) {
        super(presenter, parent, "Replace Attribute Values");
    }

    @Override
    public JPanel makeNewDialog(GeoTessModel model) {
        JPanel p = new JPanel();
        p.setLayout(new MigLayout("fill", "[]15[]", "[]20[]"));

        this.output = new FileIOComponents("Output File: ", FileOperation.SAVE, this, ParameterHelp.OUTPUT);
        this.polygonPath = new FileIOComponents("Path to Polygon File: ", FileOperation.LOAD, this, ParameterHelp.PATH_TO_POLYGON);
        this.fileOfAttributes = new FileIOComponents("File of Attributes: ", FileOperation.LOAD, this, ParameterHelp.ATTRIBUTES_FILE);
        
        p.add(polygonPath.getTitle());
        p.add(polygonPath.getTextBox());
        p.add(polygonPath.getButton(), "wrap");

        p.add(fileOfAttributes.getTitle());
        p.add(fileOfAttributes.getTextBox());
        p.add(fileOfAttributes.getButton(), "wrap");

        p.add(output.getTitle());
        p.add(output.getTextBox());
        p.add(output.getButton(), "wrap");
        
        return p;
    }

    @Override
    public ActionListener getAcceptButtonListener() {
        return new SubmitButtonListener();
    }

    @Override
    public String methodHelp() {
        return MethodHelp.REPLACE_ATTRIBUTE_VALUES.getMethodTip();
    }


    private class SubmitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            try {
                presenter.replaceAttributeValues(polygonPath.getText(), fileOfAttributes.getText(), output.getText());
            } catch(Exception e) {
            	setErrorVisible(true);
            }

        }

    }

}
