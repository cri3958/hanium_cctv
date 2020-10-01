package com.hanium.cctv.others;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
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
    TextView text1,mem_name,text3;
    ImageView btn_cctv,btn_record,btn_setting,image_cctv,image_record;
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
        text1 = (TextView)findViewById(R.id.main_text_1);
        text3 = (TextView)findViewById(R.id.main_text_3);
        mem_name = (TextView) findViewById(R.id.mem_name);
        btn_cctv = (ImageView) findViewById(R.id.btn_cctv);
        btn_record = (ImageView) findViewById(R.id.btn_record);
        btn_setting = (ImageView) findViewById(R.id.btn_setting);
        image_cctv = (ImageView) findViewById(R.id.main_image_cctv);
        image_record = (ImageView) findViewById(R.id.main_image_record);

        setUIratio();

        final Intent inIntent = getIntent();
        mem_name.setText(inIntent.getStringExtra("mem_name"));
        btn_cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CctvActivity.class);
                startActivity(intent);
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

    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) { //화면회전 방지
        super.onConfigurationChanged(newConfig);
    }
    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return  size;
    }
    public void setUIratio(){
        Point ScreenSize = getScreenSize(this);
        float density  = getResources().getDisplayMetrics().density;

        int standardSize_X = (int) (ScreenSize.x / density);
        int standardSize_Y = (int) (ScreenSize.y / density);

        ViewGroup.LayoutParams params_btn_cctv = btn_cctv.getLayoutParams();
        params_btn_cctv.height = standardSize_Y/7*8;
        btn_cctv.setLayoutParams(params_btn_cctv);

        ViewGroup.LayoutParams params_btn_record = btn_record.getLayoutParams();
        params_btn_record.height = standardSize_Y/7*8;
        btn_record.setLayoutParams(params_btn_record);

        ViewGroup.LayoutParams params_image_cctv = image_cctv.getLayoutParams();
        params_image_cctv.width = (standardSize_X/4)*7;
        image_cctv.setLayoutParams(params_image_cctv);

        ViewGroup.LayoutParams params_image_record = image_record.getLayoutParams();
        params_image_record.height = standardSize_X/4*7;
        image_record.setLayoutParams(params_image_record);

        ViewGroup.LayoutParams params_btn_setting = btn_setting.getLayoutParams();
        params_btn_setting.width = standardSize_X/2;
        params_btn_setting.height = standardSize_Y/4;
        btn_setting.setLayoutParams(params_btn_setting);

        text1.setTextSize((float)standardSize_X/19);
        mem_name.setTextSize((float)standardSize_X/18);
        text3.setTextSize((float)standardSize_X/19);
    }
}