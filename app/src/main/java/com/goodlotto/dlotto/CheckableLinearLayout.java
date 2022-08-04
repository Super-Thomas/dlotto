package com.goodlotto.dlotto;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.goodlotto.dlotto.R;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);

        return cb.isChecked();
        // return mIsChecked;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);

        setChecked(cb.isChecked() ? false : true);
        //setChecked(mIsChecked ? false : true);
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox) ;

        if (cb.isChecked() != checked) {
            cb.setChecked(checked);
        }

        // CheckBox 가 아닌 View의 상태 변경.
    }
}
