package com.example.highschoolalltime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MyItemView extends LinearLayout {
    TextView textView1, textView2, textView3, textView4, textView5; // 리스트 뷰에 들어갈 아이템 textview

    public MyItemView(Context context) {
        super(context);
        init(context);
    }

    public MyItemView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        //리스트 뷰 아이템에 넣을 xml 뷰 파일 연결
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview_item, this, true);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
    }
    public void setuserID(String userID){}
    public void setTitle(String title){
        //게시글 제목 저장 / 9글자가 넘으면 ...으로 내용을 줄임
        String temp = null;
        if(title.length() >  9){
            temp = title.substring(0, 8) + "...";
        }
        else{
            temp = title;
        }

        textView1.setText(temp);
    }
    //게시글 내용 저장 / 9글자가 넘으면 ... 으로 내용을 줄임.
    public void setContent(String content){
        String temp = null;
        if(content.length() >  9){
            temp = content.substring(0, 8) + "...";
        }
        else{
            temp = content;
        }

        textView2.setText(temp);
    }
    //나머지 게시글 정보 저장
    public void setHotCount(String hotcount){
        textView3.setText(hotcount);
    }
    public void setTime(String time){
        textView4.setText(time);
    }
    public void setComments(String comments){
        textView5.setText(comments);
    }
    public void setHotClickUser(String hotClickUser){}
}
