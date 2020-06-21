package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest_Comment extends StringRequest {
    //서버 URL 설정( PHP  파일 연동)
    final  static  private  String URL = "http://wkwjsrjekffk.dothome.co.kr/Delete_Comments.php";
    private Map<String, String> map;//string배열로 저장
    public DeleteRequest_Comment(String content_userid, String content_userSchool , String content_title, String content_content , String content_Whatboard, String content_time, String userid, String comments, String time , Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

    //map에 값넣기
        map = new HashMap<>();
        map.put("content_userid", content_userid);
        map.put("content_userSchool", content_userSchool);
        map.put("content_title", content_title);
        map.put("content_content", content_content);
        map.put("content_Whatboard", content_Whatboard);
        map.put("content_time", content_time);
        map.put("userid", userid);
        map.put("comments", comments);
        map.put("time", time);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
