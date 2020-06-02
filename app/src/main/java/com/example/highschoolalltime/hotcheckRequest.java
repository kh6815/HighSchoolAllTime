package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class hotcheckRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL="http://wkwjsrjekffk.dothome.co.kr/hotcheck.php";
    private Map<String,String> map;

    public hotcheckRequest(String userSchool,String title,String content,String Whatboard, String hotclickUser, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("userSchool",userSchool);
        map.put("title",title);
        map.put("content",content);
        map.put("Whatboard",Whatboard);
        map.put("hotclickUser",hotclickUser);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
