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
