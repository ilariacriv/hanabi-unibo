package symmetries;

import model.finale.FinalAction;
import model.finale.FinalState;
import model.raw.RawAction;
import model.raw.RawCard;
import model.raw.RawState;
import persistence.ActionReaderFile;
import persistence.GameReaderFile;
import persistence.GameWriterFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe che legge gli stati già salvati e controlla se ci sono simmetrie
 *
 */

public class SymmetriesChecker {
    private FileReader reader;
    private BufferedReader br;
    private String filename;

    public SymmetriesChecker(String filein) {
        this.filename = filein;
    }

    public boolean hasASymmetricState(FinalState stateToCheck){
        try {
            reader = new FileReader(filename);
            br = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String finalState;
        try {
            while((finalState = br.readLine())!= null){
                if(finalState.equals(stateToCheck.toString())){
                    br.close();
                    reader.close();
                    return true;
                }
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
