package cn.leo.store.utils;

import android.app.Activity;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : Jarry Leo
 * @date : 2019/1/15 17:11
 */
public class EditTextNextFocusUtil {

    private EditTextNextFocusUtil() {
    }

    public static void autoBind(Activity activity) {
        View view = activity.getWindow().getDecorView();
        autoBind(view);
    }

    public static void autoBind(Fragment fragment) {
        View view = fragment.getView();
        autoBind(view);
    }

    public static void autoBind(android.app.Fragment fragment) {
        View view = fragment.getView();
        autoBind(view);
    }

    public static void autoBind(View view) {
        if (view == null) {
            return;
        }
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                doOnIdle(v);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
            }
        });
    }

    private static void doOnIdle(final View view) {
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                doSetEditTextNextFocus(view);
                return false;
            }
        });
    }

    private static void doSetEditTextNextFocus(View view) {
        List<EditTextWrapper> list = new ArrayList<>();
        getAllEditText(list, view);
        Collections.sort(list);
        EditText pre = null;
        for (EditTextWrapper editTextWrapper : list) {
            EditText editText = editTextWrapper.getEditText();
            if (pre == null) {
                pre = editText;
            } else {
                setEditTextNextAction(pre, editText);
                pre = editText;
            }
        }
        if (pre != null) {
            pre.setSingleLine();
            pre.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }

    private static void getAllEditText(List<EditTextWrapper> list, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);
                getAllEditText(list, child);
            }
        } else {
            if (view instanceof EditText
                    && view.getVisibility() == View.VISIBLE) {
                list.add(new EditTextWrapper((EditText) view));
            }
        }
    }

    private static void setEditTextNextAction(EditText editText, final EditText nextEditText) {
        editText.setSingleLine();
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    nextEditText.requestFocus();
                }
                return true;
            }
        });
    }

    private static class EditTextWrapper implements Comparable<EditTextWrapper> {
        private EditText mEditText;

        public EditTextWrapper(EditText editText) {
            mEditText = editText;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        public int compareTo(@NonNull EditTextWrapper o) {
            int[] l1 = new int[2];
            int[] l2 = new int[2];
            mEditText.getLocationInWindow(l1);
            o.getEditText().getLocationInWindow(l2);
            int t = l1[1] - l2[1];
            int l = l1[0] - l2[0];
            return t == 0 ? l : t;
        }
    }

}
