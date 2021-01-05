package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;


public class LoadModelGridDialog extends AbstractNoModelNeeded {
	
	private FileIOComponents modelLoader;
	private FileIOComponents gridLoader;
	private ErrorLabel errorMessage;

	public LoadModelGridDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Model Grid File Selector");
	}

	@Override
	public JPanel makeMainPanelNoModel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill", "[]15[]", "[]20[]"));
		panel.setPreferredSize(new Dimension(450, 120));

		this.modelLoader = new FileIOComponents("GeoTess Model: ",
				FileOperation.LOAD, this, ParameterHelp.GEOTESS_MODEL);
		this.gridLoader = new FileIOComponents("GeoTess Grid: ",
				new GridBrowseListener(), this, ParameterHelp.GEOTESS_GRID);
		this.errorMessage = new ErrorLabel("");
		
		
		panel.add(modelLoader.getTitle());
		panel.add(modelLoader.getTextBox(), "span 2");
		panel.add(modelLoader.getButton(), "wrap");

		panel.add(gridLoader.getTitle());
		panel.add(gridLoader.getTextBox(), "span 2");
		panel.add(gridLoader.getButton(), "wrap");
		panel.add(errorMessage, "span 4, center");
		return panel;
	}

	@Override
	public String methodHelp() {
		return MethodHelp.FILE_LOADER.getMethodTip();
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	private String makeRelativePath(String modelPath, String gridPath) {
		Path model = Paths.get(modelPath);
		Path grid = Paths.get(gridPath);
		return model.relativize(grid).toString();
	}
	
	private class SubmitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
				SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
					@Override
					protected Void doInBackground() throws Exception {
						try {
							publish("Loading. . .");
							presenter
									.updateModelFilePaths(
											modelLoader.getText(),
											gridLoader.getText());
							publish("Model and Grid File Loaded Successfully!");
						} catch (IOException e) {
							publish("Error Uploading file. Please ensure that the file loaded is valid");
						}
						return null;
					}

					@Override
					protected void process(List<String> outputs) {
						for (String s : outputs) {
							presenter.writeToUtilityPanel(s + "\n\n");
							presenter.writeToExtractDataPanel(s + "\n");
							presenter.writeToMapDataPanel(s + "\n");
						}
					}
				}; // end worker
				worker.execute();
				destroy();
			
		}
	}

	private class GridBrowseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String gridPath = makeRelativePath(modelLoader.getText(), fc
						.getCurrentDirectory().getPath());
				gridLoader.setText(gridPath);
			}
		}
	}
}
