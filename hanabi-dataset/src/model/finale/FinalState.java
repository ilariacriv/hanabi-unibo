package model.finale;

import model.utils.Colors;
import model.raw.RawCard;
import model.raw.RawState;
import model.utils.Utils;

import java.util.ArrayList;
import java.util.List;

//TODO fare i cast è brutto ma non ho idee migliori
//TODO forse ha più senso raggruppare le cose per colore in modo da agevolare il controllo simmetrie

public class FinalState {
    private ArrayList<Double> state;

    public FinalState( RawState raw) {
        state= new ArrayList<>();

        state.add((double) raw.getHints());
        state.add((double) raw.getFinalround());
        state.add((double) raw.getWhite());
        state.add((double) raw.getBlue());
        state.add((double) raw.getRed());
        state.add((double) raw.getYellow());
        state.add((double) raw.getGreen());
        state.add((double) raw.getDeck());
        state.add(raw.getHandentropy_other());
        state.add(raw.getHandentropy_current());
        state.add((double) raw.getFuse());
        state.add((double) raw.getRound());
        for(RawCard c: raw.getOther_hand()){
            addCard(c);
        }
        for(RawCard c: raw.getCurrent_hand()){
            addCard(c);
        }
        for(Integer col: raw.getDiscarded()) {
            state.addAll(Utils.getDiscardedFromInt(col));
        }
    }

    public ArrayList<Double> getState() {
        return state;
    }

    public void setState(ArrayList<Double> state) {
        this.state = state;
    }

    public void add(Double val) {
        this.state.add(val);
    }

    public void addCard(RawCard card) {
        this.state.add(card.getPlayability());
        addColor(card.getColor());
        this.state.add((double) card.getValue());
        this.state.add(card.getCardentropy());
        this.state.add(card.getUselessness());
        this.state.addAll(card.getPoss_values());
        this.state.addAll(card.getPoss_colors());
    }

    public void addColor(String color){
        Double[] col = {0.0,0.0,0.0,0.0,0.0};
        switch (color){
            case "white" : col[Colors.WHITE.ordinal()]=1.0; break;
            case "blue" : col[Colors.BlUE.ordinal()]=1.0; break;
            case "red" : col[Colors.RED.ordinal()]=1.0; break;
            case "green" : col[Colors.GREEN.ordinal()]=1.0; break;
            case "yellow" : col[Colors.YELLOW.ordinal()]=1.0; break;
        }
        this.state.addAll(List.of(col));
    }


    @Override
    public String toString() {
        //TODO in base al formato che decidiamo
        return "";
    }

}
