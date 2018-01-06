package nameplaceholder.prevazanjaorg;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

    boolean BackgroundServiceRunning = true;//dobim nekak iz nastavitev

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        //Jaka
        if(BackgroundServiceRunning)//če so smsi v nastavitvah vklopljeni
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
                    return ponujamFragment.newInstance(position);
                case 1:
                    return landingFragment.newInstance(position);
                case 2:
                    return iscemFragment.newInstance(position);
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
