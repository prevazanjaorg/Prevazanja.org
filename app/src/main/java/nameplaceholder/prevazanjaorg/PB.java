package nameplaceholder.prevazanjaorg;

import android.util.Log;

import java.util.Vector;

/**
 * Created by Jaka on 03/11/2017.
 */

public class PB {
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
