package hanabi.gui;

import hanabi.game.State;

import javax.swing.*;
import java.awt.*;

/*
public class InfoPanel extends RefreshablePanel<State>
{
	JLabel fuse,hints,deck,turn;
	DiscardedCardsGUI discarded;
	public InfoPanel()
	{
		super("info");

		fuse = new JLabel();
		hints = new JLabel();
		deck = new JLabel();
		turn = new JLabel();
		discarded = new DiscardedCardsGUI();
	}

	@Override
	protected void afterChildrenInit() {

	}

	@Override
	protected void afterChildrenRefresh(State model) {

	}

	@Override
	protected void beforeChildrenInit() {
		setBackground(null);
		setLayout(new GridLayout(5,1));
		add(fuse);
		add(hints);
		add(discarded);
		add(deck);
		add(turn);
	}

	@Override
	protected void beforeChildrenRefresh(State model) {
		discarded.setModel(model.getDiscarded());
		deck.setText("Mazzo: "+model.getDeckSize());
		fuse.setText("Gettoni errore: "+model.getFuseTokens());
		hints.setText("Gettoni suggerimenti: "+model.getHintTokens());
		turn.setText("Turno: "+model.getRound());
	}

}

 */
public class InfoPanel extends JPanel
{
	private JLabel fuse,hints,deck,turn;
	private DiscardedCardsGUI discarded;
	private Board board;
	public InfoPanel(Board board)
	{
		super();

		this.board = board;

		fuse = new JLabel();
		hints = new JLabel();
		deck = new JLabel();
		turn = new JLabel();
		discarded = new DiscardedCardsGUI();

		setBackground(null);
		setLayout(new GridLayout(5,1));
		add(fuse);
		add(hints);
		add(discarded);
		add(deck);
		add(turn);
	}

	public void setModel(State model) {
		discarded.setModel(model.getDiscarded());
		deck.setText("Mazzo: "+model.getDeckSize());
		fuse.setText("Gettoni errore: "+model.getFuseTokens());
		hints.setText("Gettoni suggerimenti: "+model.getHintTokens());
		turn.setText("Turno: "+model.getRound()+"/"+board.history.size());
	}

}