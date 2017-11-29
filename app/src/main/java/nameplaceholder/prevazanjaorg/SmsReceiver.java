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

    public boolean running; // nekak iz naastavitev če je res, drugače skso laufa
    private Context contXt;
    private static OnReceiveSMS onreceive;


    public SmsReceiver(){
        running = true;
        Log.e("SMSRec-INIT", "INIT:....:");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        contXt = context;
        Log.e("SMSRec-SMSData>>>","NEW SMSData" );
        Bundle data  = intent.getExtras();
        if (data != null && running == true) {
            //GET INFO
            Object[] pdus = (Object[]) data.get("pdus");
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String msgbody = smsMessage.getDisplayMessageBody();

            //CHECK FOR PREVOZ
            if (prevozSMS(sender, msgbody)) {
                SMSData novsms = new SMSData(sender,msgbody);
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

    boolean GetUserDataDetails(SMSData curr){
        String ukaz;
        Scanner scnr = new Scanner(curr.body);
        ukaz = scnr.next();

        while (scnr.hasNext()) {
            ukaz = scnr.next();
            if (ukaz.toLowerCase().equals("stanje")) {
                Log.e("SMSRec-SCANNER:>>", "stanje");
                curr.tip = SMSData.STANJE;
                return true;
            }
            else if (ukaz.toLowerCase().equals("rezerviraj")) {
                Log.e("SMSRec-SCANNER:>> ", "rezerviraj");
                try {
                    curr.prevozID = Integer.parseInt(scnr.next());
                }
                catch (Exception e){
                    Log.e("SMSRec-SCANNER:>> ", "rezerviraj PREVOZ ID NOT FOUND");
                    return false;
                }
                if(scnr.hasNext()){
                    return false;
                }
                curr.tip = SMSData.REZERVACIJA;
                return true;
                }
            else if (ukaz.toLowerCase().equals("preklic")) {
                Log.e("SMSRec-SCANNER:>> ", "preklic rezervacije");
                try {
                    curr.prevozID = Integer.parseInt(scnr.next());
                }
                catch (Exception e){
                    Log.e("SMSRec-SCANNER:>> ", "rezerviraj PREVOZ ID NOT FOUND");
                    return false;
                }
                if(scnr.hasNext()){
                    return false;
                }
                curr.tip = SMSData.PREKLIC;
                return true;
            }
            else if (ukaz.toLowerCase().equals("stop")) {
                Log.e("SMSRec-SCANNER:>> ", "ToastSMS STOP COMMAND");
                curr.tip = SMSData.STOP;
                return true;
            }
            else if (ukaz.toLowerCase().equals("start")) {
                Log.e("SMSRec-SCANNER:>> ", "ToastSMS STOP COMMAND");
                curr.tip = SMSData.START;
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