package com.hanium.cctv.Login;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText et_id, et_pass, et_name,et_passck;
    private Button btn_register,validateButton;
    private AlertDialog dialog;
    private boolean validate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_register);

        et_id=findViewById(R.id.et_id);
        et_pass=findViewById(R.id.et_pass);
        et_name=findViewById(R.id.et_name);
        et_passck=findViewById(R.id.et_passck);
        validateButton=findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//아이디 중복여부체크
                String userID=et_id.getText().toString();
                if(validate) {
                    return;
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                et_id.setEnabled(false);
                                validate=true;
                                validateButton.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest=new ValidateRequest(userID,responseListener);
                RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);

            }
        });

        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_id.getText().toString().isEmpty() || et_pass.getText().toString().isEmpty() || et_name.getText().toString().isEmpty() || et_passck.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                else {
                    final String userID = et_id.getText().toString();
                    final String userPass = et_pass.getText().toString();
                    final String userName = et_name.getText().toString();
                    final String PassCk = et_passck.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (userPass.equals(PassCk) && validate) {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show(); //로그인창에 아이디와 비번 바로입력
                                        Intent outIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                        outIntent.putExtra("id", userID);
                                        outIntent.putExtra("pass", userPass);
                                        setResult(RESULT_OK,outIntent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    if (userPass.equals(PassCk) && validate) {
                        RegisterRequest registerRequest = new RegisterRequest(userID, userPass, userName, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                        queue.add(registerRequest);
                    } else if (!(validate))
                        Toast.makeText(getApplicationContext(), "아이디 중복확인을 해야합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
