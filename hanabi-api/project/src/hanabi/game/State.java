package hanabi.game;

import json.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

/**
 * Rappresenta lo stato di una partita.
 * {<br>
 *      "discarded"	: lista di carte scartate (CardList)</br>
 *      "current" 	: nome del giocatore cui tocca giocare (string)</br>
 *      "round" 	: numero di turno corrente (int) <br>
 *      "fuse" 		: numero di fuse tokens rimasti (int) <br>
 *      "hints"		: numero di hints tokens rimasti (int) <br>
 *		"final"		: numero del turno finale se conosciuto, -1 altrimenti (int) <br>
 *		"deck"		: numero di carte rimaste nel mazzo (int) <br>
 *		"red"		: numero di carte nel firework red (int) <br>
 *		"blue"		: numero di carte nel firework blue (int) <br>
 *		"yellow"	: numero di carte nel firework yellow (int) <br>
 *		"white"		: numero di carte nel firework white (int) <br>
 *		"green"		: numero di carte nel firework green (int) <br>
 *		"lastaction": ultima azione eseguita (Action)<br>
 *		"player"	: lista di carte nella mano del giocatore "player" (CardList) <br>
 *  * }
 */

public class State extends TypedJSONObject
{
	public State(Reader reader) throws JSONException
	{
		super(reader);
	}

	private State(State state)
	{
		super(state);
	}

	public State(JSONObject object)
	{
		super(object);
	}

/*	public State(String[] playerNames, CardList[] hands)
	{
		super(createInitialStateJSON(playerNames,hands));
	}
*/
/*	public Action getAction()
	{
		return object.get("action",Action.class);
	}
*/

	public State applyAction(Action action, Stack<Card> deck, List<String> sortedPlayers) throws IllegalArgumentException
	{
		String player = getCurrentPlayer();
		int indexOfPlayer = sortedPlayers.indexOf(player);

		State nextState = this.copy();

		//Controllo il giocatore che ha fatto la mossa
		if (!player.equals(action.getPlayer()))
			throw new IllegalArgumentException("Illegal action (current player)");

		//Differenzio in base al tipo di mossa
		if (action.getActionType().equals(Action.play))
			applyPlayAction(action,nextState,deck);
		else if (action.getActionType().equals(Action.discard))
			applyDiscardAction(action,nextState,deck);
		else if (action.getActionType().equals(Action.hint_color))
			applyHintColorAction(action,nextState);
		else
			applyHintValueAction(action,nextState);

		//Incremento il round corrente
		nextState.object.put("round",getRound()+1);

		//Modifico il giocatore corrente
		indexOfPlayer = (indexOfPlayer+1)%sortedPlayers.size();
		nextState.object.put("current",sortedPlayers.get(indexOfPlayer));

		nextState.object.put("lastaction",action);

		return nextState;
	}

	private void applyDiscardAction(Action action, State nextState, Stack<Card> deck) throws IllegalArgumentException
	{
		//Controllo l'indice della carta scartata
		CardList playerhand = nextState.getHand(getCurrentPlayer());
		int indexCard = action.getCard();
		if (indexCard>=playerhand.size())
			throw new IllegalArgumentException("Illegal action (index card)");

		//Rimuovo la carta scartata e ne pesco un'altra se disponibile
		Card discardedCard = playerhand.get(indexCard);
		Card drawnCard = null;
		playerhand.remove(indexCard);
		if (deck.size()>0)
		{
			drawnCard = deck.pop();
			playerhand.add(drawnCard);
			nextState.object.put("deck",deck.size());
			if (deck.size()==0)
				nextState.object.put("final", getRound()+getPlayersNames().size());
		}

		CardList discarded = nextState.getDiscarded();
		discarded.add(discardedCard);
		if (nextState.getHintTokens()<8)
			nextState.object.put("hints",nextState.getHintTokens()+1);
	}

	private void applyHintColorAction(Action action, State nextState) throws IllegalArgumentException
	{
		//Controllo che il giocatore cui è rivolto il suggerimento esista
		String hinted = action.getHinted();
		if (!getPlayersNames().contains(hinted))
			throw new IllegalArgumentException("Illegal action (hinted)");

		if (hinted.equals(action.getPlayer()))
			throw new IllegalArgumentException("You can not give a hint to yourself");

		//Controllo che ci sia almeno un token, se c'è scalo di uno
		if (getHintTokens() == 0)
			throw new IllegalArgumentException("Illegal action (hint tokens)");
		nextState.object.put("hints",getHintTokens()-1);

		//Controllo che il giocatore aiutato abbia in mano almeno una carta del colore specificato
		CardList hintedhand = nextState.getHand(hinted).copy();
		boolean hascolor = false;
		Card box;
		for (int i=0; i<hintedhand.size(); i++) {
			box = hintedhand.get(i);
			if (box.getColor().equals(action.getColor()))
			{
				hascolor = true;
				hintedhand.set(i,Card.createCard(box.getValue(),box.getColor(),new HashSet<>(Arrays.asList(box.getColor())),box.getPossibleValues()));
			}
			else
			{
				Set<String> s = box.getPossibleColors();
				s.remove(action.getColor());
				hintedhand.set(i,Card.createCard(box.getValue(),box.getColor(),s,box.getPossibleValues()));
			}
		}
		if (!hascolor)
			throw new IllegalArgumentException("Illegal action (hinted color)");
		nextState.object.put(hinted,hintedhand);
	}

