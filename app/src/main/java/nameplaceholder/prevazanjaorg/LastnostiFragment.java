package nameplaceholder.prevazanjaorg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LastnostiFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    //private OnFragmentInteractionListener mListener;
    private static Prevoz prevoz;

    public LastnostiFragment() {
        // Required empty public constructor
    }

    public static LastnostiFragment newInstance(int sectionNumber, Prevoz p) {
        LastnostiFragment fragment = new LastnostiFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    int prva=0;
    int len=0;
    ImageView najvecja;
    ImageView leva;
    ImageView sredina;
    ImageView desna;
    Bitmap bitmap;
    Button prvaPlusPlus;
    Button prvaMinusMinus;
    List<String> Stringi = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lastnosti, container, false);;
        desna=(ImageView) rootView.findViewById(R.id.imageView3);
        najvecja = (ImageView) rootView.findViewById(R.id.imageView4);
        sredina=(ImageView) rootView.findViewById(R.id.imageView2);
        leva=(ImageView) rootView.findViewById(R.id.imageView1);
        prvaPlusPlus=(Button) rootView.findViewById(R.id.button2);
        prvaMinusMinus=(Button)rootView.findViewById(R.id.button1);

        prvaPlusPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prva++;
                if(prva==len)
                    prva=0;
                setImages(prva);
            }
        });

        prvaMinusMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prva--;
                if(prva==0)
                    prva=len-1;
                setImages(prva);
            }
        });


        new AsyncCallSoapVrniSlike().execute();


        return rootView;

    }

    public class GetImageFromURL extends AsyncTask<String,Void,Bitmap>{

        ImageView imgV;

        public GetImageFromURL(ImageView imgV){
            this.imgV= imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay= url[0];
            bitmap = null;
            try{
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap=BitmapFactory.decodeStream(srt);
            }catch(Exception e){
                e.printStackTrace();
            }
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgV.setImageBitmap(bitmap);
        }
    }

    public class AsyncCallSoapVrniSlike extends AsyncTask<String, Void, String>{
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected String doInBackground(String... strings) {
            CallSoap CS = new CallSoap();
            String response = CS.VrniSlike(827);
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            dialog.dismiss();
            if(result.substring(0,1).equals("j"))
                new AsyncCallSoapVrniSlike().execute();
            Document doc = Jsoup.parse(result);
            String[] lines=doc.outerHtml().split(System.getProperty("line.separator"));
            for(int i = 0;i<lines.length;i++){
                if(lines[i].contains("<string>")){
                    Stringi.add(lines[i+1]);
                    len++;
                }
            }
            setImages(prva);
        }
    }
    public void setImages(int prva){
        if(prva<0)
            prva=len-1;
        else if(prva>len-1)
            prva=0;
        new GetImageFromURL(najvecja).execute(Stringi.get(prva));
        int temp=prva+1;
        if(temp<0)
            temp=len-1;
        else if(temp>len-1)
            temp=0;
        new GetImageFromURL(leva).execute(Stringi.get(temp));
        temp++;
        if(temp<0)
            temp=len-1;
        else if(temp>len-1)
            temp=0;
        new GetImageFromURL(sredina).execute(Stringi.get(temp));
        temp++;
        if(temp<0)
            temp=len-1;
        else if(temp>len-1)
            temp=0;
        new GetImageFromURL(desna).execute(Stringi.get(temp));
    }


}
