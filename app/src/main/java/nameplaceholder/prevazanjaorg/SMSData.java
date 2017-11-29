package nameplaceholder.prevazanjaorg;

import android.util.Log;

/**
 * Created by Jaka on 31/10/2017.
 */

public class SMSData {
   public String body;
   public String sender;
   public String response;
   public int prevozID;
   public int tip;
   public static final int PREVOZ = 0;
   public static final int STANJE = 1;
   public static final int REZERVACIJA = 2;
   public static final int SENT = 3;
   public static final int BAD_SMS = 4;
   public static final int PREKLIC = 5;
   public static final int STOP = -1;
   public static final int START = 6;


    public SMSData(String s, String t){
        this.body = t;
        this.sender = s;
        this.response = "";
        this.tip = PREVOZ;
        Log.e("SMSData-NEW SMSData: " , sender + "\n" + body + "\n" + "TIP: " + tip);
    }

    public SMSData(String s, String resp, int pID){
        this.sender = s;
        this.response = resp;
        this.prevozID = pID;
    }

    public SMSData(SMSData a){
        this.body = a.body;
        this.sender = a.sender;
        this.response = a.response;
        this.prevozID = a.prevozID;
        this.tip = a.tip;
    }

}
