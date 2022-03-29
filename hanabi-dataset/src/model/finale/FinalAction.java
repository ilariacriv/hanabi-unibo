package model.finale;

import model.raw.RawAction;
import model.raw.RawCard;
import model.raw.RawState;
import model.utils.ActionCode;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This class contains the parameters of the action that will be given as input to the NN
 */
public class FinalAction {
    private int[] actions;
    private FinalState finalState;
    private RawState rawState;

    public FinalAction(RawAction rawAction, FinalState finalState, RawState rawState){
        actions = new int[20];
        for(int i=0; i<20; i++) actions[i] = 0;
        this.finalState = finalState;
        this.rawState = rawState;
        this.mapAction(rawAction);
    }

    private void mapAction(RawAction rawAction){

        ArrayList<RawCard> currentHand = finalState.getOrderedHandCurrent();
        ArrayList<RawCard> oldCurrentHand = rawState.getCurrent_hand();
        RawCard card = oldCurrentHand.get(rawAction.getCard());

        int index = currentHand.indexOf(card);

        if(rawAction.getType().equalsIgnoreCase("discard")){
            actions[ActionCode.valueOf("DISCARD_1st").ordinal()+index]=1;
        }
        if(rawAction.getType().equalsIgnoreCase("play")){
            actions[ActionCode.valueOf("PLAY_1st").ordinal()+index]=1;
        }
        if(rawAction.getType().equalsIgnoreCase("hintvalue")){
            actions[ActionCode.valueOf("HINT_VALUE_"+rawAction.getValue()).ordinal()]=1;
        }
        if(rawAction.getType().equalsIgnoreCase("hintcolor")){
            int colorIndex = this.finalState.getIndexFromColor(rawAction.getColor());
            actions[ActionCode.valueOf("HINT_COLOR_1").ordinal()+colorIndex]=1;
        }

    }

    @Override
    public String toString() {
        String result="";
        for(int i=0; i<actions.length; i++){
            if(i== actions.length-1)
                result += actions[i];
            else
                result += actions[i]+",";
        }
        return result;
    }


}
