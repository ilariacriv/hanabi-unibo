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

	private List<Double> possible_colors = new ArrayList<>();
	private List<Double> possible_values = new ArrayList<>();
	private String color;
	private Colors colorEnum;
	private int value;
	private double playability,cardentropy,uselessness;

	private RawCard(RawCard card)
	{

	}

	public List<Double> getPossible_colors() {
		return possible_colors;
	}

	public void setPossible_colors(List<Double> possible_colors) {
		this.possible_colors = possible_colors;
	}

	public List<Double> getPossible_values() {
		return possible_values;
	}

	public void setPossible_values(List<Double> possible_values) {
		this.possible_values = possible_values;
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
		this.colorEnum = Colors.valueOf(this.color.toUpperCase(Locale.ROOT));
	}
}
