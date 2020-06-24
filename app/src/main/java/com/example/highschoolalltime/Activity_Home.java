package com.example.highschoolalltime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Activity_Home extends Fragment {
    ListView schedulelist,timeList; // 스케줄과 시간표 리스트 변수 선언
    String weekDay, myJSON, userId, userSchool , WhatBoard = "Notice_Board", date , WhatDate; // 해당요일변수, DB에서 받아온 값을 저장할 변수, 유저 아이디변수, 유저학교, 공지사항을 db에서 가져오기위한 보드변수선언, 해당요일 변수
    TextView cafeteria; // 급식표 변수선언
    TextView Time_Day_Date_textView; //해당요일 변수선언

    private static String TAG = "Activity_Home";//JAVA 파일 명
    private static final String TAG_RESPONSE = "response";//DB 스키마
    private static final String TAG_SUBJECT = "Subject";//DB 스키마
    private static final String TAG_POSITION = "Position";//DB 스키마

    JSONArray peoples = null;//응답으로 인한 데이터 저장 배열
    ArrayList<HashMap<String, String>> subjectList;//과목 HashMap 배열
    ArrayList<HashMap<String, String>> NoticeList;//공지사항 HashMap 배열
    private HomeTimeAdapter adapter;//시간표 과목을 리스트에 저장하기 위한 어댑터

    private static final String TAG_TITLE = "title";//공지사항에 title만 가져오기 위해서 사용된 변수

    String Notice = ""; //textview에 띄워줄 공지사항 문자열
    TextView Alarm_textView; // 공지사항문자열을 넣을 텍스트뷰 생성

    Button TimeWork_Button, myWork_btn; // 시간표 추가 버튼, 나의 스케줄 추가 버튼

    private ArrayList<DayInfo> mDayList;
    //DB접근을 위한 변수 지정.
    ArrayList<HashMap<String, String>> TodoList;//HashMap 배열
    //일정listview의 adapter 지정.
    private ScheduleAdapter adapter1;
    private static final String TAG_Todo = "Todo";//DB 스키마
    private static final String TAG_WhatDate = "WhatDate";//DB 스키마
    private static final String TAG_ScheduleID = "ScheduleID";//DB 스키마

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view; //fragment의 뷰 생성

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity__home, container, false); //activity__home.xml를 링크해줌

        Bundle bundle2 = getArguments(); // MainFrame에서 넘겨받은 bundle을 생성
        userId = bundle2.getString("userID"); //bundle로 받은 유저 아이디 값 저장
        userSchool = bundle2.getString("userSchool");//bundle로 받은 유저 학교 값 저장

        String result = "tv_result";// DB에 저장된 요일에 맞게 값을 불러오기 위해 사용되는 문자열 변수

        myWork_btn = view.findViewById(R.id.MyWork_Button); // 나의 스케줄 추가 페이지로 이동 버튼 링크해줌
        myWork_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //나의 스케줄 추가 페이지로 이동 버튼 클릭 이벤트
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);//인텐트로 Activity home -> ScheduleActivity로 이동
                intent.putExtra("userID", userId); //이동할때 유저 아이디 넘겨주기
                startActivity(intent); //액티비티 이동
            }
        });
        //시간표 추가 버튼 링크
        TimeWork_Button = view.findViewById(R.id.TimeWork_Button);
        TimeWork_Button.setOnClickListener(new View.OnClickListener() { //시간표 추가버튼 이벤트
            @Override
            public void onClick(View view) {
                MainFrame mainFrame = (MainFrame) getActivity(); //메인프레임 연결
                mainFrame.setFrag(1);//시간표 프래그먼트 띄워줌
            }
        });

        //할일
        Calendar calendar = Calendar.getInstance(); // 캘린더 데이터 가져오기
        calendar = Calendar.getInstance();//calendar에서 값을 가져오기 위한 코드.
        WhatDate = calendar.get(Calendar.YEAR) + "년" + (calendar.get(Calendar.MONTH) + 1) + "월" +
                calendar.get(Calendar.DAY_OF_MONTH) + "일";//'yyyy'년'mm'월'dd'일 형식의 string값.
        mDayList = new ArrayList<DayInfo>();
        TodoList = new ArrayList<HashMap<String, String>>();
        schedulelist = view.findViewById(R.id.mySche);//activity_schedule의 listview를 가져옴.
        adapter1 = new ScheduleAdapter();//adapter설정.
        getData2("http://highschool.dothome.co.kr/ScheduleTable.php", WhatDate);//whatdate(오늘 날짜)에 저장된 DB값을 가져온다.

        //시간표
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault()); //요일 데이터 , 형식 불러오기
        calendar = Calendar.getInstance(); // 캘린더 데이터 가져오기
        weekDay = dayFormat.format(calendar.getTime()); //해당요일 데이터 저장
        switch (weekDay) {//Switch문을 이용해 문자열과 Layout 내 TextView ID 값들과 비교해 TextView ID값을 정의, <m:월, tu:화, w:수, t:목, f:금>, 문자열 제일 뒤는 수강 시간으로 ID값을 지정
            case "월요일":
                result = result + "_m";
                break;
            case "화요일":
                result = result + "_tu";
                break;
            case "수요일":
                result = result + "_w";
                break;
            case "목요일":
                result = result + "_t";
                break;
            case "금요일":
                result = result + "_f";
                break;
            case "토요일":
                result = result + "Saturday";
                break;
            case "일요일" :
                result = result + "Sunday";
                break;
        }
        System.out.println(result); //요일 출력

        subjectList = new ArrayList<HashMap<String, String>>(); //과목 리스트 생성
        timeList = view.findViewById(R.id.myTimetable); //시간표 리스트 링크
        adapter = new HomeTimeAdapter(); //어댑터 생성
        peoples = null; // 배열 초기화
        getData("http://highschool.dothome.co.kr/HomegetTimeTable.php", result); //db에 시간표 데이터 넘겨받을 함수

        Time_Day_Date_textView = view.findViewById(R.id.Time_Day_Date_textView); //해당 년도,몇월,몇일 textview 링크
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA); //년도, 몇월, 몇일 불러오기
        String TODAY = dfDate.format(new Date()); // 지정한 날짜값 데이터 가져오기
        Time_Day_Date_textView.setText(TODAY + " " + weekDay); //+요일해서 textview에 띄워줌

        Alarm_textView = view.findViewById(R.id.Alarm_textView); //공지사항 문자열 textview 링크
       Alarm_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //textview 클릭 이벤트
                Intent intent = new Intent(getActivity(), Notice_Board.class ); //인텐트로 Activity_home -> Notice_Board로 이동
                startActivity(intent); //페이지 전환 시작
            }
        });
        Notice = ""; //공지사항 문자열 초기화
        NoticeList = new ArrayList<HashMap<String, String>>(); //db에서 공지사항을 받을 문자열 리스트 생성
        getData1("http://wkwjsrjekffk.dothome.co.kr/HomeNoticeSet.php"); //db에서 공지사항 데이터를 넘겨받을 함수
        final Animation animTransRotate = AnimationUtils.loadAnimation(getContext(),R.anim.translate_left); //anim폴더의 애니메이션 translate_left파일을 링크
        Alarm_textView.startAnimation(animTransRotate); //공시사항 textview에 애니메이션 등록

        /*android:fromXDelta="100%p" - 애니메이션이 시작되는 x좌표의 위치, 몇 퍼센트부터 시작할 것인지 나타냅니다.
        android:toXDelta="0%p" - 애니메이션이 종료되는 x좌표의 위치입니다.
                android:duration="500" - 애니메이션의 지속 시간입니다. 저의 경우 0.5초 동안 애니메이션이 진행되도록 지정해준 것입니다.
        android:repeatCount="0" - 애니메이션의 반복 횟수입니다.
        android:fillAfter="true" - 애니메이션이 종료된 후에도 위치(상태)를 유지시킬 것인지 지정해줍니다.
        true는 유지, false는 원래 위치로 다시 이동하게 됩니다.*/


        //급식
        cafeteria = view.findViewById(R.id.Cafeteria_textView);
        calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);//오늘 날짜를 int값으로 저장.
        //calendar에 현재 날짜를 1일로 설정.
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),1);
        int firstday = calendar.get(Calendar.DAY_OF_WEEK);//1일의 요일을 int값에 저장.
        if(firstday==1) {firstday += 5;}//1(일요일)이라면 6으로 저장.
        else {firstday -= 2;}//2~7이라면 -2하여 저장.
        //calendar에 현재 날짜를 다시 설정.
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),today);
        date = calendar.get(Calendar.YEAR)+""+(calendar.get(Calendar.MONTH)+1)+
                (firstday+today);//날짜는 급식표 gridview의 포지션값.
        //DB와 연동하여 저장된 Text보여주기
        Response.Listener<String> reponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {//급식이 있으면 보여줌
                        cafeteria.setText(jsonObject.getString("Menu"));
                    }else {//급식이없으면 return
                        cafeteria.setText("급식이 없습니다.");
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
            ScheduleitemView view = new ScheduleitemView(getContext());//ScheduleitemView 클래스 가져옴.
            Scheduleitem item = items.get(position);
            view.setTodo(item.getTodo());//item의 일정을 view로 넘겨줌.
            return view;
        }
    }
    //DB에서 받아온 값들을 list에 저장하는 메소드
    protected void showList2() {
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
                adapter1.addItem(new Scheduleitem(hashMap.get(TAG_Todo), hashMap.get(TAG_WhatDate),
                        hashMap.get(TAG_ScheduleID)));
            }
            schedulelist.setAdapter(adapter1);//게시글 리스트뷰에 어댑터 저장

            if (TodoList.size() < 1) {
                adapter1.addItem(new Scheduleitem("일정을 추가하세요", "", ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //DB에 저장된 값들을 가져오기 위한 메소드
    void getData2(String url, String WhatDate) {//db php 주소 받음
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
                showList2();//db에서 받은 결과값을 시간표리스트에 저장하기 위한 메서드
            }
        }
        GetDataJSON g = new GetDataJSON();//JSON형식으로 데이터 가져옴
        g.execute(url, userId, WhatDate); //url, userID, 날짜를 파라미터로 넘김.
    }

    //시간표 리스트에 등록할 어댑터 클래스
    class HomeTimeAdapter extends BaseAdapter {
        private ArrayList<HomeTimeTableItem> items = new ArrayList<>();

        public void addItem(HomeTimeTableItem item){items.add(item);} //생성자로 item을 등록

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
            HomeTimeTableItemView view = new HomeTimeTableItemView(getContext()); //시간표 아이템데이터를 저장할 리스트아이템뷰 생성
            HomeTimeTableItem item = items.get(position); //아이템의 위치값 불러옴.
            view.setWhattime(item.getPosition());// 해당하는 아이템의 몇 교시인지를 알기위한 포지션값 가져와 저장
            view.setTimeContent(item.getSubject()); //해당하는 아이템의 과목을 가져와 저장
            return view; //저장된 리스트아이템뷰 리턴
        }
    }
    //시간표 db값을 어댑터에 저장하기 위한 메서드
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON); //myJSON에 저장된 db 데이터값을 가져오기 위한 변수 생성
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);//db에서 넘어온값을 peoples의 리스트에 저장

            for(int i = 0; i<peoples.length(); i++){ //반복문을 이용하여 과목리스트에 삽입
                JSONObject c = peoples.getJSONObject(i);
                String subject = c.getString(TAG_SUBJECT);//과목값을 저장하는 변수
                String position = c.getString(TAG_POSITION);//위치(교시)를 저장하는 변수

                HashMap<String, String> persons = new HashMap<String, String>();//해쉬맵 형태의 배열을 생성

                persons.put(TAG_SUBJECT, subject); //키워드 형식으로 과목 저장
                persons.put(TAG_POSITION, position); //키워드 형식으로 위치(교시) 저장


                subjectList.add(persons);//해쉬맵 배열을 리스트에 저장
            }
            System.out.println(subjectList);//출력값 확인
            System.out.println(subjectList.size()); //리스트 사이즈값 확인

            for(int i = 0; i < subjectList.size(); i++){ //과목 리스트의 크기많큼 어탭터의 item저장
                HashMap<String, String> hashMap = subjectList.get(i);//각 배열의 값을 해쉬맵으로 불러오기
                adapter.addItem(new HomeTimeTableItem(hashMap.get(TAG_SUBJECT), hashMap.get(TAG_POSITION))); //어탭터에 저장
            }
        timeList.setAdapter(adapter);//시간표 리스트에 어댑터 저장

            if(subjectList.size() < 1){ //과목 리스트에 데이터가 없을때
                adapter.addItem(new HomeTimeTableItem("시간표를 추가하세요", ""));//시간표 추가 메세지를 하나 띄워주기 위해 어탭터에 저장
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    void getData(String url, String weekday) {//db php 주소 , 해당 요일값 받음
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];//0번째 파라미터의 주소값 저장
                String userID = params[1];//1번째 파라미터의 유저아이디값 저장
                String Day = params[2];//2번째 파라미터의 Day 값 저장
                String postParameters = "userID=" + userID + "&Day=" + Day; //php구문에 post 형식으로 넘길 문자열


                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection(); //url 연결


                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("POST"); //post 형식으로 보냄
                    con.setDoInput(true);
                    con.connect();


                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(postParameters.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();


                    int responseStatusCode = con.getResponseCode();
                    Log.d(TAG, "response code - " + responseStatusCode);

                    InputStream inputStream = con.getInputStream();
                    if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = con.getInputStream();
                    }
                    else{
                        inputStream = con.getErrorStream();
                    }
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");


                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(inputStreamReader);
                    String json;
                    while ((json = bufferedReader.readLine()) != null){
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
            protected void onPostExecute(String result){
                myJSON = result; //결과값을 저장
                showList();//db에서 받은 결과값을 시간표리스트에 저장하기 위한 메서드
            }
        }
        GetDataJSON g = new GetDataJSON(); //JSON형식으로 데이터 가져옴
        g.execute(url, userId, weekday); //url,유저아이디,요일을 파라미터로 넘김
    }

    //공지사항 db값을 어댑터에 저장하기 위한 메서드
    protected void showList1(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);//myJSON에 저장된 db 데이터값을 가져오기 위한 변수 생성
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);//db에서 넘어온값을 peoples의 리스트에 저장

            for(int i = 0; i<peoples.length(); i++){//반복문을 이용하여 공지사항 리스트에 삽입
                JSONObject c = peoples.getJSONObject(i);
                String title = c.getString(TAG_TITLE); //공지사항제목을 저장하는 변수

                HashMap<String, String> persons = new HashMap<String, String>();//해쉬맵 형태의 배열을 생성

                persons.put(TAG_TITLE, title);//키워드 형식으로 제목 저장

                NoticeList.add(persons);//해쉬맵 배열을 리스트에 저장
            }

            Collections.reverse(NoticeList);//처음 저장된 db값 부터 보여주기 위해 역순정렬
            System.out.println(NoticeList + "이것은 공지");//출력값 확인
            for(int i = 0; i < NoticeList.size(); i++){//공지사항 리스트의 크기많큼 어탭터의 item저장
                HashMap<String, String> hashMap = NoticeList.get(i);//각 배열의 값을 해쉬맵으로 불러오기
                Notice = Notice + hashMap.get(TAG_TITLE) + "/ "; //각 공지사항을 /로 사용하여 문자열로 합침.
            }
            System.out.println(Notice);//공지사항을 출력
            Alarm_textView.setText(Notice);//공지사항값을 textview에 저장.
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getData1(String url) {//db php 주소 받음
        class GetDataJSON extends AsyncTask<String,Void,String>{

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];//0번째 파라미터의 주소값 저장
                String school = params[1];//1번째 파라미터의 유저학교값 저장
                String board = params[2];//2번째 파라미터의 보드이름 값 저장
                String postParameters = "userSchool=" + school + "&Whatboard=" + board;//php구문에 post 형식으로 넘길 문자열

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();//url 연결


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
                    if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = con.getInputStream();
                    }
                    else{
                        inputStream = con.getErrorStream();
                    }
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");


                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(inputStreamReader);
                    //bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null){
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
            protected void onPostExecute(String result){
                myJSON = result;//결과값을 저장
                showList1();//db에서 받은 결과값을 공지사항리스트에 저장하기 위한 메서드
            }
        }
        GetDataJSON g = new GetDataJSON();//JSON형식으로 데이터 가져옴
        g.execute(url,userSchool,WhatBoard);//url,유저학교,보드이름을 파라미터로 넘김
    }

}

