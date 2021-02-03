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
