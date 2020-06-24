package com.example.highschoolalltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class withdraw extends AppCompatActivity {
    //edittxt와 button, String을 정의한다.
    private EditText edt_ID, edt_PW;
    private Button btn_delet;
    private String userID, userPassword;
    //자동로그인 할때 저장된 내부db를 로그아웃이나 회원탈퇴 할때 삭제 하기 위해 가져옴
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
//activity_withdraw의 edittext와 button을 가져온다.
        edt_ID = findViewById(R.id.Edittext_Withdraw_ID);
        edt_PW = findViewById(R.id.Edittext_Withdraw_Password);
        btn_delet = findViewById(R.id.Button_Withdraw_Delet);
        //use_user의 ID값과 Password값을 가져온다.
        userID = ((use_user)this.getApplication()).getUserID();
        userPassword = ((use_user)this.getApplication()).getUserPassword();
        //내부 db 가져오기
        pref = getApplication().getSharedPreferences("mine", Context.MODE_PRIVATE);
        editor = pref.edit();
        //회원탈퇴버튼 눌렀을때
        btn_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edittext에 입력된 값을 String으로 저장한다.
                String edt_userID = edt_ID.getText().toString();
                String edt_userPassword = edt_PW.getText().toString();
                //DB에 접근
                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(),
                                        "정상적으로 회원탈퇴 처리되었습니다.",
                                        Toast.LENGTH_SHORT).show();
                                //내부 db 내용 삭제
                                editor.clear();
                                editor.commit();
                                //다시 login패이지로 전환(현재는 login패이지가 없으므로 cafeteria로 대체)
                                Intent intent = new Intent(withdraw.this, Main_login.class);
                                finish();
                                startActivity(intent);
                            } else {
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //edittext에 입력된 값과 회원정보 값이 같을 경우 회원탈퇴(DB의 회원 정보 삭제)
                if (edt_userID.equals(userID) && edt_userPassword.equals(userPassword)) {//edittext에 입력된 값과 회원정보 값이 같을 때
                    //Volley를 이용해 서버로 요청
                    Delet_User delet_user = new Delet_User(edt_userID, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(withdraw.this);
                    queue.add(delet_user);
                } else {//edittext에 입력된 값과 회원정보 값이 다를 때
                    Toast.makeText(getApplicationContext(),
                            "입력된 정보가 다릅니다.", Toast.LENGTH_SHORT).show(); return;
                }
            }
        });
    }
}
