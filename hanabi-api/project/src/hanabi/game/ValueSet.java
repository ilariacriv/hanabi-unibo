package hanabi.game;

import json.JSONArray;
import json.JSONException;
import json.TypedJSONArray;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValueSet extends TypedJSONArray
{
	public ValueSet(Reader reader)
	{
		super(reader);
	}

	private ValueSet(ValueSet original)
	{
		super(original);
	}

	public ValueSet(Set<Double> set)
	{
		this(new StringReader(SetToJSONArray(set).toString(0)));
	}

	private static JSONArray SetToJSONArray(Set<Double> set)
	{
		JSONArray a = new JSONArray();
		a.addAll(set);
		return a;
	}

	@Override
	public ValueSet copy() {
		return new ValueSet(this);
	}

	@Override
	public void verify() throws JSONException
	{
		List<Double> box = new ArrayList<>(Card.values);
		for (Object o:array)
		{
			if (!(o instanceof Double) || (((Double)o).intValue()!= ((Double)o)))
					throw new JSONException("All values must be integer " + o);

			if (!Card.values.contains(o))
				throw new JSONException("Unrecognized value");

			if (!box.contains(o))
				throw new JSONException("Duplicated value");
			box.remove(o);
		}
		if (array.size() == 0)
			throw new JSONException("There must be at least one possible value");
	}

	public Set<Double> toSet()
	{
		HashSet<Double> set = new HashSet<>();
		for (Object o:array)
			set.add((Double)o);
		return set;
	}
}
