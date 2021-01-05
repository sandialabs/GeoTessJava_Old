package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractNoModelNeeded;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.RadioButtonPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class TranslatePolygonDialog extends AbstractNoModelNeeded {

    private FileIOComponents inputPolygon;
    private FileIOComponents outputPolygon;
    private RadioButtonPanel radioButtons;
    
    public TranslatePolygonDialog(GeoTessPresenter presenter, JFrame parent) {
        super(presenter, parent, "Translate Polygon");
    }

    @Override
    public JPanel makeMainPanelNoModel() {
        JPanel p = new JPanel();
        p.setLayout(new MigLayout("fill","[][][][]", "[]15[]"));
        this.inputPolygon = new FileIOComponents("Input Polygon: ", FileOperation.LOAD, this, ParameterHelp.INPUT_POLYGON);
        p.add(inputPolygon.getTitle());
        p.add(inputPolygon.getTextBox());
        p.add(inputPolygon.getButton(), "wrap");

        this.outputPolygon = new FileIOComponents("Output Polygon File: ", FileOperation.SAVE, this, ParameterHelp.OUTPUT_POLYGON);
        p.add(outputPolygon.getTitle());
        p.add(outputPolygon.getTextBox());
        p.add(outputPolygon.getButton(), "wrap");

        JLabel label = new PopupLabel("Output Type: ", ParameterHelp.OUTPUT);
        p.add(label);
        
        this.radioButtons = new RadioButtonPanel("ASCII", "VTK", "KML", "KMZ");
        p.add(radioButtons, "wrap");
        return p;
    }

    @Override
    public ActionListener getAcceptButtonListener() {
        return new SubmitButtonListener();
    }

    @Override
    public String methodHelp() {
        return MethodHelp.TRANSLATE_POLYGON.getMethodTip();

    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            try {
                presenter.translatePolygon(inputPolygon.getText(), outputPolygon.getText(), radioButtons.getSelected());
                destroy();
            } catch(IOException e) {
            	setErrorVisible(true);
            }
        }
    }
}
