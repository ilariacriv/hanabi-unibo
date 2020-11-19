package hanabi.game;

import json.*;

import java.io.Reader;
import java.io.StringReader;


/**
 * Classe che rappresenta una mossa come oggetto json. In base al valore dell'attributo "type" ci sono 3 tipi di Action:
 * <ul>
 *     <li>
 *         {<br>
 *             "player"			: nome del giocatore che effettua la mossa (string)<br>
 *             "type"  			: "play" | "discard"<br>
 *             "card" 			: indice della carta nella mano del giocatore che effettua la mossa (int)<br>
 *         }
 *     </li>
 *     <li>
 *         {<br>
 *             "player"        : nome del giocatore che effettua la mossa (string)<br>
 *             "type"          : "hint color"<br>
 *             "hinted"        : nome del giocatore che riceve il suggerimento per colore(string)<br>
 *             "color"         : colore suggerito (string)<br>
 *         }
 *     </li>
 *     <li>
 *         {<br>
 *             "player"        : nome del giocatore che effettua la mossa (string)<br>
 *             "type"          : "hint value"<br>
 *             "hinted"        : nome del giocatore che riceve il suggerimento per valore(string)<br>
 *             "value"         : valore suggerito (int)<br>
 *         }
 *     </li>
 * </ul>
 */
public class Action extends TypedJSONObject
{
	public static final String play = "play";
	public static final String discard = "discard";
	public static final String hint_color = "hint color";
	public static final String hint_value = "hint value";

	public Action(JSONObject object)
	{
		super(object);
	}

	public Action(Reader reader) throws JSONException
	{
		super(reader);
	}

	private Action(Action action)
	{
		super(action);
	}

	@Override
	public Action copy() {
		return new Action(this);
	}

	public int getCard()
	{
		return object.get(Number.class,"card").intValue();
	}

	public String getActionType()
	{
		return object.get(String.class,"type");
	}

	public String getColor()
	{
		return object.get(String.class, "color");
	}

	public String getHinted()
	{
		return object.get(String.class,"hinted");
	}

	public String getPlayer()
	{
		return object.get(String.class,"player");
	}

	public int getValue()
	{
		return object.get(Number.class,"value").intValue();
	}

	@Override
	public void verify() throws JSONException
	{
		if (!object.has("player"))
			throw new JSONException("Missing \"player\"");
		if (!object.get("player").getClass().equals(String.class))
			throw new JSONException("\"player\" must be a String");

		if (!object.has("type"))
			throw new JSONException("Missing \"type\"");
		if (!object.get("type").getClass().equals(String.class))
			throw new JSONException("\"type\" must be a String");

		String type = object.get(String.class,"type");
		if (type.equalsIgnoreCase(hint_color))
		{
			if (!object.has("hinted"))
				throw new JSONException("Missing \"hinted\"");
			if (!object.get("hinted").getClass().equals(String.class))
				throw new JSONException("\"hinted\" must be a String");

			if (!object.has("color"))
				throw new JSONException("Missing \"color\"");
			if (!Card.verifyColor(getColor()))
				throw new JSONException("Invalid \"color\"");
		}
		else if (type.equalsIgnoreCase(hint_value))
		{
			if (!object.has("hinted"))
				throw new JSONException("Missing \"hinted\"");
			if (!object.get("hinted").getClass().equals(String.class))
				throw new JSONException("\"hinted\" must be a String");

			if (!object.has("value"))
				throw new JSONException("Missing \"value\"");
			int v = getValue();
			if (v<0 || v>5 )
				throw new JSONException("Invalid \"value\"");
		}
		else if (type.equalsIgnoreCase(play)||type.equalsIgnoreCase(discard))
		{
			if (!object.has("card"))
				throw new JSONException("Missing \"card\"");
			int v = getCard();
			if (v<0 || v>4 )
				throw new JSONException("Invalid \"card\"");
		}
		else
			throw new JSONException("Invalid \"type\"");
	}
/*
	public static List<Action> getAllAction(String player)
	{
		try {
			ArrayList<Action> list = new ArrayList<>();

			for (int i = 0; i < Game.getInstance().getNumberOfCardsPerPlayer(); i++)
			{
				list.add(createAction(player, PLAY, i));
				list.add(createAction(player,DISCARD,i));
			}

			for (String name : Game.getInstance().getPlayers())
			{
				if (!name.equals(player))
				{
					for (int i=1; i<6; i++)
						list.add(createAction(player,name,i));
					for (Color color:Color.colors())
						list.add(createAction(player,name,color));
				}
			}

			return list;
		}
		catch(JSONException e){e.printStackTrace();return null;}
	}
*/

	public static Action createHintColorAction(String player, String hinted, String color) throws JSONException
	{
		JSONObject action = new JSONObject();
		action.put("player",player);
		action.put("type",hint_color);
		action.put("hinted",hinted);
		action.put("color",color);
		return new Action(new StringReader(action.toString(0)));
	}

	public static Action createHintValueAction(String player, String hinted, int value) throws JSONException
	{
		JSONObject action = new JSONObject();
		action.put("player",player);
		action.put("type",hint_value);
		action.put("hinted",hinted);
		action.put("value",value);
		return new Action(new StringReader(action.toString(0)));
	}

	public static Action createDiscardAction(String player, int card) throws JSONException
	{
		JSONObject action = new JSONObject();
		action.put("player",player);
		action.put("type",discard);
		action.put("card",card);
		return new Action(new StringReader(action.toString(0)));
	}

	public static Action createPlayAction(String player, int card) throws JSONException
	{
		JSONObject action = new JSONObject();
		action.put("player",player);
		action.put("type",play);
		action.put("card",card);
		return new Action(new StringReader(action.toString(0)));
	}
/*
	public String toString()
	{
		ActionType type = getActionType();
		String p = getPlayer();
		if (type == PLAY)
			return p+"("+Game.getInstance().getPlayerTurn(p)+") gioca la carta in posizione "+getCard()+" nella propria mano";
		else if (type == DISCARD)
			return p+"("+Game.getInstance().getPlayerTurn(p)+") scarta la carta in posizione "+getCard()+" nella propria mano";
		else {
			String h = getHinted();
			if (type == HINT_COLOR)
				return p+"("+Game.getInstance().getPlayerTurn(p)+") mostra le carte di colore "+getColor()+" a "
						+h+"("+Game.getInstance().getPlayerTurn(h)+")";
			else if (type == HINT_VALUE)
				return p+"("+Game.getInstance().getPlayerTurn(p)+") mostra le carte di valore "+getValue()+" a "
						+h+"("+Game.getInstance().getPlayerTurn(h)+")";
		}
		return "";
	}
*/
/*
	public static void main(String args[]) throws JSONException
	{
//		Logger.initStaticLogger(true);
		String[] players = {"io","altro"};
		Game.createGameFromList(players);
		System.out.println(getAllAction("io").size());
	}

 */
}
