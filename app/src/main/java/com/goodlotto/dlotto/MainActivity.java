package com.goodlotto.dlotto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class MainActivity extends AppCompatActivity {
    public static Context mContext = null;
    private ViewPager2 mViewPager = null;
    private TabLayout mTabLayout = null;
    private String mMenu[] = { "로또 현황", "번호 생성", "로또 확인" };
    private Boolean mAttached = false;
    private Boolean mAutoRefresh = false;
    private Adapter mAdapter;
    private Fragment_Result mFR;
    private Fragment_Make mFM;
    private  Fragment_Check mFC;

    private String[] mOrangeBalls = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    private String[] mBlueBalls = { "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" };
    private String[] mRedBalls = { "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" };
    private String[] mGrayBalls = { "31", "32", "33", "34", "35", "36", "37", "38", "39", "40" };
    private String[] mGreenBalls = { "41", "42", "43", "44", "45" };

    public String[] getmOrangeBalls() {
        return mOrangeBalls;
    }
    public String[] getmBlueBalls() {
        return mBlueBalls;
    }
    public String[] getmRedBalls() {
        return mRedBalls;
    }
    public String[] getmGrayBalls() {
        return mGrayBalls;
    }
    public String[] getmGreenBalls() {
        return mGreenBalls;
    }

    public String[] mBalls = new String[6];
    public String mBball;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int request = requestCode & 0xFFFF;
        if (mFC != null) {
            mFC.onActivityResult(request, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // 탭 레이아웃을 초기화 한다
        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText(mMenu[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(mMenu[1]));
        mTabLayout.addTab(mTabLayout.newTab().setText(mMenu[2]));
        mTabLayout.setTabGravity(mTabLayout.GRAVITY_FILL);

        if (mFR == null) {
            mFR = new Fragment_Result();
        }
        if (mFM == null) {
            mFM = new Fragment_Make();
        }
        if (mFC == null) {
            mFC = new Fragment_Check();
        }

        // 어댑터 초기화
        mViewPager = (ViewPager2) findViewById(R.id.viewpager2);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), getLifecycle(), 3);
        mViewPager.setAdapter(myPagerAdapter);

        // 탭 레이아웃과 연동하기 위한 TabLayoutMediator
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> tab.setText(mMenu[position])).attach();

        //탭 선택 이벤트
        //mViewPager.addOnLayoutChangeListener((View.OnLayoutChangeListener) new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void SetRound(String round) {
        TextView tv = (TextView)findViewById(R.id.round);
        tv.setText(round);
    }

    public void SetDrawDate(String drawDate) {
        TextView tv = (TextView)findViewById(R.id.drawdate);
        tv.setText(drawDate);
    }

    private void SetColorImage(ImageView iv, String number) {
        int i;

        for (i=0; i<mOrangeBalls.length; i++) {
            if (number.equals(mOrangeBalls[i])) {
                iv.setImageResource(R.drawable.orange);
                return ;
            }
        }

        for (i=0; i<mBlueBalls.length; i++) {
            if (number.equals(mBlueBalls[i])) {
                iv.setImageResource(R.drawable.blue);
                return ;
            }
        }

        for (i=0; i<mRedBalls.length; i++) {
            if (number.equals(mRedBalls[i])) {
                iv.setImageResource(R.drawable.red);
                return ;
            }
        }

        for (i=0; i<mGrayBalls.length; i++) {
            if (number.equals(mGrayBalls[i])) {
                iv.setImageResource(R.drawable.gray);
                return ;
            }
        }

        for (i=0; i<mGreenBalls.length; i++) {
            if (number.equals(mGreenBalls[i])) {
                iv.setImageResource(R.drawable.green);
                return ;
            }
        }
    }

    public void SetBalls(String[] balls, String bBall) {
        TextView[] tvBalls = new TextView[6];
        tvBalls[0] = (TextView)findViewById(R.id.ball1);
        tvBalls[1] = (TextView)findViewById(R.id.ball2);
        tvBalls[2] = (TextView)findViewById(R.id.ball3);
        tvBalls[3] = (TextView)findViewById(R.id.ball4);
        tvBalls[4] = (TextView)findViewById(R.id.ball5);
        tvBalls[5] = (TextView)findViewById(R.id.ball6);
        TextView tvBball = (TextView)findViewById(R.id.ball7);

        ImageView[] ivBalls = new ImageView[6];
        ivBalls[0] = (ImageView)findViewById(R.id.pball1);
        ivBalls[1] = (ImageView)findViewById(R.id.pball2);
        ivBalls[2] = (ImageView)findViewById(R.id.pball3);
        ivBalls[3] = (ImageView)findViewById(R.id.pball4);
        ivBalls[4] = (ImageView)findViewById(R.id.pball5);
        ivBalls[5] = (ImageView)findViewById(R.id.pball6);
        ImageView ivBball = (ImageView)findViewById(R.id.pball7);

        int i;
        for (i=0; i<6; i++) {
            mBalls[i] = balls[i];
            SetColorImage(ivBalls[i], balls[i]);
            tvBalls[i].setText(balls[i]);
        }

        mBball = bBall;
        SetColorImage(ivBball, bBall);
        tvBball.setText(bBall);
    }

    // 탭 페이지를 위한 Adapter class 정의
    public class MyPagerAdapter extends FragmentStateAdapter {
        int numOfTabs; // 탭의 개수

        public MyPagerAdapter(FragmentManager fm, Lifecycle lc, int numTabs) {
            super(fm, lc);
            this.numOfTabs = numTabs;
        }

        @Override public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    if (mFR == null) {
                        mFR = new Fragment_Result();
                    }
                    return mFR;
                case 1:
                    if (mFM == null) {
                        mFM = new Fragment_Make();
                    }
                    return mFM;
                case 2:
                    if (mFC == null) {
                        mFC = new Fragment_Check();
                    }
                    return mFC;
            }

            return null;
        }

        @Override public int getItemCount() {
            return numOfTabs;
        }
    }
}