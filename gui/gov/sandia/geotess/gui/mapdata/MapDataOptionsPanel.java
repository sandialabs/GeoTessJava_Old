package gov.sandia.geotess.gui.mapdata;

import gov.sandia.geotess.gui.interfaces.Function;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.mapdata.dialogs.*;
import gov.sandia.geotess.gui.tools.AbstractOptionsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedMap;
import java.util.TreeMap;

public class MapDataOptionsPanel extends AbstractOptionsPanel
{
	public MapDataOptionsPanel(GeoTessPresenter presenter, JFrame parent)
	{
		super(presenter, parent);
	}
	
	@Override
	public SortedMap<String,Function> specificFunctionList()
	{
		SortedMap<String, Function> map = new TreeMap<String, Function>();
        map.put("VTK Layers",new VTKLayersDialog(presenter, parent, "VTK Layers"));
		map.put("VTK Depths", new VTKDepthsDialog(presenter, parent, "VTK Depths"));
		map.put("VTK Depths 2", new VTKDepths2Dialog(presenter, parent, "VTK Depths 2"));
		map.put("VTK Layer Thickness", new VTKLayerThicknessDialog(presenter, parent, "VTK Layer Thickness"));
		map.put("VTK Layer Boundary", new VTKLayerBoundaryDialog(presenter, parent, "VTK Layer Boundary"));
        map.put("VTK Slice", new VTKSliceDialog(presenter, parent, "VTK Slice"));
        map.put("VTK Solid", new VTKSolidDialog(presenter, parent, "VTK Solid"));
        map.put("VTK 3D Block", new VTK3DBlockDialog(presenter, parent, "VTK 3D Block"));
		return map;
	}
	
	@Override
	public ActionListener helpListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
                if(getSelected() == null)
                    presenter.writeToMapDataPanel("Please Select A Function From The List Before Attempted To Receive Assistance");
                else presenter.writeToMapDataPanel(getSelectedFunction().methodHelp());
			}
		};
	}
}
