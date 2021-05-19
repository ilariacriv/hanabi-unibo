package persistence;

import model.finale.FinalAction;
import model.finale.FinalState;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class ActionWriterFile {
    private BufferedWriter actionwr;
    private FileWriter actionwriter;

    public ActionWriterFile( String actionfile) {
        try {

            this.actionwriter= new FileWriter(actionfile);
            this.actionwr= new BufferedWriter(actionwriter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void printFinalAction( FinalAction action){
        //TODO
    }


    public void close(){

        try {
            actionwr.close();
            actionwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
