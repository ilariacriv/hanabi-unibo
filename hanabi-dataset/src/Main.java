import model.finale.FinalAction;
import model.finale.FinalState;
import model.raw.RawAction;
import model.raw.RawCard;
import model.raw.RawState;
import persistence.ActionReaderFile;
import persistence.ActionWriterFile;
import persistence.GameReaderFile;
import persistence.GameWriterFile;
import symmetries.SymmetriesChecker;

import java.util.ArrayList;

public class Main {

    public static void main(String args[]){

        String gameFile = "./partite_hanabi/game_0000000.txt";
        String actionFile = "./azioni_hanabi/actions_0000000.txt";
        String finalStateFile = "./final_states/final_states_01.txt";
        String finalActionFile = "./final_actions/final_actions_01.txt";
        GameReaderFile gameReaderFile = new GameReaderFile(gameFile);
        ActionReaderFile actionReaderFile = new ActionReaderFile(actionFile);

        RawState rawState;
        RawAction rawAction = null;
        ArrayList<FinalAction> finalActionList = new ArrayList<>();
        ArrayList<FinalState> finalStateList = new ArrayList<>();

        SymmetriesChecker symmetriesChecker = new SymmetriesChecker(finalStateFile);
        while((rawState = gameReaderFile.readRawState()) != null &&
                (rawAction = actionReaderFile.readAction()) != null){
            FinalState finalState = new FinalState(rawState);
            ArrayList<RawCard> rawCards = finalState.getOrderedHandCurrent();

            FinalAction finalAction = new FinalAction(rawAction, finalState, rawState);
            //System.out.println(finalState.toString());

            finalStateList.add(finalState);
            finalActionList.add(finalAction);

        }
        actionReaderFile.close();
        gameReaderFile.close();

        ArrayList<FinalState> resultStates = new ArrayList<>();
        ArrayList<FinalAction> resultActions = new ArrayList<>();
        for(int index=0; index<finalStateList.size(); index++){
            if(!symmetriesChecker.hasASymmetricState(finalStateList.get(index))){
                resultStates.add(finalStateList.get(index));
                resultActions.add(finalActionList.get(index));
            }
            else System.out.println(index+": "+finalStateList.get(index).toString());
        }

        GameWriterFile gameWriterFile = new GameWriterFile(finalStateFile);
        ActionWriterFile actionWriterFile = new ActionWriterFile(finalActionFile);

        for(FinalState fs : finalStateList){
            gameWriterFile.printFinalState(fs);
        }
        for(FinalAction fa : finalActionList){
            actionWriterFile.printFinalAction(fa);
        }

        actionWriterFile.close();
        gameWriterFile.close();

    }


}
