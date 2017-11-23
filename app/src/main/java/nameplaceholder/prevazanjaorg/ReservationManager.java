package nameplaceholder.prevazanjaorg;

import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Jaka on 8. 11. 2017.
 */

public class ReservationManager {
    public Stack<UserData> IOSMS;
    public ArrayList<Prevoz> AktivniPrevozi;

    public ReservationManager(){
        IOSMS = new Stack<UserData>();
    }

    public boolean BindRezervacije(ArrayList<Prevoz> aktivniPrevozi){
        AktivniPrevozi = aktivniPrevozi;
        return true;
    }

    void sendSMS(String mobitel, String response){
        SmsManager manager = SmsManager.getDefault();
        try {
            ArrayList<String> deli = manager.divideMessage(response);
            manager.sendMultipartTextMessage(mobitel, null, deli, null, null);
            //manager.sendTextMessage(a.sender,null,a.response,null,null);
        }catch (Exception e) {
             Log.e("RManager-Except.>>>;: ", mobitel + " - " + response);
        }
    }

    void sendQueue(boolean log){
        if(!IOSMS.empty()) {
            do{
                UserData tmp = IOSMS.pop();
                if(log) {
                    Log.e("RManager-QUEUE: ", "SENDING " + tmp.sender);
                }
                sendSMS(tmp.sender,tmp.response);
            }while(IOSMS.empty() == false);
        }
        else{
            Log.e("RManager-QUEUE: ", "EMPTY");
        }
    }

    boolean Add(UserData a){
        for(Prevoz p : AktivniPrevozi){
            if(p.getPrevozID() == a.prevozID){
                Uporabnik
            }
        }
    }

    void sendRezervacije(boolean log, boolean IgnoreIfAlreadySentBefore){
        if(!Rezervacije.isEmpty()) {
            for(Prevoz rezervacija : Rezervacije){
                if(IgnoreIfAlreadySentBefore) {
                    String response = "Imate rezerviran prevoz: " + rezervacija.getCas() + " "+ rezervacija.getDatum()+ " " + rezervacija.getKam();
                    sendSMS(rezervacija.getMobitel(),response);
                }
                if(log) {
                    Log.e("RManager-LOG", rezervacija.getMobitel());
                }
            }
        }
        else{
            if(log) {
                Log.e("RManager-VECTOR: ", "Ni rezervacij");
            }
        }
    }

    public boolean LogReservations(){
        if(!Rezervacije.isEmpty()) {
            for(Prevoz rezervacija : Rezervacije){
                Log.e("RManager-LOG", rezervacija.response + " " + rezervacija.sender + " ID: "+rezervacija.prevozID);
            }
            return true;
        }
        else{
            Log.e("RManager-VECTOR: ", "Ni rezervacij");
            return false;
        }
    }

    void PrekliciRezervacijo(UserData a){
        for(Prevoz rezervacija : Rezervacije){
            if(rezervacija.sender == a.sender && rezervacija.prevozID == a.prevozID){
                Rezervacije.remove((UserData)rezervacija);
            }
        }
        LogReservations();
    }


    void RefreshRezervacije(Vector<UserData> pb){
        Rezervacije = pb;
    }
}
