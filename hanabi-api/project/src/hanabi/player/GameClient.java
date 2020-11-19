package hanabi.player;

import hanabi.game.Action;
import hanabi.game.State;
import hanabi.gui.Board;
import hanabi.gui.WaitingDialog;
import json.JSONException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class GameClient implements Runnable
{
	public final String serverip;
	public final int serverport;

	private String name;
	private Socket socket;
	private State currentState;
	private BufferedWriter bw;
	private BufferedReader br;
	private final Object stateMonitor = new Object();
	private WaitingDialog wd;
	public Board board;
//	private boolean gui;

	public GameClient(String serverip, int serverport, String name/*, boolean gui*/)
	{
		this.serverip = serverip;
		this.serverport = serverport;
		this.name = ""+name;
		wd = new WaitingDialog();
//		this.gui = gui;
	}

	public abstract Action chooseAction();

	public abstract void init();

	public State getCurrentState()
	{
		synchronized (stateMonitor)
		{
			return currentState;
		}
	}

	public String getName()
	{
		return name;
	}

	public Socket getSocket()
	{
		return socket;
	}

	public String waitForName() throws IOException
	{
		return br.readLine();
	}
/*
	public State waitForNewState()
	{
		synchronized (stateMonitor)
		{
			try
			{
				stateMonitor.wait();
			}
			catch(InterruptedException e)
			{
				System.exit(1);
			}
			return getCurrentState();
		}
	}
*/
	@Override
	public void run()
	{
		//Stabilisco la connessione
		try
		{
			socket = new Socket(serverip,serverport);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Connessione stabilita con l'host");

		//Invio il mio nome e lo aggiorno con quello che mi viene dato dal server
		try
		{
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw.write(name);
			bw.newLine();
			bw.flush();
			System.out.print("Nome inviato, attendo conferma... ");
			wd.setVisible(true);
			name = waitForName();
			wd.setVisible(false);
			System.out.println("confermato: "+name);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		//Attendo comunicazione stato iniziale della partita
		try {
			System.out.println("Attendo stato iniziale della partita");
			synchronized (stateMonitor)
			{
				currentState = new State(br);
	//			stateMonitor.notifyAll();
			}
			boolean finished = false;
			System.out.println("Stato ricevuto. Init giocatore");

			//Inizializzo il giocatore
			init();

			//Player routine
			while (!finished)
			{
				//Controllo se la partita è finita
				if (currentState.getFinalRound() > 0 && currentState.getRound() == currentState.getFinalRound() + 1)
					finished = true;
				else
				{
					//Se è il mio turno scelgo una mossa e la mando.
					if (currentState.getCurrentPlayer().equals(name))
					{
						bw.write(chooseAction().toString(0));
						bw.flush();
					}

					//Attendo dal server la comunicazione del nuovo stato
					System.out.println("Attendo nuovo stato");
					synchronized (stateMonitor)
					{
						currentState = new State(br);
					}
					if (board!=null)
						board.addState(currentState);
				}
			}
		}
		catch (IOException | JSONException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static int getIndexPlayerFromName(String name)
	{
		return Integer.parseInt(""+name.charAt(1));
		//return Integer.parseInt(""+name.substring("Giocatore ".length()).charAt(0));
	}

	public static String getPlayerNameByIndex(Set<String> players, int index)
	{
		for (String s:players)
		{
			if (index == getIndexPlayerFromName(s))
				return s;
		}
		return null;
	}

	public List<String> reorderPlayers(Set<String> players)
	{
		ArrayList<String> reordered = new ArrayList<>();
		reordered.add(name);
		int myindex = getIndexPlayerFromName(name);
		for (int i=1; i<players.size(); i++)
		{
			reordered.add(getPlayerNameByIndex(players,(myindex-1+i)%players.size()+1));
		}
		return reordered;
	}


}
