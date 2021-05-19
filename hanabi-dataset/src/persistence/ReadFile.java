package persistence;

import com.google.gson.Gson;
import model.raw.RawAction;
import model.raw.RawState;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
    /**
     * Classe per leggere il file Json
     */

    private BufferedReader gamebr,actionbr;
    private FileReader gamereader,actionreader;
    private Gson gson;

    public ReadFile(String gamefile, String actionfile) {
        this.gson = new Gson();
        try {
            this.gamereader= new FileReader(gamefile);
            this.gamebr= new BufferedReader(gamereader);

            this.actionreader= new FileReader(actionfile);
            this.actionbr= new BufferedReader(actionreader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public RawState readState(){
        RawState state= new RawState();
        try {
            String line= gamebr.readLine();
            state= gson.fromJson(line, RawState.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    return state;
    }

    public RawAction readAction(){
        RawAction a= new RawAction();
        try {
            String line= actionbr.readLine();
            a= gson.fromJson(line, RawAction.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }



    public void close(){

        try {
            actionbr.close();
            gamebr.close();
            gamereader.close();
            actionreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
