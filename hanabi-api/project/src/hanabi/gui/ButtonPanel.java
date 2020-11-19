package hanabi.gui;


import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel
{
	private JButton button;
	public ButtonPanel(JButton button)
	{
		super();
		this.setBackground(null);
		this.setLayout(new FlowLayout(FlowLayout.TRAILING));
		this.add(button);
	}
}
