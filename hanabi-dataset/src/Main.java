import model.RawAction;
import model.RawState;
import persistence.ReadFile;

public class Main {

    public static void main(String args[]){

        ReadFile readfile= new ReadFile("gamefile.txt", "actionfile.txt");
        RawState s= readfile.readState();
        RawAction a =readfile.readAction();

        System.out.println("letto");
        
        readfile.close();

    }

}
