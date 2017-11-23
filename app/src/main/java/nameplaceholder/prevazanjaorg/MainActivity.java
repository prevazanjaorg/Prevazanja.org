package nameplaceholder.prevazanjaorg;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView txtUsername;
    TextView txtEmail;
    Button btnLogout;
    Button btnLanding;
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        TextView lblName = (TextView) findViewById(R.id.txtUsername);
        TextView lblEmail = (TextView) findViewById(R.id.txtEmail);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // displaying user data
        lblName.setText(name);
        lblEmail.setText(email);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        Button btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                session.logoutUser();
            }
        });

        Button btnLanding = (Button)findViewById(R.id.btnLanding);
        btnLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent mojIntent = new Intent(MainActivity.this, LandingActivity.class);
                startActivity(mojIntent);
        }
        });
        Button btnNotification=(Button)findViewById(R.id.Notifications);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                NotificationCompat.Builder notification;
                private static final int uniqueID = 123;

                notification=new NotificationCompat.Builder(this);
            }
        });
    }

    public void listPrevozovActivity(View view) {
        //Temporary to show list activity
        finish();
        Intent mojIntent = new Intent(MainActivity.this, ListPrevozovActivity.class);
        startActivity(mojIntent);
    }
}
