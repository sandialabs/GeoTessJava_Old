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

package gov.sandia.geotess.gui.mapdata.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.RadioButtonPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class VTKLayerBoundaryDialog extends AbstractModelNeededDialog{

	private FileIOComponents outputFile;
	private RadioButtonPanel depthElev;
	private HorizontalInterpolationComponents horizontal;
	
	public VTKLayerBoundaryDialog(GeoTessPresenter presenter, JFrame parent,String title) {
		super(presenter, parent, title);
	}

	@Override
	public String methodHelp() {
		return MethodHelp.VTK_LAYER_BOUNDARY.getMethodTip();
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout());

        this.outputFile = new FileIOComponents("Output File: ", FileOperation.SAVE, this, ParameterHelp.OUTPUT);
        this.depthElev = new RadioButtonPanel("Depth", "Elevation");
        this.horizontal = new HorizontalInterpolationComponents();
        
        p.add(outputFile.getTitle());
        p.add(outputFile.getTextBox());
        p.add(outputFile.getButton(), "wrap");

		p.add(new PopupLabel("Depth or Elevation: ", ParameterHelp.DEPTH_OR_ELEVATION));
		p.add(depthElev, "wrap");
		
		p.add(horizontal.getTitle());
		p.add(horizontal.getButtons(), "wrap");
		return p;
	}
	
	@Override
	public ActionListener getAcceptButtonListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					presenter.vtkLayerBoundary(outputFile.getText(), depthElev.getSelected(), horizontal.getInterpolation());
					destroy();
				} catch (IOException | GeoTessException e) {
					setErrorVisible(true);
				}
				
			}
			
		};
	}

}
