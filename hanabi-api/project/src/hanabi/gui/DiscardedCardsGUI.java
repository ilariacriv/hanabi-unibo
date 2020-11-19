package hanabi.gui;

import hanabi.game.Card;
import hanabi.game.CardList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/*
public class DiscardedCardsGUI extends RefreshablePanel<CardList> implements ActionListener
{
	private JLabel label;
	private JButton button;

	public DiscardedCardsGUI()
	{
		super("discarded");
		label = new JLabel();
		button = new JButton("Controlla");
		button.setFocusable(false);
	}

	@Override
	protected void afterChildrenInit() {

	}

	@Override
	protected void afterChildrenRefresh(CardList model) {

	}

	@Override
	protected void beforeChildrenInit() {
		setBackground(null);
		button.addActionListener(this);
		this.setLayout(new GridLayout(1,2));
		this.add(label);
		this.add(button);
	}

	@Override
	protected void beforeChildrenRefresh(CardList model) {
		label.setText("Carte scartate: "+model.size());
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
*/
public class DiscardedCardsGUI extends JPanel implements ActionListener
{
	private JLabel label;
	private JButton button;
	private JScrollPane scrollPane;
	private JDialog dialog;
	private JPanel cards;

	public DiscardedCardsGUI()
	{
		super();
		label = new JLabel();
		button = new JButton("Controlla lista");
		button.setFocusable(false);

		setBackground(null);
		button.addActionListener(this);
		this.setLayout(new GridLayout(1,3));
		this.add(label);
		this.add(new JLabel());
		JPanel buttonbox = new JPanel();
		buttonbox.setBackground(null);
		buttonbox.add(button);
		this.add(buttonbox);

		dialog = new JDialog();
		cards = new JPanel();
		scrollPane = new JScrollPane(cards);
		cards.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		cards.setBackground(Board.backgroundcolor);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(scrollPane,BorderLayout.CENTER);
		dialog.setSize(500,200);
		int x = Toolkit.getDefaultToolkit().getScreenSize().width/2-dialog.getSize().width/2;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height/2-dialog.getSize().height/2;
		dialog.setLocation(x,y);
		dialog.setTitle("Carte scartate");
	}

	public void setModel(CardList model) {
		label.setText("Carte scartate: "+model.size());
		if (cards.getComponentCount() != model.size())
		{
			cards.removeAll();
			for (int i=0; i<model.size(); i++)
			{
				CardGUI c = new CardGUI(null,i,Orientation.VERTICAL);
				c.setModel(model.get(i));
				cards.add(c);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		dialog.setVisible(true);
	}
}