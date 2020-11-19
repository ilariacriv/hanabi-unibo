package hanabi.game;

import json.JSONArray;
import json.JSONComposite;
import json.JSONException;
import json.TypedJSONArray;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColorSet extends TypedJSONArray
{
	public ColorSet(Reader reader)
	{
		super(reader);
	}

	private ColorSet(ColorSet original)
	{
		super(original);
	}

	public ColorSet(Set<String> set)
	{
		this(new StringReader(SetToJSONArray(set).toString(0)));
	}

	private static JSONArray SetToJSONArray(Set<String> set)
	{
		JSONArray a = new JSONArray();
		a.addAll(set);
		return a;
	}

	@Override
	public ColorSet copy() {
		return new ColorSet(this);
	}

	@Override
	public void verify() throws JSONException
	{
		List<String> box = new ArrayList<>(Card.colors);
		for (Object o:array)
		{
			if (!(o instanceof String))
				throw new JSONException("All colors must be strings");

			if (!Card.colors.contains(o))
				throw new JSONException("Unrecognized color");

			if (!box.contains(o))
				throw new JSONException("Duplicated color");
			box.remove(o);
		}
		if (array.size() == 0)
			throw new JSONException("There must be at least one possible color");
	}

	public Set<String> toSet()
	{
		HashSet<String> set = new HashSet<>();
		for (Object o:array)
			set.add(o.toString());
		return set;
	}
}
