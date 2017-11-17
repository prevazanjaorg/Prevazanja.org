package nameplaceholder.prevazanjaorg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Scanner;


/**
 * Created by Jaka on 26. 10. 2017.
 */


public class SmsReceiver extends BroadcastReceiver {

    public boolean running;
    private Context contXt;
    private static OnReceiveSMS onreceive;


    public SmsReceiver(){
        running = true;
        Log.e("SMSRec-INIT", "INIT:....:");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        contXt = context;
        Log.e("SMSRec-UserData>>>","NEW UserData" );
        Bundle data  = intent.getExtras();
        if (data != null && running == true) {
            //GET INFO
            Object[] pdus = (Object[]) data.get("pdus");
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String msgbody = smsMessage.getDisplayMessageBody();

            //CHECK FOR PREVOZ
            if (prevozSMS(sender, msgbody)) {
                UserData novsms = new UserData(sender,msgbody);
                if(GetUserDataDetails(novsms)){
                    onreceive.messageReceived(novsms);
                }
            }
        }
    }

    boolean prevozSMS(String sender, String body){
        if(body.length() < 5){
            return false;
        }
        else if(body.toLowerCase().toLowerCase().substring(0,6).equals("prevoz")){
            Log.e("SMSRec-PREVOZ UD>>>: ", "YES");
            return true;
        }
        Log.e("SMSRec-BAD UD>>>: ", "PREVOZ NOT FOUND");
        return false;
    }

    boolean GetUserDataDetails(UserData curr){
        String ukaz;
        Scanner scnr = new Scanner(curr.body);
        ukaz = scnr.next();

        while (scnr.hasNext()) {
            ukaz = scnr.next();
            if (ukaz.toLowerCase().equals("stanje")) {
                Log.e("SMSRec-SCANNER:>>", "stanje");
                curr.tip = UserData.STANJE;
                return true;
            }
            else if (ukaz.toLowerCase().equals("rezerviraj")) {
                Log.e("SMSRec-SCANNER:>> ", "rezerviraj");
                curr.prevozID = scnr.next();
                if(scnr.hasNext()){
                    return false;
                }
                curr.tip = UserData.REZERVACIJA;
                return true;
                }
            else if (ukaz.toLowerCase().equals("preklic")) {
                Log.e("SMSRec-SCANNER:>> ", "preklic rezervacije");
                curr.prevozID = scnr.next();
                if(scnr.hasNext()){
                    return false;
                }
                curr.tip = UserData.PREKLIC;
                return true;
            }
            else{
                Log.e("SMSRec-SCANNER:>> ", "COMMAND NOT RECOGNIZED");
                return false;
            }
        }
        Log.e("SMSRec-SCANNER:>> ", "empty");
        return false;
    }

    void Off(){
        running = false;
    }

    void On(){
        running = true;
    }

    public static void bindOnReceive(OnReceiveSMS a){
        onreceive = a;
    }



}