package com.logistics.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.logistics.R;


public class MainFragmentActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private LinearLayout mTabMap;
    private LinearLayout mTabRSS;
    private LinearLayout mTabGood;
    private LinearLayout mTabProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);


        initView();


        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0)
            {
                return mFragments.get(arg0);
            }
        };

        mViewPager.setAdapter(mAdapter);


        mViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {

            private int currentIndex;

            @Override
            public void onPageSelected(int position)
            {
                resetTabBtn();
                switch (position)
                {
                    case 0:
                        ((ImageButton) mTabMap.findViewById(R.id.ic_source_b))
                                .setImageResource(R.drawable.ic_source_selected);
                        break;
                    case 1:
                        ((ImageButton) mTabRSS.findViewById(R.id.ic_rss_b))
                                .setImageResource(R.drawable.ic_rss_selected);
                        break;
                    case 2:
                        ((ImageButton) mTabGood.findViewById(R.id.ic_search_b))
                                .setImageResource(R.drawable.ic_search_selected);
                        break;
                    case 3:
                        ((ImageButton) mTabProfile.findViewById(R.id.ic_mine_b))
                                .setImageResource(R.drawable.ic_mine_selected);
                        break;
                }

                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });

    }

    protected void resetTabBtn()
    {
        ((ImageButton) mTabMap.findViewById(R.id.ic_source_b))
                .setImageResource(R.drawable.ic_source_unselected);
        ((ImageButton) mTabRSS.findViewById(R.id.ic_rss_b))
                .setImageResource(R.drawable.ic_rss_unselected);
        ((ImageButton) mTabGood.findViewById(R.id.ic_search_b))
                .setImageResource(R.drawable.ic_search_unselected);
        ((ImageButton) mTabProfile.findViewById(R.id.ic_mine_b))
                .setImageResource(R.drawable.ic_mine_unselected);
    }

    private void initView()
    {

        mTabMap = (LinearLayout) findViewById(R.id.ic_source);
        mTabRSS = (LinearLayout) findViewById(R.id.ic_rss);
        mTabGood = (LinearLayout) findViewById(R.id.ic_search);
        mTabProfile = (LinearLayout) findViewById(R.id.ic_mine);

        MapFragmentActivity tab01 = new MapFragmentActivity();
        RSSFragmentActivity tab02 = new RSSFragmentActivity();
        GoodFragmentActivity tab03 = new GoodFragmentActivity();
        ProfileFragmentActivity tab04 = new ProfileFragmentActivity();
        mFragments.add(tab01);
        mFragments.add(tab02);
        mFragments.add(tab03);
        mFragments.add(tab04);
        ((ImageButton) mTabMap.findViewById(R.id.ic_source_b))
                .setImageResource(R.drawable.ic_source_selected);

        mTabMap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                mViewPager.setCurrentItem(0,false);

            }});
        ( mTabMap.findViewById(R.id.ic_source_b)).setOnClickListener(new  Button.OnClickListener(){
            public void onClick(View v) {
                mViewPager.setCurrentItem(0,false);
            }
        });

        mTabRSS.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mViewPager.setCurrentItem(1,false);

            }});
        ( mTabRSS.findViewById(R.id.ic_rss_b)).setOnClickListener(new  Button.OnClickListener(){
            public void onClick(View v) {
                mViewPager.setCurrentItem(1,false);
            }
        });

        mTabGood.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mViewPager.setCurrentItem(2,false);

            }});
        ( mTabGood.findViewById(R.id.ic_search_b)).setOnClickListener(new  Button.OnClickListener(){
            public void onClick(View v) {
                mViewPager.setCurrentItem(2,false);
            }
        });
        mTabProfile.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mViewPager.setCurrentItem(3,false);

            }});
        ( mTabProfile.findViewById(R.id.ic_mine_b)).setOnClickListener(new  Button.OnClickListener(){
            public void onClick(View v) {
                mViewPager.setCurrentItem(3,false);
            }
        });

    }


}
