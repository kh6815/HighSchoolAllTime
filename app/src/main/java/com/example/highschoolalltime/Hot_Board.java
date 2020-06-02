package com.example.highschoolalltime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Hot_Board extends AppCompatActivity {
    private ListView list;
    private Button submit;
    private Button cencel;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6,imageView7,imageView8,imageView9,imageView10;

    private EditText title;
    private EditText content;
    private AlertDialog dialog;
    ArrayList<String> board_temp;

    String userID ,userSchool,WhatBoard;
    Button btn;
    TextView SchoolName_textView;
    //ArrayAdapter<String> adapter;

    String myJSON;
    private static String TAG = "Hot_Board";
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
    List<String> data = new ArrayList<>();
    // ArrayList<HashMap<String, String>> tempList;

    MyAdapter adapter;
    String userName,userGrade, userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot__board);
        //데이터베이스에 저장된 내용20개만 먼저 가지고 오고 새로고침이 되면 데이터를 더 가져오기
        //newShow();
        //new BackgroundTask().execute();
        adapter = new MyAdapter();
        userID = ((use_user)this.getApplication()).getUserID();
        userSchool = ((use_user)this.getApplication()).getUserSchool();
        userName = ((use_user)this.getApplication()).getUserName();
        userGrade = ((use_user)this.getApplication()).getUserGrade();
        userEmail = ((use_user)this.getApplication()).getUserEmail();
        WhatBoard = "Hot_Board";
        list = (ListView) findViewById(R.id.notice_board_list);
        list.setFocusable(false);

        personList = new ArrayList<HashMap<String, String>>();
        getData("http://wkwjsrjekffk.dothome.co.kr/HotBoardSet.php"); //php파일이름 작성
        list.setAdapter(adapter);


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

                Intent intent = new Intent(Hot_Board.this, Clickboard.class );

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
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                String school = params[1];
                String postParameters = "userSchool=" + school;; // userSchool, Whatboard 필요함.

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
        g.execute(url,userSchool);
    }
}

