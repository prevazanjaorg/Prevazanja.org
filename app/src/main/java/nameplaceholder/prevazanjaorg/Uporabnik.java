package nameplaceholder.prevazanjaorg;

/**
 * Created by Potatoseus on 11/23/2017.
 */

public class Uporabnik {
    String telefon;
    String username;

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
