package com.example.highschoolalltime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Club_Board extends AppCompatActivity {

    private ListView list;//게시글 리스트
    private Button submit;//게시글 생성 다이얼로그의 게시글 생성버튼
    private Button cencel; //게시글 생성 다이얼로그의 게시글 생성취소버튼

    private EditText title;//게시글 제목 생성 텍스트
    private EditText content;//게시글 내용 생성 텍스트
    private AlertDialog dialog;//게시글 생성 다이얼로그
    ArrayList<String> board_temp;

    String userID ,userSchool,WhatBoard;//유저 정보, 보드이름
    Button btn;// 게시글 생성 버튼

    String myJSON;// db 데이터 결과 저장변수
    private static String TAG = "Club_Board"; //공지사항 변수
    private static final String TAG_RESPONSE = "response";//응답 변수
    //게시글 정보 키워드 변수
    private static final String TAG_USERID = "userid";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_COMMENTS = "comments";
    private static final String TAG_TIME = "time";
    private static final String TAG_HOTCOUNT = "hotCount";
    private static final String TAG_HOTCLICKUSER = "hotclickUser";

    JSONArray peoples = null;// db의 json형식의 데이터 저장 배열
    ArrayList<HashMap<String, String>> personList;//리스트뷰에 저장할 db 데이터 저장 리스트

    MyAdapter adapter; //리스트 뷰에 저장할 어댑터 변수
    String userName,userGrade, userEmail;//유저 정보
    ImageButton rebtn; //새로고침 하기 위한 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);


        adapter = new MyAdapter();//어댑터 생성
        //유저 정보 저장
        userID = ((use_user)this.getApplication()).getUserID();
        userSchool = ((use_user)this.getApplication()).getUserSchool();
        userName = ((use_user)this.getApplication()).getUserName();
        userGrade = ((use_user)this.getApplication()).getUserGrade();
        userEmail = ((use_user)this.getApplication()).getUserEmail();
        WhatBoard = "Club_Board";
        //리스트뷰 링크
        list = (ListView) findViewById(R.id.notice_board_list);
        list.setFocusable(false);

        personList = new ArrayList<HashMap<String, String>>();//리스트 생성
        //db에서 먼저 게시글 정보를 불러와 리스트뷰에 저장하기 위한 메서드
        getData("http://wkwjsrjekffk.dothome.co.kr/BoardSet1.php"); //php파일이름 작성
        // 게시글 버튼 생성
        btn = (Button) findViewById(R.id.Addbutton);
        btn.setFocusable(false);
        list.setAdapter(adapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();// 게시글 생성 버튼을 누를시 다이얼로그로 이동
            }
        });

        // 게시글 데이터를 누를시 해당 게시글로 이동
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT).show();
                MyItem item = (MyItem) parent.getItemAtPosition(position);
                //게시글 정보 저장
                String userIDStr = item.getuserID();
                String titleStr = item.getTitle();
                String contentStr = item.getContent();
                String timeStr = item.getTime();
                String hotCount = item.getHotCount();
                String hotclickUser = item.getHotClickUser();

                Intent intent = new Intent(Club_Board.this, Clickboard.class );
                //인텐트로 넘길 데이터 저장
                intent.putExtra("userID", userIDStr);
                intent.putExtra("title", titleStr);
                intent.putExtra("content", contentStr);
                intent.putExtra("time", timeStr);
                intent.putExtra("hotCount", hotCount);
                intent.putExtra("Whatboard", TAG );
                intent.putExtra("hotclickUser", hotclickUser);
                finish();
                startActivity(intent);
            }
        });

        rebtn = findViewById(R.id.imagereButton); //새로고침 버튼 이미지 버튼 링크
        rebtn.setOnClickListener(new View.OnClickListener() { //버튼 클릭 이벤트
            @Override
            public void onClick(View view) {
                //리스트 초기화
                personList = null;
                adapter = null;

                personList = new ArrayList<HashMap<String, String>>();//리스트 생성
                adapter = new MyAdapter();//어댑터 생성

                //다시 db 데이터 가져오기
                getData("http://wkwjsrjekffk.dothome.co.kr/BoardSet1.php"); //php파일이름 작성
                list.setAdapter(adapter);
            }
        });


    }
    //게시글 리스트 뷰에 저장할 어댑터 클래스
    class MyAdapter extends BaseAdapter {
        private ArrayList<MyItem> items = new ArrayList<>();

        public void addItem(MyItem item){items.add(item);}//아이템 저장

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
            //리스트 아이템에 게시글 정보 저장
            MyItemView view = new MyItemView(getApplicationContext());
            MyItem item = items.get(position);
            view.setuserID(item.getuserID());
            view.setTitle(item.getTitle());
            view.setContent(item.getContent());
            view.setComments("댓글 수 : "  + item.getCommentStr());
            view.setHotCount("추천 : " + item.getHotCount());
            view.setTime(item.getTime());
            view.setHotClickUser(item.getHotClickUser());
            return view;
        }
    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);//myJSON에 저장된 db 데이터값을 가져오기 위한 변수 생성
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);//db에서 넘어온값을 peoples의 리스트에 저장

            for(int i = 0; i<peoples.length(); i++){//반복문을 이용하여 게시글 리스트에 삽입
                JSONObject c = peoples.getJSONObject(i);
                //db에서 넘어온값 변수에 저장
                String userid = c.getString(TAG_USERID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String comments = c.getString(TAG_COMMENTS);
                String time = c.getString(TAG_TIME);
                String hotCount = c.getString(TAG_HOTCOUNT);
                String hotclickUser = c.getString(TAG_HOTCLICKUSER);

                HashMap<String, String> persons = new HashMap<String, String>();//해쉬맵 형태의 배열을 생성

                //키워드 형식으로 데이터 저장
                persons.put(TAG_USERID, userid);
                persons.put(TAG_TITLE, title);
                persons.put(TAG_CONTENT, content);
                persons.put(TAG_COMMENTS, comments);
                persons.put(TAG_TIME, time);
                persons.put(TAG_HOTCOUNT, hotCount);
                persons.put(TAG_HOTCLICKUSER, hotclickUser);

                personList.add(persons);//해쉬맵 배열을 리스트에 저장
            }
            System.out.println(personList);//출력값 확인
            System.out.println(personList.size());//리스트 사이즈값 확인
            //tempList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> temp;

            Collections.reverse(personList);//최근것이 보이게 배열 역순
            for(int i = 0; i < personList.size(); i++){//리스트의 크기많큼 어탭터의 item저장
                HashMap<String, String> hashMap = personList.get(i);//각 배열의 값을 해쉬맵으로 불러오기
                //어탭터에 저장
                adapter.addItem(new MyItem(hashMap.get(TAG_USERID),hashMap.get(TAG_TITLE), hashMap.get(TAG_CONTENT), hashMap.get(TAG_COMMENTS),hashMap.get(TAG_TIME),hashMap.get(TAG_HOTCOUNT),hashMap.get(TAG_HOTCLICKUSER)));
            }
            list.setAdapter(adapter);//게시글 리스트뷰에 어댑터 저장

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getData(String url) {//db php 주소 받음
        class GetDataJSON extends AsyncTask<String,Void,String>{

            @Override
            protected String doInBackground(String... params) {
                //각 파라미터의 데이터 저장
                String uri = params[0];
                String school = params[1];
                String board = params[2];
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
                myJSON = result; //결과값을 저장
                showList();//db에서 받은 결과값을 시간표리스트에 저장하기 위한 메서드
            }
        }
        GetDataJSON g = new GetDataJSON();//JSON형식으로 데이터 가져옴
        g.execute(url,userSchool,WhatBoard);//url,유저아이디,요일을 파라미터로 넘김
    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Club_Board.this);//다이얼로그 생성
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.addboard, null);//xml링크
        builder.setView(view);

        //xml연결
        submit = (Button) view.findViewById(R.id.buttonSubmit);
        cencel = (Button) view.findViewById(R.id.buttonCanlcel);

        title = (EditText) view.findViewById(R.id.EditText_title);
        content = (EditText) view.findViewById(R.id.EditText_content);



        dialog = builder.create();
        //게시글 생성 버튼 메서드
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //저장할 게시글 정보 변수
                String title_temp= title.getText().toString();
                String content_temp = content.getText().toString();
                String comments = "";
                String Whatboard= "Club_Board";
                String hotCount = 0 + "";
                String hotclickUser = userID +"/";
                System.out.println(title_temp);

                // 현재시간을 msec 으로 구한다.
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                // nowDate 변수에 값을 저장한다.
                String formatDate = sdfNow.format(date);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//게시판 글작성 성공
                                Toast.makeText(getApplicationContext(),"게시판 글 작성 성공하셨습니다.",Toast.LENGTH_SHORT).show();

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                                //new BackgroundTask().execute();
                                /*
                                Intent intent = new Intent(Notice_Board.this, Notice_Board.class );
                                finish();
                                startActivity(intent);*/
                            }
                            else{ //게시판 글작성 실패
                                Toast.makeText(getApplicationContext(),"게시판 글 작성 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 Volley를 이용해서 요청을 함.
                RegisterRequest_Board registerRequest_board = new RegisterRequest_Board(userID, userSchool ,title_temp, content_temp, comments,Whatboard, formatDate, hotCount, hotclickUser,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Club_Board.this);
                queue.add(registerRequest_board);
                dialog.dismiss();
            }
        });

        cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
