package com.example.highschoolalltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class ChangeInformation extends AppCompatActivity {
    //activity_change_information의 activity들을 가져올 변수 지정.
    private TextView tv_title, tv_grade;
    private EditText et_userName, et_userSchool, et_userEmail, et_newPW, et_IsnewPW;
    private RadioGroup rg_grade;
    private RadioButton rb_g1, rb_g2, rb_g3;
    private Button bt_update;
    //login된 user의 정보(use_user)를 가져오기 위한 변수 선언.
    String userID, userSchool, userEmail, userGrade, userPW, userName;
    String new_userGrade;//학년을 변경할 때 radiobutton으로 가져올 String변수 지정.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        //activity_change_information의 속성들을 id를 통해 가져옴.
        tv_title = findViewById(R.id.TextView_ChInform_title);
        tv_grade = findViewById(R.id.TextView_ChInform_userGrade);
        et_userName = findViewById(R.id.Edittext_ChInform_Name);
        et_userSchool = findViewById(R.id.Edittext_ChInform_School);
        et_userEmail = findViewById(R.id.Edittext_ChInform_Email);
        et_newPW = findViewById(R.id.Edittext_ChInform_NewPW);
        et_IsnewPW = findViewById(R.id.Edittext_ChInform_IsNewPW);
        rg_grade = findViewById(R.id.RadioGroup_ChInform_Grade);
        rb_g1 = findViewById(R.id.Radio_ChInform_Grade1);
        rb_g2 = findViewById(R.id.Radio_ChInform_Grade2);
        rb_g3 = findViewById(R.id.Radio_ChInform_Grade3);
        bt_update = findViewById(R.id.Button_ChInform_Update);
        //use_user의 회원 정보 값을 가져옴.

        userID = ((use_user)this.getApplication()).getUserID();
        userSchool =((use_user)this.getApplication()).getUserSchool();
        userEmail = ((use_user)this.getApplication()).getUserEmail();
        userGrade = ((use_user)this.getApplication()).getUserGrade();
        userPW = ((use_user)this.getApplication()).getUserPassword();
        userName = ((use_user)this.getApplication()).getUserName();
        //user의 정보를 각 속성에 설정해줌.
        tv_title.setText(userName+"("+userID+")"+"님의 정보");
        tv_grade.setText(userGrade);
        et_userName.setText(userName);
        et_userSchool.setText(userSchool);
        et_userEmail.setText(userEmail);
        et_newPW.setText(userPW);
        et_IsnewPW.setText(userPW);
        //현재 학년에 해당하는 radiobutton에 check
        if(userGrade.equals(rb_g1.getText())) {rb_g1.setChecked(true);}
        else if(userGrade.equals(rb_g2.getText())) {rb_g2.setChecked(true);}
        else {rb_g3.setChecked(true);}
        //radiobutton에 check된 학년에 해당하는 값을 new_userGrade에 저장.
        new_userGrade = userGrade;//check가 바뀌지 않을경우 기존 값 저장.
        rg_grade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                new_userGrade = (String) ((RadioButton)findViewById(checkedId)).getText();
            }
        });
        //확인button눌렀을 때 수정된 정보(edittext와 radiobutton체크된 값) DB에 저장.
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edittext에 입력된 값을 가져옴.
                String new_userName = et_userName.getText().toString();
                String new_userSchool = et_userSchool.getText().toString();
                String new_userEmail = et_userEmail.getText().toString();
                String new_newPW = et_newPW.getText().toString();
                String new_IsnewPW = et_IsnewPW.getText().toString();
                //입력된 값 확인
                if (new_userName.equals("") || new_userSchool.equals("") ||
                        new_userEmail.equals("") || new_newPW.equals("") ||
                        new_IsnewPW.equals("")) {//하나라도 null값이라면
                    Toast.makeText(getApplicationContext(),
                            "정보가 비었습니다. 정보를 기입해주세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (!new_newPW.equals(new_IsnewPW)) {//새 password와 새 password확인이 같지 않다면
                    Toast.makeText(getApplicationContext(),
                            "새 Password와 새 Password확인은 완전히 같아야 합니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }//수정된 값을 DB로 저장
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {//성공하면 mypage로 전환
                                Toast.makeText(getApplicationContext(),
                                        "정상적으로 정보가 수정되었습니다.",
                                        Toast.LENGTH_SHORT).show();
                                //mypage로 전환
                                Intent intent = new Intent(ChangeInformation.this, Main_login.class);
                                finish();
                                startActivity(intent);
                            }else {
                                return;
                            }
                        }catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //volley를 사용해 서버로 요청
                Update_User update_user = new Update_User(new_userName, new_userSchool, new_userEmail,
                        new_newPW, new_userGrade, userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ChangeInformation.this);
                queue.add(update_user);
            }
        });
    }
}