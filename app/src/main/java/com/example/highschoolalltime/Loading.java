package com.example.highschoolalltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Loading extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userID;
    String userPassword;
    String userSchool;
    String userName;
    String userEmail;
    String userGrade;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        pref = getSharedPreferences("mine",MODE_PRIVATE);
        editor = pref.edit();
        imageView = (ImageView) findViewById(R.id.imageView_rotate);

        final Animation animTransRotate = AnimationUtils.loadAnimation(this,R.anim.rotate);
        imageView.startAnimation(animTransRotate);

        String prefData_School = pref.getString("userSchool",""); // 키값이 null를 반환을 때
        String prefData_userID = pref.getString("userID",""); // 키값이 null를 반환을 때
        String prefData_userPassword = pref.getString("userPassword",""); // 키값이 null를 반환을 때
        boolean check = pref.getBoolean("checkbox",true);

        autoLogin(prefData_userID, prefData_userPassword, prefData_School);
    }

    private void autoLogin(String userIDStr, String userPasswordStr, String userSchoolStr){
        String userIDtemp = userIDStr;
        String userPasswordtemp = userPasswordStr;
        String userSchoolStrtemp = userSchoolStr;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){//로그인 성공
                        userID = jsonObject.getString("userID");
                        userPassword	= jsonObject.getString("userPassword");
                        userSchool = jsonObject.getString("userSchool");
                        userName= jsonObject.getString("userName");
                        userEmail = jsonObject.getString("userEmail");
                        userGrade = jsonObject.getString("userGrade");

                        System.out.println(userID);
                        System.out.println(userPassword);
                        System.out.println(userSchool);
                        System.out.println(userName);
                        System.out.println(userEmail);
                        System.out.println(userGrade);

                        String boardChage = "0";

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent =  new Intent(getBaseContext(), MainFrame.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPassword", userPassword);
                                intent.putExtra("userSchool", userSchool);
                                intent.putExtra("userName", userName);
                                intent.putExtra("userEmail", userEmail);
                                intent.putExtra("userGrade", userGrade);
                                finish();
                                startActivity(intent);
                            }
                        },2000);
                    }
                    else{ //로그인 실패
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent =  new Intent(getBaseContext(), Main_login.class);
                                finish();
                                startActivity(intent);
                            }
                        },2000);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(userIDtemp, userPasswordtemp, userSchoolStrtemp ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Loading.this);
        queue.add(loginRequest);
    }
}
