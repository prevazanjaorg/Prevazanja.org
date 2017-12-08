package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Prevoz {
    String iz;
    String kam;
    String mobitel;
    Double strosek;
    Integer oseb;
    Integer maxOseb;
    Boolean zavarovanje;
    String avto;
    DateTime datumObjave;
    String ime;
    DateTime casDatum;
    Integer ID;
    ArrayList<Uporabnik> rezervacije;
    String opis;
    Context c;

    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;

    public Integer getOseb() {
        return oseb;
    }

    public void setOseb(Integer oseb) {
        this.oseb = oseb;
    }

    public DateTime getDatumObjave() {
        return datumObjave;
    }

    public void setDatumObjave(DateTime datumObjave) {
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

    public Prevoz(String initIz, String initKam, String initMobitel, Double initStrosek, Integer initOseb, Integer initMaxOseb, Boolean initZavarovanje, String initAvto, String initIme, DateTime initCas) {
        iz = initIz;
        kam = initKam;
        mobitel = initMobitel;
        strosek = initStrosek;
        oseb = initOseb;
        maxOseb = initMaxOseb;
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
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
        return dtf.print(casDatum);
    }

    public String getDatum() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy");
        return dtf.print(casDatum);
    }

    public String getDan() {
        int dan = casDatum.dayOfWeek().get();
        if (dan == MONDAY)
            return "Ponedeljek";
        else if (dan == TUESDAY)
            return "Torek";
        else if (dan == WEDNESDAY)
            return "Sreda";
        else if (dan == THURSDAY)
            return "Četrtek";
        else if (dan == FRIDAY)
            return "Petek";
        else if (dan == SATURDAY)
            return "Sobota";
        else if (dan == SUNDAY)
            return "Nedelja";
        else
            return "!napaka!";
    }

    public String getDanShort() {
        return getDan().substring(0,3);
    }
}