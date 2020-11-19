package hanabi.engine;

import hanabi.game.*;
import hanabi.game.Action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.List;

public final class GameServer
{
	private static List<Socket> sockets = new ArrayList<>();
	private static List<String> names = new ArrayList<>();
	private static ServerSocket serverSocket;
	private static int n = 0;
	private static int port = 0;
	private static boolean gui = true;
	private static JFrame frame = null;
	private static JButton aggiungi;
	private static JButton inizia;
	private static JPanel center;
	private static JTextField portf;

	private GameServer()
	{
		//Per impedire la costruzione di oggetti GameServer. La logica di esecuzione è nel main
	}

	/**
	 * Accetta i seguenti parametri:</br>
	 * <ul>
	 *     <li>-g x, imposta x giocatori (1&lt;x&lt;6)</li>
	 *     <li>-p x, imposta la porta x (0&lt;x&lt;256*256)</li>
	 *     <li>-t,   disattiva la gui</li>
	 * </ul>
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException
	{
		//Leggo gli argomenti e setto le impostazioni di conseguenza
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		if (args.length > 0)
		{
			for (int i=0; i<args.length; i++)
			{
				if (args[i].equals("-g"))
				{
					i++;
					if (i<args.length)
						n = Integer.parseInt(args[i]);
				}
				else if (args[i].equals("-p"))
				{
					i++;
					if (i<args.length)
						port = Integer.parseInt(args[i]);
				}
				else if (args[i].equals("-t"))
				{
					gui = false;
				}
			}
		}

		////////////////////
		serverSocket = new ServerSocket(port);
		port = serverSocket.getLocalPort();
		serverSocket.close();

		if (gui) {
			frame = new JFrame("Hanabi");
			frame.setResizable(false);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			JLabel[] ip = new JLabel[] {new JLabel(getInternalIp()),new JLabel(getExternalIp())};
			portf = new JTextField("" + port,5);
			JPanel north = new JPanel();
			JPanel north0 = new JPanel();
			JPanel ipp = new JPanel();
			JPanel north1 = new JPanel();
			JPanel portp = new JPanel();
			center = new JPanel();
			addNewComboBox();
			addNewComboBox();
			aggiungi = new JButton("Aggiungi giocatore");
			aggiungi.addActionListener(e -> {
				if (n<5) {
					addNewComboBox();
					frame.pack();
				}
				if (n==5)
					aggiungi.setEnabled(false);
			});

			inizia = new JButton("Inizia partita");
			inizia.addActionListener(e -> new Thread(() -> {
				try {
					start();
				}
				catch (Exception ex)
				{
					ex.printStackTrace(System.err);
					System.exit(1);
				}
			}).start());

			frame.getContentPane().setLayout(new BorderLayout());
			north0.setLayout(new GridLayout(1,2));
			north1.setLayout(new GridLayout(1,2));
			north.setLayout(new GridLayout(2,1));

			portp.add(new JLabel("Porta"));
			portp.add(portf);
			ipp.add(new JLabel("IP"));
			ipp.add(ip[0]);
			ipp.add(new JLabel(" - "));
			ipp.add(ip[1]);
			north0.add(ipp);
			north0.add(portp);
			north1.add(aggiungi);
			north1.add(inizia);
			north.add(north0);
			north.add(north1);
			frame.add(north,BorderLayout.NORTH);
			frame.add(center,BorderLayout.CENTER);

			frame.pack();
			int x = Toolkit.getDefaultToolkit().getScreenSize().width/2-frame.getSize().width/2;
			int y = Toolkit.getDefaultToolkit().getScreenSize().height/2-frame.getSize().height/2;
			frame.setLocation(x,y);
			frame.setVisible(true);
		}
		else
		{

		}
	}


	private static void start() throws Exception
	{
		//Disattivo i componenti grafici precedenti
		for (int i=0; i<center.getComponentCount(); i++)
			center.getComponent(i).setEnabled(false);
		aggiungi.setEnabled(false);
		inizia.setEnabled(false);
		portf.setEnabled(false);

		//Aggiungo alla finestra un componente textarea per log di starting
		JPanel south = new JPanel();
		JTextArea textarea = new JTextArea(8,20);
		textarea.setEditable(false);
		south.add(textarea);
		frame.add(south,BorderLayout.SOUTH);
		frame.pack();

		serverSocket = new ServerSocket(Integer.parseInt(portf.getText()));
		textarea.append("Aperta porta "+serverSocket.getLocalPort()+"\n");


		//Cerco nel pannello center le varie combobox. A seconda del loro valore imposto le connessioni con i giocatori
		for (int i=0; i<center.getComponentCount(); i++)
		{
			String player = ((JComboBox<String>)center.getComponent(i)).getSelectedItem().toString();

			Thread l = new Thread(() -> {
				try
				{
					textarea.append("Attendo connessione... ");
					Socket s = serverSocket.accept();
					sockets.add(s);
					names.add(connect(s));
					textarea.append("aggiunto "+names.get(names.size()-1)+" @ "+s.getInetAddress()+"\n");
					frame.pack();
				}
				catch (IOException e)
				{
					System.exit(1);
				}
			});

			l.start();

			if (player.equals("HumanPlayer"))
				Runtime.getRuntime().exec("java -jar hanabi-human-player.jar localhost "+serverSocket.getLocalPort(),null,new File(new File(System.getProperty("user.dir")).getParent()+"/hanabi-human-player"));
			else if (player.equals("SimpleBot con GUI"))
				Runtime.getRuntime().exec("java -jar hanabi-simple-bot.jar localhost "+serverSocket.getLocalPort()+" true",null,new File(new File(System.getProperty("user.dir")).getParent()+"/hanabi-simple-bot"));
			else if (player.equals("SimpleBot senza GUI"))
				Runtime.getRuntime().exec("java -jar hanabi-simple-bot.jar localhost "+serverSocket.getLocalPort()+" false",null,new File(new File(System.getProperty("user.dir")).getParent()+"/hanabi-simple-bot"));

			try //Attendo che il thread finisca
			{
				l.join();
			}
			catch (InterruptedException e)
			{

			}
		}

		frame.setVisible(false);

		//Mescolo (10 scambi casuali) i giocatori collegati e definisco così l'ordine dei turni. Poi mando il nome completo
		shufflePlayers(sockets,names);
		System.out.println("Nomi e turni comunicati");

		//Creo e mescolo un mazzo di carte
		Stack<Card> deck = Card.createDeck();
		System.out.println("Mazzo mescolato");

		//Creo le mani di partenza e quindi lo stato iniziale
		State currentState = State.createInitialState(names.toArray(new String[0]),deck);
		System.out.println("Stato iniziale creato. Inizia la partita");

		//Creo una variabile per mantenere l'ultima mossa effettuata
		Action currentAction = null;

		//Inizia il ciclo di gioco
		boolean finished = false;
		while(!finished)
		{
			System.out.println("Invio dello stato corrente. Round: "+currentState.getRound());
			//Invio stato corrente mascherato a tutti i giocatori
			sendMaskedStates(currentState,sockets,names);

			//Controllo se la partita è finita
			if (isGameFinished(currentState))
				finished = true;
			else
			{
				//Attendo la mossa dal giocatore cui tocca giocare
				String player = currentState.getCurrentPlayer();
				int indexOfPlayer = names.indexOf(player);
				currentAction = new Action(new InputStreamReader(sockets.get(indexOfPlayer).getInputStream()));

				//Verifico la legittimità della mossa e ne applico gli effetti allo stato corrente per ottenere un nuovo stato.
				currentState = currentState.applyAction(currentAction,deck,names);
			}
		}
	}

	private static void addNewComboBox()
	{
		JComboBox<String> combo = new JComboBox<>();
		combo.addItem("Aperto");
		combo.addItem("HumanPlayer");
		combo.addItem("SimpleBot con GUI");
		combo.addItem("SimpleBot senza GUI");
		if (n>1)
			combo.addItem("Chiuso");
		combo.addActionListener(e -> {
			JComboBox cb = (JComboBox)e.getSource();
			String player = (String)cb.getSelectedItem();
			if ((player != null) && player.equals("Chiuso"))
			{
				center.remove(cb);
				refreshFrame();
				if (n==5)
					aggiungi.setEnabled(true);
				n--;

			}
		});
		center.add(combo);
		n++;
	}

	private static String getInternalIp()
	{
		try
		{
			return InetAddress.getLocalHost().getHostAddress();
		}
		catch (IOException e){ return "";}
	}

	private static String getExternalIp()
	{
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

			return in.readLine(); //you get the IP as a String
		}
		catch(IOException e)
		{
			return "";
		}
	}

	private static void refreshFrame()
	{
		Dimension size = frame.getSize();
		frame.setSize(size.width+1,size.height);
		frame.setSize(size);
	}

	public static boolean isGameFinished(State currentState)
	{
		return (currentState.getFinalRound()>0 && currentState.getRound() == currentState.getFinalRound()+1) ||
				currentState.getFuseTokens() == 0;
	}

	private static void sendMaskedStates(State currentState, List<Socket> sockets, List<String> names) throws IOException
	{
		BufferedWriter bw;
		for (int i=0; i<sockets.size(); i++) {
			bw = new BufferedWriter(new OutputStreamWriter(sockets.get(i).getOutputStream()));
			State masked = currentState.mask(names.get(i));
			System.out.println("Invio a "+names.get(i)+"\n"+masked);
			bw.write(masked.toString(0));
			bw.flush();
		}
	}

	private static void shufflePlayers(List<Socket> sockets, List<String> names) throws IOException
	{
		Socket sobox;
		String stbox;
		for (int i=0; i<10; i++)
		{
			int x = (int)(Math.random()*sockets.size());
			int y = (int)(Math.random()*sockets.size());

			sobox = sockets.get(x);
			stbox = names.get(x);
			sockets.set(x,sockets.get(y));
			names.set(x,names.get(y));
			sockets.set(y,sobox);
			names.set(y,stbox);
		}

		for (int i=0; i<sockets.size(); i++)
		{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sockets.get(i).getOutputStream()));
			names.set(i,"G"+(i+1)+" ("+names.get(i)+")");
			bw.write(names.get(i));
			bw.newLine();
			bw.flush();
		}
	}

	private static String connect(Socket socket) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String name = br.readLine();

		return name;
	}
}
