package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Prevoz implements Serializable {
    public String iz;
    public String kam;
    public String mobitel;
    public double strosek;
    public int oseb;
    public int maxOseb;
    public Boolean zavarovanje;
    public String avto;
    public DateTime datumObjave;
    public String ime;
    public DateTime casDatum;
    public int ID;
    public ArrayList<Uporabnik> rezervacije;
    public String opis;
    public Context c;
    public double latitude;
    public double longitude;
    public int radius;
    public ArrayList<Integer> ocene;
    public Boolean reported = false;

    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;

    public Boolean getReported() {
        return reported;
    }

    public void setReported(Boolean reported) {
        this.reported = reported;
    }

    public int addRezervacija (Uporabnik u){
        if (rezervacije.size()<maxOseb)
            rezervacije.add(u);
        else {
            Log.d("errRezervacije", "addRezervacija: rezervacije so polne");
            return -1;
        }
        return 1;
    }

    public int remRezervacija (Uporabnik u) {
        for (int i = 0; i < rezervacije.size(); i++) {
            if (rezervacije.get(i) == u) {
                rezervacije.remove(i);
                return 1;
            }
        }
        Log.d("errRezervacija", "remRezervacija: nobena izbrisana, ker ni bilo ujemanja");
        return -1;
    }

    public Prevoz(Prevoz t){
        this.rezervacije = t.rezervacije;
        this.avto = t.avto;
        this.c = t.c;
        this.casDatum = t.casDatum;
        this.datumObjave = t.datumObjave;
        this.ID = t.ID;
        this.ime = t.ime;
        this.iz = t.iz;
        this.kam = t.kam;
        this.maxOseb = t.maxOseb;
        this.mobitel = t.mobitel;
        this.opis = t.opis;
        this.strosek = t.strosek;
        this.oseb = t.oseb;
        this.zavarovanje = t.zavarovanje;
        this.latitude = t.latitude;
        this.longitude = t.longitude;
        this.radius = t.radius;
        this.ocene = t.ocene;
    }

    public Prevoz(String initIz, String initKam, String initMobitel, double initStrosek, int initOseb, int initMaxOseb, Boolean initZavarovanje, String initAvto, String initIme, DateTime initCas,double latitude,double longitude,int rad, ArrayList<Integer> initOcene) {
        this.iz = initIz;
        this.kam = initKam;
        this.mobitel = initMobitel;
        this.strosek = initStrosek;
        this.oseb = initOseb;
        this.maxOseb = initMaxOseb;
        this.zavarovanje = initZavarovanje;
        this.avto = initAvto;
        this.ime = initIme;
        this.casDatum = initCas;
        this.rezervacije = new ArrayList<Uporabnik>();
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = rad;
        this.ocene = initOcene;
    }

    public ArrayList<Integer> getOcene() {
        return ocene;
    }

    public void setOcene(ArrayList<Integer> ocene) {
        this.ocene = ocene;
    }

    public Double getOcena() {
        Integer sum = 0;
        for (Integer a:ocene) {
            sum += a;
        }
        return (double)sum/ocene.size();
    }

    public Integer getSteviloOcen() {
        return ocene.size();
    }

    public int getOseb() {
        return oseb;
    }

    public void setOseb(int oseb) {
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
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

    public int getMaxOseb() {
        return maxOseb;
    }

    public void setMaxOseb(int maxOseb) {
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