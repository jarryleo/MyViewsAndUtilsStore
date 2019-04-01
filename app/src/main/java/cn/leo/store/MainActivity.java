package cn.leo.store;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import cn.leo.store.utils.EditTextNextFocusUtil;
import cn.leo.store.utils.SafetyMainHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditTextNextFocusUtil.autoBind(this);
        SafetyMainHandler safetyMainHandler = new SafetyMainHandler(this, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                return false;
            }
        });
    }
}
