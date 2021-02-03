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
