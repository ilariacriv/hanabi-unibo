import model.finale.FinalAction;
import model.finale.FinalState;
import model.raw.RawAction;
import model.raw.RawCard;
import model.raw.RawState;
import persistence.ActionReaderFile;
import persistence.GameReaderFile;

import java.util.ArrayList;

public class Main {

    public static void main(String args[]){

        GameReaderFile gameReaderFile = new GameReaderFile("./partite_hanabi/game_0000000.txt");
        ActionReaderFile actionReaderFile = new ActionReaderFile("./azioni_hanabi/actions_0000000.txt");
        RawState rawState;
        RawAction rawAction = null;

        int i = 0;
        while((rawState = gameReaderFile.readRawState()) != null &&
                (rawAction = actionReaderFile.readAction()) != null){
            FinalState finalState = new FinalState(rawState);
            ArrayList<RawCard> rawCards = finalState.getOrderedHandCurrent();

            FinalAction finalAction = new FinalAction(rawAction, finalState, rawState);
            System.out.println("letto "+ (++i));
        }

        actionReaderFile.close();
        gameReaderFile.close();

    }

}
