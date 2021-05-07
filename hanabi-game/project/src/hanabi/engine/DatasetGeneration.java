package hanabi.engine;

import hanabi.game.State;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DatasetGeneration {
    private FileWriter file;
    BufferedWriter bw;
    private State s;

    public DatasetGeneration(State s) {
        try {
            this.file = new FileWriter("hanabiDataset.txt");
            this.bw = new BufferedWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
        this.s=s;
    }

    public State getS() {
        return s;
    }

    public void setS(State s) {
        this.s = s;
    }

    public FileWriter getFile() {
        return file;
    }

    public void setFile(FileWriter file) {
        this.file = file;
    }

    public void generate(){

        String line = s.mask(s.getCurrentPlayer()).toString().replaceAll("\n","").replaceAll(" ", "");
        try {
            bw.append(line+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
