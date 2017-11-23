package nameplaceholder.prevazanjaorg;

import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Jaka on 8. 11. 2017.
 */

public class ReservationManager {
    public Stack<SMSData> IOSMS;
    public ArrayList<Prevoz> AktivniPrevozi;

    public ReservationManager(){
        IOSMS = new Stack<SMSData>();
    }

    public boolean BindAktivniPrevozi(ArrayList<Prevoz> aktivniPrevozi){
        AktivniPrevozi = aktivniPrevozi;
        int i = 0;
        for(Prevoz p : AktivniPrevozi){
            p.setID(i);
            i++;
        }
        Log.e("RM:" , "AktivniPrevozi BIND OK - BIND SIZE:" + AktivniPrevozi.size());
        return true;
    }

    void sendSMS(String mobitel, String response){
        SmsManager manager = SmsManager.getDefault();
        try {
            ArrayList<String> deli = manager.divideMessage(response);
            manager.sendMultipartTextMessage(mobitel, null, deli, null, null);
        }catch (Exception e) {
             Log.e("RManager-Except.>>>;: ", mobitel + " - " + response);
        }
    }

    boolean sendQueue(boolean log){
        if(!IOSMS.empty()) {
            do{
                SMSData tmp = IOSMS.pop();
                if(log) {
                    Log.e("RManager-QUEUE: ", "SENDING " + tmp.sender);
                }
                sendSMS(tmp.sender,tmp.response);
            }while(IOSMS.empty() == false);
            return true;
        }
        else{
            Log.e("RManager-QUEUE: ", "EMPTY");
            return false;
        }
    }

    void sendRezervacije(boolean log, boolean IgnoreIfAlreadySentBefore){
        if(!AktivniPrevozi.isEmpty()) {
            for (Prevoz p : AktivniPrevozi) {
                for (Uporabnik u : p.getRezervacije()) {
                    if (IgnoreIfAlreadySentBefore) {
                        String response = "Imate rezerviran prevoz: " + p.getCas() + " - " + p.getDatum() + " - " + p.getKam();
                        sendSMS(u.getTelefon(), response);
                    }
                    if (log) {
                        Log.e("RManager-LOG", u.getTelefon());
                    }
                }
            }
        }
        else{
            if(log) {
                Log.e("RManager-VECTOR: ", "Ni rezervacij");
            }
        }
    }

    boolean RezervirajSedez(SMSData curr){
        if(AktivniPrevozi.isEmpty()){
            curr.response = "Uporabnik nima prijavljenih prevozov";
            return false;
        }
        for(Prevoz p : AktivniPrevozi){
            if(p.getID() == curr.prevozID){
                p.addSMSRezervacija(curr);
                curr.response = "Sedež uspešno rezerviran ID:" + curr.prevozID + " " + p.getIz() + " - " + p.getKam() + "\n";
                LogReservations(p);
                return true;
            }
        }
        curr.response = "Rezervacija ni uspela prevozID: " + curr.prevozID + " ne obstaja" + "\n";
        return false;
    }

    boolean GetStanje(SMSData curr){
        if(AktivniPrevozi.isEmpty()){
            curr.response = "Uporabnik nima prijavljenih prevozov";
            return false;
        }
        String stanje = "";
        for(Prevoz p : AktivniPrevozi){
            stanje += "ID: " + p.getID() + " - " + p.getDatum() + " - " + p.getCas() + " - " + p.getIz() + " - " + p.getKam() + " - " + p.getMaxOseb() + "\n";

        }
        curr.response = stanje;
        return true;
    }

    public boolean LogReservations(Prevoz p){
        Log.e("REZERV", p.getKam());
        if(!p.getRezervacije().isEmpty()) {
            int i = 1;
            for(Uporabnik u : p.getRezervacije()) {
                Log.e("-  ", i + " - " + u.getTelefon() + " " + u.getUsername() + "\n");
                i++;
            }
            return true;
        }
        else{
            Log.e("RManager-REZERV: ", "Ni rezervacij");
            return false;
        }
    }

    boolean PrekliciRezervacijo(SMSData curr){
        for(Prevoz p : AktivniPrevozi){
            if(p.getID() == curr.prevozID) {
                if(p.getRezervacije().isEmpty()){
                    curr.response = "Uporabnik nima prijavljenih prevozov, rezervacija ni bila preklicana";
                    return false;
                }
                p.remRezervacijaMobitel(curr);
                LogReservations(p);
                curr.response = "Rezervacija uspešno preklicana ID:" + curr.prevozID + " " + p.getIz() + " - " + p.getKam() + "\n";
                return true;
            }
        }
        return false;
    }


    void RefreshRezervacije(ArrayList<Prevoz> rfrsh){
        AktivniPrevozi = rfrsh;
    }
}
