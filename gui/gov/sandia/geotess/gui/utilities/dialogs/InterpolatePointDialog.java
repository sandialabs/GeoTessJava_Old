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
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.RadialInterpolationComponents;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class InterpolatePointDialog extends AbstractModelNeededDialog {
	private HorizontalInterpolationComponents horizontal;
	private RadialInterpolationComponents radial;
	private ReciprocalComponents reciprocal;
	private LatLonComponents latlon;
	private TitleFieldComponents depth;
	private TitleFieldComponents layer;
	
	public InterpolatePointDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Interpolate Point");
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout());
		this.depth = new TitleFieldComponents("Depth: ", 7, ParameterHelp.DEPTH);
		this.layer = new TitleFieldComponents("Layer: ", 7,ParameterHelp.LAYER_ID);
		this.horizontal = new HorizontalInterpolationComponents();
		this.radial = new RadialInterpolationComponents();
		this.reciprocal = new ReciprocalComponents();
		p.add(fieldPanel(), "wrap");
		p.add(radioPanel(),"wrap");

		return p;
	}

	private JPanel radioPanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new MigLayout());
		p.add(horizontal.getTitle());
		p.add(horizontal.getButtons(), "wrap");

		p.add(radial.getTitle());
		p.add(radial.getButtons(), "wrap");
		
		p.add(reciprocal.getTitle());
		p.add(reciprocal.getButtons(), "wrap");
		return p;
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	@Override
	public String methodHelp() {
		return MethodHelp.INTERPOLATE_POINT.getMethodTip();
	}

	private JPanel fieldPanel() {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill", "[][]20[]"));
		this.latlon = new LatLonComponents();
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

	private class SubmitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				presenter.interpolatePoint(latlon.getLat(), latlon.getLon(),
						Double.parseDouble(depth.getFieldValue()),
						Integer.parseInt(layer.getFieldValue()),
						horizontal.getInterpolation(),
						radial.getInterpolation(), reciprocal.getSelected());
				destroy();
			} catch (GeoTessException | IOException | NumberFormatException e) {
				setErrorVisible(true);
			}


		}
	}

}
