package com.hanium.cctv;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hanium.cctv.Login.LoginActivity;
import com.hanium.cctv.function.activity_list_of_cctv;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_checkcctv = findViewById(R.id.btn_checkcctv);
        btn_checkcctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, activity_list_of_cctv.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /*Button btn_emergency = (Button) findViewById(R.id.btn_emergency);
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = "서울특별시 강서구";
                String name = "이호진";
                int age = 20;
                String specialthing = "없음";
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.putExtra("sms_body","장소 : "+place+"\n이름 : "+name+"\n나이 : "+age+"\n특이사항 : "+specialthing);
                    intent.setData(Uri.parse("smsto:"+Uri.encode("010-3063-4011")));
                    startActivity(intent);
                }

        });*/
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) { //화면회전 방지
        super.onConfigurationChanged(newConfig);
    }

}