package com.example.highschoolalltime;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class commentItemView extends LinearLayout {
    TextView textView1, textView2, textView3; // 1 - 익명 / 2 - 내용/ 3 - 시간
    String contentUserID; // 게시글 유저 아이디
    String commentUserID; // 댓글 유저 아이디

    public commentItemView(Context context) {
        super(context);
        init(context);
    }

    public commentItemView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    //리스트를 보여줄 아이템 xml 링크
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comment_item, this, true);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
    }
    //리스트 아이템에 저장될 정보 저장하기
    public void setContentuserID(String userID){
        contentUserID = userID; //게시글 유저 아이디
    }
    public void setuserID(String userID){ //댓글의 유저 아이디들
        commentUserID = userID;
        if(contentUserID.equals(commentUserID)){ //글쓴이일때 보이는 댓글 유저란
            String color = "#103EB3";
            textView1.setTextColor(Color.parseColor(color));
            textView1.setPaintFlags(textView1.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
            textView1.setText("글쓴이(익명)");
        }
        else{//글쓴이가 아닌 유저일때 보이는 댓글 유저란
            String color = "#B31010";
            textView1.setTextColor(Color.parseColor(color));
            textView1.setPaintFlags(textView1.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
            textView1.setText("익명");
        }
    }
    public void setComments(String comments){
        textView2.setText(comments);
    }
    public void setTime(String time){
        textView3.setText(time);
    }
    public void Newuser(String userID){

    }
}
