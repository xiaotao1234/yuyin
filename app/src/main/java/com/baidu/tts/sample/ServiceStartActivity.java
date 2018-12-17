package com.baidu.tts.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ServiceStartActivity extends AppCompatActivity {
    private ImageView callon;
    private ImageView smson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_start);
        callon = findViewById(R.id.call_on);
        smson = findViewById(R.id.message_on);
        callon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dda","dadad");
                Intent intent = new Intent(ServiceStartActivity.this,CallOnActivity.class);
                startActivity(intent);
            }
        });
        smson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceStartActivity.this,CallOnActivity.class);
                startActivity(intent);
            }
        });
    }
}
