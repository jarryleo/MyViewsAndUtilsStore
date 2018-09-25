package cn.leo.store.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by Leo on 2017/7/20.
 */

public class TextLengthWather implements TextWatcher {
    private TextView mTextView;
    private int max = 100;

    public TextLengthWather(TextView textView) {
        mTextView = textView;
    }

    public TextLengthWather(TextView textView, int max) {
        mTextView = textView;
        this.max = max;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int length = s.length();
        if (length < 1) {
            mTextView.setText("(最多输入" + max + "个字)");
        } else {
            mTextView.setText("(还可以输入" + (max - length) + "个字)");
        }
    }
}
