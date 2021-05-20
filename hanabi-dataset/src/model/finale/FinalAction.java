package model.finale;

import model.raw.RawAction;

import java.util.ArrayList;

public class FinalAction {
    private ArrayList<Integer> action;

    public FinalAction(RawAction raw){
        action= new ArrayList<>();
        for(int i=0; i<20; i++) action.add(0);
        this.mapAction(raw);
    }

    private void mapAction(RawAction raw){
        //TODO mappare l'azione come lista di 0 e 1 nell'azione relativa

    }

    @Override
    public String toString() {
        //TODO in base al formato che decidiamo
        return "";
    }


}
