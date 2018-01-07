package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Niko on 3. 12. 2017.
 */
public class CallSoap {
    public String Prijava(String stevilka, String username, String geslo){
        String SOAP_ACTION = "http://tempuri.org/Prijava";
        String OPERATION_NAME="Prijava";
        String NAMESPACE="http://tempuri.org/";

        String SOAP_ADDRESS="http://213.161.3.238/prevozi/webservice.asmx";
        SoapObject request = new SoapObject(NAMESPACE,OPERATION_NAME);
        PropertyInfo PI=new PropertyInfo();
        PI.setName("stevilka");
        PI.setValue(stevilka);
        PI.setType(String.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("username");
        PI.setValue(username);
        PI.setType(String.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("geslo");
        PI.setValue(geslo);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        String response;

        try{
            HttpTransportSE httpTransport=new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug=true;
            //Log.d("krneki","delaaa");
            httpTransport.call(SOAP_ACTION,envelope);
            response=httpTransport.responseDump;
        }
        catch(Exception ex){
            response = ex.toString();
            //response="Težava pri uporabi matode Prijava :/";

        }

        return response;
    }

    public String Registracija(String stevilka, String username, String mail, boolean sms_enabled, boolean avtomatski_sprejem, boolean odgovor_neznanim_Strankam, int radius_avtomatskega_sprejema, int ocena_udobja, int ocena_tocnosti, int ocena_voznika, String geslo){
        String SOAP_ACTION = "http://tempuri.org/Prijava";
        String OPERATION_NAME="Prijava";
        String NAMESPACE="http://tempuri.org/";

        //String SOAP_ADDRESS="http://213.161.3.238/prevozi/webservice.asmx";
        String SOAP_ADDRESS="http://213.161.3.238/prevozi/webservice.asmx";
        SoapObject request = new SoapObject(NAMESPACE,OPERATION_NAME);

        PropertyInfo PI=new PropertyInfo();
        PI.setName("stevilka");
        PI.setValue(stevilka);
        PI.setType(String.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("username");
        PI.setValue(username);
        PI.setType(String.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("mail");
        PI.setValue(mail);
        PI.setType(String.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("sms_enabled");
        PI.setValue(sms_enabled);
        PI.setType(boolean.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("avtomatski_sprejem");
        PI.setValue(avtomatski_sprejem);
        PI.setType(boolean.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("odgovor_neznanim_Strankam");
        PI.setValue(odgovor_neznanim_Strankam);
        PI.setType(boolean.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("radius_avtomatskega_sprejema");
        PI.setValue(radius_avtomatskega_sprejema);
        PI.setType(int.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("ocena_udobja");
        PI.setValue(ocena_udobja);
        PI.setType(int.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("ocena_tocnosti");
        PI.setValue(ocena_tocnosti);
        PI.setType(int.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("ocena_voznika");
        PI.setValue(ocena_voznika);
        PI.setType(int.class);
        request.addProperty(PI);

        PI=new PropertyInfo();
        PI.setName("geslo");
        PI.setValue(geslo);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        String response;

        try{
            HttpTransportSE httpTransport=new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug=true;
            //Log.d("krneki","delaaa");
            httpTransport.call(SOAP_ACTION,envelope);
            response=httpTransport.responseDump;
        }
        catch(Exception ex){
            response = ex.toString();
        }

        return response;
    }

    public String DodajLastnost(String lastnost){
        String SOAP_ACTION = "http://tempuri.org/VnesiLastnosti";
        String OPERATION_NAME="VnesiLastnosti";
        String NAMESPACE="http://tempuri.org/";

        String SOAP_ADDRESS="http://213.161.3.238/prevozi/webservice.asmx";
        SoapObject request = new SoapObject(NAMESPACE,OPERATION_NAME);
        PropertyInfo PI=new PropertyInfo();
        PI.setName("lastnost");
        PI.setValue(lastnost);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        String response;

        try{
            HttpTransportSE httpTransport=new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug=true;
            httpTransport.call(SOAP_ACTION,envelope);
            response=httpTransport.responseDump;
        }
        catch(Exception ex){
            response = ex.toString();
        }

        return response;
    }

    public String VrniLastnosti(){
        String SOAP_ACTION = "http://tempuri.org/VrniLastnosti";
        String OPERATION_NAME="VrniLastnosti";
        String NAMESPACE="http://tempuri.org/";

        //String SOAP_ADDRESS="http://213.161.3.238/prevozi/webservice.asmx";http://93.103.196.202/
        String SOAP_ADDRESS="http://93.103.196.202/prevozi/webservice.asmx";
        SoapObject request = new SoapObject(NAMESPACE,OPERATION_NAME);

        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        String response;

        try{
            HttpTransportSE httpTransport=new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug=true;
            //Log.d("krneki","delaaa");
            httpTransport.call(SOAP_ACTION,envelope);
            response=httpTransport.responseDump;
        }
        catch(Exception ex){
            response = ex.toString();
        }

        return response;
    }
}
