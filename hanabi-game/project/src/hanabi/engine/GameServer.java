package hanabi.engine;

import hanabi.game.*;
import hanabi.game.Action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
	private static boolean gui = true;
	private static JFrame frame = null;
	private static JTextArea textArea;
//	private static JButton aggiungi;
	private static JButton inizia;
//	private static JPanel center;
	private static JTextField portf;
	private static JTextField games;
	private static JPanel[] combos;

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
		//Ottengo una porta libera
		serverSocket = new ServerSocket(0);
		int port = serverSocket.getLocalPort();
		serverSocket.close();

		frame = new JFrame("Hanabi");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		JPanel north = new JPanel();
		north.setLayout(new GridLayout(1,2));
		JLabel[] ip = new JLabel[] {new JLabel(getInternalIp()),new JLabel(getExternalIp())};
		portf = new JTextField("" + port,5);
		JPanel ipp = new JPanel();
		JPanel portp = new JPanel();
		ipp.setLayout(new FlowLayout(FlowLayout.CENTER));
		portp.setLayout(new FlowLayout(FlowLayout.CENTER));
		portp.add(new JLabel("Porta"));
		portp.add(portf);
		ipp.add(new JLabel("IP"));
		ipp.add(ip[0]);
		ipp.add(new JLabel(" - "));
		ipp.add(ip[1]);
		north.add(ipp);
		north.add(portp);
		frame.add(north,BorderLayout.NORTH);

		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1,2));
		JPanel cleft = new JPanel();
		JPanel cright = new JPanel();
		cleft.setLayout(new GridLayout(6,1));
		cright.setLayout(new BorderLayout());
		JPanel header = new JPanel();
		header.setLayout(new GridLayout(1,2));
		header.add(new JLabel("GUI"));
		header.add(new JLabel("Giocatore"));
		cleft.add(header);
		combos = new JPanel[5];
		for (int i=0; i<5; i++) {
			combos[i] = createComboBox(i);
			cleft.add(combos[i]);
		}
		center.add(cleft);
		JPanel crn = new JPanel();
		JPanel crs = new JPanel();
		cright.add(crn,BorderLayout.NORTH);
		cright.add(crs,BorderLayout.SOUTH);
		crn.setLayout(new FlowLayout(FlowLayout.CENTER));
		crs.setLayout(new FlowLayout(FlowLayout.CENTER));
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
		crs.add(inizia);
		crn.add(new JLabel("Numero partite"));
		games = new JTextField("1",5);
		crn.add(games);
		center.add(cright);
		frame.add(center,BorderLayout.CENTER);

		JPanel south = new JPanel();
		south.setLayout(new BorderLayout());
		textArea = new JTextArea(8,20);
		textArea.setEditable(false);
		textArea.append("Impostazioni partita Hanabi\n");
		JScrollPane scrollPane = new JScrollPane(textArea);
		south.add(scrollPane,BorderLayout.CENTER);
		frame.add(south,BorderLayout.SOUTH);

		frame.pack();
		int x = Toolkit.getDefaultToolkit().getScreenSize().width/2-frame.getSize().width/2;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height/2-frame.getSize().height/2;
		frame.setLocation(x,y);
		frame.setVisible(true);

	}


	private static void start() throws Exception
	{
		//Disattivo i componenti grafici precedenti
		for (int i=0; i<combos.length; i++)
			combos[i].setEnabled(false);
		inizia.setEnabled(false);
		portf.setEnabled(false);
		games.setEnabled(false);

		serverSocket = new ServerSocket(Integer.parseInt(portf.getText()));
		textArea.append("Aperta porta "+serverSocket.getLocalPort()+"\n");

		int games = Integer.parseInt(GameServer.games.getText());
		double medscore = 0;
		for (int g = 0; g<games; g++)
		{
			textArea.append("Gioco "+(g+1)+"/"+games+"\n");

			//Cerco nel pannello center le varie combobox. A seconda del loro valore imposto le connessioni con i giocatori
			for (int i=0; i<combos.length; i++)
			{
				String player = ((JComboBox<String>)(combos[i].getComponent(1))).getSelectedItem().toString();
				boolean gui = ((JCheckBox)(combos[i].getComponent(0))).isSelected();
				if (player.equals("Chiuso"))
					continue;
				Thread l = new Thread(() -> {
					try
					{
						textArea.append("Attendo connessione... ");
						Socket s = serverSocket.accept();
						sockets.add(s);
						names.add(connect(s));
						textArea.append("aggiunto "+names.get(names.size()-1)+" @ "+s.getInetAddress()+"\n");
					}
					catch (IOException e)
					{
						System.exit(1);
					}
				});

				l.start();

				if (player.equals("HumanPlayer"))
					Runtime.getRuntime().exec("java -jar hanabi-human-player.jar localhost "+serverSocket.getLocalPort(),null,new File(new File(System.getProperty("user.dir")).getParent()+"/hanabi-human-player"));
				else if (player.equals("Bot1"))
					Runtime.getRuntime().exec("java -jar hanabi-bot1.jar localhost "+serverSocket.getLocalPort()+" "+gui,null,new File(new File(System.getProperty("user.dir")).getParent()+"/hanabi-bot1"));
				else if (player.equals("Bot2"))
					Runtime.getRuntime().exec("java -jar hanabi-bot2.jar localhost "+serverSocket.getLocalPort()+" "+gui,null,new File(new File(System.getProperty("user.dir")).getParent()+"/hanabi-bot2"));


				try //Attendo che il thread finisca
				{
					l.join();
				}
				catch (InterruptedException e)
				{

				}
			}

//		frame.setVisible(true);

			textArea.append("Partita iniziata\n");
			textArea.setCaretPosition(textArea.getDocument().getLength());

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
			Action currentAction;

			//Inizia il ciclo di gioco
			while(true)
			{
				System.out.println("Invio dello stato corrente. Round: "+currentState.getRound());
				//Invio stato corrente mascherato a tutti i giocatori
				sendMaskedStates(currentState,sockets,names);

				//Controllo se la partita è finita
				if (currentState.isLastState())
					break;

				//Attendo la mossa dal giocatore cui tocca giocare
				String player = currentState.getCurrentPlayer();
				int indexOfPlayer = names.indexOf(player);
				currentAction = new Action(new InputStreamReader(sockets.get(indexOfPlayer).getInputStream()));

				//Verifico la legittimità della mossa e ne applico gli effetti allo stato corrente per ottenere un nuovo stato.
				currentState = currentState.applyAction(currentAction,deck,names);

			}

			textArea.append("Gioco finito. ");
			int score = 0;
			if (currentState.getFuseTokens()==0)
				textArea.append("Partita persa.\n");
			else
			{
				score = currentState.getScore();
				textArea.append("Punteggio: "+score+"\n");
			}
			medscore = (medscore*g+score)/(g+1);
			textArea.append("Punteggio medio: "+medscore+"\n\n");
			textArea.setCaretPosition(textArea.getDocument().getLength());

			for (Socket s:sockets)
				s.close();
			sockets.clear();
			names.clear();
		}





	}

	private static JPanel createComboBox(int index)
	{
		JCheckBox check = new JCheckBox();
		JComboBox<String> combo = new JComboBox<>();
		JPanel panel = new JPanel(){
			public void setEnabled(boolean enabled)
			{
				super.setEnabled(true);
				check.setEnabled(enabled);
				combo.setEnabled(enabled);
			}
		};

		panel.setLayout(new GridLayout(1,2));

		if (index>1)
			combo.addItem("Chiuso");
		combo.addItem("Aperto");
		combo.addItem("HumanPlayer");
		combo.addItem("Bot1");
		combo.addItem("Bot2");
//		combo.addItem("SimpleBot senza GUI");

		combo.addActionListener(e -> {
			JComboBox cb = (JComboBox)e.getSource();
			String player = (String)cb.getSelectedItem();

			if (player != null)
			{
				if (player.equals("Chiuso") || player.equals("Aperto"))
				{
					check.setSelected(false);
					check.setEnabled(false);
				}
				else if (player.equals("HumanPlayer"))
				{
					check.setSelected(true);
					check.setEnabled(false);
				}
				else
				{
					check.setEnabled(true);
					check.setSelected(false);
				}

			}
		});
		check.setEnabled(false);
		check.setSelected(false);
		panel.add(check);
		panel.add(combo);
		return panel;
	}
/*	private static void addNewComboBox()
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
*/
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
