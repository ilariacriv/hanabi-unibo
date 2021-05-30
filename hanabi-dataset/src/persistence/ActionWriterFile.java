package persistence;

import model.finale.FinalAction;
import model.finale.FinalState;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * classe che scrive in un file di testo la FinalAction da dare poi in input alla NN
 */
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
        try {
            this.actionwr.write(action.toString()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
