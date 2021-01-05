package gov.sandia.geotess.gui.tools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dmdaily on 7/24/2014.
 */
public class RadioButtonPanel extends JPanel {

    private String selected;

    public RadioButtonPanel(String... args) {
        init(args);
    }

    private void init(String... args) {
        ButtonGroup bg = new ButtonGroup();
        ActionListener al = new RadioButtonListener();
        if(args.length != 0)
            selected = args[0];

        for(String s : args) {
            JRadioButton button = new JRadioButton(s);
            button.addActionListener(al);
            if(s.equalsIgnoreCase(selected))
                button.setSelected(true);
            bg.add(button);
            this.add(button);
        }
    }

    public String getSelected() {
        return selected;
    }


    private class RadioButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            selected = ((JRadioButton) e.getSource()).getText();
        }
    }
}
