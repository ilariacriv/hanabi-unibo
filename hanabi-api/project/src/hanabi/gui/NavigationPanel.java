package hanabi.gui;

import hanabi.game.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavigationPanel extends JPanel implements ActionListener, StateShownListener {

	private JButton avanti = new JButton("Turno successivo");
	private JButton indietro = new JButton("Turno precedente");
	private JButton primo = new JButton("Turno iniziale");
	private JButton corrente = new JButton("Turno corrente");

	private Board board;

	@Override
	public void onNewState(State newstate) {

	}

	public NavigationPanel(Board board)
	{
		this.board = board;
		board.addStateShownListener(this);
		this.setLayout(new GridLayout(2, 2));
		this.setBorder(BorderFactory.createTitledBorder("Navigazione"));
		this.setBackground(null);
		this.add(indietro);
		this.add(avanti);
		this.add(primo);
		this.add(corrente);

		avanti.addActionListener(this);
		indietro.addActionListener(this);
		primo.addActionListener(this);
		corrente.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(indietro))
			board.showState(board.shownTurn-1);
		else if (e.getSource().equals(avanti))
			board.showState(board.shownTurn+1);
		else if (e.getSource().equals(primo))
			board.showState(1);
		else if (e.getSource().equals(corrente))
			board.showState(board.history.size());
	}

	@Override
	public void onChange(int stateround)
	{
		if (stateround == board.history.size())
		{
			corrente.setEnabled(false);
			avanti.setEnabled(false);
			if (stateround == 1)
			{
				indietro.setEnabled(false);
				primo.setEnabled(false);
			}
			else {
				indietro.setEnabled(true);
				primo.setEnabled(true);
			}
		}
		else if (stateround == 1)
		{
			indietro.setEnabled(false);
			primo.setEnabled(false);
			corrente.setEnabled(true);
			avanti.setEnabled(true);
		}
		else
		{
			indietro.setEnabled(true);
			primo.setEnabled(true);
			corrente.setEnabled(true);
			avanti.setEnabled(true);
		}
	}
}
