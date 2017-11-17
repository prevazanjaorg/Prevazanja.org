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
    public Vector<UserData> Rezervacije;

    public ReservationManager(){
        IOSMS = new Stack<UserData>();
        Rezervacije = new Vector<UserData>();
    }

    void sendSMS(UserData a){
        SmsManager manager = SmsManager.getDefault();
        try {
            ArrayList<String> deli = manager.divideMessage(a.response);
            manager.sendMultipartTextMessage(a.sender, null, deli, null, null);
            //manager.sendTextMessage(a.sender,null,a.response,null,null);
        }catch (Exception e) {
             Log.e("RManager-Except.>>>;: ", a.sender + " - " + a.response);
        }
    }

    void sendQueue(boolean log){
        if(!IOSMS.empty()) {
            do{
                UserData tmp = IOSMS.pop();
                if(log) {
                    Log.e("RManager-QUEUE: ", "SENDING " + tmp.sender);
                }
                sendSMS(tmp);
            }while(IOSMS.empty() == false);
        }
        else{
            Log.e("RManager-QUEUE: ", "EMPTY");
        }
    }

    void sendRezervationVector(boolean log, boolean IgnoreIfAlreadySentBefore){
        if(!Rezervacije.isEmpty()) {
            for(UserData rezervacija : Rezervacije){
                if(IgnoreIfAlreadySentBefore) {
                    sendSMS(rezervacija);
                }
                if(log) {
                    Log.e("RManager-LOG", rezervacija.response + " " + rezervacija.sender);
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
            for(UserData rezervacija : Rezervacije){
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
        for(UserData rezervacija : Rezervacije){
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
