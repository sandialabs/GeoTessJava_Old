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

package gov.sandia.geotess.gui.utilities;

import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class UtilityPanel extends JPanel {

    private UtilityOptionsPanel options;
    private JTextArea textArea;
    private JMenuItem saveToFile;
    private JPopupMenu popupMenu = new JPopupMenu();

    public UtilityPanel(GeoTessPresenter presenter, JFrame parent) {
        this.options = new UtilityOptionsPanel(presenter, parent);
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.add(options, BorderLayout.WEST);
        this.add(mainPanel());
        saveToFile = new JMenuItem("Save console to file...");
        saveToFile.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					saveTextDialog();
				}
    	});
        popupMenu.add(saveToFile);

    }

    private JPanel mainPanel() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(900, 900));
        p.setLayout(new BorderLayout());

        //where the text area gets initialized
        textArea = new JTextArea();
        textArea.setFont(new Font("Courier", 0, 12));  // Courier makes the formatting align
        textArea.setMinimumSize(new Dimension(100,100));
        textArea.setEditable(false);
        textArea.addMouseListener(new RightClickMenu()); // pop-up menu listener

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBounds(0, 0, 300, 200);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    public void updateFileDisplayPanel(String modelFile, String grid) throws IOException {
        options.updateDialogModel(modelFile, grid);
    }

    public void updateUtilityText(String s) {
        textArea.append(s + "\n");
    }

    public void clearUtilityText() {
        textArea.setText("");
        textArea.setCaretPosition(0);
    }
    
    private void saveTextDialog()
    {
    	File output = null;
    	
    	// show file selection dialogs until user completes action or backs out
    	while(true)  
    	{	JFileChooser chooser = new JFileChooser();
    		int userOption = chooser.showOpenDialog(this);
    		if(userOption == JFileChooser.APPROVE_OPTION) 
    		{	output = chooser.getSelectedFile();
        		if (output.exists())
        		{
                    int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?",
                            "Existing file", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) 
                    {	break;
                    }
        		}
        		else
        			break;
    		}
    		else if (userOption == JFileChooser.CANCEL_OPTION)
    			return;
        }
    	/**********************************************************************/

    	// By this point, user has selected a valid file.  Attempt to write to it.
    	FileWriter fw = null;
    	try 
    	{	fw = new FileWriter(output);
			fw.write(textArea.getText());
			fw.close();
			JOptionPane.showMessageDialog(this, "Console output written successfully");
    	} 
    	catch (IOException e) 
    	{	JOptionPane.showMessageDialog(this, "Error: failed to write console output!");
    		e.printStackTrace();
    	}
    	
    }
    
 
    private class RightClickMenu extends MouseAdapter
    {
    	@Override
    	public void mouseReleased(MouseEvent ev)
    	{
    		if (SwingUtilities.isRightMouseButton(ev))
    		{
    			popupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
    		}
    		else 
    			popupMenu.setVisible(false);
    	}
    }
    
}
