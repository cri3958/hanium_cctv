package com.hanium.cctv.others;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
    LinearLayout btn_cctv,btn_record,btn_setting;
    TextView info_connect,info_place;
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
            }
        });

        btn_cctv = (LinearLayout) findViewById(R.id.main_layout_cctv);
        btn_record = (LinearLayout) findViewById(R.id.main_layout_record);
        btn_setting = (LinearLayout) findViewById(R.id.main_layout_setting);
        info_connect = (TextView) findViewById(R.id.main_info_connect);
        info_place = (TextView) findViewById(R.id.main_info_place);

        final Intent inIntent = getIntent();
        btn_cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CctvActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("mem_id", inIntent.getStringExtra("mem_id"));
                intent.putExtra("mem_pw", inIntent.getStringExtra("mem_pw"));
                startActivity(intent);
            }
        });

        DbHelper dbHelper = new DbHelper(this);
        int cctvnum = dbHelper.countCCTVLIST();
        Log.d("@@@",cctvnum+"");
        String cctvplace = dbHelper.get1CCTVPLACE();
        if(cctvplace.length()>9)
            cctvplace = cctvplace.substring(0,9).concat("...");

        if(cctvnum==1){
            info_place.setText(cctvplace);
        }else{
            cctvplace = cctvplace.concat("\n외 "+(cctvnum-1)+"곳 ");
            info_place.setText(cctvplace);
        }
        info_connect.setText(cctvnum+"");
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) { //화면회전 방지
        super.onConfigurationChanged(newConfig);
    }
}