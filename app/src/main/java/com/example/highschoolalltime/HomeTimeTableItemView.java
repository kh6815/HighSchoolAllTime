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
    TextView textView1, textView2;

    public HomeTimeTableItemView(Context context) {
        super(context);
        init(context);
    }

    public HomeTimeTableItemView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hometimetablelistitem, this, true);
        textView1 = findViewById(R.id.WhatTimetextView);
        textView2 = findViewById(R.id.timeContenttextView);
    }
    public void setWhattime(String position){
        String temp = null;
        if(position.equals("")){
            temp = "";
        }
        else {
            switch (position.substring(11)) {
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
            }
        }
        textView1.setText(temp);
    }
    public void setTimeContent(String subject){
        textView2.setText(subject);
    }
}
