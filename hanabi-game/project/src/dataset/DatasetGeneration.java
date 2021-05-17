package dataset;

import hanabi.game.Action;
import hanabi.game.State;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DatasetGeneration {
    private FileWriter game_file;
    private FileWriter actions_file;
    BufferedWriter bw_game;
    BufferedWriter bw_actions;
    private DataState dataState;
    private Action lastaction;

    public DatasetGeneration(State s, int game) {
        String g = String.format("%07d",game);
        try {
           this.game_file = new FileWriter("./partite_hanabi/game_"+g+".txt");
           this.actions_file = new FileWriter("./azioni_hanabi/actions_"+g+".txt");
            //this.file = new FileWriter("hanabiDataset.txt", true); //per fare append
            this.bw_game = new BufferedWriter(game_file);
            this.bw_actions = new BufferedWriter(actions_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lastaction = s.getLastAction();
        this.dataState = DataState.getDatastateFromState(s);
    }

    public DataState getDataState() {
        return dataState;
    }

    public void setState(State s) {
        this.lastaction = s.getLastAction();
        this.dataState = DataState.getDatastateFromState(s);
    }

    public FileWriter getGame_file() {
        return game_file;
    }

    public void setGame_file(FileWriter game_file) {
        this.game_file = game_file;
    }

    public void generate(){

        String line = dataState.toString().replaceAll("\n","").replaceAll(" ", "");

        try {
            if (lastaction != null) {
                String action = lastaction.toString().replaceAll("\n", "").replaceAll(" ", "");
                bw_actions.append(action + "\n");
            }
            bw_game.append(line+"\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            bw_game.close();
            bw_actions.close();
            game_file.close();
            actions_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
