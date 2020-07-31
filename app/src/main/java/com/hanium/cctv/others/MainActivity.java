package com.hanium.cctv.others;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hanium.cctv.R;
import com.hanium.cctv.cctv.CctvActivity;
import com.hanium.cctv.record.RecordActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mem_name = (TextView) findViewById(R.id.mem_name);
        Intent inIntent = getIntent();
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
                startActivity(intent);
            }
        });
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) { //화면회전 방지
        super.onConfigurationChanged(newConfig);
    }
}