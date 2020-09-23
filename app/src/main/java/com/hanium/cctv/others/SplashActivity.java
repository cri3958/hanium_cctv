package com.hanium.cctv.others;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanium.cctv.Login.LoginActivity;
import com.hanium.cctv.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread myThread1 = new Thread(){
            @Override
            public void run(){
                    ImageView img = (ImageView)findViewById(R.id.splash_image);
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha_anim);
                    img.startAnimation(anim);
            }
        };
        myThread1.start();

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}