package com.hanium.cctv.cctv;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CctvcheckRequest extends StringRequest {

    final static private String URL = "http://leehojin0207.dothome.co.kr/Cctvcheck.php";
    //final static private String URL = "http://13.125.249.248/Cctvcheck.php";
    private Map<String, String> map;

    public CctvcheckRequest(String object_id, String object_pw, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("object_id", object_id);
        map.put("object_pw", object_pw);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
