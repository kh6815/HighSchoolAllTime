package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BoardSetRequest extends StringRequest {
    //서버 URL 설정( PHP  파일 연동)
    final  static  private  String URL = "http://wkwjsrjekffk.dothome.co.kr/BoardSet1.php"; //php파일이름 작성
    private Map<String, String> map;

    public BoardSetRequest(String userSchool, String Whatboard ,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userSchool", userSchool);
        map.put("Whatboard", Whatboard);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
