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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String args[]){

        int symmetricCount = 0;

        String finalStateFile = "./final_states/final_states.txt";
        String finalActionFile = "./final_actions/final_actions.txt";
        File stateFile = new File(finalStateFile);
        File actionsFile = new File(finalActionFile);
        if(!stateFile.exists()){
            FileWriter filew ;
            try {
                filew = new FileWriter(finalStateFile);
                filew.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!actionsFile.exists()){
            FileWriter filew ;
            try {
                filew = new FileWriter(finalActionFile);
                filew.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<3; i++) {
            String g = String.format("%07d",i);

            String gameFile = "./partite_hanabi/game_"+g+".txt";
            String actionFile = "./azioni_hanabi/actions_"+g+".txt";
            GameReaderFile gameReaderFile = new GameReaderFile(gameFile);
            ActionReaderFile actionReaderFile = new ActionReaderFile(actionFile);

            RawState rawState;
            RawAction rawAction;
            ArrayList<FinalAction> finalActionList = new ArrayList<>();
            ArrayList<FinalState> finalStateList = new ArrayList<>();

            SymmetriesChecker symmetriesChecker = new SymmetriesChecker(finalStateFile);
            while ((rawState = gameReaderFile.readRawState()) != null &&
                    (rawAction = actionReaderFile.readAction()) != null) {
                FinalState finalState = new FinalState(rawState);
                ArrayList<RawCard> rawCards = finalState.getOrderedHandCurrent();

                FinalAction finalAction = new FinalAction(rawAction, finalState, rawState);
                //System.out.println(finalState.toString());

                finalStateList.add(finalState);
                finalActionList.add(finalAction);

            }
            actionReaderFile.close();
            gameReaderFile.close();

            GameWriterFile gameWriterFile;
            ActionWriterFile actionWriterFile;

            for (int index = 0; index < finalStateList.size(); index++) {
                if (!symmetriesChecker.hasASymmetricState(finalStateList.get(index))) {
                    gameWriterFile = new GameWriterFile(finalStateFile);
                    actionWriterFile = new ActionWriterFile(finalActionFile);
                    gameWriterFile.printFinalState(finalStateList.get(index));
                    actionWriterFile.printFinalAction(finalActionList.get(index));
                    actionWriterFile.close();
                    gameWriterFile.close();
                } else {
                    symmetricCount++;
                    System.out.println("["+symmetricCount+"] "+index + ": " + finalStateList.get(index).toString());
                }
            }
        }

    }


}
