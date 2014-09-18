package com.ryg.dynamicloadclient;

import com.ryg.utils.TestHostClass;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class TestActivity extends BaseActivity {

    private static final String TAG = "TestActivity";

    private EditText mEditText;
    private ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);
        TestButton button = (TestButton)findViewById(R.id.button1);
        button.setText(that.getResources().getString(R.string.test));
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(that, "quit : " + mDexPath, Toast.LENGTH_SHORT).show();
                that.setResult(RESULT_FIRST_USER);
                that.finish();
            }
        });
        mEditText = (EditText)findViewById(R.id.editText1);
        mEditText.setText(R.string.hello_world);
        
        Button invokeHostButton = (Button) findViewById(R.id.invokeHost);
        invokeHostButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                TestHostClass testHostClass = new TestHostClass();
                testHostClass.testMethod(that);
            }
        });
        
        
    }

    @Override
    public void onResume() {
        if (mFrom == FROM_INTERNAL) { 
            super.onResume();
        }
        mImageView = (ImageView)findViewById(R.id.imageView1);
        mImageView.setImageResource(R.drawable.ppmm);
        Log.d(TAG, "onResume");
    }
    
    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        if (mFrom == FROM_INTERNAL) { 
            super.onPause();
        }
    }

}
