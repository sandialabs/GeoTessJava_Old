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

package gov.sandia.geotess.gui.extractdata.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.DeltaOrNComponents;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class MapLayerThicknessDialog extends AbstractModelNeededDialog {

	private LatLonComponents latlon1;
	private LatLonComponents latlon2;

	private DeltaOrNComponents latSpacing;
	private DeltaOrNComponents lonSpacing;

	private HorizontalInterpolationComponents horizontal;
	private TitleFieldComponents firstLayerID;
	private TitleFieldComponents lastLayerID;

	private FileIOComponents output;
	
	public MapLayerThicknessDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Map Thickness Dialog");
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill", "[][]10[]"));

		this.output = new FileIOComponents("Output File: ", FileOperation.SAVE,
				parent, ParameterHelp.OUTPUT);
		this.latSpacing = new DeltaOrNComponents("Latitude Spacing: ");
		this.lonSpacing = new DeltaOrNComponents("Longitude Spacing: ");
		this.horizontal = new HorizontalInterpolationComponents();
		this.latlon1 = new LatLonComponents("First Latitude: ",
				"First Longitude: ");
		this.latlon2 = new LatLonComponents("Last Latitude: ",
				"Last Longitude: ");
		this.firstLayerID = layerIDPanel("First Layer ID: ",
				ParameterHelp.FIRST_LAYER_ID);
		this.lastLayerID = layerIDPanel("Last Layer ID: ",
				ParameterHelp.LAST_LAYER_ID);
		
		panel.add(latlon1.getLatTitle());
		panel.add(latlon1.getLatTextBox(), "split 2");
		panel.add(latlon1.getLatUnits());

		panel.add(latlon2.getLatTitle());
		panel.add(latlon2.getLatTextBox(), "split 2");
		panel.add(latlon2.getLatUnits(), "wrap");

		panel.add(latSpacing.getTitle());
		panel.add(latSpacing.getButtons());
		panel.add(latSpacing.getTextField(), "wrap");

		panel.add(latlon1.getLonTitle());
		panel.add(latlon1.getLonTextBox(), "split 2");
		panel.add(latlon1.getLonUnits());

		panel.add(latlon2.getLonTitle());
		panel.add(latlon2.getLonTextBox(), "split 2");
		panel.add(latlon2.getLonUnits(), "wrap");

		panel.add(lonSpacing.getTitle());
		panel.add(lonSpacing.getButtons());
		panel.add(lonSpacing.getTextField(), "wrap");

		panel.add(firstLayerID.getTitle());
		panel.add(firstLayerID.getTextBox());

		panel.add(lastLayerID.getTitle());
		panel.add(lastLayerID.getTextBox(), "wrap");

		panel.add(horizontal.getTitle());
		panel.add(horizontal.getButtons(), "wrap");

		panel.add(output.getTitle());
		panel.add(output.getTextBox(), "spanx 2");
		panel.add(output.getButton(), "wrap");
	
		
		return panel;
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String lat1 = String.valueOf(latlon1.getLat());
					String lat2 = String.valueOf(latlon2.getLat());
					String lon1 = String.valueOf(latlon1.getLon());
					String lon2 = String.valueOf(latlon2.getLon());
					presenter.mapLayerThickness(lat1, lat2, latSpacing
							.getTextField().getText(), lon1, lon2, lonSpacing
							.getTextField().getText(), Integer
							.parseInt(firstLayerID.getFieldValue()), Integer
							.parseInt(lastLayerID.getFieldValue()), horizontal.getInterpolation(),
							output.getText());

					destroy();
					
				} catch (IOException | GeoTessException | NumberFormatException ee) {
					setErrorVisible(true);
				}
			}
		};

	}

	@Override
	public String methodHelp() {
		return MethodHelp.MAP_LAYER_THICKNESS.getMethodTip();
	}

	private TitleFieldComponents layerIDPanel(String s, ParameterHelp help) {
		return new TitleFieldComponents(s, 4, help);
	}

}
