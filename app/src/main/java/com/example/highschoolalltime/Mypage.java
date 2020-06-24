package com.example.highschoolalltime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Mypage extends Fragment {
    private View view;
    private ListView Mypage_Content_list, Mypage_Comments_list;

    //게시글 관련 태그 변수 선언
    String userID,userSchool,myJSON;
    private static String TAG = "Mypage";
    private static final String TAG_RESPONSE = "response";
    private static final String TAG_USERID = "userid";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_COMMENTS = "comments";
    private static final String TAG_WHATBOARD = "Whatboard";
    private static final String TAG_TIME = "time";
    private static final String TAG_HOTCOUNT = "hotCount";
    private static final String TAG_HOTCLICKUSER = "hotclickUser";


    //댓글 관련 태그 변수 선언
    private static final String TAG_CONTENT_USERID = "content_userid";
    private static final String TAG_CONTENT_USERSHCOOL = "content_userSchool";
    private static final String TAG_CONTENT_TITLE = "content_title";
    private static final String TAG_CONTENT_CONTENT = "content_content";
    private static final String TAG_CONTENT_WHATBOARD = "content_Whatboard";
    private static final String TAG_CONTENT_TIME = "content_time";
    private static final String TAG_COMMENTS_USERID = "userid";
    private static final String TAG_COMMENTS_COMMENTS = "comments";
    private static final String TAG_COMMENTS_TIME = "time";

    //게시글 관련
    JSONArray peoples = null;// db의 json형식의 데이터 저장 배열
    ArrayList<HashMap<String, String>> personList;//리스트뷰에 저장할 db 데이터 저장 리스트
    MyAdapter adapter;//리스트 뷰에 저장할 어댑터 변수

    //댓글 관련련
    JSONArray commentsArray = null;// db의 json형식의 데이터 저장 배열
    ArrayList<HashMap<String, String>> commentsList;//리스트뷰에 저장할 db 데이터 저장 리스트
    commentAdapter adapter1;//리스트 뷰에 저장할 어댑터 변수

    //자동로그인 할때 저장된 내부db를 로그아웃이나 회원탈퇴 할때 삭제 하기 위해 가져옴
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //xml 연결
        view = inflater.inflate(R.layout.activity_mypage, container, false);//메인화면에 userid를 계속 띄우주기 위함.

        //내부 db 가져오기
        pref = getActivity().getSharedPreferences("mine", Context.MODE_PRIVATE);
        editor = pref.edit();

        //activity_mypage애서 txtview와 button을 가져온다.
        TextView txt_showID = view.findViewById(R.id.TextView_Mypage_ShowID);
        Button btn_logout = view.findViewById(R.id.Button_Mypage_Logout);
        Button btn_withdraw = view.findViewById(R.id.Button_Mypage_Withdraw);
        Button btn_change = view.findViewById(R.id.Button_Mypage_Change);
        //use_user 클래스에서 저장된 user의 값을 불러온다.
        Bundle bundle = getArguments();
        userID = bundle.getString("userID");
        String userName =  bundle.getString("userName");
        userSchool = bundle.getString("userSchool");
        txt_showID.setText(userName+"("+userID+")");

        //쓴글
        Mypage_Content_list = (ListView) view.findViewById(R.id.ListView_Mypage_Content);
        Mypage_Content_list.setFocusable(false);

        adapter = new MyAdapter();
        personList = new ArrayList<HashMap<String, String>>();
        getData_content("http://wkwjsrjekffk.dothome.co.kr/Mypage_BoardSet.php"); //php파일이름 작성
        Mypage_Content_list.setAdapter(adapter);

        //쓴 댓글
        Mypage_Comments_list = (ListView) view.findViewById(R.id.ListView_Mypage_Coment);
        Mypage_Comments_list.setFocusable(false);

        adapter1 = new commentAdapter();
        commentsList = new ArrayList<HashMap<String, String>>();
        getData_comments("http://wkwjsrjekffk.dothome.co.kr/Mypage_CommentSet.php"); //php파일이름 작성
        Mypage_Comments_list.setAdapter(adapter1);

        //login페이지 합칠경우 login페이지 이동(현재는 급식페이지로 대체)
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main_login.class);
                //로그아웃시 내부 db 삭제
                editor.clear();
                editor.commit();
                getActivity().finish();
                startActivity(intent); //액티비티 이동
            }
        });
        //회원탈퇴 페이지로 이동
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), withdraw.class);
                startActivity(intent); //액티비티 이동
            }
        });
        //정보수정 페이지로 이동
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeInformation.class);
                startActivity(intent); //액티비티 이동
            }
        });

        //DB에 저장된 나의 게시글 listview 아이템을 눌렀을 때 해당 게시글 페이지로 이동
        Mypage_Content_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT).show();
                MyItem item = (MyItem) parent.getItemAtPosition(position);

                String userIDStr = item.getuserID();
                String titleStr = item.getTitle() ;
                String contentStr = item.getContent();
                String timeStr = item.getTime();
                String hotCount = item.getHotCount();
                String Whatboard = item.getWhatboard();
                String hotclickUser = item.getHotClickUser();

                Intent intent = new Intent(getActivity(), Clickboard.class );

                intent.putExtra("userID", userIDStr);
                intent.putExtra("title", titleStr);
                intent.putExtra("content", contentStr);
                intent.putExtra("time", timeStr);
                intent.putExtra("hotCount", hotCount);
                intent.putExtra("Whatboard", Whatboard);
                intent.putExtra("hotclickUser", hotclickUser);
                //finish();
                startActivity(intent);
            }
        });//DB에 저장된 나의 댓글 listview 아이템을 눌렀을 때 해당 게시글 페이지로 이동
        Mypage_Comments_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                commentItem item = (commentItem) parent.getItemAtPosition(position);

                String userIDStr = item.getContent_userid();
                String titleStr = item.getContent_title() ;
                String contentStr = item.getContent_content();
                String timeStr = item.getContent_time();
                String Whatboard = item.getContent_Whatboard();
                passContent("http://wkwjsrjekffk.dothome.co.kr/Mypage_PassContentSet.php", userIDStr, titleStr, contentStr, Whatboard, timeStr);
            }
        });
        return view;
    }
    //댓글 리스트아이템이랑 연결할 어댑터 생성
    class commentAdapter extends BaseAdapter {
        private ArrayList<commentItem> items = new ArrayList<>();

        public void addItem(commentItem item){items.add(item);}

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
            commentItemView view = new commentItemView(getContext());
            commentItem item = items.get(position);
            view.setContentuserID(item.getContent_userid()); //게시물 글쓴이아이디
            view.setuserID(item.getUserID()); // 해당 댓글 아이디
            view.setComments(item.getComments());
            view.setTime(item.getTime());
            view.Newuser(userID);
            return view;
        }
    }
    //게시글 리스트아이템이랑 연결할 어댑터 생성
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
            MyItemView view = new MyItemView(getContext());
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
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);;//db에서 넘어온값을 peoples의 리스트에 저장

            for(int i = 0; i<peoples.length(); i++){ //반복문을 이용하여 게시글 리스트에 삽입
                JSONObject c = peoples.getJSONObject(i);

                //db에서 넘어온값 변수에 저장
                String userid = c.getString(TAG_USERID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String comments = c.getString(TAG_COMMENTS);
                String Whatboard = c.getString(TAG_WHATBOARD);
                String time = c.getString(TAG_TIME);
                String hotCount = c.getString(TAG_HOTCOUNT);
                String hotclickUser = c.getString(TAG_HOTCLICKUSER);

                HashMap<String, String> persons = new HashMap<String, String>();//해쉬맵 형태의 배열을 생성

                //키워드 형식으로 데이터 저장
                persons.put(TAG_USERID, userid);
                persons.put(TAG_TITLE, title);
                persons.put(TAG_CONTENT, content);
                persons.put(TAG_COMMENTS, comments);
                persons.put(TAG_WHATBOARD, Whatboard);
                persons.put(TAG_TIME, time);
                persons.put(TAG_HOTCOUNT, hotCount);
                persons.put(TAG_HOTCLICKUSER, hotclickUser);

                personList.add(persons);//해쉬맵 배열을 리스트에 저장
            }

            Collections.reverse(personList); //최근것이 보이게 배열 역순
            System.out.println(personList);
            for(int i = 0; i < personList.size(); i++){ //리스트의 크기많큼 어탭터의 item저장
                HashMap<String, String> hashMap = personList.get(i);//각 배열의 값을 해쉬맵으로 불러오기
                //어탭터에 저장
                MyItem myItemTemp = new MyItem(hashMap.get(TAG_USERID),hashMap.get(TAG_TITLE), hashMap.get(TAG_CONTENT), hashMap.get(TAG_COMMENTS),hashMap.get(TAG_TIME),hashMap.get(TAG_HOTCOUNT),hashMap.get(TAG_HOTCLICKUSER));
                myItemTemp.setWhatboard(hashMap.get(TAG_WHATBOARD));
                adapter.addItem(myItemTemp);
            }
            Mypage_Content_list.setAdapter(adapter);//리스트뷰에 어댑터 저장

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getData_content(String url) {//db php 주소 받음
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                String userid = params[1];
                String userschool = params[2];
                String postParameters = "userid=" + userid + "&userSchool=" + userschool; //php구문에 post 형식으로 넘길 문자열

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection(); //url 연결


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
                showList();//db에서 받은 결과값을 시간표리스트에 저장하기 위한 메서드
            }
        }
        GetDataJSON g = new GetDataJSON();//JSON형식으로 데이터 가져옴
        g.execute(url,userID,userSchool);//url,유저아이디,학교를 파라미터로 넘김
    }

    private void getData_comments(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                String userid = params[1];
                String content_userSchool = params[2];

                String postParameters = "userid=" + userid + "&content_userSchool=" + content_userSchool;//php구문에 post 형식으로 넘길 문자열
                //String postParameters = "userid=" + userid;
                System.out.println(postParameters);

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
                showList1();//db에서 받은 결과값을 시간표리스트에 저장하기 위한 메서드
            }
        }
        GetDataJSON g = new GetDataJSON();;//JSON형식으로 데이터 가져옴
        g.execute(url, userID, userSchool);//url,유저아이디,학교를 파라미터로 넘김
    }
    protected void showList1(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);//myJSON에 저장된 db 데이터값을 가져오기 위한 변수 생성
            commentsArray = jsonObj.getJSONArray(TAG_RESPONSE);//db에서 넘어온값을 commentsArray의 리스트에 저장

            for(int i = 0; i<commentsArray.length(); i++){//반복문을 이용하여 게시글 리스트에 삽입
                JSONObject c = commentsArray.getJSONObject(i);
                //db에서 넘어온값 변수에 저장
                String content_userid = c.getString(TAG_CONTENT_USERID);
                String content_userSchool = c.getString(TAG_CONTENT_USERSHCOOL);
                String content_title = c.getString(TAG_CONTENT_TITLE);
                String content_content = c.getString(TAG_CONTENT_CONTENT);
                String content_Whatboard = c.getString(TAG_CONTENT_WHATBOARD);
                String content_time = c.getString(TAG_CONTENT_TIME);

                String userid = c.getString(TAG_COMMENTS_USERID);
                String comments = c.getString(TAG_COMMENTS_COMMENTS);
                String time = c.getString(TAG_COMMENTS_TIME);

                HashMap<String, String> persons = new HashMap<String, String>();//해쉬맵 형태의 배열을 생성

                //키워드 형식으로 데이터 저장
                persons.put(TAG_CONTENT_USERID, content_userid);
                persons.put(TAG_CONTENT_USERSHCOOL, content_userSchool);
                persons.put(TAG_CONTENT_TITLE, content_title);
                persons.put(TAG_CONTENT_CONTENT, content_content);
                persons.put(TAG_CONTENT_WHATBOARD, content_Whatboard);
                persons.put(TAG_CONTENT_TIME, content_time);

                persons.put(TAG_COMMENTS_USERID, userid);
                persons.put(TAG_COMMENTS_COMMENTS, comments);
                persons.put(TAG_COMMENTS_TIME, time);
                System.out.println(persons + "persons");
                commentsList.add(persons);
                System.out.println( commentsList+ "commentsList");
            }

            Collections.reverse(commentsList);//최근것이 보이게 배열 역순
            for(int i = 0; i < commentsList.size(); i++){//리스트의 크기많큼 어탭터의 item저장
                HashMap<String, String> hashMap = commentsList.get(i);//각 배열의 값을 해쉬맵으로 불러오기
                //어탭터에 저장
                commentItem commentItemTemp = new commentItem(hashMap.get(TAG_COMMENTS_USERID), hashMap.get(TAG_COMMENTS_COMMENTS) ,hashMap.get(TAG_COMMENTS_TIME));
                commentItemTemp.setContent_userid(hashMap.get(TAG_CONTENT_USERID));
                commentItemTemp.setContent_userSchool(hashMap.get(TAG_CONTENT_USERSHCOOL));
                commentItemTemp.setContent_title(hashMap.get(TAG_CONTENT_TITLE));
                commentItemTemp.setContent_content(hashMap.get(TAG_CONTENT_CONTENT));
                commentItemTemp.setContent_Whatboard(hashMap.get(TAG_CONTENT_WHATBOARD));
                commentItemTemp.setContent_time(hashMap.get(TAG_CONTENT_TIME));
                adapter1.addItem(commentItemTemp);
            }
            Mypage_Comments_list.setAdapter(adapter1);//리스트뷰에 어댑터 저장

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    //해당 게시글로 이동하는 메서드
    //db php 주소 받음, 게시글 정보 받음
    private void passContent(String url, String content_useridStr, String content_titleStr, String content_contentStr, String WhatboardStr, String timeStr){
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                //각 파라미터의 데이터 저장
                String uri = params[0];
                String content_userid = params[1];
                String content_userSchool = userSchool;
                String content_title = params[2];
                String content_content = params[3];
                String content_Whatboard = params[4];
                String content_time = params[5];

                //php구문에 post 형식으로 넘길 문자열
                String postParameters = "userid=" + content_userid + "&userSchool=" + content_userSchool + "&title=" + content_title + "&content=" + content_content  +"&Whatboard=" + content_Whatboard + "&time=" + content_time;

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection(); //url 연결

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
                showList2();//db에서 받은 결과값을 시간표리스트에 저장하기 위한 메서드
            }
        }
        GetDataJSON g = new GetDataJSON();//JSON형식으로 데이터 가져옴
        g.execute(url, content_useridStr, content_titleStr,content_contentStr, WhatboardStr, timeStr);//url,게시글 정보를 파라미터로 넘김
    }
    protected void showList2(){
        try {
            //해당 게시글로 넘길 변수 선언
            String userid = null;
            String title = null;
            String content = null;
            String Whatboard = null;
            String time = null;
            String hotCount = null;
            String hotclickUser = null;
            JSONObject jsonObj = new JSONObject(myJSON);//myJSON에 저장된 db 데이터값을 가져오기 위한 변수 생성
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);//db에서 넘어온값을 peoples의 리스트에 저장

            for(int i = 0; i<peoples.length(); i++){ //반복문을 이용하여 게시글 리스트에 삽입 / db에서는 해당하는 게시글을 하나만 찾아서 옴
                JSONObject c = peoples.getJSONObject(i);

                //db에서 넘어온값 변수에 저장
                userid = c.getString(TAG_USERID);
                title = c.getString(TAG_TITLE);
                content = c.getString(TAG_CONTENT);
                Whatboard = c.getString(TAG_WHATBOARD);
                time = c.getString(TAG_TIME);
                hotCount = c.getString(TAG_HOTCOUNT);
                hotclickUser = c.getString(TAG_HOTCLICKUSER);
            }
            Intent intent = new Intent(getActivity(), Clickboard.class ); //해당 게시글 넘어감.
            //해당 게시글 정보를 넘겨줌
            intent.putExtra("userID", userid);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            intent.putExtra("time", time);
            intent.putExtra("hotCount", hotCount);
            intent.putExtra("Whatboard", Whatboard);
            intent.putExtra("hotclickUser", hotclickUser);
            //finish();
            startActivity(intent);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
