package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class removeRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL="http://wkwjsrjekffk.dothome.co.kr/board_remove.php";
    private Map<String,String> map;//string배열로 저장

    public removeRequest(String userid, String userSchool, String title, String content, String Whatboard, String time, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);
        //map에 값넣기
        map=new HashMap<>();
        map.put("userid",userid);
        map.put("userSchool",userSchool);
        map.put("title",title);
        map.put("content",content);
        map.put("Whatboard",Whatboard);
        map.put("time",time);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
