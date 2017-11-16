package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.widget.Toast;

public class Prevoz {
    String iz;
    String kam;
    String mobitel;
    Integer strosek;
    Integer maxOseb;
    Boolean zavarovanje;
    String avto;
    String datum;
    String ime;
    String cas;
    Context c;

    /*
shiit
    */

    public String getIme() {
        if (ime!=null)
            return ime;
        else
            return "null";
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void Show(){
        Toast.makeText(c, "test Toasta :D", Toast.LENGTH_SHORT).show();
    }

    public Prevoz(Context context) {
        c = context;
    }

    public Prevoz() {}  //default konstruktor

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public Prevoz(String initIz, String initKam, String initMobitel, Integer initStrosek, Integer initOseb, Boolean initZavarovanje, String initAvto, String initDatum, String initIme, String initCas) {
        iz = initIz;
        kam = initKam;
        mobitel = initMobitel;
        strosek = initStrosek;
        maxOseb = initOseb;
        zavarovanje = initZavarovanje;
        avto = initAvto;
        datum = initDatum;
        ime = initIme;
        cas = initCas;

    }

    public String getIz() {
        return iz;
    }

    public void setIz(String iz) {
        this.iz = iz;
    }

    public String getKam() {
        return kam;
    }

    public void setKam(String kam) {
        this.kam = kam;
    }

    public String getMobitel() {
        return mobitel;
    }

    public void setMobitel(String mobitel) {
        this.mobitel = mobitel;
    }

    public Integer getStrosek() {
        return strosek;
    }

    public void setStrosek(Integer strosek) {
        this.strosek = strosek;
    }

    public Integer getMaxOseb() {
        return maxOseb;
    }

    public void setMaxOseb(Integer maxOseb) {
        this.maxOseb = maxOseb;
    }

    public Boolean getZavarovanje() {
        return zavarovanje;
    }

    public void setZavarovanje(Boolean zavarovanje) {
        this.zavarovanje = zavarovanje;
    }

    public String getAvto() {
        return avto;
    }

    public void setAvto(String avto) {
        this.avto = avto;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}