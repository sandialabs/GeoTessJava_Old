package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class AbstractModelNeededDialog extends AbstractDialogFunction {

    public AbstractModelNeededDialog(GeoTessPresenter presenter, JFrame parent, String title) {
        super(presenter, parent, title);
    }

   @Override
   public JPanel initialPanel() {
        return errorPanel();
    }

    private JPanel errorPanel() {
        JPanel errorPane = new JPanel();
        JLabel label = new JLabel("Error: Please Enter A Model And A Grid Before Continuing");
        label.setFont(new Font("Arial", 0, 12));
        label.setForeground(Color.RED);
        errorPane.add(label, BorderLayout.CENTER);
        return errorPane;
    }

    @Override
    public abstract JPanel makeNewDialog(GeoTessModel model);

    @Override
    public abstract ActionListener getAcceptButtonListener();



}
