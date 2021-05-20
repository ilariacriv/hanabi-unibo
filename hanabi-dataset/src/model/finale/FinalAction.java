package model.finale;

import model.raw.RawAction;
import model.raw.RawCard;
import model.raw.RawState;
import model.utils.ActionCode;

import java.util.ArrayList;
import java.util.Locale;

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

    private void mapAction(RawAction raw){

        ArrayList<RawCard> currentHand = finalState.getOrderedHandCurrent();
        ArrayList<RawCard> oldCurrentHand = rawState.getCurrent_hand();
        RawCard card = oldCurrentHand.get(raw.getCard());
        int index = currentHand.indexOf(card);

        if(raw.getType().equalsIgnoreCase("discard")){
            actions[ActionCode.valueOf("DISCARD_1st").ordinal()+index]=1;
        }
        if(raw.getType().equalsIgnoreCase("play")){
            actions[ActionCode.valueOf("PLAY_1st").ordinal()+index]=1;
        }
        if(raw.getType().equalsIgnoreCase("hintvalue")){
            actions[ActionCode.valueOf("HINT_VALUE_"+raw.getValue()).ordinal()]=1;
        }
        if(raw.getType().equalsIgnoreCase("hintcolor")){
            actions[ActionCode.valueOf("HINT_"+raw.getColor().toUpperCase()).ordinal()]=1;
        }
    }

    @Override
    public String toString() {
        //TODO in base al formato che decidiamo
        return "";
    }


}
