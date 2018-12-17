package com.baidu.tts.sample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.sample.Interface.PhoneComeListener;
import com.baidu.tts.sample.PhoneBackageService.MyService;
import com.baidu.tts.sample.control.InitConfig;
import com.baidu.tts.sample.control.MySyntherizer;
import com.baidu.tts.sample.control.NonBlockSyntherizer;
import com.baidu.tts.sample.listener.UiMessageListener;
import com.baidu.tts.sample.util.AutoCheck;
import com.baidu.tts.sample.util.OfflineResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyServicephone extends Service {
    private String TAG = "daxiao";
    private MyPhoneStateListener myPhoneStateListener;
    private TelephonyManager telephonyManager;
    private PhoneComeListener phoneComeListener;
    protected String appId = "11005757";
    protected String servicethings = "我不知道说什么";
    protected String appKey = "Ovcz19MGzIKoDDb3IsFFncG1";
    protected MyService myService;
    protected String secretKey = "e72ebb6d43387fc7f85205ca7e6706e2";
    protected TtsMode ttsMode = TtsMode.MIX;
    protected String offlineVoice = OfflineResource.VOICE_MALE;
    protected MySyntherizer synthesizer;
    public MyServicephone() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initevents();
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
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG,"来电");
                    Log.d(TAG,incomingNumber);
                    synthesizer.speak(incomingNumber+"来电");
                    synthesizer.release();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG,"挂断");
                    synthesizer.speak(incomingNumber+"已挂断");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG,"空闲");
                    break;
            }
        }
    }
    private void initevents(){
        SpeechSynthesizerListener listener = null;
        Map<String, String> params = getParams();
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        Handler handler = null;
        synthesizer = new NonBlockSyntherizer(this, initConfig, handler); // 此处可以改为MySyntherizer 了解调用过程
    }

    private void getListener(PhoneComeListener phoneComeListener){
        this.phoneComeListener = phoneComeListener;
    }

    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }
    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
        }
        return offlineResource;
    }
}
