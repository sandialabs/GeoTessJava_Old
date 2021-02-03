//- ****************************************************************************
//-
//- Copyright 2009 Sandia Corporation. Under the terms of Contract
//- DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
//- retains certain rights in this software.
//-
//- BSD Open Source License.
//- All rights reserved.
//-
//- Redistribution and use in source and binary forms, with or without
//- modification, are permitted provided that the following conditions are met:
//-
//-    * Redistributions of source code must retain the above copyright notice,
//-      this list of conditions and the following disclaimer.
//-    * Redistributions in binary form must reproduce the above copyright
//-      notice, this list of conditions and the following disclaimer in the
//-      documentation and/or other materials provided with the distribution.
//-    * Neither the name of Sandia National Laboratories nor the names of its
//-      contributors may be used to endorse or promote products derived from
//-      this software without specific prior written permission.
//-
//- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//- AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//- IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//- ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
//- LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
//- CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
//- SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
//- INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
//- CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//- ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
//- POSSIBILITY OF SUCH DAMAGE.
//-
//- ****************************************************************************

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
