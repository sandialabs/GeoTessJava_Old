package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class FindClosestPointDialog extends AbstractModelNeededDialog {

	private LatLonComponents latlon;
	private TitleFieldComponents depth;
	private TitleFieldComponents layer;
	private LinkedHashMap<JCheckBox, String> boxes;

	public FindClosestPointDialog(GeoTessPresenter performer, JFrame parent) {
		super(performer, parent, "Find Closest Point Dialog");
		this.boxes = new LinkedHashMap<>();
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill"));
		this.latlon = new LatLonComponents();
		this.depth = new TitleFieldComponents("Depth: ", 7, ParameterHelp.DEPTH);
		this.layer = new TitleFieldComponents("Layer: ", 7,
				ParameterHelp.LAYER_ID);

		p.add(textEntryPanel(), "center, wrap");
		p.add(output(), "wrap");
		return p;
	}

	private JPanel textEntryPanel() {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill", "[][]20[]"));

		p.add(latlon.getLatTitle());
		p.add(latlon.getLatTextBox(), "split 2");
		p.add(latlon.getLatUnits());

		p.add(latlon.getLonTitle());
		p.add(latlon.getLonTextBox(), "split 2");
		p.add(latlon.getLonUnits(), "wrap");

		p.add(depth.getTitle());
		p.add(depth.getTextBox(), "split 2");
		p.add(depth.getUnits());

		p.add(layer.getTitle());
		p.add(layer.getTextBox(), "split 2");
		p.add(layer.getUnits(), "wrap");

		return p;
	}

	@Override
	public String methodHelp() {
		return MethodHelp.FIND_CLOSEST_POINT.getMethodTip();
	}

	private JPanel output() {
		JPanel panel = new JPanel();

		String[] outputValues = { "lat", "lon", "depth", "radius", "vertex",
				"layer", "node", "point" };
		JLabel label = new PopupLabel("Output: ", ParameterHelp.OUTPUT);
		panel.add(label);
		for (String s : outputValues) {
			JCheckBox checkbox = new JCheckBox(s);
			boxes.put(checkbox, s);
			panel.add(checkbox);
		}
		return panel;
	}

	private List<String> getOutputTypes() {
		List<String> selected = new ArrayList<String>();
		for (JCheckBox jb : boxes.keySet()) {
			if (jb.isSelected())
				selected.add(boxes.get(jb));
		}
		return selected;
	}

	private class SubmitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			List<String> out = getOutputTypes();
			try {
				presenter.findClosestPoint(latlon.getLat(), latlon.getLon(),
						Double.parseDouble(depth.getFieldValue()),
						Integer.parseInt(layer.getFieldValue()), out);
				destroy();
			} catch (IOException | GeoTessException | NumberFormatException e) {
				setErrorVisible(true);
			}
		}

	}
}
