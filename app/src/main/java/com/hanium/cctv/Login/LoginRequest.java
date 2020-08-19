package com.hanium.cctv.Login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL="http://leehojin0207.dothome.co.kr/Login.php";
    //final static private String URL="http://13.125.249.248/Login.php";
    private Map<String,String> map;

    public LoginRequest(String mem_id, String mem_pw, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("mem_pw", mem_pw);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
