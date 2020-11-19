package hanabi.game;

import json.JSONArray;
import json.JSONException;
import json.TypedJSONArray;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;

public class CardList extends TypedJSONArray implements Collection<Card>
{
	public CardList(JSONArray array)
	{
		super(array);
	}

	private CardList(CardList list)
	{
		super(list);
	}

	public CardList()
	{
		this(new JSONArray());
	}

	public CardList(Reader reader) throws JSONException
	{
		super(reader);
	}

	public boolean add(Card card)
	{
		array.add(card);
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Card> c)
	{
		return array.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return array.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return array.retainAll(c);
	}

	@Override
	public void clear() {
		array.clear();
	}

	public boolean contains(Object card)
	{
		return array.contains(card);
	}

	public CardList copy()
	{
		return new CardList(this);
	}

	public Card get(int i)
	{
		return array.get(Card.class,i);
	}

	public void set(int i, Card card)
	{
		array.set(i,card);
	}

	public int size()
	{
		return array.size();
	}

	@Override
	public boolean isEmpty() {
		return array.isEmpty();
	}

	public boolean remove(Object card)
	{
		return array.remove(card);
	}

	public void remove(int i)
	{
		array.remove(i);
	}

	public void removeColor(String color)
	{
		if (color == null)
		{
			for (int i = 0; i < size(); i++) {
				if (get(i).getColor()== null) {
					remove(i);
					i--;
				}
			}
		}
		else
		{
			for (int i = 0; i < size(); i++) {
				if (get(i).getColor().equals(color)) {
					remove(i);
					i--;
				}
			}
		}
	}

	public void removeValue(int value)
	{
		for (int i=0; i<size(); i++)
		{
			if (get(i).getValue() == value) {
				remove(i);
				i--;
			}
		}
	}

	public void retainValue(int value)
	{
		for (int i=0; i<size(); i++)
		{
			if (get(i).getValue() != value)
			{
				remove(i);
				i--;
			}
		}
	}

	public void retainColor(String color)
	{
		if (color == null)
		{
			for (int i = 0; i < size(); i++) {
				if (get(i).getColor()!= null)
				{
					remove(i);
					i--;
				}
			}
		}
		else
		{
			for (int i = 0; i < size(); i++) {
				if (!get(i).getColor().equals(color))
				{
					remove(i);
					i--;
				}
			}
		}
	}

	@Override
	public void verify() throws JSONException
	{
		for(int i=0; i<array.size(); i++)
		{
			Card card = new Card(new StringReader(array.get(String.class,i)));
			array.set(i,card);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterator<Card> iterator() {
		return (Iterator<Card>)(Iterator<?>)array.iterator();
	}

	@Override
	public Object[] toArray() {
		return array.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return array.toArray(a);
	}
}
