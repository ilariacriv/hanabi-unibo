package hanabi.gui;

public enum Orientation
{
	HORIZONTAL,VERTICAL;

	public Orientation rotate()
	{
		if (this == HORIZONTAL)
			return VERTICAL;
		if (this == VERTICAL)
			return HORIZONTAL;
		return null;
	}
}


