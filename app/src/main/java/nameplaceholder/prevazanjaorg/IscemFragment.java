package nameplaceholder.prevazanjaorg;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import android.widget.TextView;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
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

    private PopupWindow popupWindowShrani;

    private Button btnShrani;
    private Button btnPrekliciShranjevanje;

    ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();
    ArrayList<Prevoz> shranjeniPrevozi = new ArrayList<Prevoz>(); //prevozi ki jih želi uporabnik shraniti - se pomaknejo v PonujamFragment

    Bundle bundle = new Bundle();

    int count = 0;

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

    public ArrayList<Prevoz> vrniShranjenePrevoze(){
//        final String dateTime = "29.11.2017 16:00:00";
//        ArrayList<Prevoz> arList = new ArrayList<Prevoz>();
//        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
//        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
//        DateTime drugCas = dtf.parseDateTime(dateTime);
//        ArrayList<Integer> ocene = new ArrayList<Integer>();
//        ocene.add(10); ocene.add(9); ocene.add(7); ocene.add(10); ocene.add(10);
//        Prevoz dummyPrevoz1 = new Prevoz("Testex", "Koper", "040202108", 10.0, 3, 4, false, "Toyota Yaris črne barve", "Polar", trenutniCas,-34,151,100, ocene);
//        Prevoz dummyPrevoz2 = new Prevoz("Sead", "Maribor", "040256339", 5.0, 3, 3, false, "Toyota Hilux", "Polar", drugCas,-50,150,250, ocene);
//        Prevoz dummyPrevoz3 = new Prevoz("Red", "Novo Mesto", "04025897464", 7.0, 1, 4, true, "Mazda 3", "Polar", drugCas.plusDays(2),66,-50,150, ocene);
//        shranjeniPrevozi.add(dummyPrevoz1);
//        shranjeniPrevozi.add(dummyPrevoz2);
//        shranjeniPrevozi.add(dummyPrevoz3);
        //bundle.putSerializable("seznam",shranjeniPrevozi);
        //ArrayList<Prevoz> prevozi = (ArrayList<Prevoz>)getArguments().getSerializable("seznam");
        return shranjeniPrevozi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_iscem, container, false);

        final ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();
        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar); //need to cast your activity from getActivity() to AppCompatActivity first,  because getActivity() returns a FragmentActivity and you need an AppCompatActivity
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        // TESTNI PREVBOZI
        final String dateTime = "29.11.2017 16:00:00";
        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        DateTime drugCas = dtf.parseDateTime(dateTime);
        ArrayList<Integer> ocene = new ArrayList<Integer>();
        ocene.add(10); ocene.add(9); ocene.add(7); ocene.add(10); ocene.add(10);
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 3, 4, false, "Toyota Yaris črne barve", "Polar", trenutniCas,-34,151,100, ocene);
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "040256339", 5.0, 3, 3, false, "Toyota Hilux", "Polar", drugCas,-50,150,250, ocene);
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", "04025897464", 7.0, 1, 4, true, "Mazda 3", "Polar", drugCas.plusDays(2),66,-50,150, ocene);
        aktivniPrevozi.add(dummyPrevoz2);
        aktivniPrevozi.add(dummyPrevoz3);
        for (Integer i=0; i<10;i++)
            aktivniPrevozi.add(dummyPrevoz);

        // V adapter damo vse prevoze. nato adapter podamo seznamu
        listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), aktivniPrevozi);
        final ListView listViewPrevozov = (ListView) rootView.findViewById(R.id.seznamPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);

        frameLayout = (FrameLayout) rootView.findViewById(R.id.list_Iscem);

        frameLayout.getForeground().setAlpha(0); // Črn foreground nardim transparenten

        listViewPrevozov.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_shrani,null);

                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                popupWindowShrani = new PopupWindow(container, (int)(width*.4), (int)(height*.125), true);
                popupWindowShrani.showAtLocation(frameLayout, Gravity.CENTER, 0, 0);
                frameLayout.getForeground().setAlpha(75);// Ozadju nastavim transparency, da je zatemnjeno

                // Closes the popup_shrani window when touch outside.
                popupWindowShrani.setOutsideTouchable(true);
                popupWindowShrani.setFocusable(true);
                // Removes default background.
                popupWindowShrani.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btnShrani = (Button) container.findViewById(R.id.btnShrani);
                btnShrani.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Prevoz s = listAdapterPrevozov.getItem(i);
                        shranjeniPrevozi.add(s);
                        popupWindowShrani.dismiss();
                       //Toast.makeText(getContext(), "Shranjeno", Toast.LENGTH_SHORT).show();
                        String str = shranjeniPrevozi.get(shranjeniPrevozi.size() - 1).getIz();
                        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
//                        bundle.putSerializable("prevoz"+count,s);
//                        PonujamFragment pF = new PonujamFragment();
//                        pF.setArguments(bundle);
//                        getFragmentManager()
//                                .beginTransaction()
//                                .replace(android.R.id.content, pF)
//                                .commit();
//                        count++;
                    }
                });

                btnPrekliciShranjevanje = (Button) container.findViewById(R.id.btnPrekliciShranjevanje);
                btnPrekliciShranjevanje.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindowShrani.dismiss();
                    }
                });
//
                popupWindowShrani.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        frameLayout.getForeground().setAlpha(0);
                    }
                });
                return true;
            }
        });

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
