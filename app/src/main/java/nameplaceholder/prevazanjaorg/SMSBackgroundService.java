
package nameplaceholder.prevazanjaorg;


import android.app.Service;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class SMSBackgroundService extends Service {
    public Context context = this;
    public Handler handler;
    public Runnable runnable;
    private ToastSMS SMSsistem;
    private SmsReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();
        String dateTime = "29.11.2017 16:00:00";
        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        DateTime drugCas = dtf.parseDateTime(dateTime);
        ArrayList<Integer> ocene = new ArrayList<Integer>();
        ocene.add(10); ocene.add(9); ocene.add(7); ocene.add(10); ocene.add(10);
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 3, 4, false, "Toyota Yaris črne barve", "Polar", trenutniCas,-34,151,100, 9);
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "040256339", 5.0, 3, 3, false, "Toyota Hilux", "Polar", drugCas,-50,150,250, 8);
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", "04025897464", 7.0, 1, 4, true, "Mazda 3", "Polar", drugCas.plusDays(2),66,-50,150, 7);
        aktivniPrevozi.add(dummyPrevoz);
        aktivniPrevozi.add(dummyPrevoz2);
        aktivniPrevozi.add(dummyPrevoz3);


        SMSsistem = new ToastSMS();
        SMSsistem.RManager.BindAktivniPrevozi(aktivniPrevozi);
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
                if (novSMS.tip == SMSData.STOP) {
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
        }catch (Exception e){
            Log.e("SMSService: %s",e.getMessage());
        }
        Toast.makeText(this, "ToastSMS Service started!", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
}