package hanabi.game;

import hanabi.player.Analitics;
import json.*;

import java.io.Reader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class DataState extends TypedJSONObject {
    /**
     * Rappresenta lo stato di una partita.
     * {<br>
     *      "discarded"	: lista di carte scartate (CardList)</br>
     *      "current" 	: nome del giocatore cui tocca giocare (string)</br>
     *      "round" 	: numero di turno corrente (int) <br>
     *      "fuse" 		: numero di fuse tokens rimasti (int) <br>
     *      "hints"		: numero di hints tokens rimasti (int) <br>
     *		"final"		: numero del turno finale se conosciuto, -1 altrimenti (int) <br>
     *		"deck"		: numero di carte rimaste nel mazzo (int) <br>
     *		"red"		: numero di carte nel firework red (int) <br>
     *		"blue"		: numero di carte nel firework blue (int) <br>
     *		"yellow"	: numero di carte nel firework yellow (int) <br>
     *		"white"		: numero di carte nel firework white (int) <br>
     *		"green"		: numero di carte nel firework green (int) <br>
     *		"lastaction": ultima azione eseguita (Action)<br>
     *		"player"	: lista di carte nella mano del giocatore "player" (CardList) <br>
     *  * }
     */

    public DataState(Reader reader) throws JSONException{
        super(reader);
    }

    public DataState(JSONObject object)
    {
        super(object);
    }

    public static DataState getDatastateFromState(State state) throws JSONException
    {
        JSONObject datastate = new JSONObject();
        NumberFormat formatter = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
        for (String c: Card.colors)
            datastate.put(c,state.getFirework(c));

        datastate.put("discarded", discardedCards(state.getDiscarded()));
        datastate.put("round", state.getRound());
        datastate.put("fuse",state.getFuseTokens());
        datastate.put("hints",state.getHintTokens());
        datastate.put("final", state.getFinalRound());
        datastate.put("current",state.getCurrentPlayer());
        datastate.put("lastaction",state.getLastAction());


        for (String name: state.getPlayersNames())
        {
            Analitics analitics = new Analitics(name);
            analitics.setState(state);
            List<DataCard> hand = new ArrayList<>();
            for(int i=0; i<state.getHand(name).size(); i++){
                hand.add(DataCard.createDatacardFromCard(state.getHand(name).get(i), analitics,i));
            }
            JSONArray handJson= new JSONArray();
            handJson.addAll(hand);
            datastate.put(name, handJson);
            datastate.put("handentropy_"+name, formatter.format(analitics.getHandEntropy(name)));
        }

        datastate.put("deck",state.getDeckSize());
        return new DataState(datastate);
    }

    private static JSONArray discardedCards(CardList discarded){
       // List<SimpleCard> cards = new ArrayList<>();
        JSONArray disc= new JSONArray();

        for(int i=0; i< discarded.size();i++ ){
            JSONObject c= new JSONObject();
            c.put("value",discarded.get(i).getValue());
            c.put("color", discarded.get(i).getColor());
            disc.add(c);
        }


        //disc.addAll(cards);
        return disc;
    }


    @Override
    public JSONComposite copy() {
        return null;
    }

    @Override
    public void verify() throws JSONException {

    }
}
