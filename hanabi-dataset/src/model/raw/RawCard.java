package model.raw;

import model.utils.Colors;

import java.util.List;
import java.util.*;

/**
 * Classe che rappresenta una carta Hanabi.<br>
 * {<br>
 *     "color" 			: colore della carta <br>
 *     "value" 			: valore della carta <br>
 *     "color_revealed" : true se il colore di questa carta &egrave; stato rivelato al possessore, false altrimenti <br>
 *     "value_revealed" : true se il valore di questa carta &egrave; stato rivelato al possessore, false altrimenti <br>
 * }
 */
public class RawCard
{

	private List<Double> poss_colors = new ArrayList<>();
	private List<Double> poss_values = new ArrayList<>();
	private String color;
	private Colors colorEnum;
	private int value;
	private double playability,cardentropy,uselessness;

	private RawCard(RawCard card)
	{

	}

	public List<Double> getPoss_colors() {
		return poss_colors;
	}

	public void setPoss_colors(List<Double> poss_colors) {
		this.poss_colors = poss_colors;
	}

	public List<Double> getPoss_values() {
		return poss_values;
	}

	public void setPoss_values(List<Double> poss_values) {
		this.poss_values = poss_values;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public double getPlayability() {
		return playability;
	}

	public void setPlayability(double playability) {
		this.playability = playability;
	}

	public double getCardentropy() {
		return cardentropy;
	}

	public void setCardentropy(double cardentropy) {
		this.cardentropy = cardentropy;
	}

	public double getUselessness() {
		return uselessness;
	}

	public void setUselessness(double uselessness) {
		this.uselessness = uselessness;
	}

	public Colors getColorEnum() {
		return colorEnum;
	}

	public void setColorEnum() {
		this.colorEnum = Colors.valueOf(this.color.toLowerCase(Locale.ROOT));
	}
}
