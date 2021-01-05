package gov.sandia.geotess.gui.mapdata.dialogs;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class VTKDepthsDialog extends AbstractModelNeededDialog {

	private FileIOComponents output;
	private TitleFieldComponents layerID;

	private TitleFieldComponents firstDepth;
	private TitleFieldComponents lastDepth;
	private TitleFieldComponents spacing;
	private ReciprocalComponents reciprocal;
	private AttributeCheckboxPanel attributes;
	
	public VTKDepthsDialog(GeoTessPresenter presenter, JFrame parent, String title) {
		super(presenter, parent, title);
	}

	@Override
	public String methodHelp() {
		return MethodHelp.VTK_DEPTHS.getMethodTip();
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill"));

        this.output = new FileIOComponents("Output File: ", FileOperation.SAVE, this, ParameterHelp.OUTPUT);
        this.layerID = new TitleFieldComponents("Layer ID: ", 4, ParameterHelp.LAYER_ID);
		this.firstDepth = depthPanel("First Depth: ", ParameterHelp.FIRST_DEPTH);
        this.lastDepth = depthPanel("Last Depth: ", ParameterHelp.LAST_DEPTH);
        this.spacing = depthPanel("Depth Spacing: ", ParameterHelp.DEPTH_SPACING);
        this.reciprocal = new ReciprocalComponents();
        this.attributes = new AttributeCheckboxPanel(model);
        this.attributes.setPreferredSize(new Dimension(400,150));
        p.add(firstDepth.getTitle());
        p.add(firstDepth.getTextBox(), "wrap");
        p.add(lastDepth.getTitle());
        p.add(lastDepth.getTextBox(), "wrap");
        p.add(spacing.getTitle());
        p.add(spacing.getTextBox(), "wrap");
        p.add(reciprocal.getTitle());
        p.add(reciprocal.getButtons(), "wrap");
        p.add(output.getTitle());
        p.add(output.getTextBox());
        p.add(output.getButton(), "wrap");  
		p.add(attributes, "span 3, wrap");
		return p;
	}

	private TitleFieldComponents depthPanel(String s, ParameterHelp help) {
		return new TitleFieldComponents(s, 4, "km", help);
	}

	@Override
	public ActionListener getAcceptButtonListener() {

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					presenter.vtkDepths(output.getText(),
							Integer.parseInt(layerID.getFieldValue()),
							Double.parseDouble(firstDepth.getFieldValue()),
							Double.parseDouble(lastDepth.getFieldValue()),
							Double.parseDouble(spacing.getFieldValue()),
							reciprocal.getSelected(),attributes.getCheckedAttributeIndexes());
					destroy();
				} catch (Exception e) {
					setErrorVisible(true);
				}
			}

		};
	}


}
