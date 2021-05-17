package model;

import java.util.ArrayList;

public class RawState {
    /**
     * Rappresenta lo stato di una partita.
     * {<br>
     *      "discarded"	: lista di carte scartate (CardList)</br>
     *      "current" 	: nome del giocatore cui tocca giocare (string)</br>
     *      "round" 	: numero di turno corrente (int) <br>
     *      "fuse" 		: numero di fuse tokens rimasti (int) <br>
     *      "hints"		: numero di hints tokens rimasti (int) <br>
     *		"finalround": numero del turno finale se conosciuto, -1 altrimenti (int) <br>
     *		"deck"		: numero di carte rimaste nel mazzo (int) <br>
     *		"red"		: numero di carte nel firework red (int) <br>
     *		"blue"		: numero di carte nel firework blue (int) <br>
     *		"yellow"	: numero di carte nel firework yellow (int) <br>
     *		"white"		: numero di carte nel firework white (int) <br>
     *		"green"		: numero di carte nel firework green (int) <br>
     *		"lastaction": ultima azione eseguita (Action)<br>
     *		"player"	: lista di carte nella mano del giocatore "player" (CardList) <br>
     *  * }
     */

    private ArrayList<Card> discarded;
    private String current;
    private int round,fuse,hints,finalround,deck,red,blue,yellow,white,green,player;

    public RawState(){}

    public ArrayList<Card> getDiscarded() {
        return discarded;
    }

    public void setDiscarded(ArrayList<Card> discarded) {
        this.discarded = discarded;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getFuse() {
        return fuse;
    }

    public void setFuse(int fuse) {
        this.fuse = fuse;
    }

    public int getHints() {
        return hints;
    }

    public void setHints(int hints) {
        this.hints = hints;
    }

    public int getFinalround() {
        return finalround;
    }

    public void setFinalround(int finalround) {
        this.finalround = finalround;
    }

    public int getDeck() {
        return deck;
    }

    public void setDeck(int deck) {
        this.deck = deck;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getYellow() {
        return yellow;
    }

    public void setYellow(int yellow) {
        this.yellow = yellow;
    }

    public int getWhite() {
        return white;
    }

    public void setWhite(int white) {
        this.white = white;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
