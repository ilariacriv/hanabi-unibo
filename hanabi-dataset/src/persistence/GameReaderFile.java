package persistence;

import com.google.gson.Gson;
import model.finale.FinalState;
import model.raw.RawAction;
import model.raw.RawState;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GameReaderFile {
    /**
     * Classe per leggere il file Json
     */

    private BufferedReader gamebr;
    private FileReader gamereader;
    private Gson gson;

    public GameReaderFile(String gamefile) {
        this.gson = new Gson();
        try {
            this.gamereader= new FileReader(gamefile);
            this.gamebr= new BufferedReader(gamereader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public RawState readRawState(){
        RawState state= new RawState();
        try {
            String line= gamebr.readLine();
            state= gson.fromJson(line, RawState.class);
            for (int i=0; i<state.getOther_hand().size();i++){
                state.getOther_hand().get(i).setColorEnum();
            }
            for (int i=0; i<state.getCurrent_hand().size();i++){
                state.getCurrent_hand().get(i).setColorEnum();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    return state;
    }

    public FinalState readFinalState(){
        //TODO in base a come stampiamo FinalState
        return null;
    }



    public void close(){

        try {
            gamebr.close();
            gamereader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
