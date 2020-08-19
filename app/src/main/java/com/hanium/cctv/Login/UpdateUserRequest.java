package com.hanium.cctv.Login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserRequest extends StringRequest {


    final static private String URL = "http://leehojin0207.dothome.co.kr/UpdateUser.php";
    //final static  private String URL="http://13.125.249.248/UpdateUser.php";
    private Map<String, String> map;

    public UpdateUserRequest(String mem_id, String mem_pw, String mem_name, String mem_phone, String mem_emergency, String mem_imagedata, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);//위 url에 post방식으로 값을 전송

        map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("mem_name", mem_name);
        map.put("mem_phone", mem_phone);
        map.put("mem_emergency", mem_emergency);
        map.put("mem_imagedata", mem_imagedata);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}