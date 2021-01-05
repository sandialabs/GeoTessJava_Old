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
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class VTKDepths2Dialog extends AbstractModelNeededDialog {

	private FileIOComponents output;
	private TitleFieldComponents layerID;
	private JTextField depths;
	private ReciprocalComponents reciprocal;
	private AttributeCheckboxPanel attributes;

	public VTKDepths2Dialog(GeoTessPresenter presenter, JFrame parent,
			String title) {
		super(presenter, parent, title);
	}

	@Override
	public String methodHelp() {
		return MethodHelp.VTK_DEPTHS_2.getMethodTip();
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill, debug"));

		this.output = new FileIOComponents("Output File: ", FileOperation.SAVE,this, ParameterHelp.OUTPUT);
		this.reciprocal = new ReciprocalComponents();
		this.layerID = new TitleFieldComponents("Layer ID: ", 3,ParameterHelp.LAYER_ID);
		this.depths = new JTextField(18);
		this.attributes = new AttributeCheckboxPanel(model);
		this.attributes.setPreferredSize(new Dimension(400,150));
		p.add(output.getTitle());
		p.add(output.getTextBox());
		p.add(output.getButton(), "wrap");
		p.add(layerID.getTitle());
		p.add(layerID.getTextBox(), "wrap");
		p.add(new PopupLabel("Depths: ", ParameterHelp.DEPTHS));
		p.add(this.depths, "wrap");
		p.add(reciprocal.getTitle());
		p.add(reciprocal.getButtons(), "wrap");
		p.add(attributes, "span 3, wrap");
		return p;
	}

	private double[] parseDepths() {
		List<Double> doubleList = new ArrayList<Double>();
		Scanner sc = new Scanner(depths.getText());
		sc.useDelimiter(", *");

		while (sc.hasNext())
			doubleList.add(Double.parseDouble(sc.next()));

		double[] doubleArray = new double[doubleList.size()];
		for (int i = 0; i < doubleList.size(); i++)
			doubleArray[i] = doubleList.get(i);

		sc.close();
		return doubleArray;
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					presenter.vtkDepths2(output.getText(),
							Integer.parseInt(layerID.getFieldValue()),
							parseDepths(), reciprocal.getSelected(),
							attributes.getCheckedAttributeIndexes());
					destroy();
				} catch (Exception e) {
					setErrorVisible(true);
				}
			}
		};
	}

}
