package com.example.highschoolalltime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class HomeTimeTableItemView extends LinearLayout {
    TextView textView1, textView2; //홈화면 시간표 리스뷰에 저장할 텍스트데이터 변수

    public HomeTimeTableItemView(Context context) {
        super(context);
        init(context);
    }

    public HomeTimeTableItemView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    //리스트뷰에 넣을 아이템 형태 xml 링크
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hometimetablelistitem, this, true);
        textView1 = findViewById(R.id.WhatTimetextView);
        textView2 = findViewById(R.id.timeContenttextView);
    }
    //데이터 저장 메서드
    public void setWhattime(String position){
        String temp = null;
        if(position.equals("")){//시간표에 수업이 없을 때
            temp = "";
        }
        else {
            switch (position.substring(11)) { //수업이 있을 떄
                case "1":
                    temp = "1교시";
                    break;
                case "2":
                    temp = "2교시";
                    break;
                case "3":
                    temp = "3교시";
                    break;
                case "4":
                    temp = "4교시";
                    break;
                case "5":
                    temp = "5교시";
                    break;
                case "6":
                    temp = "6교시";
                    break;
                case "7":
                    temp = "7교시";
                    break;
                    //화요일일 경우
                case "u1" :
                    temp = "1교시";
                    break;
                case "u2":
                    temp = "2교시";
                    break;
                case "u3":
                    temp = "3교시";
                    break;
                case "u4":
                    temp = "4교시";
                    break;
                case "u5":
                    temp = "5교시";
                    break;
                case "u6":
                    temp = "6교시";
                    break;
                case "u7":
                    temp = "7교시";
                    break;

            }
        }
        textView1.setText(temp);
    }
    public void setTimeContent(String subject){
        textView2.setText(subject);
    }
}
