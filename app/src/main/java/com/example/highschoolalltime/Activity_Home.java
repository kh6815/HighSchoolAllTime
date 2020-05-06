package com.example.highschoolalltime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Home extends Fragment {
            TextView SchoolName_textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity__home, container, false);
        //EditText에 현재 입력되어 있는 값을 get해온다.

                /*
                if(SchoolName_textView.getText().toString().equals("학교이름")){
                    Intent intent = getIntent();
                    String userID = intent.getExtras().getString("userID");
                    System.out.println(userID);
                    //String userSchoolname = intent.getStringExtra("userSchoolname ");
                    SchoolName_textView.setText(userID);
                }
                else{
                    System.out.println("왜 else문으로 오냐??");
                }
                */


        TextView m_TextViewLog = (TextView) view.findViewById(R.id.MyWork1_textView);
        m_TextViewLog.setMovementMethod(ScrollingMovementMethod.getInstance());

                /*
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Home.this, Noticeboard.class);
                intent.putExtra("userID", userID);
                startActivity(intent); //액티비티 이동
            }
        });
        */

        return view;
    }

}

