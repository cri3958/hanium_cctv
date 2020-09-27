package com.hanium.cctv.others;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hanium.cctv.R;
import com.hanium.cctv.cctv.CctvActivity;
import com.hanium.cctv.record.RecordActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("FCM Log,", "getInstanceId failed", task.getException());
                    return;
                }
                String token = task.getResult().getToken();
                Log.d("FCM Log", "FCM 토큰 : " + token);
                //Toast.makeText(getApplicationContext(),token,Toast.LENGTH_SHORT).show();
            }
        });

        TextView mem_name = (TextView) findViewById(R.id.mem_name);
        final Intent inIntent = getIntent();
        mem_name.setText(inIntent.getStringExtra("mem_name"));

        ImageView btn_cctv = findViewById(R.id.btn_cctv);
        btn_cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CctvActivity.class);
                startActivity(intent);
            }
        });

        ImageView btn_graph = findViewById(R.id.btn_graph);
        btn_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        ImageView btn_setting = findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("mem_id", inIntent.getStringExtra("mem_id"));
                intent.putExtra("mem_pw", inIntent.getStringExtra("mem_pw"));
                startActivity(intent);
            }
        });

    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) { //화면회전 방지
        super.onConfigurationChanged(newConfig);
    }
}