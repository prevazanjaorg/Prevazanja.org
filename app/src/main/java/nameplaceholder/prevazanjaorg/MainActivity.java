package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView txtBox;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /** Ta metoda se poklice ko uporabnik pritisne gumb za prijavo */
    public void sendMessage(View view) {
        Context context = getApplicationContext();
        txtBox = (TextView)findViewById(R.id.editText2);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, txtBox.getText(), duration);
        toast.show();
    }
}
