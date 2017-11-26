package nameplaceholder.prevazanjaorg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

public class ListPrevozovActivity extends AppCompatActivity implements OnQueryTextListener {
    PrevozAdapter listAdapterPrevozov;
    SearchView searchViewPrevozov;

    private ToastSMS SMSsistem;
    private SmsReceiver Receiver;

    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //POPULATE AKTIVNI PREVOZI
        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 4, false, "Toyota Yaris črne barve", "Luka", trenutniCas);
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "040256339", 5.0, 3, false, "Toyota Hilux", "Žiga", trenutniCas);
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", "04025897464", 7.0, 4, true, "Mazda 3", "Anja", trenutniCas);
        aktivniPrevozi.add(dummyPrevoz2);
        aktivniPrevozi.add(dummyPrevoz3);
        for (Integer i=0; i<20;i++)
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

        relativeLayout = (RelativeLayout) findViewById(R.id.list_relative);
        listViewPrevozov.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popwindow,null);

                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                popupWindow = new PopupWindow(container, (int)(width*.8), (int)(height*.6), true);
                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.listSearch);
        searchViewPrevozov = (SearchView) menuItem.getActionView();
        searchViewPrevozov.setOnQueryTextListener(this);
        return true;
    }

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