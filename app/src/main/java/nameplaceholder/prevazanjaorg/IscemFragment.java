package nameplaceholder.prevazanjaorg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_iscem, container, false);

        ArrayList<Prevoz> aktivniPrevozi = new ArrayList<Prevoz>();
        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar); //need to cast your activity from getActivity() to AppCompatActivity first,  because getActivity() returns a FragmentActivity and you need an AppCompatActivity
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        // TESTNI PREVBOZI
        final String dateTime = "29.11.2017 16:00:00";
        DateTime trenutniCas = new DateTime(); //trenutni datum in točen čas
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        DateTime drugCas = dtf.parseDateTime(dateTime);
        Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 3, 4, false, "Toyota Yaris črne barve", "Luka", trenutniCas,46.557,15.650,100);
        Prevoz dummyPrevoz2 = new Prevoz("Ljubljana", "Maribor", "040256339", 5.0, 3, 3, false, "Toyota Hilux", "Žiga", drugCas,46.053912, 14.510480,250);
        Prevoz dummyPrevoz3 = new Prevoz("Celje", "Novo Mesto", "04025897464", 7.0, 1, 4, true, "Mazda 3", "Anja", drugCas.plusDays(2),46.2427,15.2640,150);
        aktivniPrevozi.add(dummyPrevoz2);
        aktivniPrevozi.add(dummyPrevoz3);
        for (Integer i=0; i<10;i++)
            aktivniPrevozi.add(dummyPrevoz);

        // V adapter damo vse prevoze. nato adapter podamo seznamu
        listAdapterPrevozov = new PrevozAdapter(getActivity().getApplicationContext(), aktivniPrevozi);
        final ListView listViewPrevozov = (ListView) rootView.findViewById(R.id.seznamPrevozov);
        listViewPrevozov.setAdapter(listAdapterPrevozov);

        frameLayout = (FrameLayout) rootView.findViewById(R.id.list_relative); // najdem activity_list.xml <frame>

        frameLayout.getForeground().setAlpha(0); // Črn foreground nardim transparenten

        listViewPrevozov.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popwindow,null);

                Prevoz prevoz = (Prevoz) adapterView.getItemAtPosition(i);
                Intent podrobnostiActivity = new Intent(getActivity(),PodrobnostiActivity.class);
                podrobnostiActivity.putExtra("Prevoz",prevoz);
                startActivity(podrobnostiActivity);
                Log.e("Iscem:" , "PodrobnostiActivity started");

                /*
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                popupWindow = new PopupWindow(container, (int)(width*.8), (int)(height*.8), true);
                popupWindow.showAtLocation(frameLayout, Gravity.CENTER, 0, 0);
                frameLayout.getForeground().setAlpha(200);// Ozadju nastavim transparency, da je zatemnjeno

                // Closes the popup window when touch outside.
                popupWindow.setOutsideTouchable(true);// Ker na Android 5.0.1 ne deluje dismiss popupa, poskusim s tem pristopom
                popupWindow.setFocusable(true);
                // Removes default background.
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                */
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
