package nameplaceholder.prevazanjaorg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView txtView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    /** Ta metoda se poklice ko uporabnik pritisne gumb za prijavo */
    public void sendMessage(View view) {
        //Temporary to show list activity
        finish();
        Intent mojIntent = new Intent(MainActivity.this, ListPrevozovActivity.class);
        startActivity(mojIntent);
    }
}
