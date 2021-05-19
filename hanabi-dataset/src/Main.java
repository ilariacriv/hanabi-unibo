import model.raw.RawAction;
import model.raw.RawState;
import persistence.ActionReaderFile;
import persistence.GameReaderFile;
import persistence.ReadFile;

public class Main {

    public static void main(String args[]){

        GameReaderFile gameReaderFile = new GameReaderFile("./partite_hanabi/game_0000000.txt");
        ActionReaderFile actionReaderFile = new ActionReaderFile("./azioni_hanabi/actions_0000000.txt");
        RawState s= gameReaderFile.readState();
        RawAction a =actionReaderFile.readAction();

        System.out.println("letto");

        actionReaderFile.close();
        gameReaderFile.close();

    }

}
