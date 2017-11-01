package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView txtView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtView = (TextView)findViewById(R.id.txtView);
        Intent intent = getIntent();
        String str = intent.getStringExtra("Welcome");
        txtView.setText(str);
    }

    /** Ta metoda se poklice ko uporabnik pritisne gumb za prijavo */
    public void sendMessage(View view) {
//        Context context = getApplicationContext();
////        txtView = (TextView)findViewById(R.id.etIme);
//        int duration = Toast.LENGTH_LONG;
//        Toast toast = Toast.makeText(context, "TOAST", duration);
//        toast.show();
        //asd
    }
}
