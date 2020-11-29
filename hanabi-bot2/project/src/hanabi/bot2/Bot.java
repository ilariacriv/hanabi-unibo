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
	private Analitics analitics;

	public Bot(String ip, int port, boolean gui)
	{
		super(ip,port,"Bot2",gui);
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
		analitics.setState(getCurrentState());
		Action action = null;
		System.out.println("Scelgo la mossa:");
		int tokens = getCurrentState().getHintTokens();
		if (tokens > 0) {
			System.out.print("\thintForPlay... ");
			action = hintForPlay();
			if (action == null) {
				System.out.println("no");
				action = securePlay();
				System.out.print("\tsecurePlay... ");
			}
		}
		if (action == null && tokens < players.size())
		{
			System.out.println("no");
			action = secureDiscard();
			System.out.print("\tsecureDiscard... ");
		}
		if (action == null && tokens > 0)
		{
			System.out.println("no");
			action = bestHint();
			System.out.print("\tbestHint... ");
		}
		if (action == null)
		{
			System.out.println("no");
			action = securePlay();
			System.out.print("\tsecurePlay... ");
		}
		if (action == null)
		{
			System.out.println("no");
			action = discardBest();
			System.out.print("\tdiscardBest... ");
		}
		if (action == null)
			System.exit(1);
		System.out.println();
		return action;
	}

	private Action hintForPlay()
	{
		CardList hand;
//		System.out.println(getCurrentState()==null);
		Analitics boxanalitics = new Analitics(players.get(0));
		Action h;
		for (int i=1; i<players.size(); i++)
		{
			hand = getCurrentState().getHand(players.get(i));
			Set<Action> hints = new HashSet<>();
			for (int j=0; j<hand.size(); j++)
			{
	//			System.out.println(hand.get(j));
				if (analitics.isPlayable(hand.get(j)) && analitics.getPlayability(players.get(i),j) < 1)
				{
					h = Action.createHintColorAction(players.get(0),players.get(i),hand.get(j).getColor());
					boxanalitics.setState(getCurrentState().applyAction(h,null,players));
					if (boxanalitics.getPlayability(players.get(i),j)==1)
						hints.add(h);

					h = Action.createHintValueAction(players.get(0),players.get(i),hand.get(j).getValue());
					boxanalitics.setState(getCurrentState().applyAction(h,null,players));
					if (boxanalitics.getPlayability(players.get(i),j)==1)
						hints.add(h);
				}
			}

			if (hints.size()>0)
			{
				Action max = null;
				double maxe = 0, e;
				for (Action action : hints) {
					boxanalitics.setState(getCurrentState().applyAction(action,null,players));
					e = analitics.getHandEntropy(players.get(i))-boxanalitics.getHandEntropy(players.get(i));
					if (e>maxe) {
						max = action;
						maxe = e;
					}
				}
				return max;
			}
		}
		return null;
	}

	private Action securePlay() {
		//Cerco nella mia mano carte con playability 1.
		CardList hand;
		hand = getCurrentState().getHand(players.get(0));
		List<Action> plays = new ArrayList<>();
		for (int j = 0; j < hand.size(); j++) {
			if (analitics.getPlayability(players.get(0), j) == 1) {
				plays.add(Action.createPlayAction(players.get(0), j));
				if (analitics.getValueProbability(players.get(0),j,5) == 1 && getCurrentState().getHintTokens() < 8)
					return plays.get(plays.size() - 1);
			}
		}
		if (plays.size() == 0)
			return null;
		if (plays.size() == 1)
			return plays.get(0);

		//Se ho più carte giocabili gioco la prima che trovo che rende giocabile almeno una carta di un altro giocatore
		for (int j = 0; j < plays.size(); j++) {
			if (analitics.getCardEntropy(players.get(0), plays.get(j).getCard()) == 0) {
				Card card = analitics.getPossibleCards(players.get(0), plays.get(j).getCard()).get(0);
				if (card.getValue()<5) {
					Card playable = Card.createCard(card.getValue() + 1, card.getColor());
					for (int k = 1; k < players.size(); k++) {
						if (getCurrentState().getHand(players.get(k)).contains(playable))
							return plays.get(j);
					}
				}
			}
		}
		return plays.get(0);
	}

	public Action secureDiscard()
	{
		CardList hand = getCurrentState().getHand(players.get(0));
		List<Action> secure = new ArrayList<>();
		for (int i = 0; i < hand.size(); i++)
		{
			if (analitics.getUselessness(players.get(0), i) == 1)
				secure.add(Action.createDiscardAction(players.get(0),i));
		}
		if (secure.size() == 0)
			return null;
		int index = 0;
		double minp = analitics.getPlayability(players.get(0),secure.get(index).getCard());
		double p;
		for (int i=1; i<secure.size(); i++)
		{
			p = analitics.getPlayability(players.get(0),secure.get(i).getCard());
			if (p<minp)
			{
				minp = p;
				index = i;
			}
		}

			return secure.get(index);
	}

	public Action bestHint()
	{
		Set<Action> hints = new HashSet<>();
		CardList hand;
		for (int i=1; i<players.size(); i++)
		{
			hand = getCurrentState().getHand(players.get(i));
			for (String color: Card.colors)
			{
				for (Card card:hand)
				{
					if (card.getColor().equals(color))
					{
						hints.add(Action.createHintColorAction(players.get(0),players.get(i),color));
						break;
					}
				}
			}
			for (double value: Card.values)
			{
				for (Card card:hand)
				{
					if (card.getValue() == value)
					{
						hints.add(Action.createHintValueAction(players.get(0),players.get(i),(int)value));
						break;
					}
				}
			}
		}

		if (hints.size()>0)
		{
			Action max = null;
			double maxe = 0, e;
			Analitics boxanalitics = new Analitics(players.get(0));
			for (Action action : hints) {
				boxanalitics.setState(getCurrentState().applyAction(action,null,players));
				e = analitics.getHandEntropy(action.getHinted())-boxanalitics.getHandEntropy(action.getHinted());
				if (e>maxe) {
					max = action;
					maxe = e;
				}
			}
			return max;
		}
		return null;
	}

	public Action discardBest()
	{
		double max = analitics.getUselessness(players.get(0),0);
		if (max == 1)
			return Action.createDiscardAction(players.get(0),0);
		double u;
		int index=0;
		for (int i=1; i<getCurrentState().getHand(players.get(0)).size(); i++)
		{
			u = analitics.getUselessness(players.get(0),i);
			if (u==1)
				return Action.createDiscardAction(players.get(0),i);
			if (u>max)
			{
				max = u;
				index = i;
			}
		}
		return Action.createDiscardAction(players.get(0),index);
	}

	public void init()
	{
		super.init();
		analitics = new Analitics(players.get(0));
	}

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
