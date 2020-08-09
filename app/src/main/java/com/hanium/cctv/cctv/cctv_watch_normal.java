package com.hanium.cctv.cctv;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.cctv.R;
import com.hanium.cctv.others.DbHelper;

public class cctv_watch_normal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv_watch_normal);

        TextView text = findViewById(R.id.normal_text);
        //영상처리
        TextView btn_emergency = findViewById(R.id.normal_btn_emergency);

        Intent intent = getIntent();
        String num = intent.getStringExtra("object_num");
        DbHelper dbHelper = new DbHelper(cctv_watch_normal.this);
        String[] object_info = dbHelper.getCCTV_info(num);//0=num,1=pw,2=name,3=place,4=special

        text.setText(object_info[0] + "의 cctv");

        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent message = new Intent(Intent.ACTION_SENDTO);
                String emergencytext = "";//응급문자 내용 넣기
                message.putExtra("sms_body", emergencytext);
                message.setData(Uri.parse("smsto:" + Uri.encode("119")));
                startActivity(message);
            }
        });
    }
}