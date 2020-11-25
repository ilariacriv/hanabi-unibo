package hanabi.gui;

import hanabi.game.CardList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
public class HandGUI extends RefreshablePanel<CardList>
{
	public final Orientation orientation;
	private List<CardGUI> cards;
	private static final FlowLayout layout = new FlowLayout(FlowLayout.CENTER,10,10);

	public HandGUI(String player, int n, Orientation orientation)
	{
		super(player);
		this. orientation = orientation;
		cards = new ArrayList<>();
		for (int i=0; i<n; i++)
			cards.add(new CardGUI(player,i, orientation.rotate()));
	}

	@Override
	protected void afterChildrenInit() {
		int n=cards.size();
		if (n==0)
			n=5;
		Dimension cardPreferredSize = orientation==Orientation.VERTICAL?CardGUI.horizontalPreferredSize:CardGUI.verticalPreferredSize;
		int width = (cardPreferredSize.width + layout.getHgap()) * n + layout.getHgap() + 5;
		int height = cardPreferredSize.height + layout.getVgap() * 2 + 20;
		this.setPreferredSize(new Dimension(width, height));
	}

	@Override
	protected void afterChildrenRefresh(CardList model) {

	}

	@Override
	protected void beforeChildrenInit() {
		this.setBackground(Board2.backgroundcolor);
		this.setBorder(BorderFactory.createTitledBorder(player));
		this.setLayout(layout);
		for (CardGUI g: cards)
			this.add(g);
	}

	@Override
	protected void beforeChildrenRefresh(CardList model) {
		if (model.size()>cards.size())
			throw new IllegalStateException("Hand length can not increase");
		if (model.size() < cards.size())
		{
			//Devo rimuovere le carte in più. Rimuovo da indice "model.size()" in poi
			while(cards.size()>model.size())
			{
				this.remove(model.size());
				cards.remove(model.size());
			}
		}

		//Aggiorno i model delle carte
		for (int i=0; i<cards.size(); i++)
			cards.get(i).setModel(model.get(i));
	}


}
*/

public class HandGUI extends JPanel
{
	private Board owner;
	public final Orientation orientation;
	private List<CardGUI> cards;
	private String player;

	public HandGUI(Board owner, String player, int n, Orientation orientation) {
		super();
		this.player = player;
		this.owner = owner;
		this.orientation = orientation;
		cards = new ArrayList<>();
		this.setBackground(null);
		this.setBorder(BorderFactory.createTitledBorder(player));

		if (orientation == Orientation.HORIZONTAL )
		{
			this.setLayout(new GridLayout(1,n));

/*			int width = CardGUI.horizontalPreferredSize.width * n;
			int height = CardGUI.horizontalPreferredSize.height + 50;
			this.setPreferredSize(new Dimension(width, height));*/

			for (int i = 0; i < n; i++) {
				cards.add(new CardGUI(this, i, orientation.rotate()));
				JPanel box = new JPanel();
				box.setBackground(null);
				box.add(cards.get(i));
				this.add(box);
			}
		}
		else
		{
			this.setLayout(new GridLayout(n,1));
/*			int height = (CardGUI.verticalPreferredSize.height + 5) * n ;
			int width = CardGUI.verticalPreferredSize.width + 50;
			this.setPreferredSize(new Dimension(width, height));*/

			for (int i = 0; i < n; i++) {
				cards.add(new CardGUI(this, i, orientation.rotate()));
				JPanel box = new JPanel();
				box.add(cards.get(i));
				box.setBackground(null);
				this.add(box);
			}
		}
/*
		int gap = 10;

		if (orientation == Orientation.HORIZONTAL )
		{
			this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
			if (n == 0)
				n = 5;
			int width = (CardGUI.horizontalPreferredSize.width + 10) * n + 10;
			int height = CardGUI.horizontalPreferredSize.height + 50;
			this.setPreferredSize(new Dimension(width, height));

			for (int i = 0; i < n; i++) {
				cards.add(new CardGUI(this, i, orientation.rotate()));
				this.add(cards.get(i));
			}
		}
		else
		{
			this.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			if (n == 0)
				n = 5;
			int height = (CardGUI.verticalPreferredSize.height + gap) * n + gap + 5;
			int width = CardGUI.verticalPreferredSize.width + gap * 2 + 20;
			this.setPreferredSize(new Dimension(width, height));

			for (int i = 0; i < n; i++) {
				cards.add(new CardGUI(this, i, orientation.rotate()));
				this.add(cards.get(i),gbc);
			}
		}
*/




	}

	public String getPlayer()
	{
		return player;
	}

	public Board getOwner() {
		return owner;
	}

	public void setHand(CardList hand)
	{
		if (hand.size() > cards.size())
		{
			//Aggiungo una carta perché è impossibile doverne aggiungere di piu.
			cards.add(new CardGUI(this, hand.size()-1, orientation.rotate()));
			JPanel box = new JPanel();
			box.setBackground(null);
			box.add(cards.get(cards.size()-1));
			this.add(box);

		}
		else if (hand.size() < cards.size()) {
			//Devo rimuovere le carte in più. Rimuovo da indice "hand.size()" in poi
			while (cards.size() > hand.size()) {
				this.remove(hand.size());
				cards.remove(hand.size());
			}
		}

		//Aggiorno i model delle carte
		for (int i = 0; i < cards.size(); i++)
			cards.get(i).setModel(hand.get(i));
	}

}