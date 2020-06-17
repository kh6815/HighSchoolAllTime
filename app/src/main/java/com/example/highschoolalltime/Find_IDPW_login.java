//ID와 PW를 찾는 기능을 구현한 class
package com.example.highschoolalltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Find_IDPW_login extends AppCompatActivity {
    //구현할 button과 edittext를 정의한다.
    Button FindIDbutton, FindPWbutton;
    EditText FindID_name, FindID_school, FindID_email, FindPW_school, FindPW_name, FindPW_email, FindPW_ID;
    String FindID_Is_name, FindID_Is_school, FindID_Is_email, FindPW_Is_name, FindPW_Is_school, FindPW_Is_email, FindPW_Is_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__i_d_p_w_login);
        //activity에서 button과 edittext를 가져온다.
        FindID_name = findViewById(R.id.ET_FindID_Name);
        FindID_school = findViewById(R.id.ET_FindID_School);
        FindID_email = findViewById(R.id.ET_FindID_Email);
        FindPW_school = findViewById(R.id.ET_FindPW_School);
        FindPW_name = findViewById(R.id.ET_FindPW_Name);
        FindPW_email = findViewById(R.id.ET_FindPW_Email);
        FindPW_ID = findViewById(R.id.ET_FindPW_ID);
        FindIDbutton = findViewById(R.id.Btn_FindID);
        FindPWbutton = findViewById(R.id.Btn_FindPW);
        //ID찾는 버튼 구현.
        FindIDbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edittext에 써져있는 값을 string형태로 저장.
                FindID_Is_school = FindID_school.getText().toString();
                FindID_Is_name = FindID_name.getText().toString();
                FindID_Is_email = FindID_email.getText().toString();
                //edittext에 정보가 하나라도 null값이라면 return.
                if (FindID_Is_school.equals("") || FindID_Is_name.equals("") ||
                        FindID_Is_email.equals("")) {//하나라도 null값이라면
                    Toast.makeText(getApplicationContext(),
                            "정보가 비었습니다. 정보를 기입해주세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //DB에서 ID 찾음.
                Response.Listener<String> reponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {//회원정보가 있다면 toast메세지로 보여줌.
                                Toast.makeText(getApplicationContext(), FindID_Is_name + "님의 ID는 " +
                                                jsonObject.getString("userID") + "입니다.",
                                        Toast.LENGTH_LONG).show();
                                return;
                            } else {//없으면 return
                                Toast.makeText(getApplicationContext(),
                                        "회원 정보가 없습니다.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //volley를 사용해 서버로 요청
                FindID_Request findID_request = new FindID_Request(FindID_Is_school, FindID_Is_name,
                        FindID_Is_email, reponseListener);
                RequestQueue queue = Volley.newRequestQueue(Find_IDPW_login.this);
                queue.add(findID_request);
            }
        });
        //password찾는 버튼 구현
        FindPWbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edittext에 써져있는 값을 string형태로 저장.
                FindPW_Is_school = FindPW_school.getText().toString();
                FindPW_Is_name = FindPW_name.getText().toString();
                FindPW_Is_email = FindPW_email.getText().toString();
                FindPW_Is_ID = FindPW_ID.getText().toString();
                //edittext에 정보가 하나라도 null값이라면 return.
                if (FindPW_Is_school.equals("") || FindPW_Is_name.equals("") ||
                        FindPW_Is_email.equals("") || FindPW_Is_ID.equals("")){//하나라도 null값이라면
                    Toast.makeText(getApplicationContext(),
                            "정보가 비었습니다. 정보를 기입해주세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //DB에서 password 찾음.
                Response.Listener<String> reponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {//회원정보가 있다면 intent로 비밀번호 변경 창으로 화면전환.
                                Intent intent = new Intent(Find_IDPW_login.this, SetPW_login.class);
                                intent.putExtra("FindPW_Is_ID", FindPW_Is_ID);//intent로 유저의 ID값을 넘겨준다.
                                startActivity(intent);
                            } else {//없으면 return
                                Toast.makeText(getApplicationContext(),
                                        "회원 정보가 없습니다.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //volley를 사용해 서버로 요청
                FindPW_Request findPW_request = new FindPW_Request(FindPW_Is_school, FindPW_Is_name,
                        FindPW_Is_email, FindPW_Is_ID, reponseListener);
                RequestQueue queue = Volley.newRequestQueue(Find_IDPW_login.this);
                queue.add(findPW_request);
            }
        });
    }
}
