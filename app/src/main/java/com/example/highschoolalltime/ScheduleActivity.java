package com.example.highschoolalltime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.highschoolalltime.adapter.cafeteria_Adapter;
import com.example.highschoolalltime.domain.DayInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{
    public static int SUNDAY = 1;
    public static int MONDAY = 2;
    public static int TUESDAY = 3;
    public static int WEDNSESDAY = 4;
    public static int THURSDAY = 5;
    public static int FRIDAY = 6;
    public static int SATURDAY = 7;
    //그리드뷰와 textview를 지정한다.
    private GridView mGvCalendar;
    private TextView mTvCalendarTitle;
    //adapter와 날짜리스트를 지정한다.
    private ArrayList<DayInfo> mDayList;
    private cafeteria_Adapter mCalendarAdapter;
    //calendar값을 가져오기 위한 변수 지정.
    Calendar mThisMonthCalendar;
    Calendar calendar;
    //cafeteria_dialog에서 값을 가져오기 위한 변수 지정.
    TextView cafe_dialog_title, cafe_dialog_menu, tv_schedule_today;
    EditText cafe_dialog_eddittext;
    Button cafe_dialog_dropbutton, cafe_dialog_addbutton, cafe_dialog_delbutton;
    String WhatDate, userID;//클릭된 날짜를 저장하기 위한 변수 지정.
    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");
        //버튼을 가져온다.
        Button bLastMonth = (Button)findViewById(R.id.gv_calendar_activity_b_last);
        Button bNextMonth = (Button)findViewById(R.id.gv_calendar_activity_b_next);
        //gridview와 textview를 가져온다.
        tv_schedule_today = findViewById(R.id.tv_schedule_today);
        mGvCalendar = (GridView) findViewById(R.id.gv_calendar_activity_gv_calendar);
        mTvCalendarTitle = (TextView) findViewById(R.id.cafe_month);
        //날짜클릭이벤트와 버튼클릭이벤트를 위한 설정
        mGvCalendar.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        bLastMonth.setOnClickListener((View.OnClickListener) this);
        bNextMonth.setOnClickListener((View.OnClickListener) this);
        //배열생성
        mDayList = new ArrayList<DayInfo>();
        //오늘의 급식 보여주기
        calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);//오늘 날짜를 int값으로 저장.
        //calendar에 현재 날짜를 1일로 설정.
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),1);
        int firstday = calendar.get(Calendar.DAY_OF_WEEK);//1일의 요일을 int값에 저장.
        if(firstday==1) {firstday += 5;}//1(일요일)이라면 6으로 저장.
        else {firstday -= 2;}//2~7이라면 -2하여 저장.
        //calendar에 현재 날짜를 다시 설정.
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),today);
        String date = calendar.get(Calendar.YEAR)+""+(calendar.get(Calendar.MONTH)+1)+
                (firstday+today);//날짜는 급식표 gridview의 포지션값.
        //DB와 연동하여 저장된 Text보여주기
        Response.Listener<String> reponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {//급식이 있으면 보여줌
                        tv_schedule_today.setText(jsonObject.getString("Menu"));
                    }else {//급식이없으면 return
                        tv_schedule_today.setText("오늘의 일정이 없습니다.");
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //volley를 사용해 서버로 요청
        Schedule schedule = new Schedule(date, userID,reponseListener);
        RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
        queue.add(schedule);
    }
    @Override
    public void onResume(){
        super.onResume();
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);

    }
    private void getCalendar(Calendar calendar) {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //지난달은 -1
        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH) + "");

        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //이번달은 +1
        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH) + "");
        //한 주가 지날 경우 +7
        if (dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth - 1) - 1;
        //textview설정
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        DayInfo day;

        Log.e("DayOfMonth", dayOfMonth + "");
        //날짜구현
        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);

            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);

            mDayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            mDayList.add(day);
        }
        //cafeteriaAdapter애 넣어주기
        initScheduleAdapter();
    }

    //지난달구현
    private Calendar getLastMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }
    //다음달구현
    private Calendar getNextMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }
    //어뎁터에 넣어주기
    private void initScheduleAdapter() {
        mCalendarAdapter = new cafeteria_Adapter(ScheduleActivity.this, R.layout.cafeteria_day, mDayList);
        mGvCalendar.setAdapter(mCalendarAdapter);
    }

    //날짜 클릭 이벤트 구현
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
        //다이얼로그 창은 xml로 만들어서 가져온다.
        //dialog만드는 코드
        AlertDialog.Builder calendar_cafeteria = new AlertDialog.Builder(ScheduleActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.schedule_dialog,null);
        calendar_cafeteria.setView(view);
        //cafeteria_dialog에서 값 가져오기
        cafe_dialog_title = view.findViewById(R.id.cafe_dialog_title);
        cafe_dialog_menu = view.findViewById(R.id.cafe_dialog_menu);
        cafe_dialog_eddittext = view.findViewById(R.id.cafe_dialog_edittext);
        cafe_dialog_dropbutton = view.findViewById(R.id.cafe_dialog_dropbutton);
        cafe_dialog_delbutton = view.findViewById(R.id.cafe_dialog_delbutton);
        cafe_dialog_addbutton = view.findViewById(R.id.cafe_dialog_addbutton);
        //string값으로 현재 년도+현재 달의 값 + 날짜(날짜는 gridview의 position으로 설정한다.)를 저장
        WhatDate = mThisMonthCalendar.get(Calendar.YEAR)+"" +
                (mThisMonthCalendar.get(Calendar.MONTH)+1)+""+position;

        final AlertDialog dialog = calendar_cafeteria.create();//dialog생성
        //다이얼로그 창 구현
        cafe_dialog_title.setText(mThisMonthCalendar.get(Calendar.MONTH)+1 + "월의 스케쥴");//다이얼로그 title설정
        //DB와 연동하여 저장된 Text보여주기
        Response.Listener<String> reponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {//급식이 있으면 보여줌
                        cafe_dialog_menu.setText(jsonObject.getString("Todo"));
                    }else {//급식이없으면 return
                        cafe_dialog_menu.setText("메모가 없습니다.");
                        return;
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //volley를 사용해 서버로 요청
        Schedule schedule = new Schedule(WhatDate, userID, reponseListener);
        RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
        queue.add(schedule);

        cafe_dialog_delbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //해당 날짜에 급식이 있는지 확인
                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(!success) {//급식이 없을 경우
                                Toast.makeText(ScheduleActivity.this,"삭제 할 메모가 없습니다.",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {//급식이 있는 경우
                                Method_delete_check();//급식 수정하는 method가져오기
                                dialog.dismiss();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //Volley를 이용해 서버로 요청
                Schedule schedule = new Schedule(WhatDate, userID,responseListener);
                RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                queue.add(schedule);
            }

            private void Method_delete_check(){

                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                Toast.makeText(ScheduleActivity.this,"메모를 삭제하였습니다.",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                return;
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //Volley를 이용해 서버로 요청
                Delete_Schedule delete_schedule = new Delete_Schedule(userID, WhatDate, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                queue.add(delete_schedule);
            }
        });
        //추가버튼 눌렀을때 DB에 값 저장
        cafe_dialog_addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //해당 날짜에 급식이 있는지 확인
                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(!success) {//급식이 없을 경우
                                Method_Add_Cafe();//급식 추가하는 method가져오기
                                dialog.dismiss();
                            }else {//급식이 있는 경우
                                Method_Update_Cafe();//급식 수정하는 method가져오기
                                dialog.dismiss();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //Volley를 이용해 서버로 요청
                Schedule schedule = new Schedule(WhatDate, userID,responseListener);
                RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                queue.add(schedule);
            }
            //급식 추가 method
            private void Method_Add_Cafe() {
                //Menu에 String값으로 EditText저장
                String Todo = cafe_dialog_eddittext.getText().toString();
                //DB에 값 넣어주기
                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                Toast.makeText(ScheduleActivity.this,"메모를 추가하였습니다.",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                return;
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //Volley를 이용해 서버로 요청
                Register_Schedule register_schedule = new Register_Schedule(userID, WhatDate, Todo, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                queue.add(register_schedule);
            }
            //급식 수정 method
            private void Method_Update_Cafe() {
                //Menu에 String값으로 EditText저장
                String Todo = cafe_dialog_eddittext.getText().toString();
                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                Toast.makeText(ScheduleActivity.this,"메모를 수정했습니다.",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                return;
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //Volley를 이용해 서버로 요청
                Update_Schedule update_schedule = new Update_Schedule(Todo, WhatDate, userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                queue.add(update_schedule);
            }
        });
        //취소했을때
        cafe_dialog_dropbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //버튼(다음달, 이전달)클릭 이벤트 구현
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //지난달 버튼구현
            case R.id.gv_calendar_activity_b_last:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
            //다음달 버튼구현
            case R.id.gv_calendar_activity_b_next:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
        }
    }
}