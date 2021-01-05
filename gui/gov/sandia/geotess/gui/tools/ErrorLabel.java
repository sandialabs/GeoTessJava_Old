package gov.sandia.geotess.gui.tools;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class ErrorLabel extends JLabel {

	public ErrorLabel(String message)
	{
		super(message);
		init();
	}
	
	private void init()
	{
        this.setForeground(Color.red);
        this.setFont(new Font("Arial", Font.BOLD, 12));
	}
	
}
