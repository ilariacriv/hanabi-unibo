package json;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static json.JSONUtils.skipSpaces;

/**
 * Definisce un'array impl come lista
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class JSONArray extends JSONComposite implements List<Object>
{
	private ArrayList<Object> list;

	/**
	 * Costruisce l'array vuoto []
	 */
	public JSONArray()
	{
		list = new ArrayList<>();
	}

	/**
	 * Legge un carattere alla volta fino a quando non ottiene una rappresentazione testuale di un array impl
	 * @param reader Reader da cui leggere una rappresentazione testuale di un array impl
	 * @throws JSONException se la stringa letta &egrave; malformata
	 */
	public JSONArray(Reader reader) throws JSONException
	{
		this();
		try
		{
			if (!reader.markSupported())
				reader = new StringReader(JSONUtils.recoverArray(reader));

			boolean flag = true;
			char t;

			if (skipSpaces(reader) != '[')
				throw new JSONException("JSONArray must starts with '['");
			reader.mark(2);
			if (reader.read()==']')
				flag = false;
			else
				reader.reset();

			while(flag)
			{
				reader.mark(Integer.MAX_VALUE);
				t = skipSpaces(reader);
				reader.reset();
				if (t == '{') {
					add(new JSONObject(reader));
					t = skipSpaces(reader);
				}
				else if (t == '[') {
					add(new JSONArray(reader));
					t = skipSpaces(reader);
				}
				else if (t == '"') {
					add(JSONUtils.recoverString(reader));
					t = skipSpaces(reader);
				}
				else if (Character.toLowerCase(t) == 't' || Character.toLowerCase(t) == 'f') {
					add(Boolean.parseBoolean(JSONUtils.recoverBoolean(reader)));
					t = skipSpaces(reader);
				}
				else if (t == 'n')
				{
					JSONUtils.recoverNull(reader);
					add(null);
					t = skipSpaces(reader);
				}
				else if (t>='0' && t<='9')
				{
					String[] n = JSONUtils.recoverNumber(reader);
					if (n[1].equals(""))
						t = JSONUtils.skipSpaces(reader);
					else
						t = n[1].charAt(0);
					add(Double.parseDouble(n[0]));
				}
				else
					throw new IOException("Unrecognized field!");
				if (t == ']')
					flag = false;
				else if (t != ',')
					throw new IOException("Value definition ends, expected ']' or ','");
			}
			reader.mark(0);
		}
		catch(IOException je)
		{
			throw new JSONException(je);
		}
	}

	@Override
	public void add(int index, Object value) throws JSONException
	{
		if (value == null)
			throw new JSONException("value can not be null");
		if (!JSONUtils.checkType(value))
			throw new JSONException("wrong value type");
		list.add(index, value);
	}

	@Override
	public boolean add(Object element)
	{
		if (JSONUtils.checkType(element)) {
			return list.add(element);
		}
		return false;
	}

	/**
	 * @return un Iterator su tutti gli attributi di questo JSONArray
	 */
	public Iterator<Object> iterator()
	{
		return list.iterator();
	}

	/**
	 * Restituisce una copia dell'attributo di posizione desiderata
	 * @param index la posizione dell'attributo nel JSONArray
	 * @return l'attributo
	 */
	public Object get(int index)
	{
		return list.get(index);
	}

	public Object get(String index) throws JSONException
	{
		try
		{
			return list.get(Integer.parseInt(index));
		}
		catch(NumberFormatException e){throw new JSONException(e);}
	}

	public <T> T get(Class<T>type, int index) throws JSONException
	{
		return get(type,""+index);
	}

	/**
	 * Assegna ad ogni attributo innestato un nuovo livello di indentazione
	 * @see JSONComposite#toString(int)
	 */
	public final String toString(int indent)
	{
		StringBuilder ret = new StringBuilder("[");
		for (Object d:list)
		{
			if (indent>0)
				ret.append("\n");
			ret.append(tabstring(indent));
			if (d instanceof JSONComposite)
			{
				if (indent > 0)
					ret.append(((JSONComposite)d).toString(indent+baseindent));
				else
					ret.append(((JSONComposite)d).toString(0));
			}
			else if (d instanceof String)
			{
				String a = d.toString();
				if (indent>0)
					a = a.replace("\n", "\n"+ tabstring(indent+1)).replace("\r", "\r"+tabstring(indent+1));
				a = "\""+a+"\"";
				ret.append(a);
			}
			else if (d instanceof Number)
			{
				BigDecimal bd = new BigDecimal(((Number) d).doubleValue());
				ret.append(bd.toString());
			}
			else
				ret.append(d);
			ret.append(",");
		}

		if (size() > 0)
		{
			ret = new StringBuilder(ret.substring(0, ret.length()-1));
			if (indent>0)
			{
				ret.append("\n");
				ret.append(tabstring(indent-baseindent));
			}
		}
		ret.append("]");
		return ret.toString();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
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

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<?> c)
	{
		for(Object o:c)
		{
			if (!(JSONUtils.checkType(o)))
				throw new JSONException("");
		}
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<?> c)
	{
		for(Object o:c)
		{
			if (!(JSONUtils.checkType(o)))
				throw new JSONException("");
		}
		return list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	public JSONArray copy()
	{
		JSONArray clone = new JSONArray();
		for (int i=0; i<size(); i++)
			if (list.get(i) instanceof JSONObject)
				clone.add(get(JSONObject.class,i).copy());
			else if (list.get(i) instanceof JSONArray)
				clone.add(get(JSONArray.class,i).copy());
			else if (list.get(i) instanceof TypedJSONObject)
				clone.add(get(TypedJSONObject.class,i).copy());
			else if (list.get(i) instanceof TypedJSONArray)
				clone.add(get(TypedJSONArray.class,i).copy());
			else
				clone.add(list.get(i));
		return clone;
	}

	@Override
	public Object set(int index, Object element)
	{
		if (!(JSONUtils.checkType(element)))
			throw new JSONException("");
		return list.set(index, element);
	}

	@Override
	public Object remove(int index) {
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<Object> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<Object> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<Object> subList(int fromIndex, int toIndex)
	{
		JSONArray sublist = new JSONArray();
		sublist.list = (ArrayList<Object>) list.subList(fromIndex, toIndex);
		return sublist;
	}

/*
	public static void main(String args[]) throws IOException
	{
		System.out.println(System.getProperty("user.dir"));
		System.out.println(new JSONArray(new FileReader("../../telegram-message-dispatcher/stored")));
	}

 */
}
