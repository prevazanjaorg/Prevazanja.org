package nameplaceholder.prevazanjaorg;

import java.io.Serializable;

/**
 * Created by Potatoseus on 11/23/2017.
 */

public class Uporabnik implements Serializable{
    public String telefon;
    public String username;
    public double ocene;
    public int stOcen = 0;

    public double getOcene() {
        return ocene;
    }

    public void setOcene(double ocene) {
        this.ocene = ocene;
    }

    public void incStOcen() {
        stOcen++;
    }

    public void decStOcen() {
        stOcen--;
    }

    public int getStOcen() {
        return stOcen;
    }

    public void setStOcen(int stOcen) {
        this.stOcen = stOcen;
    }

    public Uporabnik(String telefon, String username) {
        this.telefon = telefon;
        this.username = username;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
