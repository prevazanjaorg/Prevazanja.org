package nameplaceholder.prevazanjaorg;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class ListPrevozovActivity extends AppCompatActivity implements OnQueryTextListener {

    PrevozAdapter listAdapterPrevozov;
    SearchView searchViewPrevozov;

    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private FrameLayout frameLayout;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //POPULATE AKTIVNI PREVOZI
        final String dateTime = "29.11.2017 16:00:00";
        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        DateTime drugCas = dtf.parseDateTime(dateTime);
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 3, 4, false, "Toyota Yaris črne barve", "Luka", trenutniCas);
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "040256339", 5.0, 3, 3, false, "Toyota Hilux", "Žiga", drugCas);
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", "04025897464", 7.0, 1, 4, true, "Mazda 3", "Anja", drugCas.plusDays(2));
        aktivniPrevozi.add(dummyPrevoz2);
        aktivniPrevozi.add(dummyPrevoz3);
        //for (Integer i=0; i<20;i++)
            aktivniPrevozi.add(dummyPrevoz);


        //TESTIRANJE
        listAdapterPrevozov = new PrevozAdapter(this, aktivniPrevozi);
        final ListView listViewPrevozov = (ListView) findViewById(R.id.seznamPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);


        frameLayout = (FrameLayout) findViewById(R.id.list_relative);
        frameLayout.getForeground().setAlpha(0); // Črn foreground nardim transparenten

        listViewPrevozov.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popwindow,null);

                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                popupWindow = new PopupWindow(container, (int)(width*.8), (int)(height*.8), true);
                popupWindow.showAtLocation(frameLayout, Gravity.CENTER, 0, 0);
                frameLayout.getForeground().setAlpha(220);// Ozadju nastavim transparency, da je zatemnjeno

                popupWindow.setBackgroundDrawable(new BitmapDrawable());// Ker na Android 5.0.1 ne deluje dismiss popupa, poskusim s tem pristopom
                popupWindow.setOutsideTouchable(true);// <-----

                //   Nastavljanje vrednosti v popupu
                Prevoz p = (Prevoz) adapterView.getItemAtPosition(i);
                // Pridobivanje View-ov
                TextView ime = (TextView) container.findViewById(R.id.pop_ime);
                TextView stevilka = (TextView) container.findViewById(R.id.pop_telefon);
                TextView datum = (TextView) container.findViewById(R.id.pop_datum);
                TextView ura = (TextView) container.findViewById(R.id.pop_ura);
                TextView prostaMesta = (TextView) container.findViewById(R.id.pop_prostaMesta);
                TextView opis = (TextView) container.findViewById(R.id.pop_opis);

                // Prireditev vrednosti
                ime.setText(p.getIme());
                stevilka.setText(p.getMobitel());
                datum.setText(p.getDatum());
                ura.setText(p.getCas());
                prostaMesta.setText(p.getOseb() + "/" + p.getMaxOseb());
                opis.setText(p.getOpis());

                if (p.getOseb() == 1 || p.getOseb() == 0)
                    prostaMesta.setTextColor(Color.RED);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        frameLayout.getForeground().setAlpha(0);// Ozadje spet transparentno(normalno) ko se popup zapre
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

