package com.hanium.cctv.cctv;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hanium.cctv.R;
import com.hanium.cctv.others.DbHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class cctv_watch_emergency extends AppCompatActivity {
    private WebView webView;
    private String url = "http://52.79.107.136/play.html";
    //private String url = "http://54.180.149.38/play.html";
    String file_autoreport = "data_autoreport.txt";

    private TextView btn_emergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv_watch_emergency);

        Intent intent = getIntent();
        String object_num = intent.getStringExtra("object_num");
        final String reason = intent.getStringExtra("reason");

        btn_emergency = findViewById(R.id.emergency_btn_emergency);
        webView = (WebView) findViewById(R.id.emergency_cctv_view);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());

        final DbHelper dbHelper = new DbHelper(this);
        final String[] object_info = dbHelper.getCCTV_info(object_num);//0=num,1=pw,2=name,3=place,4=special

        Toolbar cctv_emergency_toolbar = findViewById(R.id.cctv_emergency_toolbar);
        setSupportActionBar(cctv_emergency_toolbar);
        ActionBar cctv_emergency_actionbar = getSupportActionBar();
        cctv_emergency_actionbar.setDisplayShowCustomEnabled(true);
        cctv_emergency_actionbar.setDisplayShowTitleEnabled(true);
        cctv_emergency_actionbar.setTitle(object_num + "번 cctv : " + object_info[2]);
        cctv_emergency_actionbar.setDisplayHomeAsUpEnabled(true);

        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String formatDate = sdfNow.format(date);

                File fFile3 = new File("/data/data/com.hanium.cctv/files/" + file_autoreport);// blind 데이터 불러오기
                if (fFile3.exists()) {
                    try {
                        FileInputStream inFs = openFileInput(file_autoreport);
                        byte[] txt = new byte[500];
                        inFs.read(txt);
                        if (new String(txt).trim().equals("true"))
                            dbHelper.insertRECORDLIST(formatDate, object_info[0], object_info[2], reason);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Intent message = new Intent(Intent.ACTION_SENDTO);
                String emergencytext = "'" + getString(R.string.app_name) + "' 어플에서 발송되는 응급문자입니다.\n이름 : " + object_info[2] + "\n위치 : " + object_info[3] + "\n특이사항 : " + object_info[4] + "\n신고사유 : " + reason;
                message.putExtra("sms_body", emergencytext);
                message.setData(Uri.parse("smsto:" + Uri.encode("1234")));
                //message.setData(Uri.parse("smsto:" + Uri.encode("119")));
                startActivity(message);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.blank_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}