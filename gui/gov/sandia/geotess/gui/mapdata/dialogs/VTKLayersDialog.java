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

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class VTKLayersDialog extends AbstractModelNeededDialog{

	private FileIOComponents outputFile;
	private List<JCheckBox> boxes;
	private ReciprocalComponents reciprocal;
	private AttributeCheckboxPanel attributes;
	
	public VTKLayersDialog(GeoTessPresenter presenter, JFrame parent,String title) {
		super(presenter, parent, title);
		this.boxes = new ArrayList<>();
	}

	@Override
	public String methodHelp() {
		return MethodHelp.VTK_LAYERS.getMethodTip();
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill"));

        this.outputFile = new FileIOComponents("Output File: ", FileOperation.SAVE, this, ParameterHelp.OUTPUT);
        this.reciprocal = new ReciprocalComponents();
        this. attributes = new AttributeCheckboxPanel(model);
        this.attributes.setPreferredSize(new Dimension(400,150));
        
        p.add(outputFile.getTitle());
        p.add(outputFile.getTextBox());
        p.add(outputFile.getButton(), "wrap");
		
        p.add(new PopupLabel("Layers: ", ParameterHelp.LAYER_ID));
        p.add(listLayerIndexes(model), "span 2, wrap");

        p.add(reciprocal.getTitle());
        p.add(reciprocal.getButtons(), "center, wrap");
        
        p.add(attributes, "span 4, wrap");
		return p;
	}

	private JPanel listLayerIndexes(GeoTessModel model)
	{
		JPanel p = new JPanel();
		for(Integer i = 0; i < model.getNLayers(); i++)
		{
			JCheckBox check = new JCheckBox(model.getMetaData().getLayerName(i).replace("_", " "));
			check.setName(i.toString());
			boxes.add(check);
			p.add(check);
		}
		return p;
	}

	private int[] getLayerIndexes()
	{
		List<Integer> indexList = new ArrayList<Integer>();
		for(JCheckBox cb : boxes)
		{
			if(cb.isSelected())
			{
				indexList.add(Integer.parseInt(cb.getName()));
			}
		}
		
		int[] indexesAsArray = new int[indexList.size()];
		for(int i=0;i<indexList.size();i++)
			indexesAsArray[i] = indexList.get(i);
		
		return indexesAsArray;
	}
	
	@Override
	public ActionListener getAcceptButtonListener() {
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					presenter.vtkLayers(outputFile.getText(), getLayerIndexes(), reciprocal.getSelected(), attributes.getCheckedAttributeIndexes());
					destroy();
				} catch (Exception e) {
					setErrorVisible(true);
				}
			}	
		};

	}

}
