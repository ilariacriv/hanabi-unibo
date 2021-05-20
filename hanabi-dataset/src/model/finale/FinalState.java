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
import java.util.List;

//TODO fare i cast è brutto ma non ho idee migliori
//TODO forse ha più senso raggruppare le cose per colore in modo da agevolare il controllo simmetrie

public class FinalState {
    //private ArrayList<Double> state;
    final static int DIM=176;
    private Double[] state;
    private ArrayList<ColorState> colorOrder;

    public FinalState( RawState raw) {
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

        for(Colors color : Colors.values()){
            colorOrder.add(new ColorState(color,raw));
        }
        Comparator<ColorState> csComparator = (Comparator.comparing( ( ColorState cs) -> cs.getSum()))
                .thenComparing(cs2 -> cs2.getFirework())
                .thenComparing(cs3 -> cs3.getDiscarded(0))
                .thenComparing(cs4 -> cs4.getDiscarded(1))
                .thenComparing(cs5 -> cs5.getDiscarded(2))
                .thenComparing(cs6 -> cs6.getDiscarded(3))
                .thenComparing(cs7 -> cs7.getDiscarded(4));

        //TODO va bene questo comparator???? Probabilmente funziona nella maggior Parte dei casi ma sicuramente ci sono alcuni che non vengono ordinati correttamente

        colorOrder.sort(csComparator);

        //TODO ordinare in base ai colori di colororder

        state= new Double[DIM];

        for(int i=0; i<DIM; i++){
            state[i]=0.0;
        }

        state[Features.hints.ordinal()]=(double) raw.getHints();
        state[Features.finalround.ordinal()]= (double) raw.getFinalround();
        state[Features.fuse.ordinal()]=(double) raw.getFuse();
        state[Features.deck.ordinal()]=(double) raw.getDeck();
        state[Features.firework_red.ordinal()] = (double) raw.getRed();
        state[Features.firework_blue.ordinal()] = (double) raw.getBlue();
        state[Features.firework_yellow.ordinal()] = (double) raw.getYellow();
        state[Features.firework_white.ordinal()] = (double) raw.getWhite();
        state[Features.firework_green.ordinal()] = (double) raw.getGreen();

        for(RawCard c: raw.getOther_hand()){
            addCard(c);
        }
        for(RawCard c: raw.getCurrent_hand()){
            addCard(c);
        }
        for(Integer col: raw.getDiscarded()) {
            addDiscarded(Utils.getDiscardedListFromInt(col));
        }
    }



    public FinalState( ) {
     state= new Double[DIM];
    }

    public List<Double> getStateList() {
        return List.of(state);
    }

    public void setStateList(ArrayList<Double> state) {
        this.state = (Double[]) state.toArray();
    }

    public Double[] getState() {
        return state;
    }

    public void setState(Double[] state) {
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
