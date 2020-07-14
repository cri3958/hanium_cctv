package com.hanium.cctv.Login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {


    final static  private String URL="http://leehojin0207.dothome.co.kr/Register.php";
    private Map<String,String>map;

    public RegisterRequest(String userID, String userPassword, String userName, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);
        map.put("userName",userName);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}