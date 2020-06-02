package com.example.highschoolalltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class Main_login extends AppCompatActivity {
    Button ID_PWbutton;
    Button Joinbutton;

    EditText LoginSchoolnameText, LoginIDText, LoginPasswordText;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        ID_PWbutton = findViewById(R.id.ID_PWbutton);
        Joinbutton = findViewById(R.id.Joinbutton);

        LoginSchoolnameText = findViewById(R.id.LoginSchoolnameText);
        LoginIDText = findViewById(R.id.LoginIDText);
        LoginPasswordText = findViewById(R.id.LoginPasswordText);
        btn_login = findViewById(R.id.btn_login);

        ID_PWbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_login.this, Find_IDPW_login.class);
                startActivity(intent); //액티비티 이동
            }
        });
        Joinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_login.this, Join_login.class);
                startActivity(intent); //액티비티 이동
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText에 현재 입력되어 있는 값을 get해온다.

                String userID = LoginIDText.getText().toString();
                String userPassword = LoginPasswordText.getText().toString();
                String userSchool= LoginSchoolnameText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//로그인 성공
                                String userID = jsonObject.getString("userID");
                                String userPassword	= jsonObject.getString("userPassword");
                                String userSchool = jsonObject.getString("userSchool");
                                String userName= jsonObject.getString("userName");
                                String userEmail = jsonObject.getString("userEmail");
                                String userGrade = jsonObject.getString("userGrade");

                                System.out.println(userID);
                                System.out.println(userPassword);
                                System.out.println(userSchool);
                                System.out.println(userName);
                                System.out.println(userEmail);
                                System.out.println(userGrade);

                                Toast.makeText(getApplicationContext(),"로그인에 성공하셨습니다.",Toast.LENGTH_SHORT).show();
                                String boardChage = "0";
                                Intent intent = new Intent(Main_login.this, MainFrame.class );

                                 //intent.putExtra("boardChange", boardChage);
                                 intent.putExtra("userID", userID);
                                 intent.putExtra("userPassword", userPassword);
                                 intent.putExtra("userSchool", userSchool);
                                 intent.putExtra("userName", userName);
                                 intent.putExtra("userEmail", userEmail);
                                 intent.putExtra("userGrade", userGrade);

                                startActivity(intent);
                            }
                            else{ //로그인 실패
                                Toast.makeText(getApplicationContext(),"로그인에 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, userSchool ,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Main_login.this);
                queue.add(loginRequest);
            }
        });
    }
}
