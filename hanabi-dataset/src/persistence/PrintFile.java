package persistence;

import model.finale.FinalAction;
import model.finale.FinalState;

import java.io.*;

public class PrintFile {
    private BufferedWriter gamewr,actionwr;
    private FileWriter gamewriter,actionwriter;

    public PrintFile(String gamefile, String actionfile) {
        try {
            this.gamewriter= new FileWriter(gamefile);
            this.gamewr= new BufferedWriter(gamewriter);

            this.actionwriter= new FileWriter(actionfile);
            this.actionwr= new BufferedWriter(actionwriter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printFinalState(FinalState state){
        //TODO
    }

    public void printFinalAction( FinalAction action){
        //TODO
    }


    public void close(){

        try {
            actionwr.close();
            gamewr.close();
            gamewriter.close();
            actionwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
