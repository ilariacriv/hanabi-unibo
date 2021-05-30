package model.finale;

import model.raw.RawCard;
import model.raw.RawState;
import model.utils.Colors;
import model.utils.Features;
import model.utils.Utils;
import symmetries.ColorState;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * This class contains the parameters of the state that will be given as input to the NN
 */
public class FinalState {
    //private ArrayList<Double> state;
    final static int DIM=176;
    private double[] state;
    private ArrayList<ColorState> colorStateOrder;
    private ArrayList<Colors> colorOrder;
    private ArrayList<RawCard> orderedHandCurrent, orderedHandOther;

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
        colorStateOrder = new ArrayList<>();
        colorOrder = new ArrayList<>();

        for(Colors color : Colors.values()){
            colorStateOrder.add(new ColorState(color,rawState));
        }
        Comparator<ColorState> csComparator = (Comparator.comparing( ( ColorState cs) -> cs.getSum()))
                .thenComparing(cs2 -> cs2.getFirework())
                .thenComparing(cs3 -> cs3.getDiscarded(0))
                .thenComparing(cs4 -> cs4.getDiscarded(1))
                .thenComparing(cs5 -> cs5.getDiscarded(2))
                .thenComparing(cs6 -> cs6.getDiscarded(3))
                .thenComparing(cs7 -> cs7.getDiscarded(4));

        //TODO va bene questo comparator???? Probabilmente funziona nella maggior Parte dei casi ma sicuramente ci sono alcuni che non vengono ordinati correttamente

        colorStateOrder.sort(csComparator);

        for(ColorState cs:colorStateOrder){
            colorOrder.add(cs.getColor());
        }

        state= new double[DIM];

        for(int i=0; i<DIM; i++){
            state[i]=0.0;
        }

        state[Features.hints.ordinal()]=(double) rawState.getHints();
        state[Features.finalround.ordinal()]= (double) rawState.getFinalround();
        state[Features.fuse.ordinal()]=(double) rawState.getFuse();
        state[Features.deck.ordinal()]=(double) rawState.getDeck();
        state[Features.handentropy_current.ordinal()]= rawState.getHandentropy_current();
        state[Features.handentropy_other.ordinal()]= rawState.getHandentropy_other();

        for(int i=0; i<5; i++){
            state[Features.firework_color1.ordinal()+i]= getFirework(colorStateOrder.get(i).getColor(), rawState);
        }

        Comparator<RawCard> cardComparator = new Comparator<RawCard>() {
            @Override
            public int compare(RawCard o1, RawCard o2) {
               int index1=-1,index2=-1;
               for (int i=0; i<5;i++){
                   if(colorOrder.get(i).equals(o1.getColorEnum())) index1=i;
                   if(colorOrder.get(i).equals(o2.getColorEnum())) index2=i;
               }
               return Integer.compare(index1,index2);
            }
        }.thenComparing(RawCard::getValue);

        this.orderedHandCurrent =rawState.getCurrent_hand();
        this.orderedHandCurrent.sort(cardComparator);

        this.orderedHandOther=rawState.getOther_hand();
        this.orderedHandOther.sort(cardComparator);

        for(int i=0; i<orderedHandOther.size(); i++){
            addOtherCard(orderedHandOther.get(i), i);
        }

        for(int i=0; i<orderedHandCurrent.size(); i++){
            addCurrentCard(orderedHandCurrent.get(i), i);
        }

        for(int i=0; i<5;i++) {
            Colors color = colorOrder.get(i);
            addDiscarded(Utils.getDiscardedArrayFromInt(rawState.getDiscarded().get(color.ordinal())),i);
        }
    }

    private void addOtherCard(RawCard rawCard, int i) {
        state[Features.value_other_card1.ordinal()+i]= rawCard.getValue();
        state[Features.playability_card1_other.ordinal()+i] = rawCard.getPlayability();
        state[Features.cardentropy_card1_other.ordinal()+i] = rawCard.getCardentropy();
        state[Features.uselessness_card1_other.ordinal()+i] = rawCard.getUselessness();

        for(int j=0; j<5;j++){
            int colorindex = this.getColorOrder().get(j).ordinal();
            state[Features.poss_card1_oth_color1.ordinal()+i*5+j] = rawCard.getPoss_colors().get(colorindex);
        }
        int j=0;

        Features features=null;

        switch (i){
            case 0 : features=Features.color_other_card1_white; break;
            case 1 : features=Features.color_other_card2_white; break;
            case 2 : features=Features.color_other_card3_white; break;
            case 3 : features=Features.color_other_card4_white; break;
            case 4 : features=Features.color_other_card5_white; break;
        }


        for (Colors colors: this.colorOrder){
            if(rawCard.getColorEnum().equals(colors)){
                state[features.ordinal()+j] =1;
            }else{
                state[features.ordinal()+j] =0;
            }
            j++;
        }


    }

    private void addCurrentCard(RawCard rawCard, int i) {
        state[Features.value_current_card1.ordinal()+i]= rawCard.getValue();
        state[Features.playability_card1_current.ordinal()+i] = rawCard.getPlayability();
        state[Features.cardentropy_card1_current.ordinal()+i] = rawCard.getCardentropy();
        state[Features.uselessness_card1_current.ordinal()+i] = rawCard.getUselessness();

        for(int j=0; j<5;j++){
            int colorindex = this.getColorOrder().get(j).ordinal();
            state[Features.poss_card1_curr_color1.ordinal()+i*5+j] = rawCard.getPoss_colors().get(colorindex);
        }
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

    public ArrayList<Colors> getColorOrder() {
        return colorOrder;
    }

    public void setColorOrder(ArrayList<Colors> colorOrder) {
        this.colorOrder = colorOrder;
    }

    public ArrayList<RawCard> getOrderedHandCurrent() {
        return orderedHandCurrent;
    }

    public void setOrderedHandCurrent(ArrayList<RawCard> orderedHandCurrent) {
        this.orderedHandCurrent = orderedHandCurrent;
    }

    public ArrayList<RawCard> getOrderedHandOther() {
        return orderedHandOther;
    }

    public void setOrderedHandOther(ArrayList<RawCard> orderedHandOther) {
        this.orderedHandOther = orderedHandOther;
    }



    public ArrayList<ColorState> getColorStateOrder() {
        return colorStateOrder;
    }

    public void setColorStateOrder(ArrayList<ColorState> colorStateOrder) {
        this.colorStateOrder = colorStateOrder;
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
