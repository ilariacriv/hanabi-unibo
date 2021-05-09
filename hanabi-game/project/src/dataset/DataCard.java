package dataset;

import hanabi.game.Card;
import hanabi.player.Analitics;
import json.*;

import java.io.Reader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

public class DataCard extends TypedJSONObject {

    /**
     * Classe che rappresenta una carta Hanabi.<br>
     * {<br>
     *     "color" 			: colore della carta <br>
     *     "value" 			: valore della carta <br>
     *     "color_revealed" : true se il colore di questa carta &egrave; stato rivelato al possessore, false altrimenti <br>
     *     "value_revealed" : true se il valore di questa carta &egrave; stato rivelato al possessore, false altrimenti <br>
     * }
     */
    /**
     * COLORS:
     *  "white","blue","red","yellow","green"
     */




    public DataCard(Reader reader) throws JSONException {
        super(reader);
    }
    private DataCard(DataCard card)
    {
        super(card);
    }

    public DataCard(JSONObject object) throws JSONException {
        super(object);
    }

    @Override
    public DataCard copy() {
        return new DataCard(this);
    }

    public static DataCard createDatacardFromCard(Card card, Analitics analitics, int index){
        List<String> colors = new ArrayList<>();
        List<String> values = new ArrayList<>();

        JSONObject datacard = new JSONObject();
        NumberFormat formatter = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));

        datacard.put("value",card.getValue());
        datacard.put("color",card.getColor());
        datacard.put("playability",formatter.format(analitics.getPlayability(analitics.me,index)*100));
        datacard.put("uselessness",formatter.format(analitics.getUselessness(analitics.me,index)*100));


        values.add(formatter.format(analitics.getValueProbability(analitics.me,index,1)*100));
        values.add(formatter.format(analitics.getValueProbability(analitics.me,index,2)*100));
        values.add(formatter.format(analitics.getValueProbability(analitics.me,index,3)*100));
        values.add(formatter.format(analitics.getValueProbability(analitics.me,index,4)*100));
        values.add(formatter.format(analitics.getValueProbability(analitics.me,index,5)*100));

       /* colors[0]=analitics.getColorProbability(analitics.me,index,"white")*100;
        colors[1]=analitics.getColorProbability(analitics.me,index,"blue")*100;
        colors[2]=analitics.getColorProbability(analitics.me,index,"red")*100;
        colors[3]=analitics.getColorProbability(analitics.me,index,"yellow")*100;
        colors[4]=analitics.getColorProbability(analitics.me,index,"green")*100;
*/
        colors.add(formatter.format(analitics.getColorProbability(analitics.me,index,"white")*100));
        colors.add(formatter.format(analitics.getColorProbability(analitics.me,index,"blue")*100));
        colors.add(formatter.format(analitics.getColorProbability(analitics.me,index,"red")*100));
        colors.add(formatter.format(analitics.getColorProbability(analitics.me,index,"yellow")*100));
        colors.add(formatter.format(analitics.getColorProbability(analitics.me,index,"green")*100));

        JSONArray colJson= new JSONArray();
        colJson.addAll(colors);
        JSONArray valJson= new JSONArray();
        valJson.addAll(values);

        datacard.put("poss_values",valJson);
        datacard.put("poss_colors",colJson);
        datacard.put("cardentropy",formatter.format(analitics.getCardEntropy(analitics.me,index)));

        return new DataCard(datacard);
    }

    @Override
    public void verify() throws JSONException {
        //POSSIBLE COLORS
        if (!object.has("poss_colors"))
            throw new JSONException("Missing \"poss_colors\"");

        //POSSIBLE VALUES
        if (!object.has("poss_values"))
            throw new JSONException("Missing \"poss_values\"");

        //COLOR
        if (!object.has("color"))
            throw new JSONException("Missing \"color\"");
        //VALUE
        if (!object.has("value"))
            throw new JSONException("Missing \"value\"");

    }
}
