package model.finale;

import model.raw.RawCard;
import model.raw.RawState;
import model.utils.Colors;
import model.utils.Features;
import model.utils.Utils;
import symmetries.ColorState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;


/**
 * This class contains the parameters of the state that will be given as input to the NN
 */
public class FinalState {
    //private ArrayList<Double> state;
    final static int DIM=195;
    private double[] state;
    //private ArrayList<ColorState> colorStateOrder;
    //private ArrayList<Colors> colorOrder;
    //private ArrayList<RawCard> orderedHandCurrent, orderedHandOther;

    public FinalState(RawState rawState) {
        /*state= new ArrayList<>();

        state.add(Features.hints.ordinal(),(double) raw.getHints());
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
       // state.add((double) raw.getRound());
        for(RawCard c: raw.getOther_hand()){
            addCard(c);
        }
        for(RawCard c: raw.getCurrent_hand()){
            addCard(c);
        }
        for(Integer col: raw.getDiscarded()) {
            state.addAll(Utils.getDiscardedFromInt(col));
        }
        */


        state= new double[DIM];

        for(int i=0; i<DIM; i++){
            state[i]=0.0;
        }

        state[Features.hints.ordinal()]=(double) rawState.getHints();
        //state[Features.finalround.ordinal()]= (double) rawState.getFinalround();
        state[Features.fuse.ordinal()]=(double) rawState.getFuse();
        state[Features.deck.ordinal()]=(double) rawState.getDeck();
        state[Features.handentropy_current.ordinal()]= rawState.getHandentropy_current();
        state[Features.handentropy_other.ordinal()]= rawState.getHandentropy_other();

        for(Colors c: Colors.values()){
            state[Features.firework_color1.ordinal()+c.ordinal()]= getFirework(c, rawState);
        }

        for(int i=0; i<rawState.getOther_hand().size(); i++){
            addOtherCard(rawState.getOther_hand().get(i), i);
        }

        for(int i=0; i<rawState.getCurrent_hand().size(); i++){
            addCurrentCard(rawState.getCurrent_hand().get(i), i);
        }

        for(int i=0; i<5;i++) {
            addDiscarded(Utils.getDiscardedArrayFromInt(rawState.getDiscarded().get(i)),i);
        }
    }

    private void addOtherCard(RawCard rawCard, int i) {
        state[Features.value_other_card1.ordinal()+i]= rawCard.getValue();
        state[Features.playability_card1_other.ordinal()+i] = rawCard.getPlayability();
        state[Features.cardentropy_card1_other.ordinal()+i] = rawCard.getCardentropy();
        state[Features.uselessness_card1_other.ordinal()+i] = rawCard.getUselessness();

        for(int j=0; j<5;j++){
            state[Features.poss_card1_oth_color1.ordinal()+i*5+j] = rawCard.getPoss_colors().get(j);
            state[Features.poss_card1_oth_value1.ordinal()+i*5+j] = rawCard.getPoss_values().get(j);
        }
        int j=0;

        Features features=null;

        switch (i){
            case 0 : features=Features.color_other_card1_col1; break;
            case 1 : features=Features.color_other_card2_col1; break;
            case 2 : features=Features.color_other_card3_col1; break;
            case 3 : features=Features.color_other_card4_col1; break;
            case 4 : features=Features.color_other_card5_col1; break;
        }


        for (Colors c: Colors.values()){
            if(rawCard.getColor().equalsIgnoreCase(c.name())){
                state[features.ordinal()+j] =1;
            }else{
                state[features.ordinal()+j] =0;
            }
            j++;
        }


    }

    private void addCurrentCard(RawCard rawCard, int i) {
        //state[Features.value_current_card1.ordinal()+i]= rawCard.getValue();
        state[Features.playability_card1_current.ordinal()+i] = rawCard.getPlayability();
        state[Features.cardentropy_card1_current.ordinal()+i] = rawCard.getCardentropy();
        state[Features.uselessness_card1_current.ordinal()+i] = rawCard.getUselessness();

        for(int j=0; j<5;j++){
            state[Features.poss_card1_curr_color1.ordinal()+i*5+j] = rawCard.getPoss_colors().get(j);
            state[Features.poss_card1_curr_value1.ordinal()+i*5+j] = rawCard.getPoss_values().get(j);
        }
        /* // non serve più perchè non dobbiamo sapere i nostri colori
        int j=0;

        Features features=null;

        switch (i){
            case 0 : features=Features.color_current_card1_white; break;
            case 1 : features=Features.color_current_card2_white; break;
            case 2 : features=Features.color_current_card3_white; break;
            case 3 : features=Features.color_current_card4_white; break;
            case 4 : features=Features.color_current_card5_white; break;
        }


        for (Colors colors: this.colorOrder){
            if(rawCard.getColorEnum().equals(colors)){
                state[features.ordinal()+j] =1;
            }else{
                state[features.ordinal()+j] =0;
            }
            j++;
        }
        */
    }

    private double getFirework(Colors color, RawState rawState) {
        switch (color) {
            case RED -> {
                return rawState.getRed();
            }
            case BLUE -> {
                return  rawState.getBlue();
            }
            case GREEN -> {
                return  rawState.getGreen();
            }
            case WHITE -> {
                return  rawState.getWhite();
            }
            case YELLOW -> {
                return  rawState.getYellow();
            }
        }
        return -1.0;
    }


    public FinalState( ) {
        state= new double[DIM];
    }


    public double[] getState() {
        return state;
    }

    public void setState(double[] state) {
        this.state = state;
    }


    private void addDiscarded(double[] discardedFromInt, int i) {
        for(int j=0; j<5;j++){
            state[Features.discarded_1_color1.ordinal()+i*5+j] = discardedFromInt[j];
        }
    }



    @Override
    public String toString() {
        String result="";
        for(int i=0; i<state.length; i++){
            if(i==state.length-1)
                result += state[i];
            else
                result += state[i]+",";
        }
        return result;
    }

}
