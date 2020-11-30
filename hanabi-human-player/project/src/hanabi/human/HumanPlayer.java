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
	private Keyboard keyboard;

	public HumanPlayer(String ip, int port)
	{
		super(ip,port,"Human",true);
	}

	@Override
	public Action chooseAction() {
		return keyboard.waitForAction();
	}

	@Override
	public void init()
	{
		super.init();
		keyboard = new Keyboard(players.get(0),board);

		board.add(keyboard,BorderLayout.SOUTH);
		board.addSelectedListener(keyboard);
		board.addStateListener(keyboard);
		board.addStateShownListener(keyboard);

		//Correzione bug di prima selezione
		if (keyboard.currentState == null && board.history.size()>0)
			keyboard.onNewState(board.history.get(0));

		frame.pack();
		int x = Toolkit.getDefaultToolkit().getScreenSize().width/2-frame.getSize().width/2;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height/2-frame.getSize().height/2;
		frame.setLocation(x,y);
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
	}

}
