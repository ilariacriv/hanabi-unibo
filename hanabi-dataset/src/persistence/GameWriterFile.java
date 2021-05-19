package persistence;

import com.google.gson.Gson;
import model.finale.FinalAction;
import model.finale.FinalState;

import java.io.*;

public class GameWriterFile {
    private BufferedWriter gamewr;
    private FileWriter gamewriter;

    public GameWriterFile(String gamefile) {
        try {
            this.gamewriter= new FileWriter(gamefile);
            this.gamewr= new BufferedWriter(gamewriter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printFinalState(FinalState state){
        //TODO
    }


    public void close(){

        try {

            gamewr.close();
            gamewriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
