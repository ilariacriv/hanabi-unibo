package hanabi.simplebot;

import hanabi.game.Action;
import hanabi.game.Card;
import hanabi.game.CardList;
import hanabi.game.State;
import hanabi.gui.Board;
import hanabi.gui.PlayerConnectionDialog;
import hanabi.player.Analitics;
import hanabi.player.GameClient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Bot extends GameClient
{
	private JFrame frame;
//	private Thread ui = new Thread(() -> frame.setVisible(true));
	private java.util.List<String> players;
	private Analitics analitics;

	public Bot(String ip, int port, boolean gui)
	{
		super(ip,port,"SimpleBot");
		if (gui)
			frame = new JFrame();
		else
			frame = null;
	}

	@Override
	public Action chooseAction()
	{
		analitics.setState(getCurrentState());
		String worst = getPlayerInWorstConditions(analitics);
		System.out.println("Rilevato giocatore in stato peggiore: "+worst);
		if (getCurrentState().getHintTokens()>0)
		{//Posso suggerire
			if (getConditions(worst,analitics) < 4)
			{//il giocatore messo peggio NON è in condizioni critiche
				//Gioco un eventuale carta con playability=1
				for (int i=0; i<getCurrentState().getHand(players.get(0)).size(); i++)
				{
					if (analitics.getPlayability(players.get(0),i)==1)
						return Action.createPlayAction(players.get(0),i);
				}

				//Se non ne ho controllo il numero di hint token
				if (getCurrentState().getHintTokens() < 8)
				{//Provo a scartare qualcosa di sicuro
					for (int i=0; i<getCurrentState().getHand(players.get(0)).size(); i++)
					{
						if (analitics.getUselessness(players.get(0),i)==1)
							return Action.createDiscardAction(players.get(0),i);
					}
				}
			}
			//Altrimenti do suggerimento migliore a giocatore messo peggio
			return getBestHint(worst);
		}
		else
		{//Non posso suggerire
			boolean playfirst;

			if (getConditions(worst,analitics) == 4)
				playfirst = true;
			else
				playfirst = false;

			//Cerco la carta con uselessness maggiore
			int l = getCurrentState().getHand(players.get(0)).size();
			double u[] = new double[l];
			for (int i=0; i<l; i++)
			{
				u[i] = analitics.getUselessness(players.get(0),i);
			}
			int max = 0;
			double umax = u[0];
			for (int i=1; i<u.length; i++)
			{
				if (u[i]>umax)
				{
					umax = u[i];
					max = i;
				}
			}

			if (!playfirst) {
				//Se è sicura la scarto
				if (umax == 1)
					return Action.createDiscardAction(players.get(0), max);
			}

			//Gioco un eventuale carta con playability=1
			for (int i=0; i<getCurrentState().getHand(players.get(0)).size(); i++)
			{
				if (analitics.getPlayability(players.get(0),i)==1)
					return Action.createPlayAction(players.get(0),i);
			}

			return Action.createDiscardAction(players.get(0),max);
		}
	}



	private Action getBestHint(String hinted)
	{
		ArrayList<Action> hints = new ArrayList<>();
		Action a;
		for (Card card: getCurrentState().getHand(hinted))
		{
			a = Action.createHintColorAction(players.get(0),hinted,card.getColor());
			if (!hints.contains(a))
				hints.add(a);

			a = Action.createHintValueAction(players.get(0),hinted,card.getValue());
			if (!hints.contains(a))
				hints.add(a);
		}

		int best = 0;
		Analitics boxanalitics = new Analitics(analitics.me);
		boxanalitics.setState(getCurrentState().applyAction(hints.get(0),null,players));
		int bestcondition = getConditions(hinted,boxanalitics);
		int newcondition;
		double besthandentropy = boxanalitics.getHandEntropy(hinted);
		double handentropy;

		for (int i=1; i<hints.size(); i++)
		{
			boxanalitics.setState(getCurrentState().applyAction(hints.get(i),null,players));
			newcondition = getConditions(hinted,boxanalitics);
			handentropy = boxanalitics.getHandEntropy(hinted);
			if (newcondition<bestcondition || (newcondition == bestcondition && handentropy<besthandentropy))
			{
				best = i;
				bestcondition = newcondition;
				besthandentropy = handentropy;
			}
		}

		return hints.get(best);
	}

	private String getPlayerInWorstConditions(Analitics analitics)
	{
		int[] conditions = new int[players.size()-1];
		for(int i=1; i<players.size(); i++)
		{//Cerco tra tutti i giocatori che non sono io
			conditions[i-1]=getConditions(players.get(i),analitics);
		}
		int max = 0;
		for (int i=1; i<conditions.length; i++)
		{
			if (conditions[i] > conditions[max])
				max = i;
		}
		return players.get(max+1);
	}

	private int getConditions(String player, Analitics analitics)
	{
		CardList hand = getCurrentState().getHand(player);
		for(int i=0; i<hand.size(); i++)
		{
			if (analitics.getPlayability(player,i) == 1)
				return 1;
		}
		for(int i=0; i<hand.size(); i++)
		{
			if (analitics.getUselessness(player,i) == 1)
				return 2;
		}
//		if (getCurrentState().getHintTokens()>players.size()-1) //Probabilmente la condizione è sbagliata. Se siamo in 5 e controllo il prossimo bastano 2 token
		if (getCurrentState().getHintTokens()>=players.indexOf(player))
			return 3;
		return 4;
	}

	@Override
	public void init()
	{
		players = reorderPlayers(getCurrentState().getPlayersNames());
		System.out.println(players);
		analitics = new Analitics(players.get(0));
		if (frame!=null) {
			frame.setTitle("Hanabi - " + getName());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.getContentPane().setLayout(new BorderLayout());


			String[] others = new String[players.size() - 1];
			for (int i = 0; i < others.length; i++)
				others[i] = players.get(i + 1);

			board = new Board(players.get(0), others);
			frame.add(board, BorderLayout.CENTER);
			board.addState(getCurrentState());

			frame.pack();
			int x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - frame.getSize().width / 2;
			int y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - frame.getSize().height / 2;
			frame.setLocation(x, y);

	//		ui.start();
			frame.setVisible(true);
		}
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
		Bot bot;
		if (args.length == 3)
		{
			bot = new Bot(args[0],Integer.parseInt(args[1]),Boolean.parseBoolean(args[2]));
		}
		else
		{
			PlayerConnectionDialog dialog;
			dialog = new PlayerConnectionDialog("Player Connection");
			dialog.waitForConfirm();
			bot = new Bot(dialog.getIP(),dialog.getPort(),true);
		}
		bot.run();
	/*	try
		{
			bot.ui.join();
		}
		catch (InterruptedException e)
		{

		}*/
	}
}
