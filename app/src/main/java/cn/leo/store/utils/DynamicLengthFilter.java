package cn.leo.store.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Leo on 2017/12/25.
 *
 * @author Leo
 */

public class DynamicLengthFilter {
    /**
     * 动态限制EditText长度。输入小数的时候和输入整数的时候可输入的字符长度不同
     *
     * @param editText
     * @param min      无小数点时候的位数
     * @param max      有小数点时候位数
     */
    public static void setFilter(EditText editText, final int min, final int max) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                int length = min;
                if (dest.toString().contains(".") || source.toString().contains(".")) {
                    length = max;
                }

                int keep = length - (dest.length() - (dend - dstart));
                if (keep <= 0) {
                    return "";
                } else if (keep >= end - start) {
                    return null;
                } else {
                    keep += start;
                    if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                        --keep;
                        if (keep == start) {
                            return "";
                        }
                    }
                    return source.subSequence(start, keep);
                }
            }
        }});
    }

    /**
     * 限制文本框输入的小数点后面位数
     *
     * @param editText
     * @param length   保留的小数点位数
     */
    public static void setDecimalLength(final EditText editText, final int length) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                int i = str.lastIndexOf(".");
                if (i > -1 && str.length() - i > length + 1) {
                    editText.setText(str.substring(0, i + length + 1));
                    editText.setSelection(i + length + 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String price = s.toString();
                if (price.startsWith(".")) {
                    editText.setText("0.");
                    editText.setSelection(2);
                    return;
                }
            }
        });
    }
}
