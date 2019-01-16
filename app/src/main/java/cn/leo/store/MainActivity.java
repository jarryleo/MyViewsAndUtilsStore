package cn.leo.store;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.TouchDelegate;

import cn.leo.store.utils.EditTextNextFocusUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditTextNextFocusUtil.autoBind(this);
    }
}
