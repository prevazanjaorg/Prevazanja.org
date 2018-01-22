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
    private static final ArrayList<Prevoz> dbprevozi = new ArrayList<Prevoz>();

    public IscemFragment() {
        // Required empty public constructor
    }

    public static IscemFragment newInstance(int sectionNumber, ArrayList<Prevoz> prevozi) {
        IscemFragment fragment = new IscemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        for(Prevoz p : prevozi){
            dbprevozi.add(p);
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_iscem, container, false);

        //final ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();
        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar); //need to cast your activity from getActivity() to AppCompatActivity first,  because getActivity() returns a FragmentActivity and you need an AppCompatActivity
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);


        listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), dbprevozi);
        final ListView listViewPrevozov = (ListView) rootView.findViewById(R.id.seznamPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);

        Log.e("ISCEM", "DBPREVOZI OK");


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
