package com.hanium.cctv.others;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    LinearLayout Setting_1, Setting_2, Setting_3;
    TextView text_1, text_2, text_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        text_1 = (TextView) findViewById(R.id.setting_text_1);
        text_2 = (TextView) findViewById(R.id.setting_text_2);
        text_3 = (TextView) findViewById(R.id.setting_text_3);

        Toolbar setting_toolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(setting_toolbar);
        ActionBar setting_actionbar = getSupportActionBar();
        setting_actionbar.setDisplayShowCustomEnabled(true);
        setting_actionbar.setDisplayShowTitleEnabled(true);
        setting_actionbar.setTitle(R.string.setting_actionbar);
        setting_actionbar.setDisplayHomeAsUpEnabled(true);

        Intent inIntent = getIntent();
        final String mem_id = inIntent.getStringExtra("mem_id");
        final String mem_pw = inIntent.getStringExtra("mem_pw");
        Setting_1 = (LinearLayout) findViewById(R.id.setting_1);
        Setting_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartapp();
            }
        });

        Setting_2 = (LinearLayout) findViewById(R.id.setting_2);
        Setting_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, UpdateUserActivity.class);
                intent.putExtra("mem_id", mem_id);
                intent.putExtra("mem_pw", mem_pw);
                startActivity(intent);
            }
        });

        Setting_3 = (LinearLayout) findViewById(R.id.setting_3);
        Setting_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SettingActivity.this, R.style.MyAlertDialogStyle);
                dlg.setTitle("Really want withdrawal?");
                dlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
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
                dlg.setNegativeButton("NO", null);

                final AlertDialog alertDialog = dlg.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
                    }
                });

                alertDialog.show();
            }
        });
    }

    private void restartapp() {
        finishAffinity();
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        System.exit(0);
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