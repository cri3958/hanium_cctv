package com.hanium.cctv.Login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.hanium.cctv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class UpdateUserActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView update_imagedata;
    private BitmapDrawable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        final TextView update_id = (TextView) findViewById(R.id.update_id);
        final TextView update_pw = (TextView) findViewById(R.id.update_pass);
        final TextView update_name = (TextView) findViewById(R.id.update_name);
        final TextView update_phone = (TextView) findViewById(R.id.update_phone);
        final TextView update_emergency = (TextView) findViewById(R.id.update_emergency);
        update_imagedata = (ImageView) findViewById(R.id.update_imagedata);
        Button btn_update = (Button) findViewById(R.id.btn_update);

        Intent inIntent = getIntent();
        String mem_id = inIntent.getStringExtra("mem_id");
        String mem_pw = inIntent.getStringExtra("mem_pw");

        update_id.setText(mem_id);
        update_pw.setText(mem_pw);

        update_id.setEnabled(false);
        update_pw.setEnabled(false);
        update_imagedata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update_name.getText().toString().isEmpty() || update_phone.getText().toString().isEmpty() || update_emergency.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                else {
                    final String mem_id = update_id.getText().toString();
                    final String mem_pw = update_pw.getText().toString();
                    final String mem_name = update_name.getText().toString();
                    final String mem_phone = update_phone.getText().toString();
                    final String mem_emergency = update_emergency.getText().toString();
                    d = (BitmapDrawable) ((ImageView) findViewById(R.id.update_imagedata)).getDrawable();
                    Bitmap b = d.getBitmap();
                    //b = resize(b);
                    final String mem_imagedata = getStringFromBitmap(b);
                    //Log.d("mem_imagedata : ", mem_imagedata); 이미지데이터 로그
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
                    UpdateUserRequest updateUserRequest = new UpdateUserRequest(mem_id, mem_pw, mem_name, mem_phone, mem_emergency, mem_imagedata, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(UpdateUserActivity.this);
                    queue.add(updateUserRequest);

                }
            }
        });
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            update_imagedata.setImageURI(selectedImageUri);
        }

    }
}