package symmetries;

import model.finale.FinalState;
import model.raw.RawState;
import model.utils.Colors;
import model.utils.Utils;

import java.util.*;

/**
 * Classe che rappresenta lo stato di un colore indipendentemente dal colore stesso (che è parametrico)
 * è un sottoinsieme di FinalState, sono tutte le info di FinalState di un particolare colore
 *
 * cards contiene il valore della carta in posizione i se è di colore "color" [i][0]
 * e il valore poss_col della carta [i][1]
 * es:
 * [0,4,0,5,0]
 * [20,100,0,20,0]
 */
public class ColorState {
    private Colors color = null;
    private int colorindex = -1;
    private int firework;
    private double[] discarded = new double[5];
    private double[][] cards_current = new double[5][2];
    private double[][] cards_other = new double[5][2];
    private double[] orderedPossColors_other = new double[5];
    private double[] orderedPossColors_current = new double[5];

    public ColorState(Colors color, RawState rawState) {
        this.color = color;
        discarded = Utils.getDiscardedArrayFromInt(rawState.getDiscarded().get(color.ordinal()));

        switch (color) {
            case RED -> {
                this.firework = rawState.getRed();
                break;
            }
            case BLUE -> {
                this.firework = rawState.getBlue();
                break;
            }
            case GREEN -> {
                this.firework = rawState.getGreen();
                break;
            }
            case WHITE -> {
                this.firework = rawState.getWhite();
                break;
            }
            case YELLOW -> {
                this.firework = rawState.getYellow();
                break;
            }
        }

        for (int i = 0; i < rawState.getCurrent_hand().size(); i++) {
            if (rawState.getCurrent_hand().get(i).getColor().equalsIgnoreCase(color.toString())) {
                cards_current[i][0] = Double.valueOf(rawState.getCurrent_hand().get(i).getValue());
            } else {
                cards_current[i][0] = 0.0;
            }
            cards_current[i][1] = rawState.getCurrent_hand().get(i).getPoss_colors().get(color.ordinal());
            orderedPossColors_current[i] = cards_current[i][1];
        }

        for (int i = 0; i < rawState.getOther_hand().size(); i++) {
            if (rawState.getOther_hand().get(i).getColor().equalsIgnoreCase(color.toString())) {
                cards_other[i][0] = Double.valueOf(rawState.getOther_hand().get(i).getValue());
            } else {
                cards_other[i][0] = 0.0;
            }
            cards_other[i][1] = rawState.getOther_hand().get(i).getPoss_colors().get(color.ordinal());
            orderedPossColors_other[i] = cards_other[i][1];
        }

        Arrays.sort(this.orderedPossColors_other);
        Arrays.sort(this.orderedPossColors_current);
    }

    /**
     * Probabilmente non serve..
     *
     * @param colorindex
     * @param finalState
     */
    public ColorState(int colorindex, FinalState finalState) {
        this.colorindex = colorindex;

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

    //conta il numero di volte che il numero "num" è contenuto nella mano dell'altro
    public int getOtherHandCountNumber(int num) {
        int result = 0;
        for (int i = 0; i < 5; i++) {
            if (num == this.cards_other[i][0]) {
                result++;
            }
        }
        return result;
    }

    //ordinamento crescente per possibilità di ogni carta di essere di questo colore
    public double getPossibilityColorOrderedOther(int index) {
        return this.orderedPossColors_other[index];
    }

    public double getPossibilityColorOrderedCurrent(int index) {
        return this.orderedPossColors_current[index];
    }


    public void setDiscarded(double[] discarded) {
        this.discarded = discarded;
    }

    public double[][] getCards_current() {
        return cards_current;
    }

    public void setCards_current(double[][] cards_current) {
        this.cards_current = cards_current;
    }

    public double[][] getCards_other() {
        return cards_other;
    }

    public void setCards_other(double[][] cards_other) {
        this.cards_other = cards_other;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorState that = (ColorState) o;
        return colorindex == that.colorindex && firework == that.firework && color == that.color && Arrays.equals(discarded, that.discarded) && Arrays.equals(cards_current, that.cards_current) && Arrays.equals(cards_other, that.cards_other) && Arrays.equals(orderedPossColors_other, that.orderedPossColors_other) && Arrays.equals(orderedPossColors_current, that.orderedPossColors_current);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(color, colorindex, firework);
        result = 31 * result + Arrays.hashCode(discarded);
        result = 31 * result + Arrays.hashCode(cards_current);
        result = 31 * result + Arrays.hashCode(cards_other);
        result = 31 * result + Arrays.hashCode(orderedPossColors_other);
        result = 31 * result + Arrays.hashCode(orderedPossColors_current);
        return result;
    }
}
