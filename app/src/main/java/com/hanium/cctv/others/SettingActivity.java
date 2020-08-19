package com.hanium.cctv.others;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.hanium.cctv.Login.DeleteUserRequest;
import com.hanium.cctv.Login.LoginActivity;
import com.hanium.cctv.Login.UpdateUserActivity;
import com.hanium.cctv.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent inIntent = getIntent();
        final String mem_id = inIntent.getStringExtra("mem_id");
        final String mem_pw = inIntent.getStringExtra("mem_pw");
        TableRow setting1 = (TableRow) findViewById(R.id.Setting_1);
        setting1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartapp();
            }
        });

        TableRow setting2 = (TableRow) findViewById(R.id.Setting_2);
        setting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, UpdateUserActivity.class);
                intent.putExtra("mem_id", mem_id);
                intent.putExtra("mem_pw", mem_pw);
                startActivity(intent);
            }
        });

        TableRow setting3 = (TableRow) findViewById(R.id.Setting_3);
        setting3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SettingActivity.this);
                dlg.setTitle("*주의*");
                dlg.setMessage("정말로 탈퇴하시겠습니까?");
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "회원탈퇴 성공", Toast.LENGTH_SHORT).show();
                                        restartapp();
                                    } else
                                        Toast.makeText(getApplicationContext(), "회원탈퇴 실패", Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(mem_id, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
                        queue.add(deleteUserRequest);
                    }
                });
                dlg.setNegativeButton("아니요", null);
                dlg.show();
            }
        });
    }

    private void restartapp() {
        finishAffinity();
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        System.exit(0);
    }
}