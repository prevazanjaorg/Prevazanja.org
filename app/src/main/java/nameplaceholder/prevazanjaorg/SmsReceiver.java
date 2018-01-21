package nameplaceholder.prevazanjaorg;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Scanner;


/**
 * Created by Jaka on 26. 10. 2017.
 */


public class SmsReceiver extends BroadcastReceiver {

    public boolean running = false;
    private Context contXt;


    public SmsReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) { // intenti so filtrirani v androidmanifest v receiver intentfilter: action == intent
        contXt = context;

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(contXt);
        boolean isChecked = settings.getBoolean("sporocanje_switch", false);

        if(isChecked) { // dobi nekak iz nastavitev če je vklopljen sms sistem
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) { // če je intent SMS_RECEIVED
               /*
                if(checkifRunning()) {
                    Log.e("SMSRec-Service:", "Service is already running");
                }
                else {
                    Intent SMSserviceIntent = new Intent(context, SMSBackgroundService.class);
                    context.startService(SMSserviceIntent);
                    Log.e("SMSRec-Service:", "There is no service running, starting service..");
                }
                */
                Log.e("SMSRec-SMSData>>>", "NEW SMSData");
                Bundle data = intent.getExtras();
                if (data != null) {
                    //GET INFO
                    Object[] pdus = (Object[]) data.get("pdus");
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String msgbody = smsMessage.getDisplayMessageBody();

                    //CHECK FOR PREVOZ
                    if (prevozSMS(sender, msgbody)) {
                        SMSData novSMS = new SMSData(sender, msgbody);
                        if (GetUserDataDetails(novSMS)) {
                            Intent novSMSintent = new Intent(context,SMSBackgroundService.class);
                            novSMSintent.putExtra("SMSData",novSMS);
                            context.startService(novSMSintent);
                            Log.e("BackgroundService:>> ", novSMS.body + " " + novSMS.sender + " " + novSMS.prevozID + " " + novSMS.tip);

                        }

                    }
                }
            } else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) { // če je intent BOOT_COMPLETE
                Intent SMSserviceIntent = new Intent(context, SMSBackgroundService.class);
                context.startService(SMSserviceIntent);
                Log.e("SMSBoot:>>", "SMSBackgroundService started");
            }
        }
    }

    boolean prevozSMS(String sender, String body){
        if(body.length() < 5){
            return false;
        }
        else if(body.toLowerCase().toLowerCase().substring(0,6).equals("prevoz")){
            Log.e("SMSRec-PREVOZ Data>>>: ", "YES");
            return true;
        }
        Log.e("SMSRec-BAD Data>>>: ", "PREVOZ NOT FOUND");
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
                Log.e("SMSRec-SCANNER:>> ", "ToastSMS START COMMAND");
                curr.tip = SMSData.START;
                return true;
            }
            else if (ukaz.toLowerCase().equals("admin")) {
                curr.tip = SMSData.PRIVATE;
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

    public boolean checkifRunning(){
        ActivityManager manager = (ActivityManager) contXt.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (SMSBackgroundService.class.equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
}