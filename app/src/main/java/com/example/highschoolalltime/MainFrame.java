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

    private BottomNavigationView bottomNavigationView; //하단 메뉴바
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Activity_Home activity_home;
    private Noticeboard noticeboard; //게시판 프래그먼트
    public String userID, userPassword,userSchool, userName, userEmail, userGrade, boardChage; //유저 정보 변수
    TextView SchoolName_textView;//학교 이름 textview
    private Cafeteria cafeteria; //급식표 프래그먼트
    private Mypage mypage; // 마이페이지 프래그먼트
    private AddTimeTableActivity addTimeTableActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        //학교 이름 xml연결
        SchoolName_textView = findViewById(R.id.SchoolName_textView);

        //intent로 넘겨받음 유저 정보 저장
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
        SchoolName_textView.setText(userSchool); // 유저 학교 이름 저장하기
        ((use_user)this.getApplication()).setUser(userID, userPassword,userSchool, userName, userEmail, userGrade); //유저 정보 저장 클래스에 저장

        //하단 메뉴바 순서 설정
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
        //프래그먼트에 넣을 페이지 클래스 가져오기
        activity_home = new Activity_Home();
        noticeboard = new Noticeboard();
        cafeteria = new Cafeteria();
        mypage = new Mypage();
        addTimeTableActivity = new AddTimeTableActivity();
        System.out.println(boardChage);
        setFrag(0); // 첫화면을 설정
    }

    public void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        // 각 페이지에 프래그먼트 연결
        switch (n) {
            case 0 : //홈화면
                Bundle bundle2 = new Bundle();
                bundle2.putString("userID", userID);
                bundle2.putString("userSchool", userSchool);
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
                Bundle bundle3 = new Bundle();
                bundle3.putString("userGrade", userGrade);
                cafeteria.setArguments(bundle3);
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
