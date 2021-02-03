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
