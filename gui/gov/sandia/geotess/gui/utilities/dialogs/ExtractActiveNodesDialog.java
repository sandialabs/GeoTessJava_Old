package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;

import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ExtractActiveNodesDialog extends AbstractModelNeededDialog {
	private ReciprocalComponents reciprocal;
	private AttributeCheckboxPanel attributes;
	private FileIOComponents polygonPath;
	private FileIOComponents output;

	public ExtractActiveNodesDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Extract Active Nodes Dialog");
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fillx"));
		this.attributes = new AttributeCheckboxPanel(model);
		this.polygonPath = new FileIOComponents("Path of Polygon File: ",
				FileOperation.LOAD, this, ParameterHelp.PATH_TO_POLYGON);
		this.polygonPath.getTextBox().addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (polygonPath.getTextBox().getText()
						.equalsIgnoreCase("(Optional)"))
					polygonPath.getTextBox().setText("");
				else if (polygonPath.getText().isEmpty())
					polygonPath.getTextBox().setText("(Optional)");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (polygonPath.getTextBox().getText().isEmpty())
					polygonPath.getTextBox().setText("(Optional)");
			}

		});

		this.output = new FileIOComponents("Output File: ", FileOperation.SAVE,
				this, ParameterHelp.OUTPUT);
		this.reciprocal = new ReciprocalComponents();
		this.attributes.setPreferredSize(new Dimension(440, 150));

		panel.add(polygonPath.getTitle());
		panel.add(polygonPath.getTextBox());
		panel.add(polygonPath.getButton(), "wrap");
		panel.add(output.getTitle());
		panel.add(output.getTextBox());
		panel.add(output.getButton(), "wrap");
		panel.add(reciprocal.getTitle());
		panel.add(reciprocal.getButtons(), "wrap");
		panel.add(attributes, "spanx 3, spany 2, wrap");
		return panel;
	}

	@Override
	public String methodHelp() {
		return MethodHelp.EXTRACT_ACTIVE_NODES.getMethodTip();
	}

	private class SubmitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				String polyPath = polygonPath.getText();
				if (polygonPath.getText().isEmpty() || polygonPath.getText().equalsIgnoreCase("(optional)")) {
					polyPath = "";
				}
				presenter.extractActiveNodes(
						attributes.getCheckedAttributeIndexes(),
						reciprocal.getSelected(), polyPath, output.getText());
				destroy();
			} catch (Exception e) {
				setErrorVisible(true);
			}
		}
	}
}
