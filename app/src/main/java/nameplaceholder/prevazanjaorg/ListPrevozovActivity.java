package nameplaceholder.prevazanjaorg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;

import java.util.ArrayList;

public class ListPrevozovActivity extends AppCompatActivity {
    PrevozAdapter listAdapterPrevozov;
    SearchView searchViewPrevozov;

    private ToastSMS SMSsistem;
    private SmsReceiver Receiver;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Jaka
        SMSsistem = new ToastSMS();
        SmsReceiver Receiver = new SmsReceiver();
        SMSsistem.bindContext(this);
        Log.e("SMSSistem:>>" , "Reciever started");

        Receiver.bindOnReceive(new OnReceiveSMS() {
            @Override
            public void messageReceived(UserData novSMS) {
                SMSsistem.ProcessNewUser(novSMS);
                Log.e("SMSSistem:>>" , "UserData RECEIVED" + " " + novSMS.sender + " " + novSMS.tip);
            }
        });
        //Jaka


        //TESTIRANJE
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10, 4, false, "Toyota Yaris črne barve", "18:00", "Luka", "19:00");
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "040256339", 5, 3, false, "Toyota Hilux", "16:00", "Žiga", "15:00");
        aktivniPrevozi.add(dummyPrevoz2);
        for (Integer i=0; i<50; i++)
            aktivniPrevozi.add(dummyPrevoz);

        listAdapterPrevozov = new PrevozAdapter(this, aktivniPrevozi);
        ListView listViewPrevozov = (ListView) findViewById(R.id.seznamPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);

        /*searchViewPrevozov.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                listAdapterPrevozov.getFilter().filter(query);

                return false;
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }
}
