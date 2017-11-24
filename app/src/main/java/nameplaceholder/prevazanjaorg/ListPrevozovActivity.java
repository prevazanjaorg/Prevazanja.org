package nameplaceholder.prevazanjaorg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;

public class ListPrevozovActivity extends AppCompatActivity implements OnQueryTextListener {
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

        //POPULATE AKTIVNI PREVOZI
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 4, false, "Toyota Yaris črne barve", "20.11.2017", "Luka", "19:00");
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "040256339", 5.0, 3, false, "Toyota Hilux", "20.11.2017", "Žiga", "15:00");
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", "04025897464", 7.0, 4, true, "Mazda 3", "20.11.2017", "Anja", "09:00");
        aktivniPrevozi.add(dummyPrevoz2);
        aktivniPrevozi.add(dummyPrevoz3);
        //for (Integer i=0; i<50; i++)
        aktivniPrevozi.add(dummyPrevoz);


        //Jaka
        SMSsistem = new ToastSMS();
        SMSsistem.RManager.BindAktivniPrevozi(aktivniPrevozi);
        SmsReceiver Receiver = new SmsReceiver();
        SMSsistem.bindContext(this);
        Log.e("SMSSistem:>>" , "Reciever started");

        Receiver.bindOnReceive(new OnReceiveSMS() {
            @Override
            public void messageReceived(SMSData novSMS) {
                SMSsistem.ProcessNewUser(novSMS);
                Log.e("SMSSistem:>>" , "SMSData RECEIVED" + " " + novSMS.sender + " " + novSMS.tip);
            }
        });
        //Jaka


        //TESTIRANJE
        listAdapterPrevozov = new PrevozAdapter(this, aktivniPrevozi);
        ListView listViewPrevozov = (ListView) findViewById(R.id.seznamPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);



        /*searchViewPrevozov.setOnQueryTextListener(new OnQueryTextListener() {
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
        MenuItem menuItem = menu.findItem(R.id.listSearch);
        searchViewPrevozov = (SearchView) menuItem.getActionView();
        searchViewPrevozov.setOnQueryTextListener(this);
        if (searchViewPrevozov==null)
            Log.d("TEST1", "je null");
        else
            Log.d("TEST1", "ni null");
        return true;
    }

    /*
    public boolean    onOptionsItemSelected       (MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }
    */

    @Override
    public boolean onQueryTextSubmit(String s) {
        listAdapterPrevozov.getFilter().filter(s);
        return false;
    }
    @Override
    public boolean onQueryTextChange(String query) {
        Log.d("T2", "Text changed");
        listAdapterPrevozov.getFilter().filter(query);
        return false;
    }
}