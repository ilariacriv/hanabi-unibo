package hanabi.gui;

import hanabi.game.Card;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
public class CardGUI extends RefreshablePanel<Card> implements MouseListener
{
	public static final Dimension verticalPreferredSize = new Dimension(40,60);
	public static final Dimension horizontalPreferredSize = new Dimension(60,40);
	public static final Object selectedmonitor = new Object();
	private static CardGUI selected;
	private static Border selBorder = BorderFactory.createLineBorder(Color.black,2);
	private String owner;
	private int index;
	private final JLabel valuelabel;
	public final Orientation orientation;

	public CardGUI(String owner, int index, Orientation orientation)
	{
		super(owner+"'s card #"+index);
		this.index = index;
		this.owner = owner;
		valuelabel = new JLabel();
		this.orientation = orientation;
		this.addMouseListener(this);
		valuelabel.addMouseListener(this);
	}

	@Override
	protected void afterChildrenInit() {

	}

	@Override
	protected void afterChildrenRefresh(Card model) {

	}

	@Override
	protected void beforeChildrenInit() {
		if (orientation == Orientation.VERTICAL) {
			this.setPreferredSize(verticalPreferredSize);
			valuelabel.setPreferredSize(verticalPreferredSize);
		}
		else if (orientation == Orientation.HORIZONTAL)
		{
			this.setPreferredSize(horizontalPreferredSize);
			valuelabel.setPreferredSize(horizontalPreferredSize);
		}
		else
			throw new RuntimeException("Wrong orientation");

		this.add(valuelabel);
		valuelabel.setHorizontalAlignment(SwingConstants.CENTER);
		valuelabel.setVerticalAlignment(SwingConstants.CENTER);
		valuelabel.setFont(new Font(getFont().getPlayer(),getFont().getStyle(),25));
		this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
	}

	@Override
	protected void beforeChildrenRefresh(Card model) {
		if (model != null)
		{
			this.setBackground(Card.getAwtColor(model.getColor()));
			if (model.getValue() == 0)
				valuelabel.setText("");
			else
				valuelabel.setText(""+ model.getValue());
			valuelabel.setToolTipText("<html>Colori possibili: "+ model.getPossibleColors()+"<br>Valori possibili: "+ model.getPossibleValues().toString().replace(".0","")+"</html>");
		}
		else
		{
			valuelabel.setText("");
			this.setBackground(Card.getAwtColor(null));
			valuelabel.setToolTipText(null);
		}
	}

	public int getIndex()
	{
		return index;
	}

	public String getOwner()
	{
		return owner;
	}

	public static void main(String args[])
	{
		CardGUI gui = new CardGUI("me",0,Orientation.VERTICAL);
		gui.init();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui);

		gui.setModel(Card.createCard(1, "yellow",Card.colors,Card.values));
		gui.refresh();

		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		CardGUI.select(this);
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	private static void select(CardGUI card)
	{
		synchronized (selectedmonitor) {
			if (selected != null)
				selected.setBorder(null);
			selected = card;
			selected.setBorder(selBorder);
			selectedmonitor.notifyAll();
		}
	}

	public static CardGUI getSelected()
	{
		synchronized (selectedmonitor)
		{
			return selected;
		}
	}
}

 */
public class CardGUI extends JPanel implements MouseListener
{
	public static final Dimension verticalPreferredSize = new Dimension(70,100);
	public static final Dimension horizontalPreferredSize = new Dimension(100,70);

	private HandGUI owner;
	private int index;
	private final JLabel valuelabel;
	public final Orientation orientation;
	private Card model;

	public CardGUI(HandGUI owner, int index, Orientation orientation)
	{
		super();
		this.index = index;
		this.owner = owner;
		valuelabel = new JLabel();
		this.orientation = orientation;
		this.addMouseListener(this);
		valuelabel.addMouseListener(this);

		if (orientation == Orientation.VERTICAL) {
			this.setPreferredSize(verticalPreferredSize);
			valuelabel.setPreferredSize(verticalPreferredSize);
		}
		else if (orientation == Orientation.HORIZONTAL)
		{
			this.setPreferredSize(horizontalPreferredSize);
			valuelabel.setPreferredSize(horizontalPreferredSize);
		}
		else
			throw new RuntimeException("Wrong orientation");

		this.add(valuelabel);
		valuelabel.setHorizontalAlignment(SwingConstants.CENTER);
		valuelabel.setVerticalAlignment(SwingConstants.CENTER);
		valuelabel.setFont(new Font(getFont().getName(),getFont().getStyle(),36));
		this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
	}

	public Card getModel()
	{
		return model;
	}

	public void setModel(Card model)
	{
		this.model = model;

		if (model != null)
		{
			this.setBackground(Card.getAwtColor(model.getColor()));
			if (model.getValue() == 0)
				valuelabel.setText("");
			else
				valuelabel.setText(""+ model.getValue());
			valuelabel.setToolTipText("<html>Colori possibili: "+ model.getPossibleColors()+"<br>Valori possibili: "+ model.getPossibleValues().toString().replace(".0","")+"</html>");
		}
		else
		{
			valuelabel.setText("");
			this.setBackground(Card.getAwtColor(null));
			valuelabel.setToolTipText(null);
		}
	}

	public int getIndex()
	{
		return index;
	}

	public HandGUI getOwner()
	{
		return owner;
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		if (owner!=null) {
			owner.getOwner().select(this);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
