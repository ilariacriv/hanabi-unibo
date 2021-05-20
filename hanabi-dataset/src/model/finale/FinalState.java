package model.finale;

import model.raw.RawCard;
import model.raw.RawState;
import model.utils.Colors;
import model.utils.Features;
import model.utils.Utils;
import symmetries.ColorState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

//TODO fare i cast è brutto ma non ho idee migliori
//TODO forse ha più senso raggruppare le cose per colore in modo da agevolare il controllo simmetrie

public class FinalState {
    //private ArrayList<Double> state;
    final static int DIM=176;
    private double[] state;
    private ArrayList<ColorState> colorStateOrder;
    private ArrayList<Colors> colorOrder;
    private ArrayList<RawCard> orderedHandCurrent, orderedHandOther;

    public FinalState(RawState raw) {
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
            colorStateOrder.add(new ColorState(color,raw));
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

        //TODO ordinare in base ai colori di colororder

        state= new double[DIM];

        for(int i=0; i<DIM; i++){
            state[i]=0.0;
        }

        state[Features.hints.ordinal()]=(double) raw.getHints();
        state[Features.finalround.ordinal()]= (double) raw.getFinalround();
        state[Features.fuse.ordinal()]=(double) raw.getFuse();
        state[Features.deck.ordinal()]=(double) raw.getDeck();
        state[Features.handentropy_current.ordinal()]= raw.getHandentropy_current();
        state[Features.handentropy_other.ordinal()]= raw.getHandentropy_other();

        for(int i=0; i<5; i++){
            state[Features.firework_color1.ordinal()+i]= getFirework(colorStateOrder.get(i).getColor(), raw);
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

        this.orderedHandCurrent =raw.getCurrent_hand();
        this.orderedHandCurrent.sort(cardComparator);

        this.orderedHandOther=raw.getOther_hand();
        this.orderedHandOther.sort(cardComparator);


        for(RawCard c: orderedHandOther){
            addCard(c);
        }
        for(RawCard c: orderedHandCurrent){
            addCard(c);
        }
        for(Integer col: raw.getDiscarded()) {
            addDiscarded(Utils.getDiscardedListFromInt(col));
        }
    }

    private Double getFirework(Colors color, RawState rawState) {
        switch (color) {
            case RED -> {
                return Double.valueOf(rawState.getRed());
            }
            case BLUE -> {
                return Double.valueOf(rawState.getBlue());
            }
            case GREEN -> {
                return Double.valueOf(rawState.getGreen());
            }
            case WHITE -> {
                return Double.valueOf(rawState.getWhite());
            }
            case YELLOW -> {
                return Double.valueOf(rawState.getYellow());
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


    public void addCard(RawCard card) {
       /*
        this.state.add(card.getPlayability());
        addColor(card.getColor());
        this.state.add((double) card.getValue());
        this.state.add(card.getCardentropy());
        this.state.add(card.getUselessness());
        this.state.addAll(card.getPoss_values());
        this.state.addAll(card.getPoss_colors());
        */

        //TODO
    }

    public void addColor(String color){
        /*
        Double[] col = {0.0,0.0,0.0,0.0,0.0};
        switch (color){
            case "white" : col[Colors.WHITE.ordinal()]=1.0; break;
            case "blue" : col[Colors.BlUE.ordinal()]=1.0; break;
            case "red" : col[Colors.RED.ordinal()]=1.0; break;
            case "green" : col[Colors.GREEN.ordinal()]=1.0; break;
            case "yellow" : col[Colors.YELLOW.ordinal()]=1.0; break;
        }
        this.state.addAll(List.of(col));
        */
        //TODO
    }

    private void addDiscarded(Collection<Double> discardedFromInt) {
    //TODO

    }



    @Override
    public String toString() {
        //TODO in base al formato che decidiamo
        return "";
    }

}
