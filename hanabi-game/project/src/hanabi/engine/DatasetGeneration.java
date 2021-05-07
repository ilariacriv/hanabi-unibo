package hanabi.engine;

import hanabi.game.State;

import java.io.File;

public class DatasetGeneration {
    private File file= new File("hanabiDataset.txt");
    private State s;

    public DatasetGeneration(State s) {
        this.file = file;
        this.s=s;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void generate(){



    }

}
