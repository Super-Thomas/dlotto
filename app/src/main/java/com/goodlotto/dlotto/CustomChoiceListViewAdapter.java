package com.goodlotto.dlotto;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomChoiceListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    // ListViewAdapter의 생성자
    public CustomChoiceListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    public String checkNumbers(String[] numbers) {
        String[] num = ((MainActivity)MainActivity.mContext).mBalls;
        String bnum = ((MainActivity)MainActivity.mContext).mBball;

        if (num[0] == null) {
            return "";
        }

        String grade = "";
        int i, j, count;
        for (i=0, count=0; i<6; i++) {
            for (j=0; j<6; j++) {
                if (num[i].equals(numbers[j])) {
                    count++;
                }
            }
        }

        if (count == 6) {
            grade = "1등";
        }
        else if (count == 5) {
            Boolean match = false;
            for (i=0; i<6; i++) {
                if (num[i].equals(bnum)) {
                    match = true;
                }
            }

            if (match) {
                grade = "2등";
            }
            else {
                grade = "3등";
            }
        }
        else if (count == 4) {
            grade = "4등";
        }
        else if (count == 3) {
            grade = "5등";
        }

        return grade;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView[] ivBalls = new ImageView[6];
        ivBalls[0] = (ImageView) convertView.findViewById(R.id.in_pball_1) ;
        ivBalls[1] = (ImageView) convertView.findViewById(R.id.in_pball_2) ;
        ivBalls[2] = (ImageView) convertView.findViewById(R.id.in_pball_3) ;
        ivBalls[3] = (ImageView) convertView.findViewById(R.id.in_pball_4) ;
        ivBalls[4] = (ImageView) convertView.findViewById(R.id.in_pball_5) ;
        ivBalls[5] = (ImageView) convertView.findViewById(R.id.in_pball_6) ;

        TextView[] tvBalls = new TextView[6];
        tvBalls[0] = (TextView) convertView.findViewById(R.id.in_ball_1) ;
        tvBalls[1] = (TextView) convertView.findViewById(R.id.in_ball_2) ;
        tvBalls[2] = (TextView) convertView.findViewById(R.id.in_ball_3) ;
        tvBalls[3] = (TextView) convertView.findViewById(R.id.in_ball_4) ;
        tvBalls[4] = (TextView) convertView.findViewById(R.id.in_ball_5) ;
        tvBalls[5] = (TextView) convertView.findViewById(R.id.in_ball_6) ;

       // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        TextView tvResult = convertView.findViewById(R.id.in_result);
        tvResult.setText(checkNumbers(listViewItem.getTextBalls()));

        // 아이템 내 각 위젯에 데이터 반영
        ivBalls[0].setImageDrawable(listViewItem.getIconBall1());
        ivBalls[1].setImageDrawable(listViewItem.getIconBall2());
        ivBalls[2].setImageDrawable(listViewItem.getIconBall3());
        ivBalls[3].setImageDrawable(listViewItem.getIconBall4());
        ivBalls[4].setImageDrawable(listViewItem.getIconBall5());
        ivBalls[5].setImageDrawable(listViewItem.getIconBall6());

        tvBalls[0].setText(listViewItem.getTextBall1());
        tvBalls[1].setText(listViewItem.getTextBall2());
        tvBalls[2].setText(listViewItem.getTextBall3());
        tvBalls[3].setText(listViewItem.getTextBall4());
        tvBalls[4].setText(listViewItem.getTextBall5());
        tvBalls[5].setText(listViewItem.getTextBall6());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable[] drawBalls, String balls[], String result) {
        ListViewItem item = new ListViewItem();

        item.setIconBall1(drawBalls[0]);
        item.setIconBall2(drawBalls[1]);
        item.setIconBall3(drawBalls[2]);
        item.setIconBall4(drawBalls[3]);
        item.setIconBall5(drawBalls[4]);
        item.setIconBall6(drawBalls[5]);

        item.setTextBall1(balls[0]);
        item.setTextBall2(balls[1]);
        item.setTextBall3(balls[2]);
        item.setTextBall4(balls[3]);
        item.setTextBall5(balls[4]);
        item.setTextBall6(balls[5]);

        item.setTextResult(result);

        listViewItemList.add(item);
    }

    public void remove(int index) {
        listViewItemList.remove(index);
    }
}
