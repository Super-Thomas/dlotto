package com.goodlotto.dlotto;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Make#newInstance} factory method to
 * create an instance of this fragment.
 */
// Fragment에는 OnClick 이벤트를 사용하면 안되므로 OnClickListener를 등록 후 사용한다
public class Fragment_Make extends Fragment implements View.OnClickListener {
    public class Ball {
        public String[] mBalls = new String[6];
    }

    ArrayList<Ball> mBallList = new ArrayList<Ball>();

    private ListView mListview;
    private CustomChoiceListViewAdapter mAdapter;
    private String[] mBalls = new String[6];
    private String[] mLotto = new String[45];
    private View mView;

    public Fragment_Make() {
        // Required empty public constructor
    }

    public void saveFile() {
        try {
            int i;
            String filePath = ((MainActivity)(MainActivity.mContext)).getApplicationContext().getFilesDir().getPath().toString() + "/file.txt";
            File file = new File(filePath) ;
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

            if (file.isFile() && file.canWrite()) {

                for (i=0; i<mBallList.size(); i++) {
                    String string = "";
                    string += mBallList.get(i).mBalls[0];
                    string += " ";
                    string += mBallList.get(i).mBalls[1];
                    string += " ";
                    string += mBallList.get(i).mBalls[2];
                    string += " ";
                    string += mBallList.get(i).mBalls[3];
                    string += " ";
                    string += mBallList.get(i).mBalls[4];
                    string += " ";
                    string += mBallList.get(i).mBalls[5];
                    string += " ";

                    bufferedWriter.write(string);
                    bufferedWriter.newLine();
                }

                bufferedWriter.close();
            }

        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        try {
            String filePath = ((MainActivity)(MainActivity.mContext)).getApplicationContext().getFilesDir().getPath().toString() + "/file.txt";
            File file = new File(filePath) ;
            BufferedReader bufReader = new BufferedReader(new FileReader(file));

            String line = "";
            // read file
            while ((line = bufReader.readLine()) != null) {
                System.out.println(line);

                String[] array = line.split(" ");

                if (array.length >= 6) {
                    Ball ball = new Ball();
                    Drawable[] iconBalls = new Drawable[6];

                    ball.mBalls[0] = array[0];
                    ball.mBalls[1] = array[1];
                    ball.mBalls[2] = array[2];
                    ball.mBalls[3] = array[3];
                    ball.mBalls[4] = array[4];
                    ball.mBalls[5] = array[5];

                    iconBalls[0] = getIconBall(ball.mBalls[0]);
                    iconBalls[1] = getIconBall(ball.mBalls[1]);
                    iconBalls[2] = getIconBall(ball.mBalls[2]);
                    iconBalls[3] = getIconBall(ball.mBalls[3]);
                    iconBalls[4] = getIconBall(ball.mBalls[4]);
                    iconBalls[5] = getIconBall(ball.mBalls[5]);

                    mBallList.add(ball);
                    mAdapter.addItem(iconBalls, ball.mBalls, "");
                }
            }

            bufReader.close();
        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Adapter 생성
        mAdapter = new CustomChoiceListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        // Fragment에서는 findViewById를 사용할 수 없다(Activity에서만 사용 가능)
        // 그래서 getView를 이용한다
        // inflate 하기 전 getView를 호출하면 null을 반환한다
        // 그래서 아래 방법으로 한다
        mView = inflater.inflate(R.layout.fragment__make, container, false);
        //mListview = (ListView)getView().findViewById(R.id.listview);
        mListview = (ListView)mView.findViewById(R.id.listview);
        mListview.setAdapter(mAdapter);

        // 버튼 초기화
        Button random = mView.findViewById(R.id.random);
        Button save = mView.findViewById(R.id.save);
        Button selAll = mView.findViewById(R.id.selectAll);
        Button selDel = mView.findViewById(R.id.selectDelete);
        random.setOnClickListener(this);
        save.setOnClickListener(this);
        selAll.setOnClickListener(this);
        selDel.setOnClickListener(this);

        // 공을 초기화
        int i;
        for (i=0; i<45; i++) {
            mLotto[i] = Integer.toString(i + 1);
        }

        make();

        readFile();

        // Inflate the layout for this fragment
        return mView;
    }

    // 정렬
    static class StringCompare implements Comparator<String> {
        public int extractInt(String s) {
            String num = s.replaceAll("\\D", "");
            // return 0 if no digits found
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }

        @Override
        public int compare(String o1, String o2) {
            return extractInt(o1) - extractInt(o2);
        }
    }

    public void make() {
        Random random = new Random();
        //random.nextInt(max_num_value - min_num_value + 1) + min_num_value;

        int count = 0;

        while (true) {
            int randomNum = random.nextInt(44 - 0 + 1) + 0;
            int i, j;
            Boolean overlap = false;
            for (i=0; i<6; i++) {
                if (mBalls[i] == mLotto[randomNum]) {
                    overlap = true;
                }
            }

            if (overlap == false) {
                mBalls[count] = mLotto[randomNum];
                count++;
            }

            if (count >= 6) {
                break;
            }
        }

        // 정렬
        ArrayList<String> ar = new ArrayList<String>();
        ar.add(mBalls[0]);
        ar.add(mBalls[1]);
        ar.add(mBalls[2]);
        ar.add(mBalls[3]);
        ar.add(mBalls[4]);
        ar.add(mBalls[5]);
        Collections.sort(ar, new StringCompare());

        mBalls[0] = ar.get(0);
        mBalls[1] = ar.get(1);
        mBalls[2] = ar.get(2);
        mBalls[3] = ar.get(3);
        mBalls[4] = ar.get(4);
        mBalls[5] = ar.get(5);

        //
        TextView[] tvBalls = new TextView[6];
        tvBalls[0] = (TextView)mView.findViewById(R.id.ball_1);
        tvBalls[1] = (TextView)mView.findViewById(R.id.ball_2);
        tvBalls[2] = (TextView)mView.findViewById(R.id.ball_3);
        tvBalls[3] = (TextView)mView.findViewById(R.id.ball_4);
        tvBalls[4] = (TextView)mView.findViewById(R.id.ball_5);
        tvBalls[5] = (TextView)mView.findViewById(R.id.ball_6);

        tvBalls[0].setText(mBalls[0]);
        tvBalls[1].setText(mBalls[1]);
        tvBalls[2].setText(mBalls[2]);
        tvBalls[3].setText(mBalls[3]);
        tvBalls[4].setText(mBalls[4]);
        tvBalls[5].setText(mBalls[5]);

        ImageView[] ivIcons = new ImageView[6];
        ivIcons[0] = (ImageView)mView.findViewById(R.id.pball_1);
        ivIcons[1] = (ImageView)mView.findViewById(R.id.pball_2);
        ivIcons[2] = (ImageView)mView.findViewById(R.id.pball_3);
        ivIcons[3] = (ImageView)mView.findViewById(R.id.pball_4);
        ivIcons[4] = (ImageView)mView.findViewById(R.id.pball_5);
        ivIcons[5] = (ImageView)mView.findViewById(R.id.pball_6);

        ivIcons[0].setImageDrawable(getIconBall(mBalls[0]));
        ivIcons[1].setImageDrawable(getIconBall(mBalls[1]));
        ivIcons[2].setImageDrawable(getIconBall(mBalls[2]));
        ivIcons[3].setImageDrawable(getIconBall(mBalls[3]));
        ivIcons[4].setImageDrawable(getIconBall(mBalls[4]));
        ivIcons[5].setImageDrawable(getIconBall(mBalls[5]));
    }

    public Drawable getIconBall(String ball) {
        int i;
        String[] orangeBalls = ((MainActivity)MainActivity.mContext).getmOrangeBalls();
        String[] blueBalls = ((MainActivity)MainActivity.mContext).getmBlueBalls();
        String[] grayBalls = ((MainActivity)MainActivity.mContext).getmGrayBalls();
        String[] redBalls = ((MainActivity)MainActivity.mContext).getmRedBalls();
        String[] greenBalls = ((MainActivity)MainActivity.mContext).getmGreenBalls();
        Drawable icon = null;

        for (i=0; i<orangeBalls.length; i++) {
            if (ball.equals(orangeBalls[i])) {
                icon =  ContextCompat.getDrawable(MainActivity.mContext, R.drawable.orange);
                return icon;
            }
        }

        for (i=0; i<blueBalls.length; i++) {
            if (ball.equals(blueBalls[i])) {
                icon =  ContextCompat.getDrawable(MainActivity.mContext, R.drawable.blue);
                return icon;
            }
        }

        for (i=0; i<grayBalls.length; i++) {
            if (ball.equals(grayBalls[i])) {
                icon =  ContextCompat.getDrawable(MainActivity.mContext, R.drawable.gray);
                return icon;
            }
        }

        for (i=0; i<redBalls.length; i++) {
            if (ball.equals(redBalls[i])) {
                icon =  ContextCompat.getDrawable(MainActivity.mContext, R.drawable.red);
                return icon;
            }
        }

        for (i=0; i<greenBalls.length; i++) {
            if (ball.equals(greenBalls[i])) {
                icon =  ContextCompat.getDrawable(MainActivity.mContext, R.drawable.green);
                return icon;
            }
        }

        return null;
    }

    public void save() {
        Ball ball = new Ball();
        Drawable[] iconBalls = new Drawable[6];

        iconBalls[0] = getIconBall(mBalls[0]);
        iconBalls[1] = getIconBall(mBalls[1]);
        iconBalls[2] = getIconBall(mBalls[2]);
        iconBalls[3] = getIconBall(mBalls[3]);
        iconBalls[4] = getIconBall(mBalls[4]);
        iconBalls[5] = getIconBall(mBalls[5]);

        ball.mBalls[0] = mBalls[0];
        ball.mBalls[1] = mBalls[1];
        ball.mBalls[2] = mBalls[2];
        ball.mBalls[3] = mBalls[3];
        ball.mBalls[4] = mBalls[4];
        ball.mBalls[5] = mBalls[5];

        mBallList.add(ball);
        mAdapter.addItem(iconBalls, mBalls, "");

        saveFile();
    }

    public void selectAll() {
        int count = 0;
        count = mAdapter.getCount();

        if (count > 0) {
            int i;

            if (mListview.getCheckedItemCount() == count) {
                for (i = 0; i < count; i++) {
                    mListview.setItemChecked(i, false);
                }
            }
            else {
                for (i = 0; i < count; i++) {
                    mListview.setItemChecked(i, true);
                }
            }

        }
    }

    public void deleteChecked() {
        SparseBooleanArray checkedItems = mListview.getCheckedItemPositions();
        int count = mAdapter.getCount();

        if (checkedItems.size() > 0) {
            int i;
            for (i = count - 1; i >= 0; i--) {
                if (checkedItems.get(i)) {
                    mBallList.remove(i);
                    mAdapter.remove(i);
                }
            }

            // 모든 선택 상태 초기화
            mListview.clearChoices();

            mAdapter.notifyDataSetChanged();
        }

        saveFile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.random:
                make();
                break;

            case R.id.save:
                save();

                // 리스트 뷰 갱신
                mAdapter.notifyDataSetChanged();
                mListview.setAdapter(mAdapter);
                break;

            case R.id.selectAll:
                selectAll();
                break;

            case R.id.selectDelete:
                deleteChecked();
                break;
        }
    }
}