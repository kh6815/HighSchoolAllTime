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
    String userID;
    TextView SchoolName_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        SchoolName_textView = findViewById(R.id.SchoolName_textView);

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");
        System.out.println(userID);
        //String userSchoolname = intent.getStringExtra("userSchoolname ");
        SchoolName_textView.setText(userID);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home :
                        setFrag(0);
                        break;
                    case R.id.action_board :
                        setFrag(1);
                        break;
                }
                return true;
            }
        });
        activity_home = new Activity_Home();
        noticeboard = new Noticeboard();
        setFrag(0);//첫프르그먼트 화면을 무엇으로 설정할지
    }

    private  void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (n) {
            case 0 :
                ft.replace(R.id.main_frame, activity_home);
                ft.commit();
                break;
            case 1 :
                ft.replace(R.id.main_frame, noticeboard);
                ft.commit();
                break;

        }
    }
}
