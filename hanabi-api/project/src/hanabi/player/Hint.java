package hanabi.player;

import hanabi.game.Card;

import java.util.List;

public class Hint implements Cloneable
{
	private boolean is;
	private int value;
	private String color;

	public Hint(boolean is, int value)
	{
		this(is,null,value);
	}

	public Hint(boolean is, String color)
	{
		this(is,color,-1);
	}

	private Hint(boolean is, String color, int value)
	{
		this.is = is;
		this.color = color;
		this.value = value;
	}

	public void apply(List<Card> list)
	{
		if (color == null)
		{
			if (is)
				Old_Analitics.maintainValue(value,list);
			else
				Old_Analitics.removeValue(value,list);
		}
		else
		{
			if (is)
				Old_Analitics.maintainColor(color,list);
			else
				Old_Analitics.removeColor(color,list);
		}
	}

	public Hint clone()
	{
		try
		{
			super.clone();
		}
		catch (CloneNotSupportedException e){}
		return new Hint(is,color,value);
	}

	public Hint not()
	{
		return new Hint(!is, color, value);
	}
}
