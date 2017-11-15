package nameplaceholder.prevazanjaorg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class ScrollingActivityIskanjePrevozov extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ArrayList<AktivniPrevozi> aktivniPrevozi = new ArrayList<AktivniPrevozi>();

        //TESTIRANJE
        AktivniPrevozi dummyPrevoz = new AktivniPrevozi("Maribor", "Koper", "040202108", 10, 4, false, "Toyota Yaris črne barve", "18:00", "Luka", "19:00");
        for (Integer i=0; i<50; i++)
            aktivniPrevozi.add(dummyPrevoz);

        ListAdapter listAdapterPrevozov = new AktivniPrevoziAdapter(this, aktivniPrevozi);
        ListView listViewPrevozov = (ListView) findViewById(R.id.seznamPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);

    }

}
