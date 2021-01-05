package gov.sandia.geotess.gui.mapdata.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.RadioButtonPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class VTKLayerBoundaryDialog extends AbstractModelNeededDialog{

	private FileIOComponents outputFile;
	private RadioButtonPanel depthElev;
	private HorizontalInterpolationComponents horizontal;
	
	public VTKLayerBoundaryDialog(GeoTessPresenter presenter, JFrame parent,String title) {
		super(presenter, parent, title);
	}

	@Override
	public String methodHelp() {
		return MethodHelp.VTK_LAYER_BOUNDARY.getMethodTip();
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout());

        this.outputFile = new FileIOComponents("Output File: ", FileOperation.SAVE, this, ParameterHelp.OUTPUT);
        this.depthElev = new RadioButtonPanel("Depth", "Elevation");
        this.horizontal = new HorizontalInterpolationComponents();
        
        p.add(outputFile.getTitle());
        p.add(outputFile.getTextBox());
        p.add(outputFile.getButton(), "wrap");

		p.add(new PopupLabel("Depth or Elevation: ", ParameterHelp.DEPTH_OR_ELEVATION));
		p.add(depthElev, "wrap");
		
		p.add(horizontal.getTitle());
		p.add(horizontal.getButtons(), "wrap");
		return p;
	}
	
	@Override
	public ActionListener getAcceptButtonListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					presenter.vtkLayerBoundary(outputFile.getText(), depthElev.getSelected(), horizontal.getInterpolation());
					destroy();
				} catch (IOException | GeoTessException e) {
					setErrorVisible(true);
				}
				
			}
			
		};
	}

}
