package com.example.highschoolalltime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class ScheduleitemView extends LinearLayout {
    TextView textView1;//schedule_list_item xml파일의 textview를 가져올 변수 선언.
    public ScheduleitemView(Context context) {
        super(context);
        init(context);
    }
    public ScheduleitemView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    //이 class는 schedule_list_item xml파일을 연결하여 사용한다.
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.schedule_list_item, this, true);
        textView1 = findViewById(R.id.TodotextView);//schedule_list_item xml파일의 textview를 가져온다.
    }
    public void setTodo(String Todo){ textView1.setText("●"+Todo); }//"●"+할일을 저장하여 보여준다.
}