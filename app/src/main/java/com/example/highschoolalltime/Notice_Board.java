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

public class Notice_Board extends AppCompatActivity {

    private ListView list;
    private Button submit;
    private Button cencel;

    private EditText title;
    private EditText content;
    private AlertDialog dialog;
    ArrayList<String> board_temp;

    String userID ,userSchool,WhatBoard;
    Button btn;
    TextView SchoolName_textView;
    //ArrayAdapter<String> adapter;

    String myJSON;
    private static String TAG = "Notice_Board";
    //public static final String TAG = Notice_Board.class
            //.getSimpleName();
    private static final String TAG_RESPONSE = "response";
    private static final String TAG_USERID = "userid";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_COMMENTS = "comments";
    private static final String TAG_TIME = "time";
    private static final String TAG_HOTCOUNT = "hotCount";
    private static final String TAG_HOTCLICKUSER = "hotclickUser";

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
   // ArrayList<HashMap<String, String>> tempList;

    MyAdapter adapter;
    String userName,userGrade, userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        //데이터베이스에 저장된 내용20개만 먼저 가지고 오고 새로고침이 되면 데이터를 더 가져오기
        //newShow();
        //new BackgroundTask().execute();
        adapter = new MyAdapter();
        userID = ((use_user)this.getApplication()).getUserID();
        userSchool = ((use_user)this.getApplication()).getUserSchool();
        userName = ((use_user)this.getApplication()).getUserName();
        userGrade = ((use_user)this.getApplication()).getUserGrade();
        userEmail = ((use_user)this.getApplication()).getUserEmail();
        WhatBoard = "Notice_Board";
        list = (ListView) findViewById(R.id.notice_board_list);
        list.setFocusable(false);

        personList = new ArrayList<HashMap<String, String>>();
        getData("http://wkwjsrjekffk.dothome.co.kr/BoardSet1.php"); //php파일이름 작성
        btn = (Button) findViewById(R.id.Addbutton);
        btn.setFocusable(false);
        list.setAdapter(adapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
               // Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT).show();
                MyItem item = (MyItem) parent.getItemAtPosition(position);

                String userIDStr = item.getuserID();
                String titleStr = item.getTitle() ;
                String contentStr = item.getContent();
                String timeStr = item.getTime();
                String hotCount = item.getHotCount();
                String hotclickUser = item.getHotClickUser();

                Intent intent = new Intent(Notice_Board.this, Clickboard.class );

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



    }

    class MyAdapter extends BaseAdapter {
        private ArrayList<MyItem> items = new ArrayList<>();

        public void addItem(MyItem item){items.add(item);}

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
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);

            for(int i = 0; i<peoples.length(); i++){
                JSONObject c = peoples.getJSONObject(i);
                String userid = c.getString(TAG_USERID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String comments = c.getString(TAG_COMMENTS);
                String time = c.getString(TAG_TIME);
                String hotCount = c.getString(TAG_HOTCOUNT);
                String hotclickUser = c.getString(TAG_HOTCLICKUSER);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_USERID, userid);
                persons.put(TAG_TITLE, title);
                persons.put(TAG_CONTENT, content);
                persons.put(TAG_COMMENTS, comments);
                persons.put(TAG_TIME, time);
                persons.put(TAG_HOTCOUNT, hotCount);
                persons.put(TAG_HOTCLICKUSER, hotclickUser);

                personList.add(persons);
            }
            System.out.println(personList);
            System.out.println(personList.size());
            //tempList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> temp;

            Collections.reverse(personList);
            System.out.println(personList);
            for(int i = 0; i < personList.size(); i++){
                HashMap<String, String> hashMap = personList.get(i);
                adapter.addItem(new MyItem(hashMap.get(TAG_USERID),hashMap.get(TAG_TITLE), hashMap.get(TAG_CONTENT), hashMap.get(TAG_COMMENTS),hashMap.get(TAG_TIME),hashMap.get(TAG_HOTCOUNT),hashMap.get(TAG_HOTCLICKUSER)));
            }
            list.setAdapter(adapter);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getData(String url) {
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
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url,userSchool,WhatBoard);
    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Notice_Board.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.addboard, null);
        builder.setView(view);

        submit = (Button) view.findViewById(R.id.buttonSubmit);
        cencel = (Button) view.findViewById(R.id.buttonCanlcel);

        title = (EditText) view.findViewById(R.id.EditText_title);
        content = (EditText) view.findViewById(R.id.EditText_content);



        dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title_temp= title.getText().toString();
                String content_temp = content.getText().toString();
                String comments = "";
                String Whatboard= "Notice_Board";
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
                RequestQueue queue = Volley.newRequestQueue(Notice_Board.this);
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
