package com.baidu.tts.sample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyServicephone extends Service {
    private String TAG = "daxiao";
    private MyPhoneStateListener myPhoneStateListener;
    private TelephonyManager telephonyManager;
    public MyServicephone() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myPhoneStateListener = new MyPhoneStateListener();
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG,"来电");
                    Log.d(TAG,incomingNumber);

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG,"挂断");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG,"空闲");
                    break;
            }
        }
    }
}
