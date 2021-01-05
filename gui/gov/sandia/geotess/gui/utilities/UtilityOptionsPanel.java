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
