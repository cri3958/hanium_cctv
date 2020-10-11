package com.hanium.cctv.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.hanium.cctv.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_passck, et_phone, et_emergency;
    private TextView btn_register,btn_validate;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_register);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_passck = findViewById(R.id.et_passck);
        et_phone = findViewById(R.id.et_phone);
        et_emergency = findViewById(R.id.et_emergency);
        btn_register = findViewById(R.id.register_btn_register);

        //setUIratio();



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mem_id = et_id.getText().toString();
                if (mem_id.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디는 빈 칸일 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    validate = true;
                                    register();
                                } else {
                                    Toast.makeText(getApplicationContext(), "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    validate = false;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ValidateRequest validateRequest = new ValidateRequest(mem_id, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(validateRequest);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

        btn_register.setTextSize((float)standardSize_X/19);
        btn_validate.setTextSize((float)standardSize_X/28);
    }
    public void register(){
        if (validate) {
            if (et_id.getText().toString().isEmpty() || et_pass.getText().toString().isEmpty() || et_name.getText().toString().isEmpty() || et_passck.getText().toString().isEmpty())
                Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
            else if(!et_pass.getText().toString().equals(et_passck.getText().toString())){
                Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
            else {
                final String mem_id = et_id.getText().toString();
                final String mem_pw = et_pass.getText().toString();
                final String mem_name = et_name.getText().toString();
                final String mem_pwcheck = et_passck.getText().toString();
                final String mem_phone = et_phone.getText().toString();
                final String mem_emergency = et_emergency.getText().toString();

                Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (mem_pw.equals(mem_pwcheck) && validate) {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                Log.d("abbc", "TTTTTTT");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show(); //로그인창에 아이디와 비번 바로입력
                                    Intent outIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                    outIntent.putExtra("id", mem_id);
                                    outIntent.putExtra("pass", mem_pw);
                                    setResult(RESULT_OK, outIntent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                if (mem_pw.equals(mem_pwcheck) && validate) {
                    RegisterRequest registerRequest = new RegisterRequest(mem_id, mem_pw, mem_name, mem_phone, mem_emergency, responseListener1);
                    RequestQueue queue1 = Volley.newRequestQueue(RegisterActivity.this);
                    queue1.add(registerRequest);
                }
            }
        }
    }
}
