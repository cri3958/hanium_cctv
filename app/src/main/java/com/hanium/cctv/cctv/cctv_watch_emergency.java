package com.hanium.cctv.cctv;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.cctv.R;
import com.hanium.cctv.others.DbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class cctv_watch_emergency extends AppCompatActivity {
    private WebView webView;
    private String url = "http://54.180.149.38/play.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv_watch_emergency);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");

        final String[] info = body.split("@");                            //0=카메라 번호, 1=알림의 이유(ex]화재, 넘어짐, 구조요청 / 1(화재), 2(넘어짐), 3(구조요청)) 등등..

        TextView textView = (TextView) findViewById(R.id.emergency_text);
        TextView btn_emergency = findViewById(R.id.emergency_btn_emergency);

        webView = (WebView) findViewById(R.id.emergency_cctv_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());

        final DbHelper dbHelper = new DbHelper(this);
        final String[] object_info = dbHelper.getCCTV_info(info[0]);//0=num,1=pw,2=name,3=place,4=special

        textView.setText(info[0] + "의 cctv");

        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String formatDate = sdfNow.format(date);

                dbHelper.insertRECORDLIST(formatDate, object_info[0], object_info[2], info[1]);

                Intent message = new Intent(Intent.ACTION_SENDTO);
                String emergencytext = "'" + getString(R.string.app_name) + "' 어플에서 발송되는 응급문자입니다.\n이름 : " + object_info[2] + "\n위치 : " + object_info[3] + "\n특이사항 : " + object_info[4] + "\n신고사유 : " + info[1];
                message.putExtra("sms_body", emergencytext);
                message.setData(Uri.parse("smsto:" + Uri.encode("1234")));
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
}