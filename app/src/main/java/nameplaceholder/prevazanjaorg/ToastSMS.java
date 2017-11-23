package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jaka on 8. 11. 2017.
 */

public class ToastSMS {
    public PB BAZA;
    public ReservationManager RManager;
//
    public boolean running;
    private Context contXt;

    public ToastSMS(){
        BAZA = new PB();
        RManager = new ReservationManager();
        running = true;
    }

    public boolean SendReservationQueue(boolean log, boolean IgnoreIfAlreadySentBefore){
        try{
            RManager.sendRezervacije(log, IgnoreIfAlreadySentBefore);
            return true;
        }catch (Exception e){
            Log.e("SMSRec-SEND ERROR:>>", "Sending rezervation queue failed...");
            return false;
        }
    }

    boolean FormResponse(SMSData curr){
        if(curr.tip == SMSData.STANJE){
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
