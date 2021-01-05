package gov.sandia.geotess.gui.tools;

import javax.swing.*;
import java.io.File;
import java.text.MessageFormat;

/**
 * Created by dmdaily on 8/1/2014.
 */
public class CustomFileChooser extends JFileChooser {


    @Override
    public void approveSelection() {

        //dont want to show the overwrite box when opening a file
        if(getDialogType() == JFileChooser.OPEN_DIALOG ) {
            super.approveSelection();
            return;
        }

        File f = getSelectedFile();
        if ( f.exists() ) {
            String msg = "The file \"{0}\" already exists!\nDo you want to replace it?";
            msg = MessageFormat.format(msg, new Object[]{ f.getName() });
            String title = getDialogTitle();
            int option = JOptionPane.showConfirmDialog( this, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE );
            if ( option == JOptionPane.NO_OPTION ) {
                return;
            }
        }
        super.approveSelection();
    } // end method
}