	private void applyHintValueAction(Action action, State nextState) throws IllegalArgumentException
	{
		//Controllo che il giocatore cui è rivolto il suggerimento esista
		String hinted = action.getHinted();
		if (!getPlayersNames().contains(hinted))
			throw new IllegalArgumentException("Illegal action (hinted)");

		if (hinted.equals(action.getPlayer()))
			throw new IllegalArgumentException("You can not give a hint to yourself");

		//Controllo che ci sia almeno un token, se c'è scalo di uno
		if (getHintTokens() == 0)
			throw new IllegalArgumentException("Illegal action (hint tokens)");
		nextState.object.put("hints",getHintTokens()-1);

		//Controllo che il giocatore aiutato abbia in mano almeno una carta del valore specificato
		CardList hintedhand = getHand(hinted).copy();
		boolean hasvalue = false;
		Card box;
		for (int i=0; i<hintedhand.size(); i++) {
			box = hintedhand.get(i);
			if (box.getValue() == action.getValue())
			{
				hasvalue = true;
				hintedhand.set(i,Card.createCard(box.getValue(),box.getColor(),box.getPossibleColors(),new HashSet<>(Arrays.asList((double)box.getValue()))));
			}
			else
			{
				Set<Double> s = box.getPossibleValues();
				s.remove((double)action.getValue());
				hintedhand.set(i,Card.createCard(box.getValue(),box.getColor(),box.getPossibleColors(),s));
			}
		}
		if (!hasvalue)
			throw new IllegalArgumentException("Illegal action (hinted value)");
		nextState.object.put(hinted,hintedhand);
	}

	private void applyPlayAction(Action action, State nextState, Stack<Card> deck) throws IllegalArgumentException
	{
		//Controllo l'indice della carta giocata
		CardList playerhand = nextState.getHand(getCurrentPlayer());
		int indexCard = action.getCard();
		if (indexCard>=playerhand.size())
			throw new IllegalArgumentException("Illegal action (index card)");

		//Rimuovo la carta giocata e ne pesco un'altra se disponibile
		Card playedCard = playerhand.get(indexCard);
		String color = playedCard.getColor();
		playerhand.remove(indexCard);
		if (deck.size()>0)
		{
			playerhand.add(deck.pop());
			nextState.object.put("deck",deck.size());
			if (deck.size()==0)
				nextState.object.put("final", getRound()+getPlayersNames().size());
		}

		//Controllo se la carta è giocata correttamente
		int firework = getFirework(color);
		if (firework+1 == playedCard.getValue())
		{
			nextState.object.put(color,firework+1);
			//Se questa era l'ultima carta da giocare la partita è finita
			if (getScore() == 24)
				nextState.object.put("final", getRound()+1);
			if (firework == 4 && getHintTokens()<8)
				nextState.object.put("hints",nextState.getHintTokens()+1);
		}
		else
		{
			CardList discarded = nextState.getDiscarded();
			discarded.add(playedCard);
			nextState.object.put("fuse",getFuseTokens()-1);
		}
	}

	@Override
	public State copy() {
		return new State(this);
	}

	public CardList getDiscarded()
	{
		return object.get(CardList.class,"discarded");
	}

	public int getFirework(String color)
	{
		return object.get(Number.class,color).intValue();
	}

	public Set<String> getPlayersNames()
	{
		Set<String> s = this.object.keySet();
		s.remove("discarded");
		s.remove("current");
		s.remove("round");
		s.remove("fuse");
		s.remove("hints");
		s.remove("final");
		s.remove("deck");
		s.remove("red");
		s.remove("blue");
		s.remove("yellow");
		s.remove("white");
		s.remove("green");
		s.remove("lastaction");
		return s;
	}

	public Action getLastAction()
	{
		return object.get(Action.class,"lastaction");
	}

	public CardList getHand(String playername)
	{
		return object.get(CardList.class,playername);
	}

	public String getCurrentPlayer()
	{
		return object.get(String.class,"current");
	}

	public int getRound()
	{
		return object.get(Number.class,"round").intValue();
	}

	public int getScore()
	{
		int score = 0;
		for(String c: Card.colors)
			score+=this.getFirework(c);
		return score;
	}

