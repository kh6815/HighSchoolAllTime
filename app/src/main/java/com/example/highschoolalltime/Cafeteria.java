package com.example.highschoolalltime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.highschoolalltime.adapter.cafeteria_Adapter;
import com.example.highschoolalltime.domain.DayInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class Cafeteria extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
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
    TextView cafe_dialog_title, cafe_dialog_menu, cafe_today, cafeteria_Title;
    EditText cafe_dialog_eddittext;
    Button cafe_dialog_dropbutton, cafe_dialog_addbutton;
    String WhatDate;//클릭된 날짜를 저장하기 위한 변수 지정.
    String userGrade; //교사인지 판별하기 위한 변수

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cafeteria, container, false);

        //교사인지 확인하기 위해 userGrade를 bundle로 가져온다.
        Bundle bundle3 = getArguments(); // MainFrame에서 넘겨받은 bundle을 생성
        userGrade= bundle3.getString("userGrade"); //bundle로 받은 유저 grade 값 저장

        //버튼을 가져온다.
        Button bLastMonth = (Button)view.findViewById(R.id.gv_calendar_activity_b_last);
        Button bNextMonth = (Button)view.findViewById(R.id.gv_calendar_activity_b_next);
        //gridview와 textview를 가져온다.
        mGvCalendar = (GridView) view.findViewById(R.id.gv_calendar_activity_gv_calendar);
        mTvCalendarTitle = (TextView) view.findViewById(R.id.cafe_month);
        cafe_today = view.findViewById(R.id.tv_cafe_today);
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
                        cafe_today.setText(jsonObject.getString("Menu"));
                    }else {//급식이없으면 return
                        cafe_today.setText("오늘은 급식이 없습니다.");
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //volley를 사용해 서버로 요청
        Cafeteria_Request cafeteria_request = new Cafeteria_Request(date, reponseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(cafeteria_request);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);

    }

    //달력 구현 메소드
    private void getCalendar(Calendar calendar) {
        int lastMonthStartDay;//지난달시작일
        int dayOfMonth;//요일값 받아올 변수
        int thisMonthLastDay;//이번달마지막일

        mDayList.clear();//배열 초기화
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);//이번달 1일의 요일값 저장.(mThisMonthCalendar는 1일로 설정 되었다.)
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//이번달 마지막일 저장
        //지난달은 -1하여 설정
        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH) + "");//지난달 마지막일 가져옴.
        //지난달 시작일 가져옴.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //이번달은 +1하여 설정
        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH) + "");//이번달 시작일 가져옴.
        //시작일이 일요일이면 두번쨰 줄부터 시작.
        if (dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }
        //지난달 시작일에 이번달 시작일 포지션의 -1만큼 빼준다.
        lastMonthStartDay -= (dayOfMonth - 1) - 1;
        //textview 제목 설정
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        //Dayinfo class사용.
        DayInfo day;
        //오늘 날짜 넣어주기.
        Log.e("DayOfMonth", dayOfMonth + "");
        //반복문을 통한 날짜구현
        for (int i = 0; i < dayOfMonth - 1; i++) {//이번달 시작일 전까지
            int date = lastMonthStartDay + i;//지난달 시작일부터 1++
            day = new DayInfo();//Dayinfo class 가져오기
            day.setDay(Integer.toString(date));//Dayinfo class에 넣어주기
            day.setInMonth(false);//이번달이 아니므로 faslse넣어주기
            //배열에 날짜 넣어주기.
            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {//이번달 마지막 일까지
            day = new DayInfo();//Dayinfo class 가져오기
            day.setDay(Integer.toString(i));//Dayinfo class에 넣어주기
            day.setInMonth(true);//이번달이므로 true넣어주기
            //배열에 날짜 넣어주기.
            mDayList.add(day);
        }
        //다음달 시작~42칸까지
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {//그리드뷰 42개 칸-(지난달+이번달) = 다음달 날짜들.
            day = new DayInfo();//Dayinfo class 가져오기
            day.setDay(Integer.toString(i));//Dayinfo class에 넣어주기
            day.setInMonth(false);//이번달이므로 true넣어주기
            mDayList.add(day);//배열에 날짜 넣어주기.
        }
        //cafeteria_Adapter에 넣어주기 위해 initcafeteriaAdapter 메소드를 사용.
        initCafeteriaAdapter();//initcafeteriaAdapter 메소드 불러오기
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
    private void initCafeteriaAdapter() {
        mCalendarAdapter = new cafeteria_Adapter(getActivity(), R.layout.cafeteria_day, mDayList);
        mGvCalendar.setAdapter(mCalendarAdapter);
    }

    //날짜 클릭 이벤트 구현
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
        //다이얼로그 창은 xml로 만들어서 가져온다.
        //dialog만드는 코드
        if(userGrade.equals("교사")) { //교사인 경우만 급식표를 추가 수정 삭제할 수 있음.
            AlertDialog.Builder calendar_cafeteria = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.cafeteria_dialog, null);
            calendar_cafeteria.setView(view);
            //cafeteria_dialog에서 값 가져오기
            cafe_dialog_title = view.findViewById(R.id.cafe_dialog_title);
            cafe_dialog_menu = view.findViewById(R.id.cafe_dialog_menu);
            cafe_dialog_eddittext = view.findViewById(R.id.cafe_dialog_edittext);
            cafe_dialog_dropbutton = view.findViewById(R.id.cafe_dialog_dropbutton);
            cafe_dialog_addbutton = view.findViewById(R.id.cafe_dialog_addbutton);
            //string값으로 현재 년도+현재 달의 값 + 날짜(날짜는 gridview의 position으로 설정한다.)를 저장
            WhatDate = mThisMonthCalendar.get(Calendar.YEAR) + "" +
                    (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "" + position;
            final AlertDialog dialog = calendar_cafeteria.create();//dialog생성
            //다이얼로그 창 구현
            cafe_dialog_title.setText(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월의 급식");//다이얼로그 title설정
            //DB와 연동하여 저장된 Text보여주기
            Response.Listener<String> reponseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {//급식이 있으면 보여줌
                            cafe_dialog_menu.setText(jsonObject.getString("Menu"));
                        } else {//급식이없으면 return
                            cafe_dialog_menu.setText("급식이 없습니다.");
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //volley를 사용해 서버로 요청
            Cafeteria_Request cafeteria_request = new Cafeteria_Request(WhatDate, reponseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(cafeteria_request);
            //추가버튼 눌렀을때 DB에 값 저장
            cafe_dialog_addbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //해당 날짜에 급식이 있는지 확인
                    Response.Listener responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (!success) {//급식이 없을 경우
                                    Method_Add_Cafe();//급식 추가하는 method가져오기
                                    dialog.dismiss();
                                } else {//급식이 있는 경우
                                    Method_Update_Cafe();//급식 수정하는 method가져오기
                                    dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //Volley를 이용해 서버로 요청
                    Cafeteria_Request cafeteria_request = new Cafeteria_Request(WhatDate, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(cafeteria_request);
                }

                //급식 추가 method
                private void Method_Add_Cafe() {
                    //Menu에 String값으로 EditText저장
                    final String Menu = cafe_dialog_eddittext.getText().toString();
                    //DB에 값 넣어주기
                    Response.Listener responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getActivity(), "급식을 추가하였습니다.",
                                            Toast.LENGTH_SHORT).show();
                                    cafe_today.setText(Menu);
                                    dialog.dismiss();
                                } else {
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //Volley를 이용해 서버로 요청
                    Register_Cafe register_cafe = new Register_Cafe(Menu, WhatDate, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(register_cafe);
                }

                //급식 수정 method
                private void Method_Update_Cafe() {
                    //Menu에 String값으로 EditText저장
                    final String Menu = cafe_dialog_eddittext.getText().toString();
                    Response.Listener responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getActivity(), "급식을 수정했습니다.",
                                            Toast.LENGTH_SHORT).show();
                                    cafe_today.setText(Menu);
                                    dialog.dismiss();
                                } else {
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //Volley를 이용해 서버로 요청
                    Update_Cafe update_cafe = new Update_Cafe(Menu, WhatDate, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(update_cafe);
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
        else{ //교사가 아닐경우
            cafeteria_Title = view.findViewById(R.id.cafeteria_Title);
            //string값으로 현재 년도+현재 달의 값 + 날짜(날짜는 gridview의 position으로 설정한다.)를 저장
            WhatDate = mThisMonthCalendar.get(Calendar.YEAR) + "" +
                    (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "" + position;
            cafeteria_Title.setText(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월의 급식");

            Response.Listener<String> reponseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {//급식이 있으면 보여줌
                            cafe_today.setText(jsonObject.getString("Menu"));
                        } else {//급식이없으면 return
                            cafe_today.setText("급식이 없습니다.");
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //volley를 사용해 서버로 요청
            Cafeteria_Request cafeteria_request = new Cafeteria_Request(WhatDate, reponseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(cafeteria_request);
        }
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