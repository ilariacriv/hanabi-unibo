package json;


import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Definisce un oggetto impl come {@link Map<String,Object>}
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class JSONObject extends JSONComposite implements Map<String,Object>
{
	protected HashMap<String, Object> map;

	/**
	 * Costruisce l'oggetto impl vuoto {}
	 */
	public JSONObject()
	{
		map = new HashMap<>();
	}

	/**
	 * Legge un carattere alla volta fino a quando non ottiene una rappresentazione testuale di un oggetto impl
	 * @param reader Reader da cui leggere una rappresentazione testuale di un oggetto impl
	 * @throws JSONException se la stringa letta &egrave; malformata o in caso di errore I/O
	 */
	public JSONObject(Reader reader) throws JSONException
	{
		this();
		try
		{
			if (!reader.markSupported())
				reader = new StringReader(JSONUtils.recoverObject(reader));

			boolean flag = true;
			String name;

			char t;

			if ((t = JSONUtils.skipSpaces(reader)) != '{')
				throw new JSONException("JSONObject must starts with '{', founded '"+t+"'");
			reader.mark(2);
			if (reader.read()=='}')
				flag = false;
			else
				reader.reset();
			char c;
			while(flag)
			{
				if ((c = JSONUtils.skipSpaces(reader))!='\"')
					throw new JSONException("Name definition begins, expected a string beginning with '\"' but '"+c+"' founded!");

				name = JSONUtils.readUntil(reader,'"');
				name = name.substring(0,name.length()-1);
				if (has(name))
					throw new JSONException("Property "+name+" already defined");

				if (JSONUtils.skipSpaces(reader)!=':')
					throw new JSONException("Name definition ends, expected ':'");

				reader.mark(Integer.MAX_VALUE);
				t = JSONUtils.skipSpaces(reader);
				reader.reset();
				if (t == '{') {
					put(name, new JSONObject(reader));
					t = JSONUtils.skipSpaces(reader);
				}
				else if (t == '[') {
					put(name, new JSONArray(reader));
					t = JSONUtils.skipSpaces(reader);
				}
				else if (t == '"') {
					put(name, JSONUtils.recoverString(reader));
					t = JSONUtils.skipSpaces(reader);
				}
				else if (Character.toLowerCase(t) == 't' || Character.toLowerCase(t) == 'f') {
					put(name, Boolean.parseBoolean(JSONUtils.recoverBoolean(reader)));
					t = JSONUtils.skipSpaces(reader);
				}
				else if (t == 'n')
				{
					JSONUtils.recoverNull(reader);
					put(name,null);
					t = JSONUtils.skipSpaces(reader);
				}
				else if ((t>='0' && t<='9')||t=='-')
				{
					String[] n = JSONUtils.recoverNumber(reader);
					if (n[1].equals(""))
						t = JSONUtils.skipSpaces(reader);
					else
						t = n[1].charAt(0);
					try {
						put(name,Integer.parseInt(n[0]));
					}
					catch (NumberFormatException e)
					{
						put(name,Double.parseDouble(n[0]));
					}
				}
				else
					throw new IOException("Unrecognized field!");
				if (t == '}')
					flag = false;
				else if (t != ',')
					throw new IOException("Value definition ends, expected '}' or ',', founded "+t);
			}
			reader.mark(0);
		}
		catch(IOException je)
		{
			throw new JSONException(je);
		}
	}

	@Override
	public void clear() {
		map.clear();
	}

	public JSONObject copy()
	{
		JSONObject clone = new JSONObject();
		for (String n:keySet()) {
			if (map.get(n) instanceof JSONArray)
				clone.put(n, ((JSONArray)map.get(n)).copy());
			else if (map.get(n) instanceof JSONObject)
				clone.put(n, ((JSONObject)map.get(n)).copy());
			else if (map.get(n) instanceof TypedJSONArray)
				clone.put(n, ((TypedJSONArray)map.get(n)).copy());
			else if (map.get(n) instanceof TypedJSONObject)
				clone.put(n, ((TypedJSONObject)map.get(n)).copy());
			else
				clone.put(n, map.get(n));
		}
		return clone;
	}

	/**
	 * @return {@link JSONType#OBJECT}
	 */
	/*public JSONType getJSONType()
	{
		return JSONType.OBJECT;
	}
*/
	/**
	 * @param name nome dell'attributo di cui si vuole verificare l'esistenza
	 * @return true se questo JSONObject possiede un attributo di nome specificato, false altrimenti
	 */
	public boolean has(String name)
	{
		return keySet().contains(name);
	}

	/**
	 * @return un {@link Iterator} dei nomi dei valori di questo JSONObject
	 */
	public Iterator<String> nameIterator()
	{
		return map.keySet().iterator();
	}

	/**
	 * L'oggetto restituito &egrave; una copia di quello mantenuto dal JSONObject.
	 * @param key nome dell'attributo richiesto
	 * @return L'attributo di nome specificato, null se non esiste attributo con quel nome
	 */
	@Override
	public Object get(Object key)
	{
		return map.get(key);
	}

	public Object get(String name)
	{
		return get((Object)name);
	}

	/**
	 * Assegna un attributo di nome e valore specificati. Nel JSONObject sar&agrave; conservata una copia di <tt>value</tt>
	 * @param name nome dell'attributo da impostare
	 * @param value attributo da assegnare al nome specificato
	 * @return il valore precedentemente associato a <tt>name</tt>. Se tale valore non esiste restituisce <tt>null</tt>
	 * @throws JSONException se <tt>value</tt> == <tt>null</tt>
	 */
	public Object put(String name, Object value) throws JSONException
	{
		if (JSONUtils.checkType(value))
			return map.put(name, value);
		else
			throw new JSONException("Wrong value type");
	}

	/**
	 * @see Map#remove(Object)
	 */
	public Object remove(Object name)
	{
		return map.remove(name);
	}

	public Object remove(String... keys)
	{
		Object o = get(Arrays.copyOf(keys,keys.length-1));
		if (o instanceof JSONObject)
			return ((JSONObject)o).remove(keys[keys.length-1]);
		if (o instanceof JSONArray)
			return ((JSONArray)o).remove(Integer.parseInt(keys[keys.length-1]));
		throw new JSONException("");
	}

	/**
	 * @return il numero di attributi contenuti in questo JSONObject
	 */
	public int size()
	{
		return map.size();
	}

	/**
	 * Assegna ad ogni attributo innestato un nuovo livello di indentazione
	 * @see JSONComposite#toString(int)
	 */
	public final String toString(int indent)
	{
		StringBuilder ret = new StringBuilder("{");
		int indname,newindent;
		for (String name:keySet())
		{
			if (indent>0)
				ret.append("\n");
			Object d = get(name);
			ret.append(tabstring(indent));
			ret.append("\"").append(name).append("\"");
			ret.append(":");
			indname = 3+name.length(); // 3 sta per i 2 '"' e ':'
			newindent = indent+indname;

			if (d instanceof JSONComposite)
			{
				newindent += baseindent;
				if (indent > 0)
					ret.append(((JSONComposite)d).toString(newindent));
				else
					ret.append(((JSONComposite)d).toString(0));
			}
			else if (d instanceof String)
			{
				String a = d.toString();
				if (indent>0)
					a = a.replace("\n", "\n"+ tabstring(newindent+1)).replace("\r", "\r"+tabstring(newindent+1));
				a = "\""+a+"\"";
				ret.append(a);
			}
			else if (d instanceof Number)
			{
				BigDecimal bd = new BigDecimal(((Number) d).doubleValue());
				ret.append(" "+bd.toString());
			}
			else
				ret.append(" "+d);
			ret.append(",");
		}

		if (keySet().size() > 0)
		{

			ret = new StringBuilder(ret.substring(0, ret.length()-1));
			if (indent>0)
			{
				ret.append("\n");
				ret.append(tabstring(indent-baseindent));
			}
		}
		ret.append("}");
		return ret.toString();
	}

	/**
	 * @return una {@link Collection} contenente una copia di tutti i valori degli attributi di questo JSONObject
	 */
	public Collection<Object> values()
	{
		return map.values();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public void putAll(Map<? extends String,?> m) throws JSONException
	{
		for(String key:m.keySet())
		{
			if (!(JSONUtils.checkType(m.get(key))))
				throw new JSONException("");
		}
		map.putAll(m);
	}

	@Override
	public Set<String> keySet() {
		return new HashSet<>(map.keySet());
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}
}
