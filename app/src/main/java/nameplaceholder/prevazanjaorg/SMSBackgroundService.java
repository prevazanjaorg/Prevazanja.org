
package nameplaceholder.prevazanjaorg;


import android.app.ProgressDialog;
import android.app.Service;
import android.content.*;
import android.os.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class SMSBackgroundService extends Service {
    public Context context = this;
    public Handler handler;
    public Runnable runnable;
    private ToastSMS SMSsistem;
    private SmsReceiver receiver;
    private static ArrayList<Prevoz> aktivniPrevozi = new ArrayList<>();
    private static ArrayList<Prevoz> dbprevozi = new ArrayList<>();



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class AsyncCallSoapVrniPrevoze extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(getApplicationContext());

        @Override
        protected String doInBackground(String... strings) {
            CallSoap CS = new CallSoap();

            String response = CS.VrniPrevoze();
            return response;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            dialog.dismiss();
            if(result.substring(0,1).equals("j"))
                new AsyncCallSoapVrniPrevoze().execute();
            Document doc = Jsoup.parse(result);

            //Log.d("krneki", doc.toString());
            String prevozi=doc.toString();
            Prevoz p;
            int i = 0;
            int j;
            while (i < 50000) {
                i = i + prevozi.substring(i).indexOf("<string>");
                i+=16; // zamaknemo naprej za <string>+\n
                if (prevozi.substring(i).contains("<string>")) {
                    int id = Integer.valueOf(prevozi.substring(i, i + prevozi.substring(i).indexOf('|'))); // zdaj smo pri delimiterju (ID)
                    i = i + prevozi.substring(i).indexOf('|');
                    String iz = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // pri naslednjem delimeterju (iz)
                    i = i + prevozi.substring(i).indexOf('|');
                    String ka = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // kam
                    i = i + prevozi.substring(i).indexOf('|');
                    String te = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // telefon
                    i = i + prevozi.substring(i).indexOf('|');
                    int os = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); // oseb
                    i = i + prevozi.substring(i).indexOf('|');
                    int mo = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); // maxoseb
                    i = i + prevozi.substring(i).indexOf('|');
                    Boolean za = Boolean.parseBoolean(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); //zavarovanje
                    i = i + prevozi.substring(i).indexOf('|');
                    String av = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // avto
                    i = i + prevozi.substring(i).indexOf('|');
                    DateTimeFormatter dtf = DateTimeFormat.forPattern("d.M. H:m");
                    String tempDatumString = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|') + prevozi.substring(i + prevozi.substring(i).indexOf('|') + 1).indexOf('|') + 1); // datum string
                    tempDatumString = tempDatumString.substring(tempDatumString.indexOf(',') + 1);
                    StringBuilder sb = new StringBuilder(tempDatumString);
                    sb.deleteCharAt(tempDatumString.indexOf('|'));
                    sb.deleteCharAt(tempDatumString.indexOf('.')+1);
                    sb.deleteCharAt(0);
                    tempDatumString = sb.toString();
                    i = i + prevozi.substring(i).indexOf('|');
                    DateTime dt = dtf.parseDateTime(tempDatumString);
                    i++; // preskočimo uro
                    i = i + prevozi.substring(i).indexOf('|');
                    String im = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // ime
                    i = i + prevozi.substring(i).indexOf('|');
                    String op = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // opis
                    i = i + prevozi.substring(i).indexOf('|');
                    Double la = Double.valueOf((prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))).replace(',','.')); //latitude
                    i = i + prevozi.substring(i).indexOf('|');
                    Double lo = Double.valueOf((prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))).replace(',','.')); //longtitude
                    i = i + prevozi.substring(i).indexOf('|');
                    int ra = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); // radius
                    i = i + prevozi.substring(i).indexOf('|');
                    int fk = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('\n'))); // fkUporabnik
                    i = i + prevozi.substring(i).indexOf('\n');
                    p = new Prevoz(iz, ka, te, 10.0, os, mo, za, av, im, dt, lo,lo,ra);
                    dbprevozi.add(p);
                }
                else
                    break;
            }

            if(!aktivniPrevozi.isEmpty()){
                aktivniPrevozi.clear();
            }

            for(Prevoz prev : dbprevozi){
                if(prev.getMobitel().equals("1234567")){
                    //Log.e("MOB",prev.getMobitel() + " == 1234567");
                    aktivniPrevozi.add(prev);
                }
            }
            SMSsistem.RManager.BindAktivniPrevozi(aktivniPrevozi);
            return;
        }
    }

    @Override
    public void onCreate() {
        SMSsistem = new ToastSMS();
        SMSsistem.bindContext(this);
        Log.e("SMSSistem:>>" , "SMSSistem RUNNING...OK");
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "ToastSMS Service stopped!", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        try {
            if (intent.getSerializableExtra("SMSData") != null) {
                SMSData novSMS = (SMSData) intent.getSerializableExtra("SMSData");
                if(aktivniPrevozi.isEmpty()){
                    new AsyncCallSoapVrniPrevoze().execute();
                }
                else if (novSMS.tip == SMSData.STOP) {
                    SMSsistem.Stop(novSMS);
                    //stopSelf();
                    Log.e("BackgroundService:>> ", "ToastSMS STOP COMMAND");
                } else if (novSMS.tip == SMSData.START) {
                    SMSsistem.Start(novSMS);
                } else if (novSMS.tip == SMSData.PRIVATE) {
                    SMSsistem.RManager.sendRezervacije(true);
                } else if (SMSsistem.STATUS == ToastSMS.RUNNING) {
                    SMSsistem.ProcessNewUser(novSMS);
                    Log.e("SMSSistem:>>", "SMSData RECEIVED" + " " + novSMS.sender + " " + novSMS.tip);
                } else {
                    Log.e("BackgroundService:>> ", "ToastSMS IS STOPPED");
                }
                return super.onStartCommand(intent, flags, startID);
            }
            else if(intent.getSerializableExtra("MOJIPREVOZI") != null){
                aktivniPrevozi = (ArrayList<Prevoz>)intent.getSerializableExtra("MOJIPREVOZI");
                SMSsistem.RManager.BindAktivniPrevozi(aktivniPrevozi);
            }

        }catch (Exception e){
            Log.e("SMSService: ",e.getMessage());
        }
        Toast.makeText(this, "ToastSMS Service started!", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
}