package com.example.highschoolalltime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
    ListView myScheList,timeList;
    String weekDay, myJSON, userId, userSchool , WhatBoard = "Notice_Board", date;
    TextView cafeteria;
    TextView Time_Day_Date_textView;

    private static String TAG = "Activity_Home";//JAVA 파일 명
    private static final String TAG_RESPONSE = "response";//DB 스키마
    private static final String TAG_SUBJECT = "Subject";//DB 스키마
    private static final String TAG_POSITION = "Position";//DB 스키마

    JSONArray peoples = null;//응담으로 인한 데이터 저장 배열
    ArrayList<HashMap<String, String>> subjectList;//HashMap 배열
    ArrayList<HashMap<String, String>> NoticeList;//HashMap 배열
    //private ArrayList<HomeTimeTableItem> data;
    private HomeTimeAdapter adapter;
    //private LinearLayoutManager mLayoutManager;




    private static String TAG1 = "Notice_Board";
    //public static final String TAG = Notice_Board.class
    //.getSimpleName();
    private static final String TAG_USERID = "userid";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_COMMENTS = "comments";
    private static final String TAG_TIME = "time";
    private static final String TAG_HOTCOUNT = "hotCount";
    private static final String TAG_HOTCLICKUSER = "hotclickUser";
    String Notice = "";
    TextView Alarm_textView;

    Animation tranlateLeftAnim;
    Animation tranlateRightAnim;

    Button myWork_btn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity__home, container, false);
        //EditText에 현재 입력되어 있는 값을 get해온다.


        Bundle bundle2 = getArguments();
        userId = bundle2.getString("userID");
        userSchool = bundle2.getString("userSchool");
        String result = "tv_result";//Edit Text로 입력 받은 데이터를 삽입할 TextView
        myWork_btn = view.findViewById(R.id.MyWork_Button);
        myWork_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                intent.putExtra("userID", userId);
                startActivity(intent); //액티비티 이동
            }
        });
        //시간표
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime()); //해당요일
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
        System.out.println(result);
        //data = new ArrayList<>();
        subjectList = new ArrayList<HashMap<String, String>>();
        timeList = view.findViewById(R.id.myTimetable);
        adapter = new HomeTimeAdapter();
        getData("http://highschool.dothome.co.kr/HomegetTimeTable.php", result);

        Time_Day_Date_textView = view.findViewById(R.id.Time_Day_Date_textView);
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String TODAY = dfDate.format(new Date());
        Time_Day_Date_textView.setText(TODAY + " " + weekDay);

        Alarm_textView = view.findViewById(R.id.Alarm_textView);
       Alarm_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Notice_Board.class );
                startActivity(intent);
            }
        });
        Notice = "";
        NoticeList = new ArrayList<HashMap<String, String>>();
        getData1("http://wkwjsrjekffk.dothome.co.kr/HomeNoticeSet.php");
        final Animation animTransRotate = AnimationUtils.loadAnimation(getContext(),R.anim.translate_left);
        Alarm_textView.startAnimation(animTransRotate);
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

    class HomeTimeAdapter extends BaseAdapter {
        private ArrayList<HomeTimeTableItem> items = new ArrayList<>();

        public void addItem(HomeTimeTableItem item){items.add(item);}

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
            HomeTimeTableItemView view = new HomeTimeTableItemView(getContext());
            HomeTimeTableItem item = items.get(position);
            view.setWhattime(item.getPosition());
            view.setTimeContent(item.getSubject());
            return view;
        }
    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);

            for(int i = 0; i<peoples.length(); i++){
                JSONObject c = peoples.getJSONObject(i);
                String subject = c.getString(TAG_SUBJECT);
                String position = c.getString(TAG_POSITION);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_SUBJECT, subject);
                persons.put(TAG_POSITION, position);


                subjectList.add(persons);
            }
            System.out.println(subjectList);
            System.out.println(subjectList.size());

            for(int i = 0; i < subjectList.size(); i++){
                HashMap<String, String> hashMap = subjectList.get(i);
                adapter.addItem(new HomeTimeTableItem(hashMap.get(TAG_SUBJECT), hashMap.get(TAG_POSITION)));
            }
        timeList.setAdapter(adapter);

            if(subjectList.size() < 1){
                adapter.addItem(new HomeTimeTableItem("시간표를 추가하세요", ""));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    void getData(String url, String weekday) {
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                String userID = params[1];
                String Day = params[2];
                String postParameters = "userID=" + userID + "&Day=" + Day;


                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();


                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("POST");
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
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, userId, weekday); //유저 아이디 추가
    }

    //
    protected void showList1(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);

            for(int i = 0; i<peoples.length(); i++){
                JSONObject c = peoples.getJSONObject(i);
                String title = c.getString(TAG_TITLE);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_TITLE, title);

                NoticeList.add(persons);
            }

            Collections.reverse(NoticeList);
            System.out.println(NoticeList + "이것은 공지");
            for(int i = 0; i < NoticeList.size(); i++){
                HashMap<String, String> hashMap = NoticeList.get(i);
                Notice = Notice + hashMap.get(TAG_TITLE) + "/ ";
            }
            System.out.println(Notice);
            Alarm_textView.setText(Notice);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getData1(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String>{

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                String school = params[1];
                String board = params[2];
                String postParameters = "userSchool=" + school + "&Whatboard=" + board;// userSchool, Whatboard 필요함.

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();


                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("POST");
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
                myJSON = result;
                showList1();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url,userSchool,WhatBoard);
    }

}

