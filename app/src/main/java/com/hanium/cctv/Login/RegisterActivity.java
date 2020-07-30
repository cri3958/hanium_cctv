package com.hanium.cctv.Login;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;
    private EditText et_id, et_pass, et_name, et_passck, et_phone, et_emergency;
    private ImageView et_imagedata;
    private Button btn_register, validateButton;
    private AlertDialog dialog;
    private boolean validate = false;
    private BitmapDrawable d;

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
        et_imagedata = (ImageView) findViewById(R.id.et_imagedata);
        validateButton = findViewById(R.id.validateButton);
        et_imagedata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//아이디 중복여부체크
                String mem_id = et_id.getText().toString();
                if (validate) {
                    return;
                }
                if (mem_id.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                            .setPositiveButton("확인", null)
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
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
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
        });

        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_id.getText().toString().isEmpty() || et_pass.getText().toString().isEmpty() || et_name.getText().toString().isEmpty() || et_passck.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                else {
                    final String mem_id = et_id.getText().toString();
                    final String mem_pw = et_pass.getText().toString();
                    final String mem_name = et_name.getText().toString();
                    final String mem_pwcheck = et_passck.getText().toString();
                    final String mem_phone = et_phone.getText().toString();
                    final String mem_emergency = et_emergency.getText().toString();
                    d = (BitmapDrawable) ((ImageView) findViewById(R.id.et_imagedata)).getDrawable();
                    Bitmap b = d.getBitmap();
                    //b = resize(b);
                    final String mem_imagedata = getStringFromBitmap(b);
                    Log.d("mem_imagedata : ", mem_imagedata);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (mem_pw.equals(mem_pwcheck) && validate) {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
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
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    if (mem_pw.equals(mem_pwcheck) && validate) {
                        RegisterRequest registerRequest = new RegisterRequest(mem_id, mem_pw, mem_name, mem_phone, mem_emergency, mem_imagedata, responseListener);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            et_imagedata.setImageURI(selectedImageUri);
        }

    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    private Bitmap resize(Bitmap bm) {
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 800)
            bm = Bitmap.createScaledBitmap(bm, 400, 240, true);
        else if (config.smallestScreenWidthDp >= 600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if (config.smallestScreenWidthDp >= 400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if (config.smallestScreenWidthDp >= 360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        return bm;
    }
}
