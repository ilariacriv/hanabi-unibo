package dataset;

import hanabi.game.Action;
import hanabi.game.Card;
import hanabi.game.CardList;
import hanabi.game.State;
import hanabi.player.Analitics;
import json.*;

import java.io.Reader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataState extends TypedJSONObject {
    /**
     * Rappresenta lo stato di una partita.
     * {<br>
     *      "discarded"	: lista di carte scartate (intesa come 4 interi a 6 cifre: la prima cifra rappresenta il colore; le altre 5 rappresentano il numero di carte scartate per ogni valore)</br>
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

    private static State lastState;

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
        datastate.put("finalround", state.getFinalRound());
        datastate.put("current",state.getCurrentPlayer());

        //datastate.put("lastaction", state.getLastAction());

        /*
        if(state.getLastAction() != null &&
                (state.getLastAction().getActionType().equalsIgnoreCase("play") ||
                state.getLastAction().getActionType().equalsIgnoreCase("discard"))) {
            JSONObject action = new JSONObject();
            lastState = ordinaHand(state, state.getCurrentPlayer());
            CardList hand = lastState.getHand(state.getCurrentPlayer());
            Card c = hand.get(state.getLastAction().getCard());
            int index = getCardIndex(hand,c);
            if (state.getLastAction().getActionType().equalsIgnoreCase("play")) {
                action.put("type", "play");
                action.put("card", index);
                action.put("player", state.getCurrentPlayer());
            } else if (state.getLastAction().getActionType().equalsIgnoreCase("discard")) {
                action.put("type", "discard");
                action.put("card", index);
                action.put("player", state.getCurrentPlayer());
            }
            datastate.put("lastaction", new Action(action));
        }
        else
            datastate.put("lastaction", state.getLastAction());
         */

        //State currentState;
        for (String name: state.getPlayersNames())
        {
            //currentState=ordinaHand(state, name);
            String current_player;
            if (name.equalsIgnoreCase(state.getCurrentPlayer())){
                current_player = "current";
            }
            else{
                current_player = "other";
            }

            Analitics analitics = new Analitics(name);
            analitics.setState(state);
            List<DataCard> hand = new ArrayList<>();
            for(int i=0; i<state.getHand(name).size(); i++){
                hand.add(DataCard.createDatacardFromCard(state.getHand(name).get(i), analitics,i));
            }
            JSONArray handJson= new JSONArray();
            handJson.addAll(hand);
            datastate.put(current_player+"_hand", handJson);
            datastate.put("handentropy_"+current_player, formatter.format(analitics.getHandEntropy(name)));
        }

        datastate.put("deck",state.getDeckSize());
        return new DataState(datastate);
    }

    /**
     * COLORS:
     *  1="white", 2="blue", 3="red", 4="yellow", 5="green"
     */
    private static JSONArray discardedCards(CardList discarded){
       // List<SimpleCard> cards = new ArrayList<>();
        JSONArray disc= new JSONArray();
        //int[] discarded_list_1 = {100000, 200000, 300000, 400000, 500000};
        List<Integer> discarded_list = new ArrayList<>();
        discarded_list.add(100000);
        discarded_list.add(200000);
        discarded_list.add(300000);
        discarded_list.add(400000);
        discarded_list.add(500000);
        int index = -1;
        for(int i=0; i< discarded.size();i++ ){
            if(discarded.get(i).getColor().equalsIgnoreCase("white")){
                index=0;
                //discarded_list[index] += Math.pow(10,discarded.get(i).getValue()-1);
                discarded_list.set(index, discarded_list.get(index)+ (int) Math.pow(10,discarded.get(i).getValue()-1));
            }
            if(discarded.get(i).getColor().equalsIgnoreCase("blue")){
                index=1;
                //discarded_list[index] += Math.pow(10,discarded.get(i).getValue()-1);
                discarded_list.set(index, discarded_list.get(index)+ (int) Math.pow(10,discarded.get(i).getValue()-1));
            }
            if(discarded.get(i).getColor().equalsIgnoreCase("red")){
                index=2;
                //discarded_list[index] += Math.pow(10,discarded.get(i).getValue()-1);
                discarded_list.set(index, discarded_list.get(index)+ (int) Math.pow(10,discarded.get(i).getValue()-1));
            }
            if(discarded.get(i).getColor().equalsIgnoreCase("yellow")){
                index=3;
                //discarded_list[index] += Math.pow(10,discarded.get(i).getValue()-1);
                discarded_list.set(index, discarded_list.get(index)+ (int) Math.pow(10,discarded.get(i).getValue()-1));
            }
            if(discarded.get(i).getColor().equalsIgnoreCase("green")){
                index=4;
                //discarded_list[index] += Math.pow(10,discarded.get(i).getValue()-1);
                discarded_list.set(index, discarded_list.get(index)+ (int) Math.pow(10,discarded.get(i).getValue()-1));
            }
        }
        disc.addAll(discarded_list);

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

    private static State ordinaHand(State state, String name){
        State result = state.copy();
        CardList hand = new CardList();
        Card currentBest;
        while(hand.size() != state.getHand(name).size()) {
            currentBest = result.getHand(name).get(0);
            for (Card c : result.getHand(name)) {
                if (getColorIndex(c.getColor()) < getColorIndex(currentBest.getColor())) { //colore prioritario
                    currentBest = c;
                }
                else if (getColorIndex(c.getColor()) == getColorIndex(currentBest.getColor())){ //stesso colore
                    if(c.getValue()<currentBest.getValue()){
                        currentBest = c;
                    }
                }
            }
            hand.add(currentBest);
            result.getHand(name).remove(currentBest);
        }
        result.getHand(name).addAll(hand);
        return result;
    }

    private static int getColorIndex(String color){
        List lista = new ArrayList();
        lista.add("white");
        lista.add("blue");
        lista.add("red");
        lista.add("yellow");
        lista.add("green");
        return lista.indexOf(color.toLowerCase());
    }

    private static int getCardIndex(CardList hand, Card card){
        int result = 0;
        for(Card c : hand){
            if(c.equals(card)){
                break;
            }
            result++;
        }
        return result;
    }
}
