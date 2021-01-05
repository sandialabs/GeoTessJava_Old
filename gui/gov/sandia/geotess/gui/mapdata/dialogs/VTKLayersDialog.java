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
