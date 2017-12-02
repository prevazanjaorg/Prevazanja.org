
package nameplaceholder.prevazanjaorg;


import android.app.Service;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.widget.Toast;

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
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 4, false, "Toyota Yaris črne barve", "Luka", trenutniCas);
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "040256339", 5.0, 3, false, "Toyota Hilux", "Žiga", drugCas);
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", "04025897464", 7.0, 4, true, "Mazda 3", "Anja", drugCas.plusDays(2));
        aktivniPrevozi.add(dummyPrevoz2);
        aktivniPrevozi.add(dummyPrevoz3);
        aktivniPrevozi.add(dummyPrevoz);

        SMSsistem = new ToastSMS();
        SMSsistem.RManager.BindAktivniPrevozi(aktivniPrevozi);
        SMSsistem.bindContext(this);
        Log.e("SMSSistem:>>" , "SMSSistem RUNNING...OK");
        receiver.bindOnReceive(new OnReceiveSMS() {
            @Override
            public void messageReceived(SMSData novSMS) {
                if (novSMS.tip == SMSData.STOP) {
                    SMSsistem.Stop(novSMS);
                    stopSelf();
                    Log.e("BackgroundService:>> ", "ToastSMS STOP COMMAND");
                } else if (novSMS.tip == SMSData.START) {
                    SMSsistem.Start(novSMS);
                } else if (novSMS.tip == SMSData.PRIVATE) {
                    SMSsistem.RManager.sendRezervacije(true);
                } else if (SMSsistem.STATUS == ToastSMS.RUNNING) {
                    SMSsistem.ProcessNewUser(novSMS);
                    Log.e("SMSSistem:>>", "SMSData RECEIVED" + " " + novSMS.sender + " " + novSMS.tip);
                }
            }

        });
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "ToastSMS Service stopped!", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        Toast.makeText(this, "ToastSMS Service started!", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
}