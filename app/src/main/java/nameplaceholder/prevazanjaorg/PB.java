package nameplaceholder.prevazanjaorg;

import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

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



    boolean RezervirajSedez(UserData curr){
        SoapObject Request = new SoapObject(NAMESPACE,SOAP_REZERIVRAJSEDEZ);
        PropertyInfo novarezervacijaMobitel = new PropertyInfo();
        novarezervacijaMobitel.setName("stevilka");
        novarezervacijaMobitel.setValue(curr.sender);
        novarezervacijaMobitel.setType(String.class);

        PropertyInfo novarezervacijaPrevozID = new PropertyInfo();
        novarezervacijaPrevozID.setName("prevozID");
        novarezervacijaPrevozID.setValue(curr.prevozID);
        novarezervacijaPrevozID.setType(String.class);

        Request.addProperty(novarezervacijaMobitel);
        Request.addProperty(novarezervacijaPrevozID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
        try{
            androidHttpTransport.call(SOAP_REZERIVRAJSEDEZ,envelope);
            SoapObject response = (SoapObject)envelope.getResponse();
            if(Boolean.parseBoolean(response.getProperty(0).toString())) {
                rezervacija = "Sedež uspešno rezerviran! \nPrevozID: " + curr.prevozID;
                curr.response = rezervacija;
                return true;
            }
            else{
                rezervacija = "Rezervacija ni uspela poskusite kasneje";
                Log.e("PB:CONN>>", "Napaka v bazi");
                return false;
            }
        }catch(Exception e){
            rezervacija = "Rezervacija ni uspela poskusite kasneje";
            Log.e("PB:CONN>>", e.getMessage());
            return false;
        }
    }

    boolean PrekliciRezervacijo(UserData curr){
        try{
            curr.response = "Rezervacija prevoza preklicana prevozID: " + curr.prevozID;
            return true;
        }catch(Exception e){
            rezervacija = "Preklic prevoza ni uspel poskusite kasneje";
            Log.e("PB:CONN>>", e.getMessage());
            return false;
        }
    }


    Vector<UserData> GetRezervacijeFromPB(){
        Vector<UserData> rezervacije = new Vector<UserData>();
        try{
            return rezervacije;
        }catch(Exception e){
            Log.e("PB:CONN>>", e.getMessage());
            return rezervacije;
        }
    }
}
