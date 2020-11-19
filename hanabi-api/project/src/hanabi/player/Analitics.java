package hanabi.player;

import hanabi.game.Card;
import hanabi.game.CardList;
import hanabi.game.State;

import java.util.*;

public class Analitics
{
	private static CardList allcards;
	{
		allcards = new CardList();
		Stack<Card> deck = Card.createDeck();
		for(Card card:deck)
			allcards.add(card);
	}
	private State state;
	private CardList ingamecards; //Rappresenta allcards da cui tolgo le carte giocate o scartate
	public final String me;

	private HashMap<String,Double> buffer = new HashMap<>();

	public Analitics(String player)
	{
		me = player;
	}
/*
	public double getProbability(String cardowner, int index, Card sample)
	{
		CardList filtered = ingamecards.copy();

		//Filtro guardando le mani degli altri
		for (String player : state.getPlayersNames()) {
			if (!player.equals(cardowner)) {
				for (Card c : state.getHand(player))
					filtered.remove(c);
			}
		}

		//Filtro guardando le altre carte sicure nella mia mano
		CardList hand = state.getHand(cardowner);
		for (int i = 0; i < hand.size(); i++) {
			if (index != i && hand.get(i).getPossibleValues().size() == 1 && hand.get(i).getPossibleColors().size() == 1)
				filtered.remove(index);
		}

		//Filtro secondo conoscenza della carta
		Card card = state.getHand(cardowner).get(index);
		for (String color : Card.colors) {
			if (!card.getPossibleColors().contains(color))
				filtered.removeColor(color);
		}
		for (double value:Card.values)
		{
			if (!card.getPossibleValues().contains(value))
				filtered.removeValue((int)value);
		}
	}
*/
	public double getColorProbability(String cardowner, int index, String color)
	{
		if (buffer.containsKey(cardowner+"/"+index+"/"+color))
		{
			return buffer.get(cardowner+"/"+index+"/"+color);
		}
		else
		{
			double result;
			if (!state.getHand(cardowner).get(index).getPossibleColors().contains(color))
				result = 0;
			else
			{

				CardList possible = getPossibleCards(cardowner, index);

				double cont = 0;
				int tot = possible.size();
				for (Card c : possible) {
					if (c.getColor().equals(color))
						cont++;
				}

	/*			if (!cardowner.equals(me)) {
					for (int i = 0; i < state.getHand(me).size(); i++) {
						if (getColorProbability(me, i, color) == 1) {
							cont--;
							tot--;
						}
					}
				}*/
				result = cont / tot;
			}
			if (result<0 || result>1)
				System.out.println(result+" per "+cardowner+"/"+index+"/"+color);
			buffer.put(cardowner+"/"+index+"/"+color,result);
			return result;
		}
	}

	private CardList getPossibleCards(String cardowner, int index)
	{
		//Restituisco la lista di carte che quella indicata potrebbe essere prendendo le carte rimaste in gioco e applicando 2 filtri.
		//Nota che se cardowner!=me restituisco le carte che io penso lui veda possibili.

		CardList possible = ingamecards.copy();

		//Filtro secondo conoscenza della carta richiesta
		Card card = state.getHand(cardowner).get(index);
		for (String color : Card.colors) {
			if (!card.getPossibleColors().contains(color))
				possible.removeColor(color);
		}
		for (double value:Card.values)
		{
			if (!card.getPossibleValues().contains(value))
				possible.removeValue((int)value);
		}

		//Filtro togliendo le carte possedute dai giocatori che non sono ne cardowner ne me.
		for (String player : state.getPlayersNames()) {
			if (!player.equals(cardowner) && !player.equals(me)) {
				for (Card c : state.getHand(player))
					possible.remove(c);
			}
		}

		if (!cardowner.equals(me)) {
			for (int i = 0; i < state.getHand(me).size(); i++)
			{
				for (String color: Card.colors)
				{
					for (double value: Card.values)
					{
						if (getValueProbability(me, i, value) == 1 && getColorProbability(me,i,color) == 1) {
							possible.remove(Card.createCard((int)value,color));
						}
					}
				}

			}
		}

		return possible;
	}


	public double getCardEntropy(String cardowner, int index)
	{
		if (buffer.containsKey(cardowner+"/"+index+"/E"))
		{
			return buffer.get(cardowner+"/"+index+"/E");
		}
		else
		{
			double entropy = 0;
			double p;
			for (String color: Card.colors)
			{
				for (double value: Card.values)
				{
					p = getColorProbability(cardowner,index,color)*getValueProbability(cardowner,index,value);
					entropy -= p*mathlog(p); // 0*log(0) deve fare 0
				}
			}
			entropy = entropy/Math.log(2);
			buffer.put(cardowner+"/"+index+"/E",entropy);
			return entropy;
		}
	}

	public double getHandEntropy(String cardowner)
	{
		if (buffer.containsKey(cardowner+"/E"))
		{
			return buffer.get(cardowner);
		}
		else
		{
			double entropy = 0;
			for (int i=0; i<state.getHand(cardowner).size(); i++)
				entropy += getCardEntropy(cardowner,i);
			buffer.put(cardowner,entropy);
			return entropy;
		}
	}

