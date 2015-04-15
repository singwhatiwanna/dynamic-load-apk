
package com.ryg.dynamicload.sample.mainplugin;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ryg.dynamicload.DLBasePluginFragmentActivity;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.sample.mainplugina.R;

public class TestFragmentActivity extends DLBasePluginFragmentActivity
        implements OnClickListener {

    private static final String TAG = "TestFragmentActivity";

    private EditText mEditText;
    private ImageView mImageView;
    private Button mShowFragmentButton;

    private Button mStartPluginB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);
        // 输出Parcelable对象信息
        Toast.makeText(that, getIntent().getExtras().getParcelable("person").toString(),
                Toast.LENGTH_SHORT).show();

        Log.d(TAG, "### person info : " + getIntent().getExtras().getParcelable("person"));
        TestButton button = (TestButton) findViewById(R.id.button1);
        button.setText(that.getResources().getString(R.string.test));
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(that, "quit", Toast.LENGTH_SHORT).show();
                that.setResult(RESULT_FIRST_USER);
                that.finish();
            }
        });

        mEditText = (EditText) findViewById(R.id.editText1);
        mEditText.setText(R.string.hello_world);
        mShowFragmentButton = (Button) findViewById(R.id.show_fragment);
        mShowFragmentButton.setOnClickListener(this);

        mStartPluginB = (Button) findViewById(R.id.start_plugin_b);
        mStartPluginB.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageView = (ImageView) findViewById(R.id.imageView1);
        mImageView.setImageResource(R.drawable.ppmm);
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onClick(View v) {
        if (v == mShowFragmentButton) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container,
                    new TestFragment().setPluginPackageName(getPackageName()));
            transaction.addToBackStack("TestFragment#1");
            transaction.commit();
        } else if (v == mStartPluginB) {
            int result = startPluginActivity(new DLIntent("com.ryg.dynamicload.sample.mainpluginb",
                    ".MainActivity"));
            if (result != DLPluginManager.START_RESULT_SUCCESS) {
                Toast.makeText(this, "start Activity failed", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
