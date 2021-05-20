package symmetries;

import model.raw.RawState;
import model.utils.Colors;
import model.utils.Features;

import java.util.*;

/**
 * La mappa contiene solo le carte di questo colore e la loro lista di valori
 * Probabilmente però non va bene
 */
public class ColorState {
    private Colors color;
    private Double sum;
    private Double[] discarded = new Double[5];
    private TreeMap<Double, Double[]> cards = new TreeMap<>();


    public ColorState(Colors color, RawState rawstate){
        this.color=color;

        //TODO
    }

    




}
