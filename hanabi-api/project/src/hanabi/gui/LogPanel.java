package hanabi.gui;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class LogPanel extends JScrollPane
{
	private JTextArea textArea;
	public LogPanel()
	{
		this.setBorder(BorderFactory.createTitledBorder("Log"));
		textArea = new JTextArea();
		textArea.setOpaque(false);
		textArea.setEditable(false);
		textArea.setBackground(null);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		this.setBackground(null);
		this.setViewportView(textArea);
		this.setOpaque(false);
		this.getViewport().setOpaque(false);
		this.getViewport().setBackground(null);
	}

	public void log(String s)
	{
		textArea.append(s);
	}
}
