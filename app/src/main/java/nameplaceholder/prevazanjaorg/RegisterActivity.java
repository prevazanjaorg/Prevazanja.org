package nameplaceholder.prevazanjaorg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class RegisterActivity extends AppCompatActivity {

    Button btnReg;
    private EditText usernameView;
    private EditText emailView;
    private EditText ageView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameView = (EditText) findViewById(R.id.etIme);
        emailView = (EditText) findViewById(R.id.etEmail);
        ageView = (EditText) findViewById(R.id.etAge);
        passwordView = (EditText) findViewById(R.id.etGeslo);

        btnReg = (Button)findViewById(R.id.btnRegister);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RegisterActivity.AsyncCallSoapRegistracija().execute();
                //Toast.makeText(getApplicationContext(),usernameView.getText().toString() + "   " + emailView.getText().toString() + "   " + ageView.getText().toString() + "   " + passwordView.getText().toString(), Toast.LENGTH_LONG).show(); // naredi login da daš al username,mail al pa stevilko /slashi/, naredi register
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    public class AsyncCallSoapRegistracija extends AsyncTask<String, Void, String>{
        private final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);

        @Override
        protected String doInBackground(String... strings) {
            CallSoap CS = new CallSoap();
            String response = CS.Registracija("4201337",usernameView.getText().toString(),emailView.getText().toString(),false,false,false,0,0,0,0,passwordView.getText().toString());
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            dialog.dismiss();
        }
    }
}



