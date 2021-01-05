package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.ParameterHelp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileIOComponents {

    private JLabel title;
    private JTextField textBox;
    private JButton button;
    private JFrame parent;
    private static CustomFileChooser chooser;


    public FileIOComponents(String label, FileOperation operation, JFrame parent, ParameterHelp help) {
        this.parent = parent;
        if(chooser == null) chooser = new CustomFileChooser();
        makePanel(label, getListener(operation), help);
    }

    public FileIOComponents(String label, ActionListener customListener, JFrame parent, ParameterHelp help) {
        this.parent = parent;
        if(chooser == null) chooser = new CustomFileChooser();
        makePanel(label, customListener, help);

    }

    private void makePanel(String label, ActionListener listener, ParameterHelp help) {

        this.title = new PopupLabel(label, help);
        Dimension textBoxDimension = new Dimension(200, 30);
        Dimension buttonDimension = new Dimension(85, 30);

        this.textBox = new JTextField();
        this.textBox.setPreferredSize(textBoxDimension);
        this.textBox.setEnabled(true);
        this.textBox.setEditable(true);

        this.button = new JButton("Browse...");
        button.addActionListener(listener);
        button.setPreferredSize(buttonDimension);

    }

    public JLabel getTitle() {
        return title;
    }

    public JTextField getTextBox()
    {
        return textBox;
    }

    public JButton getButton()
    {
        return button;
    }

    private ActionListener getListener(FileOperation chooser) {
        if(chooser == FileOperation.LOAD)
            return new LoadListener();
        return new SaveListener();
    }

    public void setText(String value) {
        textBox.setText(value);
    }

    public String getText() {
        return textBox.getText();
    }

    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            int returnVal = chooser.showSaveDialog(parent);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                textBox.setText(chooser.getSelectedFile().getAbsolutePath());

            }
        }
    }

    private class LoadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            int returnVal = chooser.showOpenDialog(parent);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                textBox.setText(chooser.getSelectedFile().getAbsolutePath());
                chooser.setSelectedFile(new File("output.txt"));
            }
        }
    }
}
