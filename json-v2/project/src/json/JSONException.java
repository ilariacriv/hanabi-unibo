package json;

import java.io.Reader;

/**
 * Tipo di eccezione ritornata quando si tenta di costruire o modificare un JSONData.
 * Estende {@link RuntimeException} affinch&egrave; si possa ritornare questo tipo di eccezione anche da metodi ereditati
 * da interfacce che originariamente non prevedevano la restituzione di eccezioni.
 */
@SuppressWarnings({"unused"})
public class JSONException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public JSONException(Exception e)
	{
		super(e);
	}

	public JSONException(String s)
	{
		super(s); 
	}

	public JSONException(String s, Exception e)
	{
		super(s,e);
	}
}
