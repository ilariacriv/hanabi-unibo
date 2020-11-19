package json;


import java.io.Reader;

/**
 * Un TypedJSONObject rappresenta la classe madre di un qualsiasi un oggetto impl di struttura predefinita.
 * La struttura di TypedJSONObject &egrave; libera</br>
 */
public abstract class TypedJSONObject extends JSONComposite implements Verifiable,Cloneable
{
	protected JSONObject object;

	public TypedJSONObject(Reader reader) throws JSONException
	{
		object = new JSONObject(reader);
		verify();
	}

	public TypedJSONObject(JSONObject object) throws JSONException
	{
		this.object = object.copy();
		verify();
	}

	public TypedJSONObject(TypedJSONObject copyOf)
	{
		if (copyOf.getClass().equals(this.getClass()))
		{
			object = copyOf.object.copy();
		}
		else throw new JSONException("Wrong parameter class");
	}

	/**
	 * Ridefinisce il metodo copy di JSONData chiedendo l'esistenza di un costruttore {@link TypedJSONObject#TypedJSONObject(JSONObject)}
	 * piuttosto che {@link TypedJSONObject#TypedJSONObject(Reader)} come in {@link JSONComposite}
	 *
	 * Ha una piccola inefficienza in quanto invocando il costruttore si esegue il metodo verify() sul copy.
 	 * @return
	 */
/*	public TypedJSONObject copy()
	{
		try
		{
			return this.getClass().getConstructor(Reader.class).newInstance(new StringReader(object.toString(0)));
		}
		catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
		{
			throw new JSONException(e);
		}
	}
*/

	@Override
	protected Object get(String name) {
		return object.get(name);
	}

	@Override
	public String toString(int indent) {
		return object.toString(indent);
	}

}
