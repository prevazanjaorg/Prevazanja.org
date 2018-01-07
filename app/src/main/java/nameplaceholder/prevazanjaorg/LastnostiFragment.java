package nameplaceholder.prevazanjaorg;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LastnostiFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    //private OnFragmentInteractionListener mListener;
    private static Prevoz prevoz;

    public LastnostiFragment() {
        // Required empty public constructor
    }

    public static LastnostiFragment newInstance(int sectionNumber, Prevoz p) {
        LastnostiFragment fragment = new LastnostiFragment();
        prevoz = p;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    TextView izpis;
    ViewPager viewPager;
    CustomSwipeAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lastnosti, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewer);
        adapter = new CustomSwipeAdapter(getActivity());
        viewPager.setAdapter(adapter);




        izpis=(TextView)rootView.findViewById(R.id.izpis);
        izpis.setMovementMethod(new ScrollingMovementMethod());
        new AsyncCallSoapPrikaziLastnosti().execute();



        return rootView;
    }

    public class AsyncCallSoapPrikaziLastnosti extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected String doInBackground(String... strings) {
            CallSoap CS = new CallSoap();

            String response = CS.VrniLastnosti();
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            dialog.dismiss();
            if(result.substring(0,1).equals("j"))
                new AsyncCallSoapPrikaziLastnosti().execute();
            Document doc = Jsoup.parse(result);
            Elements element = doc.getElementsByTag("VrniLastnostiResult");

            int lastIndex = 0;
            int count=0;
            while(lastIndex != -1){

                lastIndex = element.toString().indexOf("imeLastnosti",lastIndex);

                if(lastIndex != -1){
                    count ++;
                    lastIndex += "imeLastnosti".length();
                }
            }
            int x= 0; //zacetni indeks
            int y; //koncni indeks
            String izpisi="";
            for(int i = 0;i<count;i++){
                x = element.toString().indexOf("imeLastnosti",x+1);
                y=element.toString().indexOf("\"",x+17);
                izpisi+="- "+element.toString().substring(x+15, y)+"\n";

            }
            izpis.setText(izpisi);
        }
    }
}
