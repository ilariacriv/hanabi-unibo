package hanabi.human;

import hanabi.game.Action;
import hanabi.game.State;
import hanabi.gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Keyboard extends JPanel implements ActionListener, SelectedListener, StateListener, StateShownListener
{
	private JButton play;
	private JButton discard;
	private JButton hintc;
	private JButton hintv;
	private String player;

	private CardGUI selectedCard = null;
	private State currentState = null;

	private Object actionMonitor = new Object();
	private Action action;

	private Board board;

	public Keyboard(String player, Board board)
	{
		super();
		this.board = board;
		this.player = player;

		play = new JButton("Gioca");
		discard = new JButton("Scarta");
		hintc = new JButton("Suggerisci le carte con stesso colore");
		hintv = new JButton("Suggerisci le carte con stesso valore");

		play.addActionListener(this);
		discard.addActionListener(this);
		hintc.addActionListener(this);
		hintv.addActionListener(this);

		this.setLayout(new GridLayout(2,2));
		this.add(play);
		this.add(discard);
		this.add(hintc);
		this.add(hintv);

		board.addStateShownListener(this);
	}

	private void refresh()
	{
		if (board.history.size() == board.shownTurn && currentState.getCurrentPlayer().equals(player) && selectedCard!=null)
		{
			if (selectedCard.getOwner().getPlayer().equals(player))
			{
				play.setEnabled(true);
				discard.setEnabled(true);
				hintc.setEnabled(false);
				hintv.setEnabled(false);
			}
			else
			{
				play.setEnabled(false);
				discard.setEnabled(false);
				hintc.setEnabled(true);
				hintv.setEnabled(true);
			}
		}
		else
		{
			play.setEnabled(false);
			discard.setEnabled(false);
			hintc.setEnabled(false);
			hintv.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		play.setEnabled(false);
		discard.setEnabled(false);
		hintc.setEnabled(false);
		hintv.setEnabled(false);

		if (e.getSource().equals(play))
			action = Action.createPlayAction(player,selectedCard.getIndex());
		else if (e.getSource().equals(discard))
			action = Action.createDiscardAction(player,selectedCard.getIndex());
		else if (e.getSource().equals(hintc))
			action = Action.createHintColorAction(player,selectedCard.getOwner().getPlayer(),selectedCard.getModel().getColor());
		else if (e.getSource().equals(hintv))
			action = Action.createHintValueAction(player,selectedCard.getOwner().getPlayer(),selectedCard.getModel().getValue());

		synchronized (actionMonitor)
		{
			actionMonitor.notifyAll();
		}
	}

	@Override
	public void onNewSelection(CardGUI card)
	{
		selectedCard = card;
		refresh();
	}

	@Override
	public void onNewState(State state)
	{
		currentState = state;
		refresh();
	}

	public Action waitForAction()
	{
		synchronized (actionMonitor)
		{
			try
			{
				actionMonitor.wait();
			}
			catch (InterruptedException e)
			{
				System.exit(1);
			}
		}

//		board.log(action.getActionType());

		return action;
	}

	@Override
	public void onChange(int stateround) {
		refresh();
	}
}
