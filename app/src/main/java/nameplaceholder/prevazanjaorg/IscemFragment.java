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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.io.Serializable;
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

    private PopupWindow popupWindowShrani;

    private Button btnShrani;
    private Button btnPrekliciShranjevanje;

    ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();
    ArrayList<Prevoz> shranjeniPrevozi = new ArrayList<Prevoz>(); //prevozi ki jih želi uporabnik shraniti - se pomaknejo v PonujamFragment

    private static final ArrayList<Prevoz> dbprevozi = new ArrayList<Prevoz>();

    Bundle bundle = new Bundle();

    int count = 0;

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
            Prevoz p;
            int i = 0;
            int j;
            while (i < 5000) {
                i = i + prevozi.substring(i).indexOf("<string>");
                i+=16; // zamaknemo naprej za <string>+\n
                if (prevozi.substring(i).contains("<string>")) {
                    Log.d("fuck", String.valueOf(i)+" in "+String.valueOf(i + prevozi.substring(i).indexOf('|')));
                    int id = Integer.valueOf(prevozi.substring(i, i + prevozi.substring(i).indexOf('|'))); // zdaj smo pri delimiterju (ID)
                    i = i + prevozi.substring(i).indexOf('|');
                    String iz = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // pri naslednjem delimeterju (iz)
                    i = i + prevozi.substring(i).indexOf('|');
                    String ka = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // kam
                    i = i + prevozi.substring(i).indexOf('|');
                    String te = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // telefon
                    i = i + prevozi.substring(i).indexOf('|');
                    int os = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); // oseb
                    i = i + prevozi.substring(i).indexOf('|');
                    int mo = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); // maxoseb
                    i = i + prevozi.substring(i).indexOf('|');
                    Boolean za = Boolean.parseBoolean(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); //zavarovanje
                    i = i + prevozi.substring(i).indexOf('|');
                    String av = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // avto
                    i = i + prevozi.substring(i).indexOf('|');
                    DateTimeFormatter dtf = DateTimeFormat.forPattern("d.M. H:m");
                    String tempDatumString = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|') + prevozi.substring(i + prevozi.substring(i).indexOf('|') + 1).indexOf('|') + 1); // datum string
                    tempDatumString = tempDatumString.substring(tempDatumString.indexOf(',') + 1);
                    StringBuilder sb = new StringBuilder(tempDatumString);
                    sb.deleteCharAt(tempDatumString.indexOf('|'));
                    sb.deleteCharAt(tempDatumString.indexOf('.')+1);
                    sb.deleteCharAt(0);
                    tempDatumString = sb.toString();
                    i = i + prevozi.substring(i).indexOf('|');
                    DateTime dt = dtf.parseDateTime(tempDatumString);
                    i++; // preskočimo uro
                    i = i + prevozi.substring(i).indexOf('|');
                    String im = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // ime
                    i = i + prevozi.substring(i).indexOf('|');
                    String op = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // opis
                    i = i + prevozi.substring(i).indexOf('|');
                    Double la = Double.valueOf((prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))).replace(',','.')); //latitude
                    i = i + prevozi.substring(i).indexOf('|');
                    Double lo = Double.valueOf((prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))).replace(',','.')); //longtitude
                    i = i + prevozi.substring(i).indexOf('|');
                    int ra = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); // radius
                    i = i + prevozi.substring(i).indexOf('|');
                    int fk = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('\n'))); // fkUporabnik
                    i = i + prevozi.substring(i).indexOf('\n');
                    p = new Prevoz(iz, ka, te, 10.0, os, mo, za, av, im, dt, lo,lo,ra);
                    dbprevozi.add(p);
                }
                else
                    break;
            }
            return;
        }
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
