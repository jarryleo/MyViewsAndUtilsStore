package cn.leo.store.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 输入法弹出隐藏类
 * create by : Jarry Leo
 * date : 2018/8/14 14:23
 */
public class SoftInputUtil {
    /**
     * 弹出输入法
     *
     * @param v 需要输入的view
     */
    public static void showSoftInput(@NonNull View v) {
        Context context = v.getContext();
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        if (inputMethodManager != null) {
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
            if (activity != null && !activity.isFinishing()) {
                activity.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
    }

    /**
     * 收起输入法
     *
     * @param v 当前页面的view
     */
    public static void hideSoftInput(@NonNull View v) {
        Context context = v.getContext();
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
