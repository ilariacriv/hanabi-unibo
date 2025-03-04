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

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String args[]){

        int symmetricCount = 0;
        int numStatesRead = 0;

        String finalStateFile = "./final_states/final_states_prova.txt";
        String finalActionFile = "./final_actions/final_actions_prova.txt";
        File stateFile = new File(finalStateFile);
        File actionsFile = new File(finalActionFile);
        if(!stateFile.exists()){
            FileWriter filew ;
            try {
                filew = new FileWriter(finalStateFile);
                filew.write("0");
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

        for(int i=1; i<3; i++) {
            String g = String.format("%07d",i);
            System.out.println("["+i+"] reading game_"+g+".txt...");

            String gameFile = "./partite_hanabi/game_"+g+".txt";
            String actionFile = "./azioni_hanabi/actions_"+g+".txt";
            GameReaderFile gameReaderFile = new GameReaderFile(gameFile);
            ActionReaderFile actionReaderFile = new ActionReaderFile(actionFile);

            RawState rawState;
            RawAction rawAction;
            ArrayList<FinalAction> finalActionList = new ArrayList<>();
            ArrayList<FinalState> finalStateList = new ArrayList<>();

            ArrayList<Boolean> found = new ArrayList<>();

            //SymmetriesChecker symmetriesChecker = new SymmetriesChecker(finalStateFile);
            while ((rawState = gameReaderFile.readRawState()) != null &&
                    (rawAction = actionReaderFile.readAction()) != null) {
                FinalState finalState = new FinalState(rawState);
                ArrayList<RawCard> rawCards = finalState.getOrderedHandCurrent();

                FinalAction finalAction = new FinalAction(rawAction, finalState, rawState);
                //System.out.println(finalState.toString());

                finalStateList.add(finalState);
                finalActionList.add(finalAction);
                found.add(false);
                numStatesRead++;
            }
            actionReaderFile.close();
            gameReaderFile.close();

            GameWriterFile gameWriterFile;
            ActionWriterFile actionWriterFile;

            FileReader reader;
            BufferedReader br;
            try {
                /*reader = new FileReader(finalStateFile);
                br = new BufferedReader(reader);
                String finalState;
                while((finalState=br.readLine())!=null){
                    for (int index = 0; index < finalStateList.size(); index++) {
                        if (finalState.equals(finalStateList.get(index).toString())) {
                            found.set(index, true);
                            symmetricCount++;
                            System.out.println(symmetricCount+": " + finalStateList.get(index).toString());
                        }
                    }
                }
                br.close();
                */
                gameWriterFile = new GameWriterFile(finalStateFile);
                actionWriterFile = new ActionWriterFile(finalActionFile);

                for(int j=0; j<finalStateList.size(); j++){
                    if(!found.get(j)) {
                        gameWriterFile.printFinalState(finalStateList.get(j));
                        actionWriterFile.printFinalAction(finalActionList.get(j));
                    }
                }
                actionWriterFile.close();
                gameWriterFile.close();
            } catch (Exception e ) {
                e.printStackTrace();
            }
            System.out.println("game_"+g+".txt: completed");
        }
        System.out.println();
        System.out.println("Symmetrical states found: "+symmetricCount);
        System.out.println("Number of states read: "+ numStatesRead);
    }


}
