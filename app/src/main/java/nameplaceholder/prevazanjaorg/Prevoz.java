package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import org.joda.time.DateTime;

public class Prevoz {
    String iz;
    String kam;
    String mobitel;
    Double strosek;
    Integer maxOseb;
    Boolean zavarovanje;
    String avto;
    Calendar datumObjave;
    String ime;
    DateTime casDatum;
    Integer ID;
    ArrayList<Uporabnik> rezervacije;
    String opis;
    Context c;

    public Calendar getDatumObjave() {
        return datumObjave;
    }

    public void setDatumObjave(Calendar datumObjave) {
        this.datumObjave = datumObjave;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public ArrayList<Uporabnik> getRezervacije() {
        return rezervacije;
    }

    public void setRezervacije(ArrayList<Uporabnik> rezervacije) {
        this.rezervacije = rezervacije;
    }

    public void addSMSRezervacija(SMSData rezervacija) {
        Uporabnik tmp = new Uporabnik(rezervacija.sender,null);
        this.rezervacije.add(tmp);
    }

    public void remRezervacijaMobitel (SMSData rezervacija) {
        for (Iterator<Uporabnik> it = rezervacije.iterator(); it.hasNext(); ) {
            Uporabnik user = it.next();
            if (user.getTelefon().equals(rezervacija.sender)) {
                Log.e("PREVOZ RM:", user.getTelefon());
                it.remove();
            }
        }
    }



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
        rezervacije = new ArrayList<Uporabnik>();
    }

    public Prevoz() {
        rezervacije = new ArrayList<Uporabnik>();
    }  //default konstruktor

    public DateTime getCasDatum() {
        return casDatum;
    }

    public void setCasDatum(DateTime casDatum) {
        this.casDatum = casDatum;
    }

    public Prevoz(String initIz, String initKam, String initMobitel, Double initStrosek, Integer initOseb, Boolean initZavarovanje, String initAvto, String initIme, DateTime initCas) {
        iz = initIz;
        kam = initKam;
        mobitel = initMobitel;
        strosek = initStrosek;
        maxOseb = initOseb;
        zavarovanje = initZavarovanje;
        avto = initAvto;
        ime = initIme;
        casDatum = initCas;
        rezervacije = new ArrayList<Uporabnik>();
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

    public Double getStrosek() {
        return strosek;
    }

    public void setStrosek(Double strosek) {
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

    public String getCas() {
        return casDatum.toLocalTime().getHourOfDay()+":"+casDatum.toLocalTime().getMinuteOfHour();
    }

    public String getDatum() {
        return casDatum.toLocalDate().getDayOfMonth()+"."+casDatum.toLocalDate().getMonthOfYear();
    }
}