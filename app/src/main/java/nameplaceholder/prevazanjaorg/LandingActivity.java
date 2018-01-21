package nameplaceholder.prevazanjaorg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private PonujamFragment ponujamFragment;
    private IscemFragment iscemFragment;
    private LandingFragment landingFragment;
    private static final ArrayList<Prevoz> dbprevozi = new ArrayList<Prevoz>();
    private static final ArrayList<Prevoz> mojiPrevozi = new ArrayList<Prevoz>();


    public class AsyncCallSoapVrniPrevoze extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(getApplicationContext());

        @Override
        protected String doInBackground(String... strings) {
            CallSoap CS = new CallSoap();

            String response = CS.VrniPrevoze();
            return response;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            dialog.dismiss();
            if(result.substring(0,1).equals("j"))
                new AsyncCallSoapVrniPrevoze().execute();
            Document doc = Jsoup.parse(result);

            //Log.d("krneki", doc.toString());
            String prevozi=doc.toString();
            Prevoz p;
            int i = 0;
            int j;
            while (i < 50000) {
                i = i + prevozi.substring(i).indexOf("<string>");
                i+=16; // zamaknemo naprej za <string>+\n
                if (prevozi.substring(i).contains("<string>")) {
                    int id = Integer.valueOf(prevozi.substring(i, i + prevozi.substring(i).indexOf('|'))); // zdaj smo pri delimiterju (ID)
                    i = i + prevozi.substring(i).indexOf('|');
                    String iz = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // pri naslednjem delimeterju (iz)
                    i = i + prevozi.substring(i).indexOf('|');
                    String ka = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // kam
                    i = i + prevozi.substring(i).indexOf('|');
                    String te = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // telefon
                    i = i + prevozi.substring(i).indexOf('|');
                    int os = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); // oseb
                    i = i + prevozi.substring(i).indexOf('|');
                    int mo = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); // maxoseb
                    i = i + prevozi.substring(i).indexOf('|');
                    Boolean za = Boolean.parseBoolean(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); //zavarovanje
                    i = i + prevozi.substring(i).indexOf('|');
                    String av = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // avto
                    i = i + prevozi.substring(i).indexOf('|');
                    DateTimeFormatter dtf = DateTimeFormat.forPattern("d.M. H:m");
                    String tempDatumString = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|') + prevozi.substring(i + prevozi.substring(i).indexOf('|') + 1).indexOf('|') + 1); // datum string
                    tempDatumString = tempDatumString.substring(tempDatumString.indexOf(',') + 1);
                    StringBuilder sb = new StringBuilder(tempDatumString);
                    sb.deleteCharAt(tempDatumString.indexOf('|'));
                    sb.deleteCharAt(tempDatumString.indexOf('.')+1);
                    sb.deleteCharAt(0);
                    tempDatumString = sb.toString();
                    i = i + prevozi.substring(i).indexOf('|');
                    DateTime dt = dtf.parseDateTime(tempDatumString);
                    i++; // preskočimo uro
                    i = i + prevozi.substring(i).indexOf('|');
                    String im = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // ime
                    i = i + prevozi.substring(i).indexOf('|');
                    String op = prevozi.substring(++i, i + prevozi.substring(i).indexOf('|')); // opis
                    i = i + prevozi.substring(i).indexOf('|');
                    Double la = Double.valueOf((prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))).replace(',','.')); //latitude
                    i = i + prevozi.substring(i).indexOf('|');
                    Double lo = Double.valueOf((prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))).replace(',','.')); //longtitude
                    i = i + prevozi.substring(i).indexOf('|');
                    int ra = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('|'))); // radius
                    i = i + prevozi.substring(i).indexOf('|');
                    int fk = Integer.valueOf(prevozi.substring(++i, i + prevozi.substring(i).indexOf('\n'))); // fkUporabnik
                    i = i + prevozi.substring(i).indexOf('\n');
                    p = new Prevoz(iz, ka, te, 10.0, os, mo, za, av, im, dt, lo,lo,ra);
                    dbprevozi.add(p);
                }
                else
                    break;
            }

            for(Prevoz prev : dbprevozi){
                if(prev.getMobitel().equals("1234567")){
                    mojiPrevozi.add(prev);
                }
            }

            Log.e("LA", "DB PREVOZI: " + dbprevozi.size());
            Log.e("MP", "MOJI PREVOZI: " + mojiPrevozi.size());
            Intent novSMSintent = new Intent(getApplicationContext(),SMSBackgroundService.class);
            novSMSintent.putExtra("MOJIPREVOZI",mojiPrevozi);
            getApplicationContext().startService(novSMSintent);

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setCurrentItem(1);

            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        new AsyncCallSoapVrniPrevoze().execute();//PRIDOBITEV PREVOZEV IZ BAZE
        Toast.makeText(getApplicationContext(),"Pridobivanje prevozev iz baze!", Toast.LENGTH_LONG);
        //Jaka
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(settings.getBoolean("sporocanje_switch", false))//če so smsi v nastavitvah vklopljeni
            startService(new Intent(this, SMSBackgroundService.class));
        //Jaka
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landingfragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0:
                    return ponujamFragment.newInstance(position,mojiPrevozi);
                case 1:
                    return landingFragment.newInstance(position);
                case 2:
                    return iscemFragment.newInstance(position,dbprevozi);
                default:
                    return landingFragment.newInstance(position);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}

