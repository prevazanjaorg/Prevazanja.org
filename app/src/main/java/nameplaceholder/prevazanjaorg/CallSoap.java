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

        String SOAP_ADDRESS="http://213.161.3.238/prevozi/webservice.asmx";
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
