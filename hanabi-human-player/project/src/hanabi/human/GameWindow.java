package hanabi.human;

import hanabi.game.State;
import hanabi.gui.Board2;
import hanabi.gui.RefreshablePanel;

public class GameWindow extends RefreshablePanel<State>
{
	private RefreshablePanel<State> board;
	private RefreshablePanel<State> buttons;

	public GameWindow(State initialState, String myname)
	{
		super("Human window");
		if (initialState.getPlayersNames().size() == 2);
		//	board = new Board2(myname);
	}

	@Override
	protected void afterChildrenInit() {

	}

	@Override
	protected void afterChildrenRefresh(State model) {

	}

	@Override
	protected void beforeChildrenInit() {

	}

	@Override
	protected void beforeChildrenRefresh(State model) {

	}
}