/* USELESS KODA IZ MAIN ACTIVITY ČE BO KDO KAJ RABO */

//    TextView txtUsername;
//    TextView txtEmail;
//    Button btnLogout;
//    Button btnLanding;
//    SessionManager session;
//
//    private void showNotification(){
//        //channel
//        String id = "main_channel";
//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            CharSequence name = "Channel Name";
//            String desctription = "Channel description";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
//            notificationChannel.setDescription(desctription);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.WHITE);
//            notificationChannel.enableVibration(true);
//            if(notificationManager != null){
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//        }
//        //notification
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,id);
//        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        notificationBuilder.setContentTitle("PrEvOz *Koper-Šper* ČeZ 30 MiNuT");
//        notificationBuilder.setContentText("V primeru, da ste si premislili to sporočite *darkotu*");
//        notificationBuilder.setLights(Color.WHITE,500,5000);
//        notificationBuilder.setColor(Color.RED);
//        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(1000,notificationBuilder.build());
//    }
//
//
//    Button notification;
//    Button btnPodrobnosti;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_landing);
//
//        btnPodrobnosti = (Button) findViewById(R.id.btnPodrobnosti);
//        btnPodrobnosti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Prevoz dummyPrevoz = new Prevoz("Maribor", "Koper", "040202108", 10.0, 3, 4, false, "Toyota Yaris črne barve", "Luka", null,-34,151,100);
//                Intent mojIntent = new Intent(LandingActivity.this, PodrobnostiActivity.class);
//                mojIntent.putExtra("Prevoz", dummyPrevoz);
//                startActivity(mojIntent);
//            }
//        });

//
//           notification = (Button) findViewById(R.id.Notifications   );
//                   notification.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//                ToggleButton toggleButtonZanotif=(ToggleButton) findViewById(R.id.toggleButtonZanotif);
//                if(toggleButtonZanotif.isChecked())
//                showNotification();
//                }
//                });


//

//
//                session = new SessionManager(getApplicationContext());
//
//                        // get user data from session
//                        HashMap<String, String> user = session.getUserDetails();
//
//                // name
//                String name = user.get(SessionManager.KEY_NAME);
//                // email
//                String email = user.get(SessionManager.KEY_EMAIL);
//                Button btnLogout = (Button) findViewById(R.id.btnLogout);
//                btnLogout.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//                finish();
//                session.logoutUser();
//                }
//                });
