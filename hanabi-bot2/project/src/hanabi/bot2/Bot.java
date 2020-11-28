package hanabi.bot2;

import hanabi.game.Card;
import hanabi.game.CardList;
import hanabi.gui.Board;
import hanabi.game.Action;
import hanabi.gui.PlayerConnectionDialog;
import hanabi.player.Analitics;
import hanabi.player.GameClient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

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

	/**
	 * <ol>
	 *     <li>
	 *         Se ho hint token, controllo gli altri giocatori: se uno ha una carta giocabile non sicura dagli il
	 *         suggerimento più significativo (in termini di diminuzione dell'entropia della mano) che coinvolge quella carta.
	 *         Nel caso in cui il giocatore avesse più carte giocabili non sicure indicagli quella che, se giocata, rende
	 *         giocabili il maggior numero di carte possedute dai giocatori.
	 *     </li>
	 *     <li>
	 *         Se ho hint token e ho una carta giocabile sicura la gioco. Nel caso in cui avessi più carte giocabili gioco
	 *         quella che rende giocabili il maggior numero di carte possedute dai giocatori.
	 *     </li>
	 *     <li>
	 *         Se non ho hint token massimi (8) e ho una carta sicura da scartare la scarto.
	 *       (  Mi piacerebbe fare in modo che nel caso in cui avessi più carte scartabili scarto quella pi&ugrave; lontana
	 *         dall'attuale picco del firework dello stesso colore, ma per farlo dovrei conoscere valore e colore della carta
	 *         che non è scontato! )
	 *     </li>
	 *     <li>
	 *         Se ho hint token dai il suggerimento (a qualsiasi giocatore) che diminuisce del massimo l'entropia delle carte.
	 *     </li>
	 *     <li>
	 *         Se ho una carta giocabile sicura la gioco. Vedi punto 2 per casi di molteplicità di carte.
	 *     </li>
	 *     <li>
	 *         Scarta la carta con uselessness maggiore.
	 *         Anche qui mi piacerebbe fare come nel punto 3.
	 *     </li>
	 * </ol>
	 * @return
	 */
	@Override
	public Action chooseAction()
	{
		Action action = null;
		int tokens = getCurrentState().getHintTokens();
		if (tokens > 0) {
			action = hintForPlay();
			if (action == null)
				action = securePlay();
		}
		if (action == null && tokens < 8)
			action = secureDiscard();
		if (action == null && tokens > 0)
			action = bestHint();
		if (action == null)
			action = securePlay();
		if (action == null)
			action = discardBest();
		return action;
	}

	private Action hintForPlay()
	{
		CardList hand;
		for (int i=1; i<players.size(); i++)
		{
			hand = getCurrentState().getHand(players.get(i));
			Set<Action> hints = new HashSet<>();
			for (int j=0; j<hand.size(); j++)
			{
				if (analitics.isPlayable(hand.get(j)) && analitics.getPlayability(players.get(i),j) < 1)
				{
					hints.add(Action.createHintColorAction(players.get(0),players.get(i),hand.get(j).getColor()));
					hints.add(Action.createHintValueAction(players.get(0),players.get(i),hand.get(j).getValue()));
				}
			}
			if (hints.size()>0)
			{
				Action max = null;
				double maxe = 0, e=0;
				Analitics boxanalitics = new Analitics(players.get(0));
				for (Action action : hints) {
					boxanalitics.setState(getCurrentState().applyAction(action,null,players));
					e = analitics.getHandEntropy(players.get(i))-boxanalitics.getHandEntropy(players.get(i));
					if (e>maxe)
						max = action;
				}
				return max;
			}
		}
		return null;
	}

	private Action securePlay()
	{
		CardList hand;
		for (int i=1; i<players.size(); i++)
		{
			hand = getCurrentState().getHand(players.get(i));
			List<Action> plays = new ArrayList<>();
			for (int j=0; j<hand.size(); j++)
			{
				if (analitics.getPlayability(players.get(i),j) == 1)
					plays.add(Action.createPlayAction(players.get(0),j));
				if (hand.get(j).getValue() == 5 && getCurrentState().getHintTokens()<8)
					return plays.get(plays.size()-1);
			}
			if (plays.size() == 0)
				return null;
			if (plays.size() == 1)
				return plays.get(0);

			for (int j = 0; j<plays.size(); j++)
			{
				if (analitics.getCardEntropy(players.get(0),plays.get(j).getCard()) == 0)
				{
					Card card = analitics.getPossibleCards(players.get(0),plays.get(j).getCard()).get(0);
					Card playable = Card.createCard(card.getValue()+1,card.getColor());
					for (int k=1; k<players.size(); k++)
					{
						if (getCurrentState().getHand(players.get(k)).contains(playable))
							return plays.get(j);
					}
				}
			}
			return plays.get(0);

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
			bot = new Bot(args[0], Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]));
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
