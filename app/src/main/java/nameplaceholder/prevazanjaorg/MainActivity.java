package nameplaceholder.prevazanjaorg;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView txtUsername;
    TextView txtEmail;
    Button btnLogout;
    Button btnLanding;
    SessionManager session;

    private void showNotification(){
        //channel
        String id = "main_channel";
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Channel Name";
            String desctription = "Channel description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
            notificationChannel.setDescription(desctription);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.enableVibration(true);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        //notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,id);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle("PrEvOz *Koper-Šper* ČeZ 30 MiNuT");
        notificationBuilder.setContentText("V primeru, da ste si premislili to sporočite *darkotu*");
        notificationBuilder.setLights(Color.WHITE,500,5000);
        notificationBuilder.setColor(Color.RED);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1000,notificationBuilder.build());
    }


    Button notification;
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

        notification = (Button) findViewById(R.id.Notifications   );

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton toggleButtonZanotif=(ToggleButton) findViewById(R.id.toggleButtonZanotif);
                if(toggleButtonZanotif.isChecked())
                    showNotification();
            }
        });

        Button Prijava = (Button) findViewById(R.id.TestPrijava   );
        Prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mojIntent = new Intent(MainActivity.this, Lastnosti_prevoza.class);
                startActivity(mojIntent);
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
