package nameplaceholder.prevazanjaorg;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class IscemFragment extends Fragment implements OnQueryTextListener {

    // deklaracije
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PrevozAdapter listAdapterPrevozov;
    private SearchView searchViewPrevozov;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private FrameLayout frameLayout;

    public IscemFragment() {
        // Required empty public constructor
    }

    public static IscemFragment newInstance(int sectionNumber) {
        IscemFragment fragment = new IscemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public class AsyncCallSoapVrniPrevoze extends AsyncTask<String, Void, String>{
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

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




        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_iscem, container, false);

        new AsyncCallSoapVrniPrevoze().execute();

        final ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();
        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar); //need to cast your activity from getActivity() to AppCompatActivity first,  because getActivity() returns a FragmentActivity and you need an AppCompatActivity
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        // TESTNI PREVBOZI
        final String dateTime = "29.11.2017 16:00:00";
        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        DateTime drugCas = dtf.parseDateTime(dateTime);
        //ArrayList<Integer> ocene = new ArrayList<Integer>();
        //ocene.add(10); ocene.add(9); ocene.add(7); ocene.add(10); ocene.add(10);
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 3, 4, false, "Toyota Yaris črne barve", "Polar", trenutniCas,-34,151,100, 4.5);
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "040256339", 5.0, 3, 3, false, "Toyota Hilux", "Polar", drugCas,-50,150,250, 5);
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", "04025897464", 7.0, 1, 4, true, "Mazda 3", "Polar", drugCas.plusDays(2),66,-50,150, 9.5);
        aktivniPrevozi.add(dummyPrevoz2);
        aktivniPrevozi.add(dummyPrevoz3);
        for (Integer i=0; i<10;i++)
            aktivniPrevozi.add(dummyPrevoz);

        // V adapter damo vse prevoze. nato adapter podamo seznamu
        listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), aktivniPrevozi);
        final ListView listViewPrevozov = (ListView) rootView.findViewById(R.id.seznamPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);

        listViewPrevozov.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Prevoz prevoz = (Prevoz) adapterView.getItemAtPosition(i);
                Intent podrobnostiActivity = new Intent(getActivity(),PodrobnostiActivity.class);
                podrobnostiActivity.putExtra("Prevoz",prevoz);
                startActivity(podrobnostiActivity);
                Log.e("Iscem:" , "PodrobnostiActivity started");

                //PodrobnostiFragment frgmnt_obj = (PodrobnostiFragment) getFragmentManager().findFragmentById(R.id.podrobnosti_constraintLayout);
                //TextView relacija = (TextView) frgmnt_obj.getView().findViewById(R.id.podrobnosti_txtview_relacija);

            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_iscem, menu);
        MenuItem menuItem = menu.findItem(R.id.listSearch);
        searchViewPrevozov = (SearchView) menuItem.getActionView();
        searchViewPrevozov.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        listAdapterPrevozov.getFilter().filter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        listAdapterPrevozov.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnSettings:
                btnSettingsClick(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnSettingsClick(MenuItem item){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

}
