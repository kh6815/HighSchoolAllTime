package com.example.highschoolalltime;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.highschoolalltime.adapter.cafeteria_Adapter;
import com.example.highschoolalltime.domain.DayInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static int SUNDAY = 1;//일요일이면 1로 저장.
    //달력 생성에 사용할 그리드뷰와 textview를 지정한다.
    private GridView mGvCalendar;
    private TextView mTvCalendarTitle;
    //달력 생성에 사용할 adapter와 날짜리스트를 지정한다.
    private ArrayList<DayInfo> mDayList;
    private cafeteria_Adapter mCalendarAdapter;
    //dialog지정
    private AlertDialog dialog;
    //calendar값을 가져오기 위한 변수 지정.
    Calendar mThisMonthCalendar;//달력 생성에 사용.(이번달의 1일로 지정해 사용할 예정)
    Calendar calendar;//현재 날짜를 가져올 때 사용.
    //activity_schedule, schedule_dialog에서 값을 가져오기 위한 변수 지정.
    TextView tv_schedule_title, schedule_dialog_title;
    ListView schedulelist;
    EditText schedule_dialog_eddittext;
    Button schedule_dialog_add, schedule_dialog_drop;
    //DB에 저장된 string값들을 저장하기 위한 변수 지정.
    String WhatDate, userID, myJSON, ScheduleID;
    private static String TAG = "ScheduleActivity";//JAVA 파일 명
    private static final String TAG_RESPONSE = "response";//DB 스키마
    private static final String TAG_Todo = "Todo";//DB 스키마
    private static final String TAG_WhatDate = "WhatDate";//DB 스키마
    private static final String TAG_ScheduleID = "ScheduleID";//DB 스키마
    //DB접근을 위한 변수 지정.
    JSONArray peoples = null;//응담으로 인한 데이터 저장 배열
    ArrayList<HashMap<String, String>> TodoList;//HashMap 배열
    //일정listview의 adapter 지정.
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);//이 class는 activity_schedule xml파일을 보여준다.
        //userID를 가져온다.
        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");
        //버튼을 가져온다.
        Button btn_schedule_add = findViewById(R.id.Button_Schedule_Add);//일정 추가 버튼
        Button bLastMonth = (Button) findViewById(R.id.gv_calendar_activity_b_last);//지난달 버튼
        Button bNextMonth = (Button) findViewById(R.id.gv_calendar_activity_b_next);//다음달 버튼
        //달력 생성에 사용할 gridview와 textview를 가져온다.
        mGvCalendar = (GridView) findViewById(R.id.gv_calendar_activity_gv_calendar);
        mTvCalendarTitle = (TextView) findViewById(R.id.cafe_month);
        //달력의 날짜클릭이벤트와 버튼들의 클릭이벤트를 위한 설정
        mGvCalendar.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        bLastMonth.setOnClickListener((View.OnClickListener) this);
        bNextMonth.setOnClickListener((View.OnClickListener) this);
        btn_schedule_add.setOnClickListener((View.OnClickListener) this);
        //activity_schedule의 textview를 id값으로 가져온다.
        tv_schedule_title = findViewById(R.id.tv_schedule_title);
        //오늘날짜를 WhatDate에 저장.
        calendar = Calendar.getInstance();//calendar에서 값을 가져오기 위한 코드.
        WhatDate = calendar.get(Calendar.YEAR) + "년" + (calendar.get(Calendar.MONTH) + 1) + "월" +
                calendar.get(Calendar.DAY_OF_MONTH) + "일";//'yyyy'년'mm'월'dd'일 형식의 string값.
        tv_schedule_title.setText(WhatDate+"의 일정");//whatdate의 값을 일정title에 설정한다.
        //배열생성(listview에 DB에 저장된 값을 가져오기 위한 코드.)
        mDayList = new ArrayList<DayInfo>();
        TodoList = new ArrayList<HashMap<String, String>>();
        schedulelist = findViewById(R.id.ListView_Schedule_Content);//activity_schedule의 listview를 가져옴.
        adapter = new ScheduleAdapter();//adapter설정.
        getData("http://highschool.dothome.co.kr/ScheduleTable.php", WhatDate);//whatdate(오늘 날짜)에 저장된 DB값을 가져온다.
        //리스트 아이템 클릭 구현
        schedulelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Scheduleitem 클래스에서 클릭된 일정을 DB에서 정보 가져오기.
                Scheduleitem item = (Scheduleitem) parent.getItemAtPosition(position);//Scheduleitem 클래스 연결
                final String whatdateStr = item.getWhatDate(); // 해당 일정의 날짜 Scheduleitem에서 가져옴.
                final String TodoStr = item.getTodo();//해당 일정의 내용 Scheduleitem에서 가져옴.
                final String whatscheduleIDStr = item.getScheduleID();// 해당 일정의 삽입시간 Scheduleitem에서 가져옴.
                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);//dialog생성
                builder.setTitle("해당 항목을 선택하십시오");//dialog 제목 설정
                builder.setMessage("일정 수정 or 삭제");//dialog 메세지 설정
                //dialog버튼 구현
                builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {//수정 버튼 눌렀을 때
                        ScheduleUpdate(whatdateStr, TodoStr, whatscheduleIDStr);//ScheduleUpdate 메소드에 날짜, 내용, 삽입시간 넣고 메소드 불러옴.
                    }
                });
                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {//삭제 버튼 눌렀을 때
                        ScheduleDelete(whatdateStr, whatscheduleIDStr);//ScheduleDelete 메소드에 날짜, 삽입시간 넣고 메소드 불러옴.
                    }
                });
                builder.show();//dialog 띄움.
            }
        });
    }
    //일정 수정 메소드
    void ScheduleUpdate(final String whatdate, final String todo, final String scheduleid){
        //dialog창 만드는 코드
        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.schedule_dialog_update, null);//dialog창은 schedule_dialog_update xml파일을 보여준다.
        builder.setView(view);//dialog에 view설정.
        //schedule_dialog_update xml에서 사용할 item들을 가져온다.
        TextView title = (TextView) view.findViewById(R.id.schedule_dialog_update_title);//dialog의 제목
        final EditText newtodo = (EditText) view.findViewById(R.id.schedule_dialog_update_edittext);//dialog의 edittext
        Button update = (Button) view.findViewById(R.id.schedule_dialog_update_updtbutton);//수정(확인)버튼
        Button cencel = (Button) view.findViewById(R.id.schedule_dialog_update_dropbutton);//취소 버튼
        newtodo.setText(todo);//edittext에 이미 저장되어있는 내용을 세팅해준다.
        title.setText(whatdate);//title에 클릭된 리스트의 날짜를 세팅한다.
        dialog = builder.create();//dialog생성
        //수정 버튼 구현
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Todo = newtodo.getText().toString();//Todo에 edittext에 입력된 내용 저장.
                //DB에 값 넣어주기
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//성공할 경우 일정 수정
                                Toast.makeText(getApplicationContext(),"일정을 수정하셨습니다.",Toast.LENGTH_SHORT).show();//Toast메세지 띄우기
                                //페이지 새로고침
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{//실패할 경우
                                Toast.makeText(getApplicationContext(),"일정 수정을 실패하셨습니다.",Toast.LENGTH_SHORT).show();//Toast메세지 띄우기
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 Volley를 이용해서 요청을 함.
                Update_Schedule update_schedule = new Update_Schedule(userID, whatdate, Todo, scheduleid, responseListener);//Update_Schedule 클래스 사용.
                RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                queue.add(update_schedule);
                dialog.dismiss();//dialog닫기
            }
        });
        //취소버튼 구현
        cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//dialog닫기
            }
        });
        dialog.show();//dialog 띄우기
    }
    //일정 삭제 메소드
    void ScheduleDelete(final String whatdate, final String scheduleid){
        //dialog만드는 코드
        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
        builder.setTitle("일정을 삭제하시겠습니까?");//dialog 제목 설정
        //확인버튼 구현
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //DB에 삭제 요청
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//일정 삭제 성공
                                Toast.makeText(getApplicationContext(),"일정을 삭제하셨습니다.",Toast.LENGTH_SHORT).show();//Toast메세지 띄우기
                                //페이지 새로고침
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{ //일정 삭제 실패
                                Toast.makeText(getApplicationContext(),"일정 삭제를 실패하셨습니다.",Toast.LENGTH_SHORT).show();//Toast메세지 띄우기
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 Volley를 이용해서 요청을 함.
                Delete_Schedule delete_schedule = new Delete_Schedule(userID, whatdate, scheduleid,responseListener);//Delete_Schedule 클래스 사용.
                RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                queue.add(delete_schedule);
                dialog.dismiss();//dialog 닫기
            }
        });
        //취소 버튼 구현
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//dialog 닫기
                    }
                });
        builder.show();//dialog 띄움.
    }
    //ScheduleAdapter구현
    class ScheduleAdapter extends BaseAdapter {
        private ArrayList<Scheduleitem> items = new ArrayList<>();//배열생성
        //Scheduleitem class에 item넣기
        public void addItem(Scheduleitem item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            ScheduleitemView view = new ScheduleitemView(ScheduleActivity.this);//ScheduleitemView 클래스 가져옴.
            Scheduleitem item = items.get(position);
            view.setTodo(item.getTodo());//item의 일정을 view로 넘겨줌.
            return view;
        }
    }
    //DB에서 받아온 값들을 list에 저장하는 메소드
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);//myJSON에 저장된 db 데이터값을 가져오기 위한 변수 생성
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);//db에서 넘어온값을 peoples의 리스트에 저장

            for (int i = 0; i < peoples.length(); i++) { //반복문을 이용하여 게시글 리스트에 삽입
                JSONObject c = peoples.getJSONObject(i);
                //db에서 넘어온값 변수에 저장
                String todo = c.getString(TAG_Todo);
                String whatdate = c.getString(TAG_WhatDate);
                String scheduleid = c.getString(TAG_ScheduleID);

                HashMap<String, String> persons = new HashMap<String, String>();//해쉬맵 형태의 배열을 생성
                //키워드 형식으로 데이터 저장
                persons.put(TAG_Todo, todo);
                persons.put(TAG_WhatDate, whatdate);
                persons.put(TAG_ScheduleID, scheduleid);

                TodoList.add(persons);//해쉬맵 배열을 리스트에 저장
            }
            System.out.println(TodoList);//출력값 확인
            System.out.println(TodoList.size());//리스트 사이즈값 확인

            for (int i = 0; i < TodoList.size(); i++) {//리스트의 크기많큼 어탭터의 item저장
                HashMap<String, String> hashMap = TodoList.get(i);//각 배열의 값을 해쉬맵으로 불러오기
                //어탭터에 저장
                adapter.addItem(new Scheduleitem(hashMap.get(TAG_Todo), hashMap.get(TAG_WhatDate),
                        hashMap.get(TAG_ScheduleID)));
            }
            schedulelist.setAdapter(adapter);//게시글 리스트뷰에 어댑터 저장

            if (TodoList.size() < 1) {
                adapter.addItem(new Scheduleitem("일정을 추가하세요", "", ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //DB에 저장된 값들을 가져오기 위한 메소드
    void getData(String url, String WhatDate) {//db php 주소 받음
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                //각 파라미터의 데이터 저장
                String uri = params[0];
                String userID = params[1];
                String Day = params[2];
                String postParameters = "userID=" + userID + "&Day=" + Day;//php구문에 post 형식으로 넘길 문자열

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();//url 연결


                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("POST");//post 형식으로 보냄
                    con.setDoInput(true);
                    con.connect();


                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(postParameters.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();


                    int responseStatusCode = con.getResponseCode();
                    Log.d(TAG, "response code - " + responseStatusCode);

                    InputStream inputStream = con.getInputStream();
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = con.getInputStream();
                    } else {
                        inputStream = con.getErrorStream();
                    }
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");


                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(inputStreamReader);
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    con.disconnect();

                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;//결과값을 저장
                showList();//db에서 받은 결과값을 시간표리스트에 저장하기 위한 메서드
            }
        }
        GetDataJSON g = new GetDataJSON();//JSON형식으로 데이터 가져옴
        g.execute(url, userID, WhatDate); //url, userID, 날짜를 파라미터로 넘김.
    }
    //창에 다시 돌아왔을 때
    @Override
    public void onResume() {
        super.onResume();
        mThisMonthCalendar = Calendar.getInstance();//mThisMonthCalendar의 calendar값 가져오기 위한 변수
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);//mThisMonthCalendar의 날짜를 1일로 설정
        getCalendar(mThisMonthCalendar);//getCalendar메소드에 mThisMonthCalendar넣고 메소드 불러오기
    }
    //달력구현 메소드
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
        initcafeteriaAdapter();//initcafeteriaAdapter 메소드 불러오기
    }
    //지난달구현 메소드
    private Calendar getLastMonth(Calendar calendar) {//지난달 버튼을 통해 mThisMontheCalendar를 가져옴.
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);//calendar의 날짜를 해당 월의 1일로 설정
        calendar.add(Calendar.MONTH, -1);//지난달 이기 때문에 -1
        //달력 제목을 바뀐 calendar의 연도와 날짜로 설정한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;//calendar반환
    }
    //다음달구현 메소드
    private Calendar getNextMonth(Calendar calendar) {//지난달 버튼을 통해 mThisMontheCalendar를 가져옴.
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);//calendar의 날짜를 해당 월의 1일로 설정
        calendar.add(Calendar.MONTH, +1);//다음달 이기 때문에 +1
        //달력 제목을 바뀐 calendar의 연도와 날짜로 설정한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;//calendar반환
    }
    //어뎁터에 넣어주기
    private void initcafeteriaAdapter() {
        mCalendarAdapter = new cafeteria_Adapter(ScheduleActivity.this, R.layout.cafeteria_day, mDayList);//cafeteria_adapter 불러오기.
        mGvCalendar.setAdapter(mCalendarAdapter);//adapter에 넣어주기
    }
    //날짜 클릭 이벤트 구현
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
        //클릭된 날짜를 WhatDate에 저장
        int firstday = mThisMonthCalendar.get(Calendar.DAY_OF_WEEK);//1일의 요일을 int값에 저장.(mThisCalendar의 날짜는 1일로 설정되어있다.)
        switch (firstday) {//일요일 부터 토요일까지 1~7
            case 1://일요일
                position -= 6;//포지션 값 - 6
                break;
            case 3://화요일
                position -= 1;//포지션 값 - 1
                break;
            case 4://수요일
                position -= 2;//포지션 값 - 2
                break;
            case 5://목요일
                position -= 3;//포지션 값 - 3
                break;
            case 6://금요일
                position -= 4;//포지션 값 - 4
                break;
            case 7://토요일
                position -= 5;//포지션 값 - 5
                break;
        }
        //날짜를 WhatDate에 저장.
        WhatDate = mThisMonthCalendar.get(Calendar.YEAR) + "년" + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월" +
                position + "일";//날짜는 급식표 gridview의 포지션값.
        //textview에 클릭된 날짜 설정.
        tv_schedule_title.setText(WhatDate + "의 일정");
        //DB와 연동하여 저장된 Text보여주기
        mDayList = new ArrayList<DayInfo>();//배열생성
        TodoList = new ArrayList<HashMap<String, String>>();//hashmap 생성
        schedulelist = findViewById(R.id.ListView_Schedule_Content);//listview가져오기
        adapter = new ScheduleAdapter();//adapter생성.
        getData("http://highschool.dothome.co.kr/ScheduleTable.php", WhatDate);//url에 클릭된 날짜를 넣어준다.
    }
    //버튼(다음달, 이전달, 추가)클릭 이벤트 구현
    @Override
    public void onClick (View v){
        switch (v.getId()) {//button의 id로 구분
            //지난달 버튼구현
            case R.id.gv_calendar_activity_b_last:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);//mThisMonthCalendar를 지난달 구현 메소드에 넣어준다.
                getCalendar(mThisMonthCalendar);//mThisMonthCalendar를 달력 구현 메소드에 넣어준다.
                break;
            //다음달 버튼구현
            case R.id.gv_calendar_activity_b_next:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);//mThisMonthCalendar를 다음달 구현 메소드에 넣어준다.
                getCalendar(mThisMonthCalendar);//mThisMonthCalendar를 달력 구현 메소드에 넣어준다.
                break;
            //일정 추가 버튼 구현
            case R.id.Button_Schedule_Add:
                //다이얼로그 창은 xml로 만들어서 가져온다.
                //dialog만드는 코드
                AlertDialog.Builder add_schedule = new AlertDialog.Builder(ScheduleActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.schedule_dialog, null);//dialog 화면은 schedule_dialog xml파일을 사용한다.
                add_schedule.setView(view);
                //schedule_dialog에서 값 가져오기
                schedule_dialog_title = view.findViewById(R.id.schedule_dialog_title);
                schedule_dialog_eddittext = view.findViewById(R.id.schedule_dialog_edittext);
                schedule_dialog_drop = view.findViewById(R.id.schedule_dialog_dropbutton);
                schedule_dialog_add = view.findViewById(R.id.schedule_dialog_addbutton);
                final AlertDialog dialog = add_schedule.create();//dialog생성
                schedule_dialog_title.setText(WhatDate);//해당 날짜를 제목에 설정해준다.
                //dialog의 추가 버튼 구현
                schedule_dialog_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Todo에 String값으로 EditText를 저장
                        String Todo = schedule_dialog_eddittext.getText().toString();
                        //ScheduleID에 일정을 추가한 시간을 string값으로 저장
                        ScheduleID = calendar.get(Calendar.HOUR_OF_DAY)+""+
                                calendar.get(Calendar.MINUTE)+""+calendar.get(Calendar.SECOND);
                        //DB에 값 넣어주기
                        Response.Listener responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {//일정 추가 성공
                                        Toast.makeText(ScheduleActivity.this, "일정을 추가하였습니다.",
                                                Toast.LENGTH_SHORT).show();//Toast메세지 띄우기
                                        //페이지 새로고침
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    } else {//일정 추가 실패
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //Volley를 이용해 서버로 요청
                        Register_Schedule register_schedule = new Register_Schedule(userID, WhatDate,
                                Todo, ScheduleID, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                        queue.add(register_schedule);
                        dialog.dismiss();//dialog 닫기
                    }
                });
                //dialog 취소버튼 구현
                schedule_dialog_drop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();//dialog 닫기
                    }
                });
                dialog.show();//dialog 띄우기
                break;
        }
    }
}