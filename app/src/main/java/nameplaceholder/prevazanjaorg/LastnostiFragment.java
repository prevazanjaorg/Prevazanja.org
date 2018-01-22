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
    int prva=0;;
    int len=0;
    ImageView najvecja;
    ImageView leva;
    ImageView sredina;
    ImageView desna;
    Bitmap bitmap;
    List<String> Stringi = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lastnosti, container, false);;
        desna=(ImageView) rootView.findViewById(R.id.imageView3);
        najvecja = (ImageView) rootView.findViewById(R.id.imageView4);
        sredina=(ImageView) rootView.findViewById(R.id.imageView2);
        leva=(ImageView) rootView.findViewById(R.id.imageView1);
        new AsyncCallSoapVrniSlike().execute();
        new GetImageFromURL(najvecja).execute("https://i.vimeocdn.com/video/577610798_780x439.jpg");
        new GetImageFromURL(leva).execute("https://i.vimeocdn.com/video/577610798_780x439.jpg");
        new GetImageFromURL(desna).execute("https://i.vimeocdn.com/video/577610798_780x439.jpg");
        new GetImageFromURL(sredina).execute("https://i.vimeocdn.com/video/577610798_780x439.jpg");


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

        }
    }
    public void setImages(int prva){
        int temp=prva;
        if(temp<len) {
            new GetImageFromURL(najvecja).execute(Stringi.get(temp));
            if(++temp<len)
                new GetImageFromURL(leva).execute(Stringi.get(temp));
            else
                new GetImageFromURL(leva).execute(Stringi.get(temp));
            new GetImageFromURL(desna).execute(Stringi.get(3));
            new GetImageFromURL(sredina).execute(Stringi.get(4));
        }
        else{
            prva=0;
        }
    }
}
