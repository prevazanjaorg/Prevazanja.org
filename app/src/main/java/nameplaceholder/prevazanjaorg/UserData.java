package nameplaceholder.prevazanjaorg;

import android.util.Log;

/**
 * Created by Jaka on 31/10/2017.
 */

public class UserData {
   public String body;
   public String sender;
   public String response;
   public String prevozID;
   public int tip;
   public static final int PREVOZ = 0;
   public static final int STANJE = 1;
   public static final int REZERVACIJA = 2;
   public static final int SENT = 3;
   public static final int BAD_SMS = 4;
   public static final int PREKLIC = 5;
   public static final int PRIVATE = 6;


    public UserData(String s, String t){
        this.body = t;
        this.sender = s;
        this.response = "";
        this.tip = PREVOZ;
        Log.e("UserData-NEW UserData: " , sender + "\n" + body + "\n" + "TIP: " + tip);
    }

    public UserData(UserData a){
        this.body = a.body;
        this.sender = a.sender;
        this.response = a.response;
        this.prevozID = a.prevozID;
        this.tip = a.tip;
    }

}
