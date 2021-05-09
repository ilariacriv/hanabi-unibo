package hanabi.engine;

import hanabi.game.DataState;
import hanabi.game.State;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DatasetGeneration {
    private FileWriter file;
    BufferedWriter bw;
    private DataState s;

    public DatasetGeneration(State s) {
        try {
            this.file = new FileWriter("hanabiDataset.txt", true);
            this.bw = new BufferedWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
        this.s= DataState.getDatastateFromState(s);
    }

    public DataState getS() {
        return s;
    }

    public void setState(State s) {
        this.s= DataState.getDatastateFromState(s);
    }

    public FileWriter getFile() {
        return file;
    }

    public void setFile(FileWriter file) {
        this.file = file;
    }

    public void generate(){

        String line = s.toString().replaceAll("\n","").replaceAll(" ", "");
        try {
            bw.append(line+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            bw.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
