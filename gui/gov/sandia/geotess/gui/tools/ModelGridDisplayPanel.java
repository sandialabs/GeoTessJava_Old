package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ModelGridDisplayPanel extends JPanel {

    private GeoTessPresenter presenter;
	private JLabel modelValue;
	private JLabel gridValue;
	private JFrame parent;

	public ModelGridDisplayPanel(GeoTessPresenter presenter, JFrame parent) {
		this.presenter = presenter;
		this.parent = parent;
		init();
	}

	private void init() {
		this.setBorder(new TitledBorder("File Info"));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(300,150));

        JLabel modelFile = new JLabel("Model File:");
        JLabel gridFile = new JLabel("Grid File: ");

        this.modelValue = new JLabel("(No Model File Uploaded)");
        this.gridValue = new JLabel("(No Grid File Uploaded)");
		this.add(nameAndValuePanel(modelFile, modelValue));
		this.add(nameAndValuePanel(gridFile, gridValue));
		this.add(makeButton("Load Model And Grid File",new LoadModelGridDialog(presenter, parent)));
	}

	private JPanel makeButton(String title, final JFrame pane) {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		JButton button = new JButton(title);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.pack();
				pane.setLocationRelativeTo(parent);
				pane.setVisible(true);
			}
		});
		p.add(button);
		return p;
	}

	private JPanel nameAndValuePanel(JLabel label, JLabel file) {
		JPanel p = new JPanel();
		p.add(label);
		p.add(file);
		return p;
	}

	public void updateModelValue(String path) {
		// kludge so that the full path of the file isn't shown, just the name
		File f = new File(path);
        String fileName = f.getName();
        modelValue.setText(stringToDisplay(fileName));
    }

	public void updateGridValue(String path) {
		if (path.isEmpty()) gridValue.setText("Included In Model File");
		else gridValue.setText(stringToDisplay(path));
	}

    // If the model name being loaded is longer than 30 characters, the first 30 characters are
    // displayed followed by "..."
    private String stringToDisplay(String path)
    {
        final int maxNameLength = 30;
        if(path.length() >= maxNameLength)
            return path.substring(0, maxNameLength) + "...";
        return path;
    }
}
