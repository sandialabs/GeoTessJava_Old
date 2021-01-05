package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractNoModelNeeded;
import gov.sandia.geotess.gui.tools.FileIOComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class EqualsDialog extends AbstractNoModelNeeded {

	private FileIOComponents model1;
	private FileIOComponents grid1;
	private FileIOComponents model2;
	private FileIOComponents grid2;

	public EqualsDialog(GeoTessPresenter performer, JFrame parent) {
		super(performer, parent, "Equals File Loader");
	}

	@Override
	public JPanel makeMainPanelNoModel() {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill"));

		model1 = new FileIOComponents("Model 1: ", FileOperation.LOAD, this,
				ParameterHelp.GEOTESS_MODEL);
		grid1 = new FileIOComponents("Grid 1: ", FileOperation.LOAD, this,
				ParameterHelp.GEOTESS_GRID);
		model2 = new FileIOComponents("Model 2: ", FileOperation.LOAD, this,
				ParameterHelp.GEOTESS_MODEL);
		grid2 = new FileIOComponents("Grid 2: ", FileOperation.LOAD, this,
				ParameterHelp.GEOTESS_GRID);
		
		p.add(model1.getTitle());
		p.add(model1.getTextBox(), "span 2");
		p.add(model1.getButton(), "wrap");
		p.add(grid1.getTitle());
		p.add(grid1.getTextBox(), "span 2");
		p.add(grid1.getButton(), "wrap");

		p.add(model2.getTitle());
		p.add(model2.getTextBox(), "span 2");
		p.add(model2.getButton(), "wrap");

		p.add(grid2.getTitle());
		p.add(grid2.getTextBox(), "span 2");
		p.add(grid2.getButton(), "wrap");

		return p;
	}

	@Override
	public String methodHelp() {
		return MethodHelp.EQUALS.getMethodTip();
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	private class SubmitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
				try {
					presenter.updateEquals(model1.getText(), grid1.getText(),model2.getText(), grid2.getText());
				} catch (IOException e) {
					e.printStackTrace();
				}
				destroy();
			
		}

	}
}
