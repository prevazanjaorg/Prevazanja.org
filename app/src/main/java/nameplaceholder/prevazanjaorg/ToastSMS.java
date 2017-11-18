package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jaka on 8. 11. 2017.
 */

public class ToastSMS {
    public PB BAZA;
    private ReservationManager RManager;
//
    public boolean running;
    private Context contXt;

    public ToastSMS(){
        BAZA = new PB();
        RManager = new ReservationManager(BAZA.GetRezervacijeFromPB());
        running = true;
    }

    public boolean SendResponseQueue(boolean log){
        try{
            RManager.sendQueue(log);
            return true;
        }catch (Exception e){
            Log.e("SMSRec-SEND ERROR:>>", "Sending response queue failed...");
            return false;
        }
    }

    public boolean SendReservationQueue(boolean log, boolean IgnoreIfAlreadySentBefore){
        try{
            RManager.sendRezervationVector(log, IgnoreIfAlreadySentBefore);
            return true;
        }catch (Exception e){
            Log.e("SMSRec-SEND ERROR:>>", "Sending rezervation queue failed...");
            return false;
        }
    }

    public boolean ProcessNewUser(UserData a){ //novSMS iz MainActivity
        printToast("Nov UserData prejet: " + a.sender + " " + a.body + " TIP:" + a.tip, Toast.LENGTH_LONG);
        if(BAZA.FormResponse(a)) {
            Log.e("ToastSMS-GOOD RES:>>: ", a.sender +" "+ a.response + " TIP:" + a.tip);
            printToast("Nov UserData prejet: " + a.sender + " " + a.body, Toast.LENGTH_LONG);
            RManager.IOSMS.push(a);
            if(a.tip == UserData.REZERVACIJA){
                UserData b = new UserData(a);
                b.response = "Imate rezerviran sedež! čez 15 min gremo!" + "\n" + "prevozID: " + b.prevozID;
                RManager.Rezervacije.add(b);
                Log.e("ToastSMS-Nova rezerv:>>", a.sender);
            }
            else if(a.tip == UserData.PREKLIC){
                RManager.PrekliciRezervacijo(a);
            }
            SendResponseQueue(true);
            return true;
        }
        else{
            Log.e("ToastSMS-BAD RES:>>: ", a.sender + "\n" + a.body + "\n" + a.response);
            RManager.sendSMS(a);
            return false;
        }
    }

    public void bindContext(Context a){
        contXt = a;
    }

    public void printToast(String text, int dur){
        Toast.makeText(contXt,text, dur).show();
    }


        /*

    void deleteDQUEUE() {
        if (!deleteQueue.empty()) {
            do {
                UserData tmp = deleteQueue.pop();
                Log.e("Delete QUEUE: ", tmp.sender + "\n" + tmp.body);
                deleteSMS(tmp);
            } while (deleteQueue.empty() == false);
        }
        else{
            Log.e("Delete QUEUE: ", "EMPTY");
        }
    }

    void deleteSMS(UserData sms){
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
