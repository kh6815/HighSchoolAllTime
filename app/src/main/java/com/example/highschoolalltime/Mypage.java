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

    //게시글
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


    //댓글
    private static final String TAG_CONTENT_USERID = "content_userid";
    private static final String TAG_CONTENT_USERSHCOOL = "content_userSchool";
    private static final String TAG_CONTENT_TITLE = "content_title";
    private static final String TAG_CONTENT_CONTENT = "content_content";
    private static final String TAG_CONTENT_WHATBOARD = "content_Whatboard";
    private static final String TAG_CONTENT_TIME = "content_time";
    private static final String TAG_COMMENTS_USERID = "userid";
    private static final String TAG_COMMENTS_COMMENTS = "comments";
    private static final String TAG_COMMENTS_TIME = "time";

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    MyAdapter adapter;

    JSONArray commentsArray = null;
    ArrayList<HashMap<String, String>> commentsList;
    commentAdapter adapter1;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mypage, container, false);//메인화면에 userid를 계속 띄우주기 위함.

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
                editor.clear();
                editor.commit();
                //editor.remove("userID");
                //editor.remove("userPassword");
                //editor.remove("userSchool");
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


        //DB에 저장된 글 listview는 차후 개발예정
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
        });
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
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);

            for(int i = 0; i<peoples.length(); i++){
                JSONObject c = peoples.getJSONObject(i);

                String userid = c.getString(TAG_USERID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String comments = c.getString(TAG_COMMENTS);
                String Whatboard = c.getString(TAG_WHATBOARD);
                String time = c.getString(TAG_TIME);
                String hotCount = c.getString(TAG_HOTCOUNT);
                String hotclickUser = c.getString(TAG_HOTCLICKUSER);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_USERID, userid);
                persons.put(TAG_TITLE, title);
                persons.put(TAG_CONTENT, content);
                persons.put(TAG_COMMENTS, comments);
                persons.put(TAG_WHATBOARD, Whatboard);
                persons.put(TAG_TIME, time);
                persons.put(TAG_HOTCOUNT, hotCount);
                persons.put(TAG_HOTCLICKUSER, hotclickUser);

                personList.add(persons);
            }

            Collections.reverse(personList);
            System.out.println(personList);
            for(int i = 0; i < personList.size(); i++){
                HashMap<String, String> hashMap = personList.get(i);
                MyItem myItemTemp = new MyItem(hashMap.get(TAG_USERID),hashMap.get(TAG_TITLE), hashMap.get(TAG_CONTENT), hashMap.get(TAG_COMMENTS),hashMap.get(TAG_TIME),hashMap.get(TAG_HOTCOUNT),hashMap.get(TAG_HOTCLICKUSER));
                myItemTemp.setWhatboard(hashMap.get(TAG_WHATBOARD));
                adapter.addItem(myItemTemp);
            }
            Mypage_Content_list.setAdapter(adapter);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getData_content(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                String userid = params[1];
                String userschool = params[2];
                String postParameters = "userid=" + userid + "&userSchool=" + userschool; // userSchool, Whatboard 필요함.

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
        g.execute(url,userID,userSchool);
    }

    private void getData_comments(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                String userid = params[1];
                String content_userSchool = params[2];

                String postParameters = "userid=" + userid + "&content_userSchool=" + content_userSchool;
                //String postParameters = "userid=" + userid;
                System.out.println(postParameters);

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
        g.execute(url, userID, userSchool);
    }
    protected void showList1(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            commentsArray = jsonObj.getJSONArray(TAG_RESPONSE);
            System.out.println(commentsArray);

            for(int i = 0; i<commentsArray.length(); i++){
                JSONObject c = commentsArray.getJSONObject(i);
                String content_userid = c.getString(TAG_CONTENT_USERID);
                String content_userSchool = c.getString(TAG_CONTENT_USERSHCOOL);
                String content_title = c.getString(TAG_CONTENT_TITLE);
                String content_content = c.getString(TAG_CONTENT_CONTENT);
                String content_Whatboard = c.getString(TAG_CONTENT_WHATBOARD);
                String content_time = c.getString(TAG_CONTENT_TIME);

                String userid = c.getString(TAG_COMMENTS_USERID);
                String comments = c.getString(TAG_COMMENTS_COMMENTS);
                String time = c.getString(TAG_COMMENTS_TIME);

                HashMap<String, String> persons = new HashMap<String, String>();

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

            Collections.reverse(commentsList);
            for(int i = 0; i < commentsList.size(); i++){
                HashMap<String, String> hashMap = commentsList.get(i);
                commentItem commentItemTemp = new commentItem(hashMap.get(TAG_COMMENTS_USERID), hashMap.get(TAG_COMMENTS_COMMENTS) ,hashMap.get(TAG_COMMENTS_TIME));
                commentItemTemp.setContent_userid(hashMap.get(TAG_CONTENT_USERID));
                commentItemTemp.setContent_userSchool(hashMap.get(TAG_CONTENT_USERSHCOOL));
                commentItemTemp.setContent_title(hashMap.get(TAG_CONTENT_TITLE));
                commentItemTemp.setContent_content(hashMap.get(TAG_CONTENT_CONTENT));
                commentItemTemp.setContent_Whatboard(hashMap.get(TAG_CONTENT_WHATBOARD));
                commentItemTemp.setContent_time(hashMap.get(TAG_CONTENT_TIME));
                adapter1.addItem(commentItemTemp);
            }
            Mypage_Comments_list.setAdapter(adapter1);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void passContent(String url, String content_useridStr, String content_titleStr, String content_contentStr, String WhatboardStr, String timeStr){
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                String content_userid = params[1];
                String content_userSchool = userSchool;
                String content_title = params[2];
                String content_content = params[3];
                String content_Whatboard = params[4];
                String content_time = params[5];

                String postParameters = "content_userid=" + content_userid + "&content_userSchool=" + content_userSchool + "&content_title=" + content_title + "&content_content=" + content_content  +"&content_Whatboard=" + content_Whatboard + "&content_time=" + content_time;

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
                showList2();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, content_useridStr, content_titleStr,content_contentStr, WhatboardStr, timeStr);
    }
    protected void showList2(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);

            for(int i = 0; i<peoples.length(); i++){
                JSONObject c = peoples.getJSONObject(i);

                String userid = c.getString(TAG_USERID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String Whatboard = c.getString(TAG_WHATBOARD);
                String time = c.getString(TAG_TIME);
                String hotCount = c.getString(TAG_HOTCOUNT);
                String hotclickUser = c.getString(TAG_HOTCLICKUSER);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_USERID, userid);
                persons.put(TAG_TITLE, title);
                persons.put(TAG_CONTENT, content);
                persons.put(TAG_WHATBOARD, Whatboard);
                persons.put(TAG_TIME, time);
                persons.put(TAG_HOTCOUNT, hotCount);
                persons.put(TAG_HOTCLICKUSER, hotclickUser);

                personList.add(persons);
            }

            HashMap<String, String> hashMap = personList.get(0);
            Intent intent = new Intent(getActivity(), Clickboard.class );

            intent.putExtra("userID", hashMap.get(TAG_USERID));
            intent.putExtra("title", hashMap.get(TAG_TITLE));
            intent.putExtra("content",  hashMap.get(TAG_CONTENT));
            intent.putExtra("time", hashMap.get(TAG_TIME));
            intent.putExtra("hotCount", hashMap.get(TAG_HOTCOUNT));
            intent.putExtra("Whatboard", hashMap.get(TAG_WHATBOARD));
            intent.putExtra("hotclickUser", hashMap.get(TAG_HOTCLICKUSER));
            //finish();
            startActivity(intent);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
