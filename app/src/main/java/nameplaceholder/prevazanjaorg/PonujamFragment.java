package nameplaceholder.prevazanjaorg;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PonujamFragment extends Fragment implements OnQueryTextListener{

    // deklaracije
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PrevozAdapter listAdapterPrevozov;
    private SearchView searchViewPrevozov;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindowIzbrisi;
    private PopupWindow popupWindowDodaj;
    private FrameLayout frameLayout;

    private Button btnIzbrisi;
    private Button btnPrekliciBrisanje;

    private Button btnDodaj;
    private Button btnPrekliciDodajanje;

    private EditText etOd;
    private EditText etDo;
    private EditText etCas;
    private EditText etCena;

    ArrayList<Prevoz> shranjeniPrevozi = new ArrayList<Prevoz>();
    IscemFragment iscemFragment = new IscemFragment();
    private final static ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();

    private static String TELEFONSKA_STEVILKA = "040420069";

    public PonujamFragment() {
        // Required empty public constructor
    }

    public static PonujamFragment newInstance(int sectionNumber, ArrayList<Prevoz> prevozi) {
        PonujamFragment fragment = new PonujamFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        for(Prevoz p : prevozi){
            aktivniPrevozi.add(p);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_ponujam, container, false);

        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar); //need to cast your activity from getActivity() to AppCompatActivity first,  because getActivity() returns a FragmentActivity and you need an AppCompatActivity
        String title = "Moji " + getContext().getString((R.string.app_name));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);

//        Bundle bundle = this.getArguments();
//        if(bundle != null){
//            try{
//                for(Integer i = 0; i < 20; i++){
//                    shranjeniPrevozi.add((Prevoz)bundle.getSerializable("prevoz"+i));
//                }
//            }
//            catch(Exception ex){
//                Log.e("EXCEPTION:",ex.getMessage());
//            }
//        }

        // TESTNI PREVOZI
        final String dateTime = "29.11.2017 16:00:00";
        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        DateTime drugCas = dtf.parseDateTime(dateTime);

        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", TELEFONSKA_STEVILKA, 10.0, 3, 4, false, "Toyota Yaris črne barve", "Polar", trenutniCas,-34,151,100);
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", TELEFONSKA_STEVILKA, 5.0, 3, 3, false, "Toyota Hilux", "Polar", drugCas,-50,150,250);
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", TELEFONSKA_STEVILKA, 7.0, 1, 4, true, "Mazda 3", "Polar", drugCas.plusDays(2),66,-50,150);

//        if(shranjeniPrevozi.size() == 0){
//            shranjeniPrevozi.add(dummyPrevoz);
//            shranjeniPrevozi.add(dummyPrevoz2);
//            shranjeniPrevozi.add(dummyPrevoz3);
//        }

