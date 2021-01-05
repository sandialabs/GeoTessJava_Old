package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.RadioButtonPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ExtractGridDialog extends AbstractModelNeededDialog {

	private RadioButtonPanel outputType;
	private FileIOComponents output;

	public ExtractGridDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Extract Grid Dialog");
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel main = new JPanel();
		main.setLayout(new MigLayout("fill", "10[]10[]", "[]20[]"));
		this.output = new FileIOComponents("Output File: ", FileOperation.SAVE,
				this, ParameterHelp.OUTPUT);
		this.outputType = new RadioButtonPanel("Binary", "Lat-Lon Format",
				"GMT", "VTK", "KML", "KMZ", "ASCII");
		main.add(new PopupLabel("Output Format: ", ParameterHelp.OUTPUT));
		main.add(outputType, "span 2, wrap");
		main.add(outputFile(), "center, spanx 2, wrap");

		return main;
	}

	private JPanel outputFile() {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout());
		p.add(output.getTitle());
		p.add(output.getTextBox());
		p.add(output.getButton());
		return p;
	}

	@Override
	public String methodHelp() {
		return MethodHelp.EXTRACT_GRID.getMethodTip();
	}

	private class SubmitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {

				// TODO change this to be the tessID rather than -1
				presenter.extractGrid(outputType.getSelected(),
						output.getText(), -1);
				destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
