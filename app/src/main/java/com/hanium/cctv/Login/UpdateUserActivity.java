package com.hanium.cctv.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.hanium.cctv.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateUserActivity extends AppCompatActivity {
    TextView update_id,update_pw,update_name,update_phone,update_emergency, btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        update_id = (TextView) findViewById(R.id.update_id);
        update_pw = (TextView) findViewById(R.id.update_pass);
        update_name = (TextView) findViewById(R.id.update_name);
        update_phone = (TextView) findViewById(R.id.update_phone);
        update_emergency = (TextView) findViewById(R.id.update_emergency);
        btn_update = (TextView) findViewById(R.id.update_btn_update);

        Intent inIntent = getIntent();
        String mem_id = inIntent.getStringExtra("mem_id");
        String mem_pw = inIntent.getStringExtra("mem_pw");

        update_id.setText(mem_id);
        update_pw.setText(mem_pw);

        update_id.setEnabled(false);
        update_pw.setEnabled(false);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update_name.getText().toString().isEmpty() || update_phone.getText().toString().isEmpty() || update_emergency.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                else {
                    final String mem_id = update_id.getText().toString();
                    final String mem_name = update_name.getText().toString();
                    final String mem_phone = update_phone.getText().toString();
                    final String mem_emergency = update_emergency.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "회원정보 수정 성공", Toast.LENGTH_SHORT).show();
                                    finishAffinity();
                                    Intent intent = new Intent(UpdateUserActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    System.exit(0);
                                } else
                                    Toast.makeText(getApplicationContext(), "회원정보 수정 실패", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    UpdateUserRequest updateUserRequest = new UpdateUserRequest(mem_id, mem_name, mem_phone, mem_emergency, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(UpdateUserActivity.this);
                    queue.add(updateUserRequest);

                }
            }
        });
    }
}