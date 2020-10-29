package com.hanium.cctv.others;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hanium.cctv.R;
import com.hanium.cctv.cctv.CctvActivity;
import com.hanium.cctv.record.RecordActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    LinearLayout btn_cctv, btn_record, btn_setting;
    TextView info_connect, info_place;
    ImageView btn_notification;
    Switch blind, autoreport;
    String file_notification = "data_notification.txt";
    String file_blind = "data_blind.txt";
    String file_autoreport = "data_autoreport.txt";

    FileOutputStream outFs;
    String str;
    private long backKeyPressedTime = 0;
    private Toast toast;

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
        btn_notification = (ImageView) findViewById(R.id.main_btn_notification);
        blind = (Switch) findViewById(R.id.main_switch_blind);
        autoreport = (Switch) findViewById(R.id.main_switch_autoreport);

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
        Log.d("@@@", cctvnum + "");
        String cctvplace = dbHelper.get1CCTVPLACE();
        if (cctvplace.length() > 9)
            cctvplace = cctvplace.substring(0, 9).concat("...");

        if (cctvnum == 1) {
            info_place.setText(cctvplace);
        } else {
            cctvplace = cctvplace.concat("\n외 " + (cctvnum - 1) + "곳 ");
            info_place.setText(cctvplace);
        }
        info_connect.setText(cctvnum + "");

        btn_notification.setSelected(true);
        File fFile1 = new File("/data/data/com.hanium.cctv/files/" + file_notification);// notifcation 데이터 불러오기
        if (fFile1.exists()) {
            try {
                FileInputStream inFs = openFileInput(file_notification);
                byte[] txt = new byte[500];
                inFs.read(txt);
                if (new String(txt).trim().equals("true"))
                    btn_notification.setSelected(true);
                else
                    btn_notification.setSelected(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected()); // 선택여부 바꾸기
                if (view.isSelected()) {
                    try {
                        outFs = openFileOutput(file_notification, Context.MODE_PRIVATE);
                        str = "true";
                        outFs.write(str.getBytes());
                        outFs.close();
                        toast = Toast.makeText(getApplicationContext(), "알림을 받습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        outFs = openFileOutput(file_notification, Context.MODE_PRIVATE);
                        str = "false";
                        outFs.write(str.getBytes());
                        outFs.close();
                        toast = Toast.makeText(getApplicationContext(), "알림을 안받습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        File fFile2 = new File("/data/data/com.hanium.cctv/files/" + file_blind);// blind 데이터 불러오기
        if (fFile2.exists()) {
            try {
                FileInputStream inFs = openFileInput(file_blind);
                byte[] txt = new byte[500];
                inFs.read(txt);
                if (new String(txt).trim().equals("true"))
                    blind.setChecked(true);
                else
                    blind.setChecked(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    try {
                        outFs = openFileOutput(file_blind, Context.MODE_PRIVATE);
                        str = "true";
                        outFs.write(str.getBytes());
                        outFs.close();
                        toast = Toast.makeText(getApplicationContext(), "영상의 객체를 블라인딩처리 합니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        outFs = openFileOutput(file_blind, Context.MODE_PRIVATE);
                        str = "false";
                        outFs.write(str.getBytes());
                        outFs.close();
                        toast = Toast.makeText(getApplicationContext(), "영상의 객체를 블라인딩처리 하지 않습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        File fFile3 = new File("/data/data/com.hanium.cctv/files/" + file_autoreport);// blind 데이터 불러오기
        if (fFile3.exists()) {
            try {
                FileInputStream inFs = openFileInput(file_autoreport);
                byte[] txt = new byte[500];
                inFs.read(txt);
                if (new String(txt).trim().equals("true"))
                    autoreport.setChecked(true);
                else
                    autoreport.setChecked(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        autoreport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    try {
                        outFs = openFileOutput(file_autoreport, Context.MODE_PRIVATE);
                        str = "true";
                        outFs.write(str.getBytes());
                        outFs.close();
                        toast = Toast.makeText(getApplicationContext(), "응급상황 신고에 대한 정보를 기록 합니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        outFs = openFileOutput(file_autoreport, Context.MODE_PRIVATE);
                        str = "false";
                        outFs.write(str.getBytes());
                        outFs.close();
                        toast = Toast.makeText(getApplicationContext(), "응급상황 신고에 대한 정보를 기록 하지 않습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) { //화면회전 방지
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
        }
    }
}