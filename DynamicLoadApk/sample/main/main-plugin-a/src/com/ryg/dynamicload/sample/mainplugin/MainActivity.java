package com.ryg.dynamicload.sample.mainplugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
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
import com.ryg.dynamicload.service.ITestServiceInterface;

public class MainActivity extends DLBasePluginActivity {

    private static final String TAG = "Client-MainActivity";
    private ServiceConnection mConnecton;
    
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
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setBackgroundColor(Color.parseColor("#F79AB5"));
        Button button = new Button(context);
        button.setText("Start TestActivity");
        layout.addView(button, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
        
        Button button2 = new Button(context);
        button2.setText("Start Service");
        layout.addView(button2, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DLIntent intent = new DLIntent(getPackageName(), TestService.class);
                startPluginService(intent);
            }
        });
        
       
        Button button3 = new Button(context);
        button3.setText("bind Service");
        layout.addView(button3, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConnecton == null) {
                    mConnecton = new ServiceConnection() {
                        public void onServiceDisconnected(ComponentName name) {
                        }
                        public void onServiceConnected(ComponentName name, IBinder binder) {
                            int sum = ((ITestServiceInterface)binder).sum(5, 5);
                            Log.e("MainActivity", "onServiceConnected sum(5 + 5) = " + sum);
                        }
                    };
                }
                DLIntent intent = new DLIntent(getPackageName(), TestService.class);
                bindPluginService(intent, mConnecton, Context.BIND_AUTO_CREATE);
            }
        });
        
        Button button4 = new Button(context);
        button4.setText("unbind Service");
        layout.addView(button4, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        button4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (mConnecton != null) {
                    DLIntent intent = new DLIntent(getPackageName(), TestService.class);
                    unBindPluginService(intent, mConnecton);
                    mConnecton = null;
                }
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

    public static class Person implements Parcelable {

        private String mName;
        private int mAge;

        public Person(String name, int age) {
            mName = name;
            mAge = age;
        }

        @Override
        public int describeContents() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            // TODO Auto-generated method stub
            dest.writeString(mName);
            dest.writeInt(mAge);
        }

        public Person(Parcel in) {
            mName = in.readString();
            mAge = in.readInt();
        }

        public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
            public Person createFromParcel(Parcel in) {
                return new Person(in);
            }

            public Person[] newArray(int size) {
                return new Person[size];
            }
        };

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return "name = " + mName + " age = " + mAge;
        }

    }
}
