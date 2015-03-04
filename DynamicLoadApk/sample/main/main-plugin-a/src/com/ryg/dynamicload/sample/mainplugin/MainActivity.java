
package com.ryg.dynamicload.sample.mainplugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.sample.mainplugina.R;

public class MainActivity extends DLBasePluginActivity {

    private static final String TAG = "Client-MainActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        that.setContentView(generateContentView(that));
    }

    private View generateContentView(final Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        layout.setBackgroundColor(Color.parseColor("#F79AB5"));
        Button button = new Button(context);
        button.setText("Start TestActivity");
        layout.addView(button, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DLIntent intent = new DLIntent(getPackageName(), TestFragmentActivity.class);
                // 传递Parcelable类型的数据
                intent.putExtra("person", new Person("plugin-a", 22));
                intent.putExtra("dl_extra", "from DL framework");
                startPluginActivityForResult(intent, 0);
            }
        });
        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult resultCode=" + resultCode);
        if (resultCode == RESULT_FIRST_USER) {
            that.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
