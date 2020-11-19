package hanabi.gui;

import hanabi.game.State;

public interface StateListener
{
	void onNewState(State newstate);
}
