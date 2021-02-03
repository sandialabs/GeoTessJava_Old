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
