package com.hanium.cctv.cctv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Display;
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

public class cctv_watch_normal extends AppCompatActivity {
    private WebView webView;
    //private String url = "http://52.79.107.136";
    private String url = "http://54.180.149.38/play.html";
    private String reason = "보호자의 신고";
    private TextView text,btn_emergency;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv_watch_normal);

        text = findViewById(R.id.normal_text);
        btn_emergency = findViewById(R.id.normal_btn_emergency);
        webView = (WebView) findViewById(R.id.normal_cctv_view);

        setUIratio();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());

        Intent intent = getIntent();
        String num = intent.getStringExtra("object_num");
        final DbHelper dbHelper = new DbHelper(cctv_watch_normal.this);
        final String[] object_info = dbHelper.getCCTV_info(num);//0=num,1=pw,2=name,3=place,4=special

        text.setText(object_info[0] + "번 cctv : " + object_info[2]);
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String formatDate = sdfNow.format(date);

                dbHelper.insertRECORDLIST(formatDate, object_info[0], object_info[2], reason);

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

        text.setTextSize((float)standardSize_X/20);
        btn_emergency.setTextSize((float)standardSize_X/20);
    }
}