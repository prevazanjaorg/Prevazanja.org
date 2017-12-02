package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Jaka on 8. 11. 2017.
 */

class PB {
    final String NAMESPACE = "http://nikozver.org";
    final String SOAP_REZERIVRAJSEDEZ = "http://nikozver.org/RezervirajSedez";
    final String URL = "http://89.142.135.17/VipEvents/Services/Basicservices.asmx";
    String stanje;
    String rezervacija;

    public PB(){
    }



    boolean PrekliciRezervacijo(SMSData curr){
        try{
            curr.response = "Rezervacija prevoza preklicana prevozID: " + curr.prevozID;
            return true;
        }catch(Exception e){
            rezervacija = "Preklic prevoza ni uspel poskusite kasneje";
            Log.e("PB:CONN>>", e.getMessage());
            return false;
        }
    }


    Vector<SMSData> GetRezervacijeFromPB(){
        Vector<SMSData> rezervacije = new Vector<SMSData>();
        try{
            return rezervacije;
        }catch(Exception e){
            Log.e("PB:CONN>>", e.getMessage());
            return rezervacije;
        }
    }
}

class ReservationManager {
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

    void sendRezervacije(boolean log){
        if(!AktivniPrevozi.isEmpty()) {
            for (Prevoz p : AktivniPrevozi) {
                for (Uporabnik u : p.getRezervacije()) {
                    String response = "Imate rezerviran prevoz: " + p.getCas() + " - " + p.getDatum() + " - " + p.getKam();
                    sendSMS(u.getTelefon(), response);
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

public class ToastSMS {
    public PB BAZA;
    public ReservationManager RManager;

    public int STATUS;
    static public int RUNNING = 1;
    static public int STOPPED = 0;

    private Context contXt;

    public ToastSMS(){
        BAZA = new PB();
        RManager = new ReservationManager();
        STATUS = ToastSMS.RUNNING;
    }

    boolean FormResponse(SMSData curr){
        if(curr.tip == SMSData.STANJE && STATUS == ToastSMS.RUNNING){
            if(RManager.GetStanje(curr)){
                Log.e("RM-RES", "STANJE GOOD");
                return true;
            }
            else{
                Log.e("RM-RES", "STANJE BAD");
                return true;
            }
        }
        else if(curr.tip == SMSData.REZERVACIJA){
            if(RManager.RezervirajSedez(curr)){
                Log.e("RM-RES", "REZERVACIJA GOOD");
                return true;
            }
            else{
                Log.e("RM-RES", "REZERVACIJA BAD");
                return false;
            }
        }
        else if(curr.tip == SMSData.PREKLIC){
            if(RManager.PrekliciRezervacijo(curr)){
                Log.e("RM-RES", "REZERVACIJA GOOD");
                return true;
            }
            else{
                Log.e("RM-RES", "REZERVACIJA BAD");
                return false;
            }
        }
        else{
            curr.response = "Napaka pri interpretaciji sporočila";
            Log.e("RM-EXCEPT:>> ", "NAPACEN TIP");
        }
        Log.e("RM-SCANNER:>> ", "empty");
        return false;
    }

    public boolean ProcessNewUser(SMSData a){ //novSMS iz MainActivity
        printToast("Nov SMSData prejet: " + a.sender + " " + a.body + " TIP:" + a.tip, Toast.LENGTH_LONG);
        if(FormResponse(a)) {
            Log.e("ToastSMS-GOOD RES:>>: ", a.sender +"\n"+ a.response + " TIP:" + a.tip);
            printToast("Nov SMSData prejet: " + a.sender + " " + a.body, Toast.LENGTH_LONG);
            RManager.IOSMS.push(a);
            if(a.tip == SMSData.REZERVACIJA){
                Log.e("ToastSMS-Nova rezerv:>>", a.sender);
            }
            RManager.sendQueue(true);
            return true;
        }
        else{
            RManager.IOSMS.push(a);
            Log.e("ToastSMS-BAD RES:>>: ", a.sender + "\n" + a.body + "\n" + a.response);
            RManager.sendQueue(true);
            return false;
        }
    }

    public void bindContext(Context a){
        contXt = a;
    }

    public void printToast(String text, int dur){
        Toast.makeText(contXt,text, dur).show();
    }

    public void Stop(SMSData a){
        RManager.sendSMS(a.sender,"Prevoz sms service stopped. Send prevoz start to start it again!");
        STATUS = ToastSMS.STOPPED;
    }

    public void Start(SMSData a){
        RManager.sendSMS(a.sender,"Prevoz sms service started! Send prevoz stop to stop it.");
        STATUS = ToastSMS.RUNNING;
    }

        /*

    void deleteDQUEUE() {
        if (!deleteQueue.empty()) {
            do {
                SMSData tmp = deleteQueue.pop();
                Log.e("Delete QUEUE: ", tmp.sender + "\n" + tmp.body);
                deleteSMS(tmp);
            } while (deleteQueue.empty() == false);
        }
        else{
            Log.e("Delete QUEUE: ", "EMPTY");
        }
    }

    void deleteSMS(SMSData sms){
        Cursor c = contXt.getContentResolver().query(Uri.parse("content://sms/"),
                                                    new String[]{"_id", "thread_id", "address", "person", "date", "body"},
                                                    null, null, null);
        Log.e("log>>>", "Deleting...");
        try {
            if ( c != null && c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    long threadID = c.getLong(1);
                    String address = c.getString(2);
                    String body = c.getString(5);
                    String date = c.getString(3);
                    if (sms.body.equals(body) && sms.sender.equals(address)) {
                        Log.e("log>>>", address + " " + body + " " + date);
                        contXt.getContentResolver().delete(Uri.parse("content://sms/conversations/" + threadID), "_id = ?",null);
                        Log.e("log>>>", "Delete success......");
                    }
                } while (c.moveToNext());
            }
        }catch (Exception e){
            Log.e("log>>>", e.toString());
        }

    }
    */
}