	public double getPlayability(String cardowner, int index)
	{
		if (buffer.containsKey(cardowner+"/"+index+"/P"))
		{
			return buffer.get(cardowner+"/"+index+"/P");
		}
		else
		{
			/*
			CardList possible = getPossibleCards(cardowner,index,true,false,true);


			//Cerco quali tipi di carte sono giocabili
			CardList playable = new CardList();
			for (String color:Card.colors)
			{
				int x = state.getFirework(color);
				if(x<5)
					playable.add(Card.createCard(x+1,color));
			}

			//Conto quante carte possibili sono giocabili.
			double cont = 0;
			for (Card c:possible)
			{
				if (playable.contains(c))
					cont++;
			}

			int tot = possible.size();

			//Se sto cercando la playability di carte non mie e io ho in mano carte che so essere giocabili al 100% le tolgo dai casi possibili
			if (!cardowner.equals(me))
			{
				for (int i=0; i<state.getHand(me).size(); i++)
				{
					if (getPlayability(me,i) == 1) {
						cont--;
						tot--;
					}
				}
			}
			buffer.put(cardowner+"/"+index+"/P",cont/tot);
			return cont/tot;
			*/
			double p = 0;
			//HashSet<Card> set = new HashSet<>(ingamecards);
			CardList set = new CardList();
			CardList possible = getPossibleCards(cardowner,index);
			for (int i=0; i<possible.size(); i++)
			{
				if (!set.contains(possible.get(i)))
					set.add(possible.get(i));
			}

			for (Card c:set)
			{
				if (isPlayable(c))
					p+=getValueProbability(cardowner,index,c.getValue())*getColorProbability(cardowner,index,c.getColor());
			}
			buffer.put(cardowner+"/"+index+"/P",p);
			return p;
		}
	}

	public double getUselessness(String cardowner, int index)
	{
		if (buffer.containsKey(cardowner+"/"+index+"/U"))
		{
			return buffer.get(cardowner+"/"+index+"/U");
		}
		else {
/*
			CardList possible = getPossibleCards(cardowner, index, false, false, true);
			//Nota che ai fini di calcolo della uselessness non filtro le carte degli altri

			//Una carta è inutile se non è più giocabile o se è tra le possibili più di una volta
			double cont = 0;
			CardList box;
			int fire;
			Card card;
			for (int i = 0; i < possible.size(); i++)
			{
				//Una carta non è più giocabile se il valore del firework è >= del suo
				//oppure se non ci sono più carte dello stesso colore che andrebbero giocate
				card = possible.get(i);
				fire = state.getFirework(card.getColor());
				if (fire>= card.getValue())
					cont++;
				else
				{
					Card mustplay = Card.createCard(fire+1,possible.get(i).getColor());
					if (!possible.contains(mustplay))
						cont++;
					else
					{
						box = possible.copy();
						box.remove(i);
						if (box.contains(possible.get(i)))
							cont++;
					}
				}
			}
			buffer.put(cardowner+"/"+index+"/U",cont/possible.size());
			return cont / possible.size();

 */
			double p = 0;
			//Devo togliere da ingamecards le carte ripetute.
			//HashSet<Card> set = new HashSet<>(ingamecards);
			CardList set = new CardList();
			CardList possible = getPossibleCards(cardowner,index);
			for (int i=0; i<possible.size(); i++)
			{
				if (!set.contains(possible.get(i)))
					set.add(possible.get(i));
			}

			for (Card c:set)
			{
				if (isUseless(c))
					p+=getValueProbability(cardowner,index,c.getValue())*getColorProbability(cardowner,index,c.getColor());
			}
			buffer.put(cardowner+"/"+index+"/U",p);
			return p;
		}
	}

	public double getValueProbability(String cardowner, int index, double value)
	{

		if (buffer.containsKey(cardowner+"/"+index+"/"+value))
		{
			return buffer.get(cardowner+"/"+index+"/"+value);
		}
		else {

			double result;

			if (!state.getHand(cardowner).get(index).getPossibleValues().contains(value))
				result = 0;
			else {

				CardList possible = getPossibleCards(cardowner, index);

				double cont = 0;
				int tot = possible.size();
				for (Card c : possible) {
					if (c.getValue() == value)
						cont++;
				}

				result = cont/tot;

			}
			if (result<0 || result>1)
				System.out.println(result+" per "+cardowner+"/"+index+"/"+value);
			buffer.put(cardowner + "/" + index + "/" + value, result);
			return result;
		}
	}

	public boolean isPlayable(Card card)
	{
		return state.getFirework(card.getColor())+1 == card.getValue();
	}

	public boolean isUseless(Card card)
	{
		int fire = state.getFirework(card.getColor());
		//Se il firework dello stesso colore ha valore >= della carta
		if (fire>= card.getValue())
			return true;
		Card mustplay = Card.createCard(fire+1,card.getColor());
		//Se in gioco non ci sono più le carte di valore minore che andrebbero giocate
		if (!ingamecards.contains(mustplay))
			return true;
		//Se in gioco esiste una copia della carta
		CardList box = ingamecards.copy();
		box.remove(card);
		return box.contains(card);
	}

	public State getState()
	{
		return state;
	}

	private double mathlog(double d)
	{
		if (d==0)
			return 0;
		return Math.log(d);
	}

	public void setState(State state)
	{
		this.state = state;
		ingamecards = allcards.copy();

		for (Card card:state.getDiscarded())
			ingamecards.remove(card);

		for (String color:Card.colors)
		{
			for (int i=1; i<=state.getFirework(color); i++)
				ingamecards.remove(Card.createCard(i,color));
		}
		buffer.clear();
	}


}
