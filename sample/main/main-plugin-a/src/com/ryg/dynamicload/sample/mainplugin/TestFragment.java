package com.ryg.dynamicload.sample.mainplugin;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.sample.mainplugina.R;

public class TestFragment extends Fragment implements OnClickListener{

    private String mPluginPackageName;
    private Button button1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fragment, container, false);
    }

    @Override
    public void onResume() {
        button1 = (Button)(getView().findViewById(R.id.button1));
        button1.setOnClickListener(this);
        super.onResume();
    }

    public TestFragment setPluginPackageName(String pluginPackageName) {
        mPluginPackageName = pluginPackageName;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == button1) {
            Context context = getActivity();
            DLIntent dlIntent = new DLIntent(mPluginPackageName, MainActivity.class);
            DLPluginManager.getInstance(context).startPluginActivity(context, dlIntent);
        }
        
    }

}