//        for(Integer i = 0; i < aktivniPrevozi.size(); i++){
//            Prevoz a = aktivniPrevozi.get(i);
//            if(a.getMobitel() != TELEFONSKA_STEVILKA){
//                aktivniPrevozi.remove(a);
//                //Toast.makeText(getContext(), a.getIme(), Toast.LENGTH_SHORT).show();
//            }
//        }

        // V adapter damo vse prevoze. nato adapter podamo seznamu
        //shranjeniPrevozi = iscemFragment.vrniShranjenePrevoze();
        //iscemFragment.vrniShranjenePrevoze();
        //shranjeniPrevozi = (ArrayList<Prevoz>)getArguments().getSerializable("seznam");

        listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), aktivniPrevozi);
        final ListView listViewPrevozov = (ListView) rootView.findViewById(R.id.seznamMojihPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);
        Log.e("PONUJAM", "AKTVNI PREVOZI OK");

        frameLayout = (FrameLayout) rootView.findViewById(R.id.list_Ponujam);

        frameLayout.getForeground().setAlpha(0); // Črn foreground nardim transparenten

        listViewPrevozov.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_izbrisi,null);

                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                popupWindowIzbrisi = new PopupWindow(container, (int)(width*.4), (int)(height*.125), true);
                popupWindowIzbrisi.showAtLocation(frameLayout, Gravity.CENTER, 0, 0);
                frameLayout.getForeground().setAlpha(75);// Ozadju nastavim transparency, da je zatemnjeno

                // Closes the popup_izbrisi window when touch outside.
                popupWindowIzbrisi.setOutsideTouchable(true);// Ker na Android 5.0.1 ne deluje dismiss popupa, poskusim s tem pristopom
                popupWindowIzbrisi.setFocusable(true);
                // Removes default background.
                popupWindowIzbrisi.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btnIzbrisi = (Button) container.findViewById(R.id.btnIzbrisi);
                btnIzbrisi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        aktivniPrevozi.remove(i);
                        listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), aktivniPrevozi);
                        final ListView listViewPrevozov = (ListView) getView().findViewById(R.id.seznamMojihPrevozov);
                        listViewPrevozov.setAdapter(listAdapterPrevozov);
                        popupWindowIzbrisi.dismiss();
                        Toast.makeText(getContext(), "Izbrisano", Toast.LENGTH_SHORT).show();
                    }
                });

                btnPrekliciBrisanje = (Button) container.findViewById(R.id.btnPrekliciBrisanje);
                btnPrekliciBrisanje.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindowIzbrisi.dismiss();
                    }
                });

                popupWindowIzbrisi.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        frameLayout.getForeground().setAlpha(0);// Ozadje spet transparentno(normalno) ko se popup_izbrisi zapre
                    }
                });
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ponujam, menu);
        MenuItem menuItem = menu.findItem(R.id.listSearchPonujam);
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
            case R.id.btnPlus:
                btnPlusClick(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnSettingsClick(MenuItem item){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    public void btnPlusClick(MenuItem item){
        layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_dodaj,null);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        popupWindowDodaj = new PopupWindow(container, (int)(width*.7), (int)(height*.4), true);
        popupWindowDodaj.showAtLocation(frameLayout, Gravity.CENTER, 0, 0);
        frameLayout.getForeground().setAlpha(75);// Ozadju nastavim transparency, da je zatemnjeno

        // Closes the popup_dodaj window when touch outside.
        popupWindowDodaj.setOutsideTouchable(true);// Ker na Android 5.0.1 ne deluje dismiss popupa, poskusim s tem pristopom
        popupWindowDodaj.setFocusable(true);
        // Removes default background.
        popupWindowDodaj.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        etOd = (EditText) container.findViewById(R.id.etOd);
        etDo = (EditText) container.findViewById(R.id.etDo);
        etCas = (EditText) container.findViewById(R.id.etCas);
        etCena = (EditText) container.findViewById(R.id.etCena);

        btnDodaj = (Button) container.findViewById(R.id.btnDodaj);
        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String dateTime = "29.11.2017 16:00:00";
                DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
                DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
                DateTime drugCas = dtf.parseDateTime(dateTime);
                Prevoz dodanPrevoz = new Prevoz(etOd.getText().toString(), etDo.getText().toString(), "04025897464",Double.parseDouble(etCena.getText().toString()), 1, 4, true, "Mazda 3", "Polar", drugCas.plusDays(2),60,32,100);
                aktivniPrevozi.add(dodanPrevoz);
                listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), aktivniPrevozi);
                final ListView listViewPrevozov = (ListView) getView().findViewById(R.id.seznamMojihPrevozov);
                listViewPrevozov.setAdapter(listAdapterPrevozov);
                popupWindowDodaj.dismiss();
            }
        });

        btnPrekliciDodajanje = (Button) container.findViewById(R.id.btnPrekliciDodajanje);
        btnPrekliciDodajanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowDodaj.dismiss();
            }
        });

        popupWindowDodaj.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                frameLayout.getForeground().setAlpha(0);// Ozadje spet transparentno(normalno) ko se popup_dodaj zapre
            }
        });
    }
}
