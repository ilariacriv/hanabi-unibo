package hanabi.bot2;

import hanabi.game.Card;
import hanabi.game.CardList;
import hanabi.game.State;
import hanabi.gui.Board;
import hanabi.game.Action;
import hanabi.gui.PlayerConnectionDialog;
import hanabi.player.Analitics;
import hanabi.player.GameClient;

import java.util.*;
import java.util.List;

public class Bot extends GameClient
{
	private Analitics analitics;
	public State previousState;
	private List<String> hintedplayers = new ArrayList<>();
	private List<State> cicle = new ArrayList<>();
	private String firstplayer;
	private boolean[] notuseless = new boolean[5]; //true se la carta i è indicata come not useless (da tenere)

	public Bot(String ip, int port, boolean gui)
	{
		super(ip,port,"Bot2",gui);
	}

	public void manageNewState(State current)
	{

	}

	/**
	 * <ol>
	 *     <li>
	 *         Se ho ricevuto un suggerimento che mi ha dato playability = 1 su una carta la gioco. Altrimenti gioco la più a destra se
	 *         la sua playability>0. Se ho ricevuto 2 o più suggerimenti?
	 *     </li>
	 *     <li>
	 *         Se ho un playability 100% lo gioco
	 *     </li>
	 *     <li>
	 *         Cerco uno con una carta giocabile oppure non scartabile
	 *         Suggerimento in modo che la carta da giocare sia la più a destra o che diventi 100% di playability (nel primo caso),
	 *         oppure che renda la playability = 0 (nel secondo caso). Se non è possibile vado avanti
	 *         Nel secondo caso mi segno che ho già segnalato la carta come non scartabile
	 *         Nel primo caso cerca di suggerire la carta più a sinistra
	 *     </li>
	 *     <li>
	 *			Se non ho hint token scarto la carta più a sinistra tra quelle con uselessness maggiore se non è nella lista delle non scartabili
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
		if (hintReceived())
		{
			System.out.print("\tplaySecureHintedCard... ");
			action = playHintedCard();
			if (action == null)
				System.out.println("no");
		}
		if (action == null) {
			System.out.print("\tplaySecureCard... ");
			action = playSecureCard();
		}
		if (action == null && tokens>0)
		{
			System.out.println("no");
			System.out.print("\thint... ");
			action = hint();
		}
		if (action == null)
		{
			System.out.println("no");
			System.out.print("\tdiscardBest... ");
			action = discardBest();
		}
		if (action == null)
			System.exit(1);

		if (action.getActionType().equals(Action.play)||action.getActionType().equals(Action.discard))
		{
			int x = action.getCard();
			for (int i=x; i<getCurrentState().getHand(players.get(0)).size()-1; )
		}
		System.out.println();
		return action;
	}




	/*

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
		double p;
		System.out.println();
		for (int j = 0; j < hand.size(); j++) {
			p = analitics.getPlayability(players.get(0), j);
			System.out.println("\t\tp("+j+")="+p);
			if (p == 1) {
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
		//Tra le carte con uselessness 1 scarta una con playability 0
		CardList hand = getCurrentState().getHand(players.get(0));
		List<Action> secure = new ArrayList<>();
		for (int i = 0; i < hand.size(); i++) {
			if (analitics.getUselessness(players.get(0), i) == 1)
				secure.add(Action.createDiscardAction(players.get(0), i));
		}

		for (Action a:secure)
		{
			if (analitics.getPlayability(players.get(0), a.getCard()) == 0)
				return a;
		}
		return null;
	}

	public Action bestHintForFuturePlay()
	{
		CardList hand;
		Analitics boxanalitics = new Analitics(players.get(0));
		Action h;
		Set<Action> hints = new HashSet<>();
		for (int i=1; i<players.size(); i++) {
			hand = getCurrentState().getHand(players.get(i));
			for (int j = 0; j < hand.size(); j++) {
				if (analitics.isPlayable(hand.get(j)) && analitics.getPlayability(players.get(i), j) < 1) {
					h = Action.createHintColorAction(players.get(0), players.get(i), hand.get(j).getColor());
					hints.add(h);

					h = Action.createHintValueAction(players.get(0), players.get(i), hand.get(j).getValue());
					hints.add(h);
				}
			}
		}

		if (hints.size()>0) {
			Action max = null;
			double maxe = 0, e;
			for (Action action : hints) {
				boxanalitics.setState(getCurrentState().applyAction(action, null, players));
				e = analitics.getHandEntropy(action.getHinted()) - boxanalitics.getHandEntropy(action.getHinted());
				if (e > maxe) {
					max = action;
					maxe = e;
				}
			}
			return max;
		}
		return null;
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

	public Action discardUnknown()
	{
		List<Action> box = new ArrayList<>();
		box.add(Action.createDiscardAction(players.get(0),0));
		double max = -1;
		double u;
		for (int i=0; i<getCurrentState().getHand(players.get(0)).size(); i++)
		{
			if ((getCurrentState().getHand(players.get(0)).get(i).getPossibleColors().size()>1)||
					(getCurrentState().getHand(players.get(0)).get(i).getPossibleValues().size()>1))
				continue;
			u = analitics.getUselessness(players.get(0),i);
			if (u == max)
				box.add(Action.createDiscardAction(players.get(0),i));
			else if (u>max)
			{
				max = u;
				box.clear();
				box.add(Action.createDiscardAction(players.get(0),i));
			}
		}
		if (box.size()==0)
			return null;
		int index = 0;
		double min = analitics.getPlayability(players.get(0),box.get(index).getCard());
		double p;
		for (int i=1; i<box.size(); i++) {
			p = analitics.getPlayability(players.get(0), box.get(i).getCard());
			if (p < min) {
				min = p;
				index = i;
			}
		}
		return box.get(index);
	}
*/
	public Action discardBest()
	{
		//Tra le carte che non sono segnate da tenere prendi quelle con uselessness maggiore e scarta quella con playability minore

		List<Action> box = new ArrayList<>();
		double max = -1;
		double u;
		for (int i=0; i<getCurrentState().getHand(players.get(0)).size(); i++)
		{
			if (!notuseless[i]) {
				u = analitics.getUselessness(players.get(0), i);
				if (u == max)
					box.add(Action.createDiscardAction(players.get(0), i));
				else if (u > max) {
					max = u;
					box.clear();
					box.add(Action.createDiscardAction(players.get(0), i));
				}
			}
		}

		if (box.size() == 0)
		{
			for (int i=0; i<getCurrentState().getHand(players.get(0)).size(); i++)
			{
				u = analitics.getUselessness(players.get(0), i);
				if (u == max)
					box.add(Action.createDiscardAction(players.get(0), i));
				else if (u > max) {
					max = u;
					box.clear();
					box.add(Action.createDiscardAction(players.get(0), i));
				}
			}
		}

		int index = 0;
		double min = analitics.getPlayability(players.get(0),box.get(index).getCard());
		double p;
		for (int i=1; i<box.size(); i++) {
			p = analitics.getPlayability(players.get(0), box.get(i).getCard());
			if (p < min) {
				min = p;
				index = i;
			}
		}
		return box.get(index);
	}

	public void init()
	{
		super.init();
		System.out.println("Giocatori: "+players);
		analitics = new Analitics(players.get(0));
		Arrays.fill(notuseless,0);
	}

	public static void main(String args[])
	{
		Bot bot;
		boolean gui = true;
		if (args.length == 3)
		{
			gui = Boolean.parseBoolean(args[2]);
			bot = new Bot(args[0], Integer.parseInt(args[1]), gui);
		}
		else
		{
			PlayerConnectionDialog dialog;
			dialog = new PlayerConnectionDialog("Player Connection");
			dialog.waitForConfirm();
			bot = new Bot(dialog.getIP(),dialog.getPort(),gui);
		}
		bot.run();
		if (!gui)
			System.exit(0);
	/*	try
		{
			bot.ui.join();
		}
		catch (InterruptedException e)
		{

		}*/
	}
}
