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

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.interfaces.Function;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public abstract class AbstractDialogFunction extends JFrame implements Function {
    public JButton accept;

    protected GeoTessPresenter presenter;
    protected JFrame parent;
    protected JPanel mainPane;
    protected JPanel childPane;
    private GeoTessModel model;
    private JPanel existingPanel;
    private ErrorLabel errorMessage;
    
    public AbstractDialogFunction(GeoTessPresenter presenter, JFrame parent, String title) {
        this.presenter = presenter;
        this.parent = parent;
        init(title);
    }

    private void init(String title) {
        this.setTitle(title);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.addWindowListener(new WindowCloseListener());

        JPanel buttons = buttonPane();
        JPanel status = statusBar(this.getWidth());

        this.childPane = initialPanel();
        this.mainPane = new JPanel();
        this.mainPane.setLayout(new MigLayout());
        this.mainPane.setBorder(new TitledBorder("IO Options"));
        this.mainPane.add(childPane);
        
        this.add(mainPane, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.EAST);
        this.add(status, BorderLayout.SOUTH);
        this.errorMessage = new ErrorLabel("Error. Please check the validity of the input arguments");
        this.errorMessage.setVisible(false);
    }

    private JPanel statusBar(int frameWidth) {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(frameWidth, 18));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel("For help, right click on the parameter name");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        return statusPanel;
    }

    @Override
    public void execute() {
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    @Override
    public void updateModel(GeoTessModel model) {

        //keeps the error panel if model is null
        if(model == null) return;
        this.accept.setEnabled(true);
        this.mainPane.remove(childPane);
        this.childPane = mainDialogPanel(model);
        this.mainPane.add(childPane, "wrap");
        this.mainPane.add(errorMessage, "center");
        this.add(mainPane, BorderLayout.CENTER);
        this.revalidate();
    }

    private JPanel mainDialogPanel(GeoTessModel newModel)
    {
        if(newModel.equals(model)) 
        	return existingPanel;
        this.model = newModel;
        this.existingPanel = makeNewDialog(model);
        return existingPanel;
    }

    private JPanel buttonPane() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setPreferredSize(new Dimension(120, 200));
        panel.setBorder(new TitledBorder("Runtime Options"));

        this.accept = new JButton("Accept");
        this.accept.addActionListener(getAcceptButtonListener());
        this.accept.setPreferredSize(new Dimension(120, 40));

        JButton cancel = new JButton("Cancel");
        cancel.setVerifyInputWhenFocusTarget(false);
        cancel.addActionListener(new CancelButtonListener());

        panel.add(accept);
        panel.add(cancel);
        return panel;
    }

    public void destroy() {
    	this.errorMessage.setVisible(false);
        this.setVisible(false);
        this.dispose();
    }
    
    public abstract ActionListener getAcceptButtonListener();

    public abstract JPanel initialPanel();

    public abstract JPanel makeNewDialog(GeoTessModel model);
    
    public void setErrorVisible(boolean val)
    {
    	errorMessage.setVisible(val);
    }
    
    public void setErrorMessage(String error)
    {
    	this.errorMessage.setText(error);
    }
    
    private class WindowCloseListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            destroy();
        }
    }

    private class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            destroy();
        }
    }
}
