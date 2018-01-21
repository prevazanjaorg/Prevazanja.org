package nameplaceholder.prevazanjaorg;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PodrobnostiFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PodrobnostiFragment() {
        // Required empty public constructor
    }

    public static PodrobnostiFragment newInstance(int sectionNumber, Prevoz p) {
        PodrobnostiFragment fragment = new PodrobnostiFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_podrobnosti, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        final Prevoz prevoz = ((PodrobnostiActivity) getActivity()).getPrevoz();

        TextView relacija = (TextView) getView().findViewById(R.id.podrobnosti_txtview_relacija);
        relacija.setText(prevoz.getIz() + " - " + prevoz.getKam());

        TextView datum = (TextView) getView().findViewById(R.id.podrobnosti_txtview_datum);
        datum.setText(prevoz.getDatum());

        TextView ura = (TextView) getView().findViewById(R.id.podrobnosti_txtview_ura);
        ura.setText(prevoz.getCas());

        TextView prostamesta = (TextView) getView().findViewById(R.id.podrobnosti_prostaMesta);
        prostamesta.setText(prevoz.getOseb() + "/" + prevoz.getMaxOseb());
        if (prevoz.getOseb() < 2)
            prostamesta.setTextColor(Color.parseColor("#ff4444")); //red

        TextView strosek = (TextView) getView().findViewById(R.id.podrobnosti_cena);
        strosek.setText(prevoz.getStrosek().toString() + " €");

        TextView zavarovanje = (TextView) getView().findViewById(R.id.podrobnosti_zavarovanje);
        if (prevoz.getZavarovanje()) {
            zavarovanje.setText("DA");
            zavarovanje.setTextColor(Color.parseColor("#ffff9800")); //main orange
        }
        else
            zavarovanje.setText("NE");

        TextView telefon = (TextView) getView().findViewById(R.id.podrobnosti_telefon);
        telefon.setText(prevoz.getMobitel());

        telefon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialintent = new Intent(Intent.ACTION_DIAL);
                dialintent.setData(Uri.parse("tel:"+prevoz.getMobitel()));
                startActivity(dialintent);
            }
        });

        TextView ocena = (TextView) getView().findViewById(R.id.podrobnosti_ocena);
        ocena.setText(String.format("%.1f", prevoz.getOcena()));
        if (prevoz.getOcena() < 7)
            prostamesta.setTextColor(Color.parseColor("#ff4444")); //red

        TextView ocenapodrobno = (TextView) getView().findViewById(R.id.podrobnosti_opis);
        ocenapodrobno.setText(prevoz.getOpis() + "\n" + prevoz.getIme());

        TextView opis = (TextView) getView().findViewById(R.id.podrobnosti_ocenaStevilo);
        opis.setText("(" + prevoz.getSteviloOcen() + " ocen)");

        //TODO funkcionalnost rezerviraj buttona

        /*
            Začasen sample uporabnik. To bo prijavljeni uporabnik
         */
        final Uporabnik prijavljeni = new Uporabnik("040202108", "lukec");

        final Button rezerviraj = (Button) getView().findViewById(R.id.podrobnosti_btn);
        rezerviraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rezerviraj.getText() != "PREKLIČI") {
                    if (prevoz.addRezervacija(prijavljeni) != -1)
                        rezerviraj.setText("PREKLIČI");
                } else {
                    if (prevoz.remRezervacija(prijavljeni) != -1)
                        rezerviraj.setText("REZERVIRAJ");
                }

            }
        });

        final ImageButton report = (ImageButton) getView().findViewById(R.id.podrobnosti_btn_report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!prevoz.reported)
                    new AlertDialog.Builder(getContext())
                        .setMessage("Želiš res prijaviti prevoz kot neustrezen?")
                        .setCancelable(false)
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                prevoz.setReported(true);
                            }
                        })
                        .setNegativeButton("Ne", null)
                        .show();

            }
        });


    }


}