	public int getFuseTokens()
	{
		return object.get(Number.class,"fuse").intValue();
	}

	public int getPoints()
	{
		return getFirework("red")+getFirework("blue")+getFirework("white")+getFirework("yellow")+getFirework("green");
	}

	public int getHintTokens()
	{
		return object.get(Number.class,"hints").intValue();
	}

	public int getFinalRound()
	{
		return object.get(Number.class,"final").intValue();
	}

	public int getDeckSize()
	{
		return object.get(Number.class,"deck").intValue();
	}

	public boolean isLastState()
	{
		return (getFinalRound()>0 && getRound() == getFinalRound()+1) || getFuseTokens() == 0;
	}

	/*
json = new JSONObject();
		json.set("discarded",new JSONArray());
		json.set("action", new JSONObject());
		json.set("red",new Firework());
		json.set("green",new Firework());
		json.set("white",new Firework());
		json.set("blue",new Firework());
		json.set("yellow",new Firework());
		json.set("current",""+Game.getInstance().getPlayer(0));
		json.set("order",""+0);
		json.set("fuse",""+3);
		json.set("hints",""+8);
		json.set("final",""+-1);
		Card[] cards = new Card[Game.getInstance().getNumberOfCardsPerPlayer()];
		for (String n:Game.getInstance().getPlayers())
		{
			for (int i=0; i<cards.length; i++)
				cards[i] = deck.pop();
			json.set(n,new Hand(cards));
		}
		json.set("deck",""+deck.size());
 */

	public State mask(String player)
	{
		State newstate = this.copy();
		CardList hand = newstate.getHand(player);
		int boxv;
		String boxc;
		for (int i=0; i<hand.size(); i++)
		{
			if (!hand.get(i).isColorRevealed())
				boxc = null;
			else
				boxc = hand.get(i).getColor();
			if (!hand.get(i).isValueRevealed())
				boxv = 0;
			else
				boxv = hand.get(i).getValue();
			hand.set(i,Card.createCard(boxv,boxc,hand.get(i).getPossibleColors(),hand.get(i).getPossibleValues()));
		}
		return newstate;
	}

	@Override
	public void verify() throws JSONException
	{
/*		if (Game.getInstance() == null)
			throw new JSONException("Game non inizializzato");
*/
		if (!object.has("discarded"))
			throw new JSONException("Missing discarded");
		CardList discarded = new CardList(new StringReader(object.get(String.class,"discarded")));
		object.put("discarded",discarded);

		int f;
		for (String color: Card.colors) {
			f = getFirework(color);
			if (f<0 || f>5)
				throw new JSONException("Missing or illegal "+color+" firework");
		}

		try{
			if (!getPlayersNames().contains(getCurrentPlayer()))
				throw new JSONException("Unrecognized current player");
		}catch(NullPointerException e){
			throw new JSONException("Missing current player",e);
		}
		
		try{
			if (getRound()<0)
				throw new JSONException("Negative round");
		}catch(NullPointerException e){
			throw new JSONException("Missing round");
		}

		if (!object.has("lastaction"))
			throw new JSONException("Missing discarded");
		String string = object.get(String.class,"lastaction");
		if (string!=null)
		{
			Action lastaction = new Action(new StringReader(string));
			object.put("lastaction",lastaction);
		}

		try{
			getFuseTokens();
		}catch(NullPointerException e){
			throw new JSONException("Missing fuse tokens counter");
		}

		try{
			getHintTokens();
		}catch(NullPointerException e){
			throw new JSONException("Missing hint tokens counter");
		}

		try{
			getFinalRound();
		}catch(NullPointerException e){
			throw new JSONException("Missing final round");
		}

		try{
			getDeckSize();
		}catch(NullPointerException e){
			throw new JSONException("Missing deck size");
		}

		for (String s: getPlayersNames())
		{
			CardList hand = new CardList(new StringReader(object.get(String.class,s)));
			object.put(s,hand);
		}
	}

	public static State createInitialState(String[] players, Stack<Card> deck) throws JSONException
	{
		if (players.length < 2 ||  players.length > 5)
			throw new JSONException("Illegal number of players ("+players.length+")");

		int n = players.length>3?4:5;

		JSONObject state = new JSONObject();
		for (String c: Card.colors)
			state.put(c,0);

		state.put("discarded",new CardList());
		state.put("round", 1);
		state.put("fuse",3);
		state.put("hints",8);
		state.put("final", -1);
		state.put("current",players[0]);
		state.put("lastaction",null);

		CardList hand;
		for (int i=0; i<players.length; i++)
		{
			hand = new CardList();
			for (int j=0; j<n; j++)
				hand.add(deck.pop());
			state.put(players[i], hand);
		}

		state.put("deck",deck.size());

		return new State(state);
	}
}
