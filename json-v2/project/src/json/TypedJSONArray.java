package json;

import java.io.Reader;

public abstract class TypedJSONArray extends JSONComposite implements Verifiable,Cloneable
{
	protected JSONArray array;

	public TypedJSONArray(Reader reader) throws JSONException
	{
		array = new JSONArray(reader);
		verify();
	}

	public TypedJSONArray(JSONArray array) throws JSONException
	{
		this.array = array.copy();
		verify();
	}

	public TypedJSONArray(TypedJSONArray copyOf)
	{
		if (copyOf.getClass().equals(this.getClass()))
		{
			array = copyOf.array.copy();
		}
		else throw new JSONException("Wrong parameter class");
	}

	@Override
	protected Object get(String name) {
		return array.get(name);
	}

	@Override
	public String toString(int indent) {
		return array.toString(indent);
	}

}
