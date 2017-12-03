package nameplaceholder.prevazanjaorg;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Lastnosti_prevoza extends AppCompatActivity {

    Button prijava;
    TextView TV;
    EditText usernameTxt;
    EditText passwordTxt;
    Button refresh;
    TextView PrikazLastnostiTV;
    Button DodajLastnost;
    EditText vstaviLastnosteT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lastnosti_prevoza);

        prijava=(Button) findViewById(R.id.prijavaBth);
        TV=(TextView) findViewById(R.id.prikazLasntosti);
        usernameTxt=(EditText) findViewById(R.id.usernameeT);
        passwordTxt=(EditText) findViewById(R.id.passwordeT);
        refresh=(Button) findViewById(R.id.refresh);
        PrikazLastnostiTV=(TextView) findViewById(R.id.PrikazLastnostiTV);
        DodajLastnost=(Button) findViewById(R.id.dodajBtn);
        vstaviLastnosteT=(EditText) findViewById(R.id.vstaviLastnosteT);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncCallSoapPrikaziLastnosti().execute();
            }
        });
        DodajLastnost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncCallSoapDodajLastnost().execute();
            }
        });
        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncCallSoapPrijava().execute();
            }
        });
    }

    public class AsyncCallSoapPrijava extends AsyncTask<String, Void, String>{
        private final ProgressDialog dialog = new ProgressDialog(Lastnosti_prevoza.this);

        @Override
        protected String doInBackground(String... strings) {
            CallSoap CS = new CallSoap();

            String response = CS.Prijava(usernameTxt.getText().toString(), usernameTxt.getText().toString(), passwordTxt.getText().toString());
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            dialog.dismiss();
            if(result.substring(0,1).equals("j"))
                new AsyncCallSoapPrijava().execute();
            Document doc = Jsoup.parse(result);
            Elements element = doc.getElementsByTag("PrijavaResult");
            String s="falsee";
            if(!result.substring(0,1).equals("j")) {
                s = element.toString().substring(element.toString().indexOf(">") + 1);
                s = s.substring(1, s.indexOf("<"));
            }
            TV.setText(s);
        }
    }

    public class AsyncCallSoapPrikaziLastnosti extends AsyncTask<String, Void, String>{
        private final ProgressDialog dialog = new ProgressDialog(Lastnosti_prevoza.this);

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
            PrikazLastnostiTV.setText(izpisi);
        }
    }

    public class AsyncCallSoapDodajLastnost extends AsyncTask<String, Void, String>{
        private final ProgressDialog dialog = new ProgressDialog(Lastnosti_prevoza.this);

        @Override
        protected String doInBackground(String... strings) {
            CallSoap CS = new CallSoap();

                String response = CS.DodajLastnost(vstaviLastnosteT.getText().toString());
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            dialog.dismiss();
        }
    }
}
