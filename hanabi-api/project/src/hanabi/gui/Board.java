package hanabi.gui;

import hanabi.game.Action;
import hanabi.game.Card;
import hanabi.game.State;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel
{
	public static final Color backgroundcolor = new Color(190, 190, 190);

	private LogPanel log;
	private DetailPanel details;
	private NavigationPanel navigation;
	private final HandGUI[] othershands;
	private final HandGUI myhand;
	public final String[] othersnames;
	public final String myname;
	private final FireworksPanel fireworksPanel;
	private final InfoPanel infoPanel;


	public ArrayList<State> history;
	public int shownTurn;

	public final Object selectedmonitor = new Object();
	private CardGUI selected;
	private static Border selBorder = BorderFactory.createLineBorder(Color.black,2);
	private ArrayList<StateListener> stateListeners;
	private ArrayList<SelectedListener> selectedListeners;
	private ArrayList<StateShownListener> stateShownListeners;

	public Board(String myname, String[] othersnames)
	{
		super();
		this.othersnames = othersnames;
		this.myname = myname;

		stateListeners = new ArrayList<>();
		selectedListeners = new ArrayList<>();
		stateShownListeners = new ArrayList<>();

		log = new LogPanel();
		details = new DetailPanel(this,myname);
		navigation = new NavigationPanel(this);
		fireworksPanel = new FireworksPanel();
		infoPanel = new InfoPanel(this);
		history = new ArrayList<>();

		myhand = new HandGUI(this,myname,5, Orientation.HORIZONTAL);
		othershands = new HandGUI[othersnames.length];

		JPanel main = new JPanel();
		JPanel sidecolumn = new JPanel();
		this.setLayout(new BorderLayout());
		this.add(main,BorderLayout.CENTER);
		this.add(sidecolumn,BorderLayout.EAST);
		this.setBackground(backgroundcolor);

		//SideColumn Panel
		sidecolumn.setBorder(BorderFactory.createLineBorder(Card.getAwtColor(null)));
		sidecolumn.setLayout(new BorderLayout());
		sidecolumn.setBackground(null);

		JPanel sidecenter = new JPanel();
		sidecolumn.add(navigation,BorderLayout.SOUTH);
		sidecolumn.add(sidecenter,BorderLayout.CENTER);

		sidecenter.setBackground(null);
		sidecenter.setLayout(new GridLayout(2,1));
		sidecenter.add(log);
		sidecenter.add(details);


		//Main Panel
		main.setBorder(BorderFactory.createLineBorder(Card.getAwtColor(null)));
//		main.setLayout(new GridLayout(3,1));
		main.setLayout(new BorderLayout());
		main.setBackground(null);
		JPanel north = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel();
		main.add(north,BorderLayout.NORTH);
		main.add(center,BorderLayout.CENTER);
		main.add(south,BorderLayout.SOUTH);

		center.setBackground(null);
		center.setLayout(new GridLayout(1,2));
		center.add(fireworksPanel);
		center.add(infoPanel);

		south.setBackground(null);
		south.setLayout(new FlowLayout(FlowLayout.CENTER));
		south.add(myhand);

		north.setBackground(null);
		north.setLayout(new FlowLayout(FlowLayout.CENTER));
		if (othersnames.length == 1)
		{
			othershands[0] = new HandGUI(this,othersnames[0],5,Orientation.HORIZONTAL);
			north.add(othershands[0]);
		}
		else if (othersnames.length == 2)
		{
			othershands[0] = new HandGUI(this,othersnames[0],5,Orientation.HORIZONTAL);
			othershands[1] = new HandGUI(this,othersnames[1],5,Orientation.HORIZONTAL);
			north.add(othershands[0]);
			north.add(othershands[1]);
		}
		else if (othersnames.length == 3)
		{
			othershands[0] = new HandGUI(this,othersnames[0],4,Orientation.VERTICAL);
			othershands[1] = new HandGUI(this,othersnames[1],4,Orientation.HORIZONTAL);
			othershands[2] = new HandGUI(this,othersnames[2],4,Orientation.VERTICAL);
			JPanel east = new JPanel();
			east.setBackground(null);
			east.setLayout(new FlowLayout(FlowLayout.CENTER));
			JPanel west = new JPanel();
			west.setBackground(null);
			west.setLayout(new FlowLayout(FlowLayout.CENTER));

			main.add(east,BorderLayout.EAST);
			main.add(west,BorderLayout.WEST);
			west.add(othershands[0]);
			north.add(othershands[1]);
			east.add(othershands[2]);
		}
		else if (othersnames.length == 4)
		{
			othershands[0] = new HandGUI(this,othersnames[0],4,Orientation.VERTICAL);
			othershands[1] = new HandGUI(this,othersnames[1],4,Orientation.HORIZONTAL);
			othershands[2] = new HandGUI(this,othersnames[2],4,Orientation.HORIZONTAL);
			othershands[3] = new HandGUI(this,othersnames[3],4,Orientation.VERTICAL);
			JPanel east = new JPanel();
			east.setBackground(null);
			east.setLayout(new FlowLayout(FlowLayout.CENTER));
			JPanel west = new JPanel();
			west.setBackground(null);
			west.setLayout(new FlowLayout(FlowLayout.CENTER));

			main.add(east,BorderLayout.EAST);
			main.add(west,BorderLayout.WEST);
			west.add(othershands[0]);
			north.add(othershands[1]);
			north.add(othershands[2]);
			east.add(othershands[3]);
		}
	}

	public void addState(State state)
	{
		int expected = history.size()==0?1:history.get(history.size()-1).getRound()+1;
		if (state.getRound() == expected)
			history.add(state);
		else throw new IllegalStateException("Expected round "+expected);

		for (StateListener s:stateListeners)
			s.onNewState(state);

		showState(expected);

		if (expected > 1)
			log(printAction(state.getLastAction())+"\n\n");
		String curr = state.getCurrentPlayer();
		if (state.isLastState())
			log("Partita finita");
		else
			log("Turno "+state.getRound()+", tocca a "+(curr.equals(myname)?"te":curr)+"\n");
	}

	public void addSelectedListener(SelectedListener listener)
	{
		selectedListeners.add(listener);
	}

	public void addStateListener(StateListener listener)
	{
		stateListeners.add(listener);
	}

	public void addStateShownListener(StateShownListener listener){
		stateShownListeners.add(listener);
	}

	public void log(String s)
	{
		log.log(s);
	}

	private String printAction(Action action)
	{
		if (action.getActionType().equals(Action.play))
			return "Gioca una carta";
		if (action.getActionType().equals(Action.discard))
			return "Scarta una carta";
		if (action.getActionType().equals(Action.hint_color))
			return "Suggerisce a "+action.getHinted()+" le carte di colore "+action.getColor();
		if (action.getActionType().equals(Action.hint_value))
			return "Suggerisce a "+action.getHinted()+" le carte di valore "+action.getValue();
		return "error";
	}

	public void showState(int round)
	{
		shownTurn = round;
		State state = history.get(round-1);
		myhand.setHand(state.getHand(myname));
		for (int i=0; i<othershands.length; i++)
			othershands[i].setHand(state.getHand(othersnames[i]));
		fireworksPanel.setModel(state);
		infoPanel.setModel(state);
		for (StateShownListener s:stateShownListeners)
			s.onChange(round);
		if (selected!=null) {
			for (SelectedListener s : selectedListeners)
				s.onNewSelection(selected);
		}
	}

	public void select(CardGUI card)
	{
		synchronized (selectedmonitor) {
			if (selected != null)
				selected.setBorder(null);
			selected = card;
			selected.setBorder(selBorder);
		}
		for(SelectedListener s:selectedListeners)
			s.onNewSelection(card);
	}
}
