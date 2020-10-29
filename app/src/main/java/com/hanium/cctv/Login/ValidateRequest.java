package com.hanium.cctv.Login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {

    final static private String URL = "http://leehojin0207.dothome.co.kr/UserValidate.php";
    //final static private String URL="http://13.125.249.248/UserValidate.php";
    private Map<String, String> map;

    public ValidateRequest(String mem_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("mem_id", mem_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
