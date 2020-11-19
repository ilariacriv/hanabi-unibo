package hanabi.gui;

import hanabi.game.State;
import hanabi.player.Analitics;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailPanel extends JPanel implements SelectedListener,StateShownListener
{
	private JLabel playability;
	private JLabel uselessness;
	private JLabel one;
	private JLabel two;
	private JLabel three;
	private JLabel four;
	private JLabel five;
	private JLabel green;
	private JLabel blue;
	private JLabel white;
	private JLabel yellow;
	private JLabel red;
	private JLabel cardentropy;
	private JLabel handentropy;

	private ArrayList<State> history = new ArrayList<>();
	private Analitics analitics;

	private Board board;

	public DetailPanel(Board board, String currentPlayer)
	{
		this.board = board;
		analitics = new Analitics(currentPlayer);

		board.addSelectedListener(this);
		board.addStateShownListener(this);
		board.addStateListener(this);
		playability = new JLabel("");
		uselessness = new JLabel("");
		one = new JLabel("");
		two = new JLabel("");
		three = new JLabel("");
		four = new JLabel("");
		five = new JLabel("");
		green = new JLabel("");
		blue = new JLabel("");
		white = new JLabel("");
		yellow = new JLabel("");
		red = new JLabel("");
		cardentropy = new JLabel("");
		handentropy = new JLabel("");

		this.setBorder(BorderFactory.createTitledBorder("Dettagli selezione"));
		this.setBackground(null);
		this.setLayout(new BorderLayout());

		JPanel north = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel();
		north.setLayout(new GridLayout(1,4));
		north.setBackground(null);
		center.setLayout(new GridLayout(1,2));
		center.setBackground(null);
		south.setLayout(new GridLayout(1,4));
		south.setBackground(null);
		this.add(north,BorderLayout.NORTH);
		this.add(center,BorderLayout.CENTER);
		this.add(south,BorderLayout.SOUTH);

		JPanel values = new JPanel();
		JPanel colors = new JPanel();
		values.setLayout(new GridLayout(5,2));
		colors.setLayout(new GridLayout(5,2));
		values.setBackground(null);
		colors.setBackground(null);
		center.add(values);
		center.add(colors);

		north.add(new JLabel("Giocabile: "));
		north.add(playability);
		north.add(new JLabel("Inutile: "));
		north.add(uselessness);

		values.add(new JLabel("1: "));
		values.add(one);
		values.add(new JLabel("2: "));
		values.add(two);
		values.add(new JLabel("3: "));
		values.add(three);
		values.add(new JLabel("4: "));
		values.add(four);
		values.add(new JLabel("5: "));
		values.add(five);

		colors.add(new JLabel("Blu: "));
		colors.add(blue);
		colors.add(new JLabel("Verde: "));
		colors.add(green);
		colors.add(new JLabel("Rosso: "));
		colors.add(red);
		colors.add(new JLabel("Bianco: "));
		colors.add(white);
		colors.add(new JLabel("Giallo: "));
		colors.add(yellow);

		south.add(new JLabel("Entropia carta: "));
		south.add(cardentropy);
		south.add(new JLabel("Entropia mano: "));
		south.add(handentropy);
	}

	@Override
	public void onChange(int stateround) {
		analitics.setState(history.get(stateround-1));
	}

	@Override
	public void onNewState(State newstate) {
		history.add(newstate);
	}

	@Override
	public void onNewSelection(CardGUI card)
	{
		String player = card.getOwner().getPlayer();
		int index = card.getIndex();

		DecimalFormat df = new DecimalFormat("#.##");
		playability.setText(""+df.format(analitics.getPlayability(player,index)*100)+"%");
		uselessness.setText(""+df.format(analitics.getUselessness(player,index)*100)+"%");

		one.setText(""+df.format(analitics.getValueProbability(player,index,1)*100)+"%");
		two.setText(""+df.format(analitics.getValueProbability(player,index,2)*100)+"%");
		three.setText(""+df.format(analitics.getValueProbability(player,index,3)*100)+"%");
		four.setText(""+df.format(analitics.getValueProbability(player,index,4)*100)+"%");
		five.setText(""+df.format(analitics.getValueProbability(player,index,5)*100)+"%");

		blue.setText(""+df.format(analitics.getColorProbability(player,index,"blue")*100)+"%");
		white.setText(""+df.format(analitics.getColorProbability(player,index,"white")*100)+"%");
		red.setText(""+df.format(analitics.getColorProbability(player,index,"red")*100)+"%");
		yellow.setText(""+df.format(analitics.getColorProbability(player,index,"yellow")*100)+"%");
		green.setText(""+df.format(analitics.getColorProbability(player,index,"green")*100)+"%");

		cardentropy.setText(""+df.format(analitics.getCardEntropy(player,index))+" bit");
		handentropy.setText(""+df.format(analitics.getHandEntropy(player))+" bit");
	}
}
