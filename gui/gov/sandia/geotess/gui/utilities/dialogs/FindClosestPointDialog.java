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
