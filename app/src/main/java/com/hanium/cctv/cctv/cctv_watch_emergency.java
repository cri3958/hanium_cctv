package com.hanium.cctv.cctv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.cctv.R;

public class cctv_watch_emergency extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv_watch_emergency);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");

        TextView textView = (TextView) findViewById(R.id.emergency_text);
        textView.setText(title + " @@ " + body);
    }
}