package json;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
 * Rappresenta un qualsiasi tipo di dato impl
 */

public abstract class JSONComposite implements Cloneable
{
	/**
	 * Definisce il numero di whitespaces in un livello di indentazione
	 */
	public static int baseindent = 3;

	public abstract JSONComposite copy();

	public Object get(String... keys) throws JSONException
	{
		Object temp = this;
		for (int i=0; i<keys.length-1; i++)
		{
			if (temp instanceof JSONComposite)
				temp = ((JSONComposite)temp).get(keys[i]);
			else throw new JSONException("");
		}
		if (temp instanceof JSONComposite)
			temp = ((JSONComposite)temp).get(keys[keys.length-1]);
		else throw new JSONException("");
		return temp;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, String... keys) throws JSONException
	{
		Object temp = get(keys);
		try
		{
			if (temp == null)
				return null;

			return type.cast(temp);
		}
		catch (ClassCastException e)
		{
			if (type.equals(String.class))
			{
				if (temp instanceof Number)
				{
					BigDecimal bd = new BigDecimal(((Number) temp).doubleValue());
					return (T)bd.toString();
				}
				return (T)temp.toString();
			}

			if (Number.class.isAssignableFrom(type) && temp instanceof Number)
			{
				Number n = (Number)temp;
				if (type.equals(Double.class))
					return (T)new Double(n.doubleValue());
				else if (type.equals(Integer.class))
					return (T)new Integer(n.intValue());
				else if (type.equals(Long.class))
					return (T)new Long(n.longValue());
				else if (type.equals(Short.class))
					return (T)new Short(n.shortValue());
				else if (type.equals(Float.class))
					return (T)new Float(n.floatValue());
				else if (type.equals(Byte.class))
					return (T)new Byte(n.byteValue());
			}

			throw new JSONException(e);
		}
	}

	protected abstract Object get(String name);

	/**
	 * @param indent numero di whitespaces per livello di indentazione
	 * @return una rappresentazione testuale del dato impl usando "indent" whitespaces in ogni livello di indentazione
	 */
	public abstract String toString(int indent);

	public String toString()
	{
		return toString(baseindent);
	}

	public boolean equals(Object d)
	{
		if (d instanceof JSONComposite)
			return this.toString(0).equals(((JSONComposite)d).toString(0));
		else
			return false;
	}

	static String tabstring(int indent)
	{
		StringBuilder s = new StringBuilder();
		for (int i=0; i<indent; i++)
			s.append(" ");
		return s.toString();
	}
}
