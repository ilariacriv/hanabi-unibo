import model.RawAction;
import model.RawState;
import persistence.ReadFile;

public class Main {

    public static void main(String args[]){

        ReadFile readfile= new ReadFile("./partite_hanabi/game_0000000.txt", "./azioni_hanabi/actions_0000000.txt");
        RawState s= readfile.readState();
        RawAction a =readfile.readAction();

        System.out.println("letto");

        readfile.close();

    }

}
