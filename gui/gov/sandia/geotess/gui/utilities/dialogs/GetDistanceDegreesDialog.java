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

import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractNoModelNeeded;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class GetDistanceDegreesDialog extends AbstractNoModelNeeded {

	private LatLonComponents latlon1;
	private LatLonComponents latlon2;
	private TitleFieldComponents nPoints;

	public GetDistanceDegreesDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Distance Degrees");
	}

	@Override
	public JPanel makeMainPanelNoModel() {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill"));
		p.add(latlonPanel(), "wrap");
		p.add(nPointPanel(), "center, wrap");
		return p;
	}

	private JPanel nPointPanel() {
		JPanel p = new JPanel();
		this.nPoints = new TitleFieldComponents("Enter n Points: ", 7,
				ParameterHelp.N_POINTS);
		p.add(nPoints.getTitle());
		p.add(nPoints.getTextBox());
		return p;
	}

	private JPanel latlonPanel() {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill", "[][]15[]"));
		this.latlon1 = new LatLonComponents("Latitude 1: ", "Longitude 1: ");
		this.latlon2 = new LatLonComponents("Latitude 2: ", "Longitude 2: ");

		p.add(latlon1.getLatTitle());
		p.add(latlon1.getLatTextBox(), "split 2");
		p.add(latlon1.getLatUnits());

		p.add(latlon1.getLonTitle());
		p.add(latlon1.getLonTextBox(), "split 2");
		p.add(latlon1.getLonUnits(), "wrap");

		p.add(latlon2.getLatTitle());
		p.add(latlon2.getLatTextBox(), "split 2");
		p.add(latlon2.getLatUnits());

		p.add(latlon2.getLonTitle());
		p.add(latlon2.getLonTextBox(), "split 2");
		p.add(latlon2.getLonUnits(), "wrap");

		return p;
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	@Override
	public String methodHelp() {
		return MethodHelp.GET_DISTANCE_DEGREES.getMethodTip();

	}

	private class SubmitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int points = Integer.parseInt(nPoints.getFieldValue().trim());
			presenter.getDistanceDegrees(latlon1.getLat(), latlon1.getLon(),
					latlon2.getLat(), latlon2.getLon(), points);
			destroy();

		}
	}
}
