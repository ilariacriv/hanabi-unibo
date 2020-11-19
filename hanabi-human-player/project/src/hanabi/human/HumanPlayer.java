package hanabi.human;

import hanabi.game.Action;
import hanabi.gui.PlayerConnectionDialog;
import hanabi.player.GameClient;
import hanabi.gui.Board;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class HumanPlayer extends GameClient
{
	private JFrame frame;
	private Keyboard keyboard;
	private Thread ui = new Thread(() -> frame.setVisible(true));


	public HumanPlayer(String ip, int port)
	{
		super(ip,port,"Human");
	}

	@Override
	public Action chooseAction() {
		return keyboard.waitForAction();
	}

	@Override
	public void init()
	{
		frame = new JFrame();
		frame.setTitle("Hanabi - "+getName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new BorderLayout());

		java.util.List<String> players = reorderPlayers(getCurrentState().getPlayersNames());
		String[] others = new String[players.size()-1];
		for (int i=0; i< others.length; i++)
			others[i] = players.get(i+1);

		board = new Board(players.get(0),others);

		keyboard = new Keyboard(players.get(0),board);

		board.add(keyboard,BorderLayout.SOUTH);
		frame.add(board,BorderLayout.CENTER);


		board.addSelectedListener(keyboard);
		board.addStateListener(keyboard);
		board.addState(getCurrentState());

		frame.pack();
		int x = Toolkit.getDefaultToolkit().getScreenSize().width/2-frame.getSize().width/2;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height/2-frame.getSize().height/2;
		frame.setLocation(x,y);

		ui.start();
	}
/*
	public String waitForName() throws IOException
	{
//		wd.setVisible(true);
		String s = super.waitForName();
//		wd.setVisible(false);
		return s;
	}
*/
	public static void main(String args[])
	{
		HumanPlayer human;
		if (args.length == 2)
		{
			human = new HumanPlayer(args[0],Integer.parseInt(args[1]));
		}
		else
		{
			PlayerConnectionDialog dialog;
			dialog = new PlayerConnectionDialog("Human Player Connection");
			dialog.waitForConfirm();
			human = new HumanPlayer(dialog.getIP(),dialog.getPort());
		}
		human.run();
		try
		{
			human.ui.join();
		}
		catch (InterruptedException e)
		{

		}
	}

}
