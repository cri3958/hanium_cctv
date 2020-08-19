package com.hanium.cctv.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.hanium.cctv.R;
import com.hanium.cctv.others.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//http://leehojin0207.dothome.co.kr/myadmin/
//test11@1111
public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    private CheckBox chk_autologin;
    String fileName = "data_autologin.txt";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            et_id.setText(data.getStringExtra("id"));
            et_pass.setText(data.getStringExtra("pass"));
            chk_autologin.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_login);

        CheckInternetState();

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        chk_autologin = findViewById(R.id.chk_autologin);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckInternetState()) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternetState()) {
                    final String mem_id = et_id.getText().toString();
                    String mem_pw = et_pass.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    String mem_id = jsonObject.getString("mem_id");
                                    String mem_pw = jsonObject.getString("mem_pw");
                                    String mem_name = jsonObject.getString("mem_name");
                                    Log.d("@@@@@", "로그인 성공" + mem_id + "/" + mem_pw);

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("mem_name", mem_name);
                                    intent.putExtra("mem_id", mem_id);
                                    intent.putExtra("mem_pw", mem_pw);
                                    if (chk_autologin.isChecked()) { //자동로그인 데이터 저장
                                        try {
                                            FileOutputStream outFs = openFileOutput(fileName, Context.MODE_PRIVATE);
                                            String str = mem_id + "@" + mem_pw;
                                            outFs.write(str.getBytes());
                                            outFs.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(mem_id, mem_pw, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            }
        });
        File fFile1 = new File("/data/data/com.hanium.cctv/files/"+fileName);// 자동로그인 데이터 불러오기
        if(fFile1.exists()){
            try {
                FileInputStream inFs = openFileInput(fileName);
                byte[] txt = new byte[500];
                inFs.read(txt);
                String[] fdata = (new String(txt)).trim().split("@");
                et_id.setText(fdata[0]);
                et_pass.setText(fdata[1]);
                chk_autologin.setChecked(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    private boolean CheckInternetState(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean check = true;
            if (!(networkInfo != null && networkInfo.isConnectedOrConnecting())){
                check = false;
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                dialog.setTitle("인터넷을 연결해주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Thread.sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                moveTaskToBack(true);
                                finishAndRemoveTask();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .show();
            }
            return check;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
