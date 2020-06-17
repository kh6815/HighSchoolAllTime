package com.example.highschoolalltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

public class SetPW_login extends AppCompatActivity {
    //구현할 button과 edittext를 정의한다.
    Button SetPW;
    EditText et_newPW, et_IsnewPW;
    String newPW, IsnewPW, userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_p_w_login);
        //activity에서 button과 edittext를 가져온다.
        SetPW = findViewById(R.id.Btn_SetPW);
        et_newPW = findViewById(R.id.ET_SetPW_PassWord);
        et_IsnewPW = findViewById(R.id.ET_SetPW_IsPassWord);
        //Find_IDPW_login에서 intent로 유저의 ID값을 받는다.
        Intent intent = getIntent();
        userID = intent.getStringExtra("FindPW_Is_ID");//userID에 ID값을 저장.
        //비밀번호 변경 버튼 구현.
        SetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edittext에 써져있는 값을 string형태로 저장.
                newPW = et_newPW.getText().toString();
                IsnewPW = et_IsnewPW.getText().toString();
                //edittext에 정보가 하나라도 null값이라면 return.
                if (newPW.equals("") || IsnewPW.equals("")) {//하나라도 null값이라면
                    Toast.makeText(getApplicationContext(),
                            "정보가 비었습니다. 정보를 기입해주세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else if(!newPW.equals(IsnewPW)) {
                    Toast.makeText(getApplicationContext(),
                            "새 Password와 새 Password확인은 완전히 같아야 합니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //DB에 새password 업데이트.
                Response.Listener<String> reponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {//업데이트 성공
                                Toast.makeText(getApplicationContext(),"비밀번호가 변경되었습니다.",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SetPW_login.this, Main_login.class);
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
                //volley를 사용해 서버로 요청
                Update_User_PW update_user_pw = new Update_User_PW(newPW, userID, reponseListener);
                RequestQueue queue = Volley.newRequestQueue(SetPW_login.this);
                queue.add(update_user_pw);
            }
        });
    }
}