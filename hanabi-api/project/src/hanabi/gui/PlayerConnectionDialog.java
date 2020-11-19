package hanabi.gui;

import json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerConnectionDialog extends JFrame
{
	private final Object confirmMonitor = new Object();
	private TextField ipfield;
	private TextField portfield;
	private JButton confirm;
	private JSONObject datauser;
	private ActionListener listener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			setVisible(false);
			try
			{
				datauser.put("last_ip",ipfield.getText());
				datauser.put("last_port",portfield.getText());
				BufferedWriter bw = new BufferedWriter(new FileWriter("lastinsertion"));
				bw.write(datauser.toString());
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			synchronized (confirmMonitor)
			{
				confirmMonitor.notify();
			}
		}
	};

	public PlayerConnectionDialog()
	{
		this("",null,0);
	}

	public PlayerConnectionDialog(String title)
	{
		this(title,null,0);
	}

	public PlayerConnectionDialog(String title, String ip, int port)
	{
		super(title);
		init();
		if (ip!=null)
			ipfield.setText(ip);
		if (port>0 && port<256*256)
			portfield.setText(""+port);
	}

	private void init()
	{
		try
		{
			datauser = new JSONObject(new FileReader("lastinsertion"));
		}
		catch(IOException e)
		{
			datauser = new JSONObject();
		}

		ipfield = new TextField();
		ipfield.setPreferredSize(new Dimension(200,20));
		ipfield.addActionListener(listener);
		ipfield.setText(datauser.get(String.class,"last_ip"));

		portfield = new TextField();
		portfield.setPreferredSize(new Dimension(50,20));
		portfield.addActionListener(listener);
		portfield.setText(datauser.get(String.class,"last_port"));

		confirm = new JButton("Conferma");
		confirm.addActionListener(listener);

		this.setLayout(new GridLayout(3,1));
		JPanel p0 = new JPanel(),p1 = new JPanel(),p2 = new JPanel();
		p0.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		p1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		p2.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));

		this.add(p0);
		this.add(p1);
		this.add(p2);

		p0.add(new JLabel("Inserisci indirizzo ip host"));
		p0.add(ipfield);
		p1.add(new JLabel("Inserisci porta host"));
		p1.add(portfield);
		p2.add(confirm);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		int x = Toolkit.getDefaultToolkit().getScreenSize().width/2-this.getSize().width/2;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height/2-this.getSize().height/2;
		this.setLocation(x,y);
	}

	public String getIP()
	{
		return ipfield.getText();
	}

	public int getPort()
	{
		return Integer.parseInt(portfield.getText());
	}

	public void waitForConfirm()
	{
		new Thread(() -> this.setVisible(true)).start();
		try
		{
			synchronized (confirmMonitor)
			{
				confirmMonitor.wait();
			}
		}
		catch (InterruptedException e)
		{}
	}
}
