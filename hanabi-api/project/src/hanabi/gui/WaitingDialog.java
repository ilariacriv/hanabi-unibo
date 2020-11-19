package hanabi.gui;

import javax.swing.*;
import java.awt.*;

public class WaitingDialog extends JFrame
{
	public WaitingDialog()
	{
		super();
		this.setSize(200,100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		this.add(new JLabel("In attesa di altri giocatori..."));
		int x = Toolkit.getDefaultToolkit().getScreenSize().width/2-this.getSize().width/2;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height/2-this.getSize().height/2;
		this.setLocation(x,y);
	}

}
