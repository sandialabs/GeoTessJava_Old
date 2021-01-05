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

public class ReformatDialog extends AbstractModelNeededDialog {

	private FileIOComponents outputModel;
	private FileIOComponents outputGrid;
	
	public ReformatDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Reformat");
	}
	
	@Override
	public JPanel makeNewDialog(GeoTessModel model)
	{
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill", "[]15[]", "[]15[]"));

        this.outputModel = new FileIOComponents("Output Model File: ",FileOperation.SAVE, this, ParameterHelp.OUTPUT_MODEL);
        this.outputGrid = new FileIOComponents("Output Grid File: ",FileOperation.SAVE, this, ParameterHelp.OUTPUT_GRID);
        
        p.add(outputModel.getTitle());
        p.add(outputModel.getTextBox(), "span 2");
        p.add(outputModel.getButton(), "wrap");

        p.add(outputGrid.getTitle());
        p.add(outputGrid.getTextBox(), "span 2");
        p.add(outputGrid.getButton(), "wrap");
        return p;
	}

	@Override
	public String methodHelp() {
		return MethodHelp.REFORMAT.getMethodTip();
	}
	
	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}
	
	private class SubmitButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				presenter.reformat(outputModel.getText(), outputGrid.getText());
				destroy();
			} catch (Exception e) {
				setErrorVisible(true);
			}
		}
		
	}
}
