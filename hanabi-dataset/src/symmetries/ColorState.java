package symmetries;

import model.finale.FinalState;
import model.raw.RawState;
import model.utils.Colors;
import model.utils.Features;
import model.utils.Utils;

import java.util.*;

/**
 * cards contiene il valore della carta in posizione i se è di colore "color"
 * e il valore poss_col della carta
 */
public class ColorState {
    private Colors color=null;
    private int colorindex=-1;
    private double sum;
    private int firework;
    private double[] discarded = new double[5];
    private double[][] cards_curr = new double[5][2];
    private double[][] cards_oth = new double[5][2];


    public ColorState(Colors color, RawState rawState){
        this.color=color;
        this.sum=0.0;
        discarded= Utils.getDiscardedArrayFromInt(rawState.getDiscarded().get(color.ordinal()));

        switch (color){
            case RED -> {
                this.firework=rawState.getRed();
                break;
            }
            case BlUE -> {
                this.firework=rawState.getBlue();
                break;
            }
            case GREEN -> {
                this.firework=rawState.getGreen();
                break;
            }
            case WHITE -> {
                this.firework=rawState.getWhite();
                break;
            }
            case YELLOW -> {
                this.firework=rawState.getYellow();
                break;
            }
        }
        this.sum+=firework;

        for(int i=0; i<5; i++){
            if(rawState.getCurrent_hand().get(i).getColor().equalsIgnoreCase(color.toString())){
                cards_curr[i][0]= Double.valueOf(rawState.getCurrent_hand().get(i).getValue());
            }else{
                cards_curr[i][0]=0.0;
            }
            if(rawState.getOther_hand().get(i).getColor().equalsIgnoreCase(color.toString())){
                cards_oth[i][0]= Double.valueOf(rawState.getOther_hand().get(i).getValue());
            }else{
                cards_oth[i][0]=0.0;
            }
            cards_oth[i][1]=rawState.getOther_hand().get(i).getPoss_colors().get(color.ordinal());
            cards_curr[i][1]=rawState.getCurrent_hand().get(i).getPoss_colors().get(color.ordinal());
            this.sum+=discarded[i]+cards_curr[i][1]+cards_oth[i][1];
        }
    }

    public ColorState(int colorindex, FinalState finalState){
        this.colorindex=colorindex;

        //TODO in base a come stampiamo le cose
    }


    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public int getColorindex() {
        return colorindex;
    }

    public void setColorindex(int colorindex) {
        this.colorindex = colorindex;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public int getFirework() {
        return firework;
    }

    public void setFirework(int firework) {
        this.firework = firework;
    }

    public double[] getDiscarded() {
        return discarded;
    }

    public double getDiscarded(int i) {
        return discarded[i];
    }

    public void setDiscarded(double[] discarded) {
        this.discarded = discarded;
    }

    public double[][] getCards_curr() {
        return cards_curr;
    }

    public void setCards_curr(double[][] cards_curr) {
        this.cards_curr = cards_curr;
    }

    public double[][] getCards_oth() {
        return cards_oth;
    }

    public void setCards_oth(double[][] cards_oth) {
        this.cards_oth = cards_oth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorState that = (ColorState) o;
        return firework == that.firework && sum == that.sum && Arrays.equals(discarded, that.discarded)
                && Arrays.equals(cards_curr, that.cards_curr) && Arrays.equals(cards_oth, that.cards_oth);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sum, firework);
        result = 31 * result + Arrays.hashCode(discarded);
        result = 31 * result + Arrays.hashCode(cards_curr);
        result = 31 * result + Arrays.hashCode(cards_oth);
        return result;
    }
}
