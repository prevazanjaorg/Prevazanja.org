package nameplaceholder.prevazanjaorg;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class ScrollingActivityIskanjePrevozov extends AppCompatActivity {
    ListAdapter listAdapterPrevozov;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ArrayList<AktivniPrevozi> aktivniPrevozi = new ArrayList<AktivniPrevozi>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);



        //TESTIRANJE
        AktivniPrevozi dummyPrevoz = new AktivniPrevozi("Maribor", "Koper", "040202108", 10, 4, false, "Toyota Yaris črne barve", "18:00", "Luka", "19:00");
        AktivniPrevozi dummyPrevoz2 = new AktivniPrevozi("Ljubljana", "Maribor", "040256339", 5, 3, false, "Toyota Hilux", "16:00", "Žiga", "15:00");
        aktivniPrevozi.add(dummyPrevoz2);
        for (Integer i=0; i<50; i++)
            aktivniPrevozi.add(dummyPrevoz);

        listAdapterPrevozov = new AktivniPrevoziAdapter(this, aktivniPrevozi);
        ListView listViewPrevozov = (ListView) findViewById(R.id.seznamPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
//        MenuItem item = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView)item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                return false;
//            }
//        });
//
        return true;
    }
}
