package json;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JSONUtils
{
	static boolean checkType(Object value)
	{
		return value == null || value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof JSONComposite;
	}

	public static String recoverArray(Reader reader) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		char i = skipSpaces(reader);
		if (i=='[')
		{
			sb.append(i);
			int cont = 1; //Conta le graffe aperte e non ancora chiuse. Leggo caratteri finché diverso da 0
			while(cont > 0)
			{
				i = (char)reader.read();
				if (i == (char)-1)
					throw new EOFException();
				sb.append(i);
				if (i=='[')
					cont++;
				else if (i==']')
					cont--;
			}
		}
		else
			throw new IOException("Must start with "+'[');
		return sb.toString();
	}

	public static String recoverBoolean(Reader reader) throws IOException
	{
		char i = skipSpaces(reader);
		char[] c = new char[4];
		if (Character.toLowerCase(i) == 't')
		{
			if (reader.read(c, 0, 3)<3)
				throw new EOFException();
			if (Character.toLowerCase(c[0])=='r' && Character.toLowerCase(c[1])=='u' && Character.toLowerCase(c[2])=='e')
				return ""+i+c[0]+c[1]+c[2];
			else throw new IOException("Not a boolean!");
		}
		else if (Character.toLowerCase(i) == 'f')
		{
			if (reader.read(c)<4)
				throw new EOFException();
			if (Character.toLowerCase(c[0])=='a' && Character.toLowerCase(c[1])=='l'
					&& Character.toLowerCase(c[2])=='s' && Character.toLowerCase(c[3])=='e')
				return ""+i+c[0]+c[1]+c[2]+c[3];
			else throw new IOException("Not a boolean!");
		}
		else throw new IOException("Not a boolean!");
	}

	public static void recoverNull(Reader reader) throws IOException
	{
		String s = ""+JSONUtils.skipSpaces(reader)+(char)reader.read()+(char)reader.read()+(char)reader.read();
		if (!s.equals("null"))
			throw new IOException("Not null");
	}

	/**
	 * Il dati di tipo NUMBER sono gli unici a non avere un carattere terminatore. Ci&ograve; significa che il metodo
	 * recover legger&agrave; dal reader un carattere in pi&ugrave; del necessario. Tale carattere deve essere mantenuto
	 * in quanto parte del messaggio da leggere tramite reader.
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static String[] recoverNumber(Reader reader) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		char i = skipSpaces(reader);
		boolean point = false;
		while(true)
		{
			try
			{
				Double.parseDouble(""+i);
			}
			catch(NumberFormatException nfe)
			{
				if (i=='.' && !point)
					point = true;
				else if (i!='-')
					break;
			}
			sb.append(i);
			i = (char)reader.read();
		}
		String value = sb.toString();
		String lastchar = ""+i;
		try
		{
			//Verifico se è un carattere skippabile
			JSONUtils.skipSpaces(new StringReader(lastchar));
		}
		catch(EOFException eof)
		{
			//Se avevo un carattere skippabile devo continuare a skippare
			lastchar = "";
		}
		return new String[]{value,lastchar};
	}

	public static String recoverObject(Reader reader) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		char i = skipSpaces(reader);
		if (i=='{')
		{
			sb.append(i);
			int cont = 1; //Conta le graffe aperte e non ancora chiuse. Leggo caratteri finché diverso da 0
			while(cont > 0)
			{
				i = (char)reader.read();
				if (i == (char)-1)
					throw new EOFException();
				sb.append(i);
				if (i=='{')
					cont++;
				else if (i=='}')
					cont--;
			}
		}
		else
			throw new IOException("Must start with "+'{');
		return sb.toString();
	}

	public static String recoverString(Reader reader) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		char i = skipSpaces(reader);
		if (i=='"')
		{
			sb.append(readUntil(reader,'"'));
			sb.deleteCharAt(sb.length()-1);
		}
		else throw new IOException("Must start with \"");
		return sb.toString();
	}

	/**
	 * Effettua escape sui seguenti caratteri: \n,\r,\t,\,",{,[,],}
	 * @param s Una stringa qualsiasi
	 * @return Una stringa di una sola riga con escape sui caratteri elencati sopra
	 */
	static String quote(String s)
	{
		return s.replace("\\", "\\\\").replace("\t", "\\t").replace("\r","\\r")
				.replace("\n", "\\n").replace("\"", "\\\"").replace("{", "\\{")
				.replace("[", "\\[").replace("}", "\\}").replace("]", "\\]");
	}

	/**
	 * Questo metodo legge da Reader un carattere alla volta fino alla lettura di uno dei caratteri terminatori passati come parametro.<br>
	 * Restituisce la concatenazione di tutti i caratteri letti, terminatore incluso.
	 * @param reader Reader da cui leggere caratteri
	 * @param cs Array di caratteri
	 * @return String letta da reader
	 * @throws IOException in caso di errori I/O, vedi {@link Reader#read()}
	 * @throws EOFException se il metodo non termina entro l'EOF del Reader
	 */

	static String readUntil(Reader reader, char... cs) throws IOException
	{
		StringBuilder s = new StringBuilder();
		char box = ' ',box1;
		boolean flag = true;
		while(flag)
		{
			box1 = box;
			box = (char) reader.read();
			if (box == (char)-1)
				throw new EOFException();
			s.append(box);
			if (box1 != '\\')
			{
				for (char c:cs)
				{
					if (box == c)
						flag = false;
				}
			}
		}
		return s.toString();
	}

	/**
	 * Questo metodo legge da Reader un carattere alla volta fino alla lettura di un carattere terminatore diverso
	 * da quelli passati come parametro.<br>
	 * Restituisce 2 stringhe: la concatenazione di tutti i caratteri letti, terminatore escluso, e il terminatore.
	 * @param reader {@link Reader} da cui leggere caratteri
	 * @param cs array di caratteri la cui lettura non provoca terminazione
	 * @return String letta da reader
	 * @throws IOException in caso di errori I/O, vedi {@link Reader#read()}
	 * @throws EOFException se il metodo non termina entro l'EOF del Reader
	 */
	static String[] readWhile(Reader reader, char...cs) throws IOException
	{
		StringBuilder s = new StringBuilder();
		boolean flag = true;
		char box = ' ',box1;
		while(flag)
		{
			flag = false;
			box1 = box;
			box = (char) reader.read();
			if (box == (char)-1)
				throw new EOFException();
			if (box1 != '\\')
			{
				for (char c:cs)
				{
					if (box == c) {
						flag = true;
						break;
					}
				}
			}
			if (flag)
				s.append(box);
		}
		return new String[] {s.toString(),""+box};
	}

	/**
	 * Operazione inversa di {@link #quote(String)}
	 * @param s stringa ottenuta tramite {@link #quote(String)}
	 * @return la stringa originale parametro di {@link #quote(String)}
	 */
	static String unquote(String s)
	{
		return s.replace("\\\\", "\\").replace("\\t", "\t").replace("\\r","\r")
				.replace("\\n", "\n").replace("\\\"", "\"").replace("\\{", "{")
				.replace("\\[", "[").replace("\\}", "}").replace("\\]", "]");
	}

	static char skipSpaces(Reader r) throws IOException
	{
		return readWhile(r,'\t',' ','\n','\r')[1].charAt(0);
	}
}
