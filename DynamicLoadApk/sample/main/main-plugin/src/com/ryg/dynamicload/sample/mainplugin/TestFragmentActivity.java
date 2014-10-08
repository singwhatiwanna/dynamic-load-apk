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
import com.ryg.utils.DLConstants;

public class TestFragmentActivity extends DLBasePluginFragmentActivity
implements OnClickListener{

    private static final String TAG = "TestFragmentActivity";

    private EditText mEditText;
    private ImageView mImageView;
    private Button mShowFragmentButton;
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
        mShowFragmentButton = (Button)findViewById(R.id.show_fragment);
        mShowFragmentButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        if (mFrom == DLConstants.FROM_INTERNAL) { 
            super.onResume();
        }
        mImageView = (ImageView)findViewById(R.id.imageView1);
        mImageView.setImageResource(R.drawable.ppmm);
        Log.d(TAG, "onResume");
    }
    
    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        if (mFrom == DLConstants.FROM_INTERNAL) { 
            super.onPause();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mShowFragmentButton) {
            FragmentManager manager= getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, new TestFragment());
            transaction.addToBackStack("TestFragment#1");
            transaction.commit();
        }
        
    }
    
}
