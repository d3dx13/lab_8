package lab_7.world;

public class IdMaster {
    public static int idCounter = 4096;
    public static int create(){
        idCounter++;
        return idCounter;
    }
}
