package hanabi.nn;

import com.google.gson.Gson;
import dataset.DataState;
import hanabi.game.Action;
import hanabi.gui.PlayerConnectionDialog;
import hanabi.player.Analitics;
import hanabi.player.GameClient;
import model.finale.FinalState;
import model.raw.RawState;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;


public class Bot extends GameClient {

    private Analitics analitics;
    private Gson gson = new Gson();
    private Thread sent;
    private Thread receive;
    private Socket socket;

    private BufferedReader bf;
    private PrintWriter out;

    public Bot(String serverip, int serverport, boolean gui) {
        super(serverip, serverport, "NeuralNetwork", gui);
    }

    public void init()
    {
        super.init();
        System.out.println("Giocatori: "+players);
        analitics = new Analitics(players.get(0));

        //********** trial code here ***********
        try {
            socket = new Socket("localhost",9999);
            bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        /*
        sent = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    BufferedReader stdIn =new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    while(true){
                        System.out.println("Trying to read...");
                        String in = stdIn.readLine();
                        System.out.println(in);
                        out.print("Try"+"\r\n");
                        out.flush();
                        System.out.println("Message sent");
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        sent.start();
        try {
            sent.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    //TODO capire se ha senso aprire qua ogni volta un processo: tutte le volte che c'è da scegliere un'azione, lanciamo lo script python su un processo
    @Override
    public Action chooseAction() {
        analitics.setState(getCurrentState());
        DataState dataState=  DataState.getDatastateFromState(getCurrentState());
        String lineState = dataState.toString().replaceAll("\n","").replaceAll(" ", "");
        RawState rawState= gson.fromJson(lineState, RawState.class);
        FinalState finalState = new FinalState(rawState);
        //AtomicInteger action= new AtomicInteger(-1); //TODO mi ha suggerito lui questo atomic integer, controllare cosaa è
        int action = -1;

    //    System.out.println(finalState);
        try {
            //TODO deve essere sotto forma di FinalState
            // getCurrentState è di tipo State, per avere un FinalState ci serve un RawState
            // dobbiamo capire come passare da State a RawState
            //PROBLEMA: RawState lo otteniamo solo da lettura file
            String currentState = finalState.toString();

            try {
                out.print(currentState);
                out.flush();
                System.out.println("Message sent. Trying to read...");
                action = Integer.parseInt(bf.readLine());
                System.out.println(action);
                out.print("int ricevuto: "+action);
                out.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*
            final Process p = Runtime.getRuntime().exec("neural_network.exe "+currentState,null,new File(new File(System.getProperty("user.dir")).getParent()+"/hanabi-neural-network"));

            //Se creo un processo devo svuotarne il buffer di scrittura (System.out) altrimenti si riempe e il programma si blocca
            if (p!=null) {
                //	bufflist.add(new BufferedReader(new InputStreamReader(p.getInputStream())));
                Thread l = new Thread(() -> {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    try {
                        String box = "";
                        while (box != null) {
                            //if (br1.ready()) {
                            box = br1.readLine();
                            System.out.println("Box = " + box);
                            action.set(Integer.parseInt(box));
                            //}

                            //System.out.println("Here is the standard output of the command:\n");
                            //while ((s = stdInput.readLine()) != null) {
                             //   System.out.println(s);
                            //}
                            //break;
                            //	if (br2.ready())
                            //		box = br2.readLine();
                            //	System.out.println(box);
                        }
                        //Se devi svuotare anche System.err usa un altro thread
                    } catch (IOException e) {
                    }
                });
                l.start();

                //TODO forse bisogna fare il join per aspettare che il processo finisca
                try //Attendo che il thread finisca
                {
                    l.join();
                } catch (InterruptedException e) {

                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Azione num: " + action);
        Action result = null;
        //TODO riodrinare le carte perchè la abbiamo ordinate a seconda del final state
        if (action < 5)
            result = Action.createPlayAction(players.get(0), action);
        else if (action < 10)
            result = Action.createDiscardAction(players.get(0), action-5);
        else if (action < 15)
            result = Action.createHintValueAction(players.get(0), "hint value" ,action-10);
        else if (action < 20)
            result = Action.createHintColorAction(players.get(0), "hint color" , finalState.getColorOrder().get(action-15).toString());

        return result;
    }

    public static void main(String args[])
    {
        //TODO capire cosa mettere qua

        Bot bot;
        if (args.length == 3)
        {
            bot = new Bot(args[0],Integer.parseInt(args[1]),Boolean.parseBoolean(args[2]));
        }
        else
        {
            PlayerConnectionDialog dialog;
            dialog = new PlayerConnectionDialog("Player Connection");
            dialog.waitForConfirm();
            bot = new Bot(dialog.getIP(),dialog.getPort(),true);
        }
        bot.run();
        System.out.println("Partita finita.");

//		System.out.println(bot.frame);
        if (bot.frame == null)
            System.exit(0);

	/*	try
		{
			bot.ui.join();
		}
		catch (InterruptedException e)
		{

		}*/
    }
}
