package model.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {

    public static Collection<Double> getDiscardedListFromInt(int discarded){
        List<Double> result = new ArrayList<>();
        //es: 410010 = per il colore 4, un 5 scartato e un 2 scartato
        String s_discarded = String.valueOf(discarded);
        result.add((double)s_discarded.charAt(1)); //5 scartati
        result.add((double)s_discarded.charAt(2)); //4 scartati
        result.add((double)s_discarded.charAt(3)); //3 scartati
        result.add((double)s_discarded.charAt(4)); //2 scartati
        result.add((double)s_discarded.charAt(5)); //1 scartati

        return result;
    }

    public static double[] getDiscardedArrayFromInt(int discarded){
        double[] result = new double[5];
        //es: 410010 = per il colore 4, un 5 scartato e un 2 scartato
        String s_discarded = String.valueOf(discarded);
        result[0] = s_discarded.charAt(1);
        result[1] = s_discarded.charAt(2);
        result[2] = s_discarded.charAt(3);
        result[3] = s_discarded.charAt(4);
        result[4] = s_discarded.charAt(5);
        return result;
    }

}
