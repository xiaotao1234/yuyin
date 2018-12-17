package com.baidu.tts.sample;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class CallOnActivity extends AppCompatActivity {
    private CheckBox mComeCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_on);
        mComeCall = findViewById(R.id.ld_check);
        mComeCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    Log.d("dda","check");
                    if(!isServiceRunning()){
                        Intent intent = new Intent(CallOnActivity.this,MyServicephone.class);
                        startService(intent);
                        Log.d("daxiao","Service start");
                    }
                }else {
                    Intent intent = new Intent(CallOnActivity.this,MyServicephone.class);
                    stopService(intent);
                }
            }
        });
    }
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.baidu.tts.sample.MyServicephone".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
