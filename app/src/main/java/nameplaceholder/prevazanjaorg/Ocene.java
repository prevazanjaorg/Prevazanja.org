package nameplaceholder.prevazanjaorg;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Potatoseus on 1/8/2018.
 */

public class Ocene implements Serializable{
    public ArrayList<Integer> udobje;
    public ArrayList<Integer> točnost;
    public ArrayList<Integer> oseba;

    public Ocene(ArrayList<Integer> udobje, ArrayList<Integer> točnost, ArrayList<Integer> oseba) {
        this.udobje = udobje;
        this.točnost = točnost;
        this.oseba = oseba;
    }

    public ArrayList<Integer> getUdobje() {
        return udobje;
    }

    public void setUdobje(ArrayList<Integer> udobje) {
        this.udobje = udobje;
    }

    public ArrayList<Integer> getTočnost() {
        return točnost;
    }

    public void setTočnost(ArrayList<Integer> točnost) {
        this.točnost = točnost;
    }

    public ArrayList<Integer> getOseba() {
        return oseba;
    }

    public void setOseba(ArrayList<Integer> oseba) {
        this.oseba = oseba;
    }
}
