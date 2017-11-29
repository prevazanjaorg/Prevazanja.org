package nameplaceholder.prevazanjaorg;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LandingActivity extends FragmentActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_landing);
//
////        PonujamFragment ponujam = PonujamFragment.newInstance("Fragment 1", "Instance 1");
////        Intent mojIntent = new Intent(LandingActivity.this, PonujamFragment.class);
////        startActivity(mojIntent);
//
////        FragmentManager fragmentManager = getSupportFragmentManager();
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////
////        PonujamFragment ponujamFragment = new PonujamFragment();
////        fragmentTransaction.add(R.id.ponujamFragment,ponujamFragment);
////        fragmentTransaction.commit();
//    }

    static final int ITEMS = 1;
    MyAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PonujamFragment();
                default:
                    return new PonujamFragment();
            }
        }
    }
}
