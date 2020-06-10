package com.example.highschoolalltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainFrame extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //하단바
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Activity_Home activity_home;
    private Noticeboard noticeboard;
    public String userID, userPassword,userSchool, userName, userEmail, userGrade, boardChage;
    TextView SchoolName_textView;
    private Cafeteria cafeteria;
    private Mypage mypage;
    private AddTimeTableActivity addTimeTableActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        SchoolName_textView = findViewById(R.id.SchoolName_textView);

        Intent intent = getIntent();
        //boardChage = intent.getExtras().getString("boardChage");
        userID = intent.getExtras().getString("userID");
        userPassword = intent.getExtras().getString("userPassword");
        userSchool= intent.getExtras().getString("userSchool");
        userName = intent.getExtras().getString("userName");
        userEmail = intent.getExtras().getString("userEmail");
        userGrade = intent.getExtras().getString("userGrade");
        System.out.println(userID);
        //System.out.println(userSchoolname);
        //System.out.println(userName);
        //String userSchoolname = intent.getStringExtra("userSchoolname ");
        SchoolName_textView.setText(userSchool);
        ((use_user)this.getApplication()).setUser(userID, userPassword,userSchool, userName, userEmail, userGrade);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home :
                        setFrag(0);
                        break;
                    case R.id.action_schedule :
                        setFrag(1);
                        break;
                    case R.id.action_rastaurant :
                        setFrag(2);
                        break;
                    case R.id.action_board :
                        setFrag(3);
                        break;
                    case R.id.action_my :
                        setFrag(4);
                        break;
                }
                return true;
            }
        });
        activity_home = new Activity_Home();
        noticeboard = new Noticeboard();
        cafeteria = new Cafeteria();
        mypage = new Mypage();
        addTimeTableActivity = new AddTimeTableActivity();
        System.out.println(boardChage);
        setFrag(0); // 첫화면을 설정
    }

    private  void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (n) {
            case 0 : //홈화면
                Bundle bundle2 = new Bundle();
                bundle2.putString("userID", userID);
                activity_home.setArguments(bundle2);
                ft.replace(R.id.main_frame, activity_home);
                ft.commit();
                break;
            case 1 : //시간표
                Bundle bundle1 = new Bundle();
                bundle1.putString("userID", userID);
                addTimeTableActivity.setArguments(bundle1);
                ft.replace(R.id.main_frame, addTimeTableActivity);
                ft.commit();
                break;
            case 2 : //급식
                ft.replace(R.id.main_frame, cafeteria);
                ft.commit();
                break;
            case 3 : // 게시판
                ft.replace(R.id.main_frame, noticeboard);
                ft.commit();
                break;
            case 4 : //내 정보
                Bundle bundle = new Bundle();
                bundle.putString("userName" , userName);
                bundle.putString("userID", userID);
                bundle.putString("userSchool", userSchool);
                mypage.setArguments(bundle);
                ft.replace(R.id.main_frame, mypage);
                ft.commit();
                break;

        }
    }
}
