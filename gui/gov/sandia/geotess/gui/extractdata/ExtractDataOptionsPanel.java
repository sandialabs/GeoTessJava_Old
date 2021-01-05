package gov.sandia.geotess.gui.extractdata;

import gov.sandia.geotess.gui.extractdata.dialogs.*;
import gov.sandia.geotess.gui.interfaces.Function;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractOptionsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedMap;
import java.util.TreeMap;

public class ExtractDataOptionsPanel extends AbstractOptionsPanel {

	public ExtractDataOptionsPanel(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent);
	}

	@Override
	public SortedMap<String, Function> specificFunctionList() {

        SortedMap<String, Function> map = new TreeMap<String, Function>();
		map.put("Slice", new SliceDialog(presenter, parent));
        map.put("SliceDistAz", new SliceDistAzDialog(presenter, parent));
   		map.put("Map Values Depth",  new MapValuesDepthDialog(presenter, parent));
		map.put("Map Values Layer", new MapValuesLayerDialog(presenter, parent));
        map.put("Map Layer Boundary", new MapLayerBoundaryDialog(presenter, parent));
        map.put("Map Layer Thickness", new MapLayerThicknessDialog(presenter, parent));
        map.put("Values 3D Block", new Values3DBlockDialog(presenter, parent));
		return map;
	}
	
	@Override
	public ActionListener helpListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(getSelected() == null)
					presenter.writeToExtractDataPanel("Please Select A Function From The List Before Attempted To Receive Assistance");
				else presenter.writeToExtractDataPanel(getSelectedFunction().methodHelp());
			}
		};
	}
}
