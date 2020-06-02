package com.example.highschoolalltime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Join_login extends AppCompatActivity {

    private EditText IDText, PasswordText, NameText, EmailText, password_CheckText, SchoolText;
    private RadioButton selbtn;
    private Button btn_register, IDCheckbutton;
    private ImageView passwordCheckImage;
    private boolean validate = false;
    private AlertDialog dialog;
    private int PWcheck = 0, gradeCheck = 0;
    private RadioGroup userGrade_radioGroup;
    private String  temp;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_login);

        SchoolText = findViewById(R.id.SchoolText);
        IDText = findViewById(R.id.IDText);
        PasswordText = findViewById(R.id.LoginPasswordText);
        password_CheckText = findViewById(R.id.password_CheckText);
        passwordCheckImage = findViewById(R.id.passwordCheckImage);
        NameText = findViewById(R.id.LoginIDText);
        EmailText = findViewById(R.id.EmailText);
        IDCheckbutton = findViewById(R.id.IDCheckbutton);

        //아이디 중복체크
        IDCheckbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = IDText.getText().toString();
                if(validate){
                    return;
                }
                if(userID.equals("") || userID.length() <=3 ){
                    AlertDialog.Builder builder=new AlertDialog.Builder( Join_login.this );
                    dialog=builder.setMessage("아이디는 빈 칸 또는 3글자 이하 일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( Join_login.this  );
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                IDText.setEnabled(false);
                                validate=true;
                                IDCheckbutton.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( Join_login.this  );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest=new ValidateRequest(userID,responseListener);
                RequestQueue queue= Volley.newRequestQueue(Join_login.this );
                queue.add(validateRequest);
            }
        });

        //비밀번호 체크
        password_CheckText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(PasswordText.getText().toString().equals(password_CheckText.getText().toString()) || PasswordText.getText().toString().length() > 5) {
                    passwordCheckImage.setImageResource(R.drawable.odraw);
                    PWcheck = 1;
                }
                else{
                    passwordCheckImage.setImageResource(R.drawable.xdraw);
                    PWcheck = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userGrade_radioGroup = (RadioGroup) findViewById(R.id.userGrade_radioGroup);
        userGrade_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                temp = (String) ((RadioButton)findViewById(checkedId)).getText();
                gradeCheck = 1;
            }
        });

        //selbtn = (RadioButton) findViewById(userGrade_radioGroup.getCheckedRadioButtonId());
        //selbtnText = selbtn.getText().toString();

        //회원가입 버튼 클릭시 수행
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PasswordText.getText().toString().equals(password_CheckText.getText().toString())) {

                }
                else{
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치 하지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(SchoolText.getText().toString().equals("") || IDText.getText().toString().equals("") || validate == false || PasswordText.getText().toString().equals("") || password_CheckText.getText().toString().equals("")
                || PWcheck == 0 || NameText.getText().toString().equals("") || EmailText.getText().toString().equals("") || gradeCheck == 0 ){
                    Toast.makeText(getApplicationContext(),"가입정보를 정확히 확인하십시오(중복확인, 비밀번호 5자리 이상)",Toast.LENGTH_SHORT).show();
                    return;
                }
                String userID = IDText.getText().toString();
                String userPassword = PasswordText.getText().toString();
                String userSchool = SchoolText.getText().toString();
                String userName = NameText.getText().toString();
                String userEmail = EmailText.getText().toString();
                String userGrade = temp; //라디오 버튼 가능하게 바꿔야함.
                System.out.println(userGrade);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//회원등록 성공
                                Toast.makeText(getApplicationContext(),"회원등록에 성공하셨습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Join_login.this, Main_login.class );
                                startActivity(intent);
                            }
                            else{ //회원등록 실패
                                Toast.makeText(getApplicationContext(),"회원등록에 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 Volley를 이용해서 요청을 함.
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userSchool,userName, userEmail, userGrade,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Join_login.this);
                queue.add(registerRequest);
            }
        });
    }
}