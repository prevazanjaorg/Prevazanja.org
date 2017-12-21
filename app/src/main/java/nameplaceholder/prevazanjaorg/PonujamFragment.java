package nameplaceholder.prevazanjaorg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import android.widget.TextView;
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
    private PopupWindow popupWindow;
    private FrameLayout frameLayout;

    private Button btnIzbrisi;
    private Button btnPreklici;

    ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();

    private static String TELEFONSKA_STEVILKA = "040420069";

    public PonujamFragment() {
        // Required empty public constructor
    }

    public static PonujamFragment newInstance(int sectionNumber) {
        PonujamFragment fragment = new PonujamFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
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

        // TESTNI PREVBOZI
        final String dateTime = "29.11.2017 16:00:00";
        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        DateTime drugCas = dtf.parseDateTime(dateTime);
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 3, 4, false, "Toyota Yaris črne barve", "Luka", trenutniCas);
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "0402356339", 5.0, 3, 3, false, "Toyota Hilux", "Polar", drugCas);
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", "040420069", 7.0, 1, 4, true, "Mazda 3", "Polar", drugCas.plusDays(2));
        Prevoz dummyPrevoz4 = new Prevoz("Slatna", "Celje", "031321345", 10.0, 3, 4, false, "Fiat Multipla", "Jaka", trenutniCas);
        aktivniPrevozi.add(dummyPrevoz);
        aktivniPrevozi.add(dummyPrevoz2);
        aktivniPrevozi.add(dummyPrevoz3);
        aktivniPrevozi.add(dummyPrevoz4);

        for (Integer i=0; i<3;i++) {
            aktivniPrevozi.add(dummyPrevoz2);
        }

//        for(Integer i = 0; i < aktivniPrevozi.size(); i++){
//            Prevoz a = aktivniPrevozi.get(i);
//            if(a.getMobitel() != TELEFONSKA_STEVILKA){
//                aktivniPrevozi.remove(a);
//                //Toast.makeText(getContext(), a.getIme(), Toast.LENGTH_SHORT).show();
//            }
//        }

        // V adapter damo vse prevoze. nato adapter podamo seznamu
        listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), aktivniPrevozi);
        final ListView listViewPrevozov = (ListView) rootView.findViewById(R.id.seznamMojihPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);

        frameLayout = (FrameLayout) rootView.findViewById(R.id.list_relative); // najdem activity_list.xml <frame>

        frameLayout.getForeground().setAlpha(0); // Črn foreground nardim transparenten

        listViewPrevozov.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup,null);

                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                popupWindow = new PopupWindow(container, (int)(width*.4), (int)(height*.125), true);
                popupWindow.showAtLocation(frameLayout, Gravity.CENTER, 0, 0);
                frameLayout.getForeground().setAlpha(75);// Ozadju nastavim transparency, da je zatemnjeno

                // Closes the popup window when touch outside.
                popupWindow.setOutsideTouchable(true);// Ker na Android 5.0.1 ne deluje dismiss popupa, poskusim s tem pristopom
                popupWindow.setFocusable(true);
                // Removes default background.
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btnIzbrisi = (Button) container.findViewById(R.id.btnIzbrisi);
                btnIzbrisi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        aktivniPrevozi.remove(i);
                        listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), aktivniPrevozi);
                        final ListView listViewPrevozov = (ListView) getView().findViewById(R.id.seznamMojihPrevozov);
                        listViewPrevozov.setAdapter(listAdapterPrevozov);
                        popupWindow.dismiss();
                        Toast.makeText(getContext(), "Izbrisano", Toast.LENGTH_SHORT).show();
                    }
                });

                btnPreklici = (Button) container.findViewById(R.id.btnPreklici);
                btnPreklici.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });


                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        frameLayout.getForeground().setAlpha(0);// Ozadje spet transparentno(normalno) ko se popup zapre
                    }
                });

                //Toast.makeText(getContext(), "Long press", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ponujam, menu);
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
        listAdapterPrevozov.getFilter().filter(query + " " + TELEFONSKA_STEVILKA);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnSettings:
                btnSettingsClick(item);
                return true;
            case R.id.btnDodaj:
                btnDodajClick(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnSettingsClick(MenuItem item){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    public void btnDodajClick(MenuItem item){
        final String dateTime = "29.11.2017 16:00:00";
        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        DateTime drugCas = dtf.parseDateTime(dateTime);
        Prevoz dummyPrevoz = new Prevoz("Spar", "Pohorje", "04025897464", 7.0, 1, 4, true, "Mazda 3", "Polar", drugCas.plusDays(2));
        aktivniPrevozi.add(dummyPrevoz);
        listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), aktivniPrevozi);
        final ListView listViewPrevozov = (ListView) getView().findViewById(R.id.seznamMojihPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);
        Toast.makeText(getContext(), "Dodano", Toast.LENGTH_LONG).show();
    }
}
