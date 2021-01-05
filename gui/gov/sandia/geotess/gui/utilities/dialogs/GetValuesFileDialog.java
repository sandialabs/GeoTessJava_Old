package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.RadialInterpolationComponents;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class GetValuesFileDialog extends AbstractModelNeededDialog {

	private FileIOComponents latlonFile;
	private AttributeCheckboxPanel attributes;
	private HorizontalInterpolationComponents horizontal;
    private RadialInterpolationComponents radial;
	private ReciprocalComponents reciprocal;
	private FileIOComponents output;
	
	public GetValuesFileDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Get Values File");
	}
	
	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		return mainPanel(model);
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	@Override
	public String methodHelp() {
		return MethodHelp.GET_VALUES_FILE.getMethodTip();
	}
	
	private JPanel mainPanel(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill"));

        this.latlonFile = new FileIOComponents("Lat-Lon File: ", FileOperation.LOAD, this, ParameterHelp.LAT_LON_FILE);
        this.attributes = new AttributeCheckboxPanel(model);
        this.attributes.setPreferredSize(new Dimension(400, 150));
        this.horizontal = new HorizontalInterpolationComponents();
        this.radial = new RadialInterpolationComponents();
        this.reciprocal = new ReciprocalComponents();
        this.output = new FileIOComponents("Output File: ", FileOperation.LOAD, this, ParameterHelp.OUTPUT);
        
        p.add(latlonPanel(), "wrap");
		p.add(radioPanel(), "wrap");
		p.add(attributes, "span 2, wrap");
		p.add(outputPanel(), "wrap");
		
		return p;
	}
	
	private JPanel latlonPanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new MigLayout());
        p.add(latlonFile.getTitle());
        p.add(latlonFile.getTextBox());
        p.add(latlonFile.getButton());
		return p;
	}
	
	private JPanel outputPanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new MigLayout());
		p.add(output.getTitle());
        p.add(output.getTextBox());
        p.add(output.getButton());
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

	private class SubmitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				presenter.getValuesFile(latlonFile.getText(),
						attributes.getCheckedAttributeIndexes(),
						horizontal.getInterpolation(),
						radial.getInterpolation(),
						reciprocal.getSelected(), output.getText());
				destroy();
			} catch (GeoTessException | IOException | NumberFormatException e) {
				setErrorVisible(true);
			}
		}
	}
}
