package hanabi.game;

import json.*;

import java.awt.Color;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

/**
 * Classe che rappresenta una carta Hanabi.<br>
 * {<br>
 *     "color" 			: colore della carta <br>
 *     "value" 			: valore della carta <br>
 *     "color_revealed" : true se il colore di questa carta &egrave; stato rivelato al possessore, false altrimenti <br>
 *     "value_revealed" : true se il valore di questa carta &egrave; stato rivelato al possessore, false altrimenti <br>
 * }
 */
public class Card extends TypedJSONObject
{
	public static Set<String> colors = new HashSet<>(Arrays.asList("white","blue","red","yellow","green"));
	public static Set<Double> values = new HashSet<>(Arrays.asList(1.,2.,3.,4.,5.));

	private Card(Card card)
	{
		super(card);
	}

	public Card(Reader reader) throws JSONException
	{
		super(reader);
	}

	@Override
	public Card copy() {
		return new Card(this);
	}

	public String getColor()
	{
		String c = object.get(String.class,"color");
		if (c == null || c.equals(""))
			return null;
		return c;
	}

	public Set<String> getPossibleColors()
	{
		return object.get(ColorSet.class,"possible_colors").toSet();
	}

	public Set<Double> getPossibleValues()
	{
		return object.get(ValueSet.class,"possible_values").toSet();
	}

	public int getValue()
	{
		try
		{
			return object.get(Number.class,"value").intValue();
		}
		catch (NullPointerException e)
		{
			return 0;
		}
	}

	public boolean isColorRevealed()
	{
		return getPossibleColors().size() == 1;
	}

	public boolean isValueRevealed()
	{
		return getPossibleValues().size() == 1;
	}

	/**
	 * Due Card sono uguali se lo sono i loro colori e valori. Un colore o un valore sconosciuto non &egrave; mai uguale ad un altro.
	 * @return true se il parametro &egrave; una Card e se &egrave; uguale a questa carta.
	 **/
	public boolean equals(Object o){
		if(o instanceof Card){
			Card c = (Card)o;
			if (c.getColor() == null || getColor() == null || c.getValue() == 0 || getValue() == 0)
				return false;
			return c.getColor().equals(getColor()) && c.getValue()==getValue();
		}
		return super.equals(o);
	}

	@Override
	public void verify() throws JSONException
	{
		//POSSIBLE COLORS
		if (!object.has("possible_colors"))
			throw new JSONException("Missing \"possible_colors\"");
		ColorSet colorset = new ColorSet(new StringReader(object.get(String.class,"possible_colors")));
		object.put("possible_colors",colorset);

		//POSSIBLE VALUES
		if (!object.has("possible_colors"))
			throw new JSONException("Missing \"possible_colors\"");
		ValueSet valueSet = new ValueSet(new StringReader(object.get(String.class,"possible_values")));
		object.put("possible_values",valueSet);

		//COLOR
		if (!object.has("color"))
			throw new JSONException("Missing \"color\"");
		String color = getColor();
		if (!(color == null || color.equalsIgnoreCase("blue")||color.equalsIgnoreCase("red")
				||color.equalsIgnoreCase("green")||color.equalsIgnoreCase("white")
				||color.equalsIgnoreCase("yellow")))
			throw new JSONException("Illegal color");

		//VALUE
		if (!object.has("value"))
			throw new JSONException("Missing \"value\"");
		int value = getValue();
		if (value<0 || value>5)
			throw new JSONException("Illegal value");



		if (value>0 && !getPossibleValues().contains((double)value))
			throw new JSONException("Possible values does not contain the actual value "+value+"\n\n"+this.toString(3));

		if (color!=null && !getPossibleColors().contains(color))
			throw new JSONException("Possible colors does not contain the actual color");
	}
/*
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (getColor()!=null)
			sb.append(getColor());
		else
			sb.append("   ");
		sb.append("-");
		if (getValue()>0)
			sb.append(getValue());
		else
			sb.append("   ");
		return sb.toString();
	}
*/

	public static Card createCard(int value, String color)
	{
		return createCard(value,color,Card.colors,Card.values);
	}

	public static Card createCard(int value, String color, Set<String> poss_col, Set<Double> poss_val)
	{
		JSONObject json = new JSONObject();
		json.put("value",value);
		json.put("color",color);
		json.put("possible_colors", new ColorSet(poss_col));
		json.put("possible_values", new ValueSet(poss_val));
		return new Card(new StringReader(json.toString(0)));
	}

	public static int getInitialCount(int valuecard)
	{
		if (valuecard<1 || valuecard>5)
			return -1;
		if (valuecard == 1)
			return 3;
		if (valuecard == 5)
			return 1;
		return 2;
	}

	public static Stack<Card> createDeck()
	{
		List<Card> cards = new ArrayList<>();
		for (String color:Card.colors)
		{
			cards.add(Card.createCard(1,color,Card.colors,Card.values));
			cards.add(Card.createCard(1,color,Card.colors,Card.values));
			cards.add(Card.createCard(1,color,Card.colors,Card.values));

			cards.add(Card.createCard(2,color,Card.colors,Card.values));
			cards.add(Card.createCard(2,color,Card.colors,Card.values));

			cards.add(Card.createCard(3,color,Card.colors,Card.values));
			cards.add(Card.createCard(3,color,Card.colors,Card.values));

			cards.add(Card.createCard(4,color,Card.colors,Card.values));
			cards.add(Card.createCard(4,color,Card.colors,Card.values));

			cards.add(Card.createCard(5,color,Card.colors,Card.values));
		}

		Stack<Card> deck = new Stack<>();
		Random r = new Random();
		int i;
		while(cards.size()>0)
		{
			i = r.nextInt(cards.size());
			deck.push(cards.get(i));
			cards.remove(i);
		}

		return deck;
	}

	public static Color getAwtColor(String color)
	{
		if (color == null || color.equals(""))
			return new java.awt.Color(150, 150, 150);
		if (color.equalsIgnoreCase("blue"))
			return new java.awt.Color(0, 150, 255);
		if (color.equalsIgnoreCase("red"))
			return java.awt.Color.red;
		if (color.equalsIgnoreCase("white"))
			return java.awt.Color.white;
		if (color.equalsIgnoreCase("green"))
			return new java.awt.Color(0, 150, 0);
		if (color.equalsIgnoreCase("yellow"))
			return java.awt.Color.yellow;

		return null;
	}

	public static boolean verifyColor(String color)
	{
		return color == null || color.equals("") || colors.contains(color);
	}
}
