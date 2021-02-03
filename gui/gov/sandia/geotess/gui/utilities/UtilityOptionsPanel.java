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

package gov.sandia.geotess.gui.utilities;

import gov.sandia.geotess.gui.interfaces.Function;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractOptionsPanel;
import gov.sandia.geotess.gui.utilities.dialogs.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedMap;
import java.util.TreeMap;

public class UtilityOptionsPanel extends AbstractOptionsPanel {

    public UtilityOptionsPanel(GeoTessPresenter presenter, JFrame parent) {

        // This constructor calls the constructor of the AbstractOptionsPanel.  The
        // performer and parent variables are stored in the AbstractOptionsPanel and
        // are accessed by this child class.
        super(presenter, parent);
    }

    @Override
    public SortedMap<String, Function> specificFunctionList() {

        SortedMap<String, Function> map = new TreeMap<String, Function>();

        map.put("Borehole", new BoreholeDialog(presenter, parent));
        map.put("ToString", new ToString(presenter));
        map.put("Statistics", new Statistics(presenter));
        map.put("Extract Grid", new ExtractGridDialog(presenter, parent));
        map.put("Extract Active Nodes", new ExtractActiveNodesDialog(presenter, parent));
        map.put("Equals", new EqualsDialog(presenter, parent));
        map.put("Replace Attribute Values", new ReplaceAttributeValuesDialog(presenter, parent));
        map.put("Reformat", new ReformatDialog(presenter, parent));
        map.put("Get Values", new GetValuesDialog(presenter, parent));
        map.put("Get Values File", new GetValuesFileDialog(presenter, parent));
        map.put("Interpolate Point", new InterpolatePointDialog(presenter, parent));
        map.put("Profile", new ProfileDialog(presenter, parent));
        map.put("Find Closest Point", new FindClosestPointDialog(presenter, parent));
        map.put("Get Distance Degrees", new GetDistanceDegreesDialog(presenter, parent));
        map.put("Translate Polygon", new TranslatePolygonDialog(presenter, parent));
        map.put("Function (Not yet Implemented)", new FunctionDialog(presenter, parent));
        return map;
    }

    @Override
    public ActionListener helpListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(getSelected() == null)
                    presenter.writeToUtilityPanel("Please Select A Function From The List Before Attempted To Receive Assistance");
                else presenter.writeToUtilityPanel(getSelectedFunction().methodHelp());
            }
        };
    }
}
