package com.example.highschoolalltime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MyItemView extends LinearLayout {
    TextView textView1, textView2, textView3, textView4, textView5;

    public MyItemView(Context context) {
        super(context);
        init(context);
    }

    public MyItemView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
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
        textView1.setText(title);
    }
    public void setContent(String content){
        textView2.setText(content);
    }
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
