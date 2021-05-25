package persistence;

import com.google.gson.Gson;
import model.raw.RawAction;
import model.raw.RawState;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ActionReaderFile {
    /**
     * Classe per leggere il file Json che contiente le azioni di una partita
     */

    private BufferedReader actionbr;
    private FileReader actionreader;
    private Gson gson;

    public ActionReaderFile(String actionfile) {
        this.gson = new Gson();
        try {

            this.actionreader= new FileReader(actionfile);
            this.actionbr= new BufferedReader(actionreader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            actionreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
