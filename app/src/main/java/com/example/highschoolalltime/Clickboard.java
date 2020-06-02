package com.example.highschoolalltime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class Clickboard extends AppCompatActivity {
    String title, content, time, hotCount,userID, getuserID;
    String userSchool, Whatboard, hotclickUser;
    TextView textView1, textView2, timeTextview, hotCountTextview;
    EditText commentEdiText;
    Button hotbutton, removebutton, updatabutton, commentbutton;
    String hotCount_temp;
    int check = 0, deletetemp = 0;
    String[] array;
    String userName,userGrade, userEmail;
    private AlertDialog dialog;

    private ListView list;
    commentAdapter adapter;

    private static String TAG = "Clickboard";
    String myJSON;
    JSONArray commentsArray = null;
    ArrayList<HashMap<String, String>> commentsList;

    private View commentView;
    Button commentupdateBtn, commentdeleteBtn;

    private static final String TAG_RESPONSE = "response";
    private static final String TAG_USERID = "userid";
    private static final String TAG_COMMENTS = "comments";
    private static final String TAG_TIME = "time";
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Whatboard.equals("Notice_Board")){
            Intent intent = new Intent(Clickboard.this, Notice_Board.class );
            startActivity(intent);
        }
        else if(Whatboard.equals("Free_Board")){
            Intent intent = new Intent(Clickboard.this, Free_Board.class );
            startActivity(intent);
        }
        else if(Whatboard.equals("Hot_Board")){
            Intent intent = new Intent(Clickboard.this, Hot_Board.class );
            startActivity(intent);
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clickboard);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        check = 0;
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        timeTextview = (TextView) findViewById(R.id.time_textView);
        hotCountTextview = (TextView) findViewById(R.id.hotCount_textView);
        hotbutton = (Button) findViewById(R.id.hotbutton);
        removebutton = (Button) findViewById(R.id.removebutton);
        updatabutton = (Button) findViewById(R.id.updatebutton);
        commentEdiText = (EditText) findViewById(R.id.commentText);
        commentbutton = (Button) findViewById(R.id.commentbutton);
        list = (ListView) findViewById(R.id.addcomment_list);
        Intent intent = getIntent();

        userID = ((use_user)this.getApplication()).getUserID(); // 해당하는 나의 아이디
        getuserID = intent.getExtras().getString("userID"); // 게시물의 유저 아이디
        userName = ((use_user)this.getApplication()).getUserName();
        userGrade = ((use_user)this.getApplication()).getUserGrade();
        userEmail = ((use_user)this.getApplication()).getUserEmail();

        if(userID.equals(getuserID)){
            updatabutton.setVisibility(View.VISIBLE);
            removebutton.setVisibility(View.VISIBLE);
        }
        else{
            updatabutton.setVisibility(View.INVISIBLE);
            removebutton.setVisibility(View.INVISIBLE);
        }

        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        time = intent.getExtras().getString("time");
        hotCount = intent.getExtras().getString("hotCount");
        Whatboard = intent.getExtras().getString("Whatboard");
        userSchool = ((use_user)this.getApplication()).getUserSchool();
        hotclickUser = intent.getExtras().getString("hotclickUser");

        textView1.setText("제목 : " + title);
        textView2.setText(content);
        timeTextview.setText(time);
        hotCountTextview.setText(hotCount);

        adapter = new commentAdapter();
        commentsList = new ArrayList<HashMap<String, String>>();
        DBgetcomments("http://wkwjsrjekffk.dothome.co.kr/Comments_Get.php");


        hotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //추천값 수정
                Response.Listener<String> responseListener=new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success=jsonObject.getBoolean("success");
                            if(success){
                               //String hotCount = jsonObject.getString("hotCount");
                                String hotclick= jsonObject.getString("hotclickUser");
                                System.out.println(hotclick + "클릭유저");
                                array = hotclick.split("/");

                                for(int i =0 ; i < array.length; i++){
                                    if(array[i].equals(userID)){
                                        //System.out.println(" 왜 여기 안들어와");
                                        Toast.makeText(getApplicationContext(),"중복추천, 자신의 게시물 추천은 불가합니다.",Toast.LENGTH_SHORT).show();
                                        check = 1;
                                        break;
                                    }
                                }
                                if(check == 0){
                                        int tempint = Integer.parseInt(hotCount) + 1; //추천 카운터를 1 증가시킴
                                        hotCount_temp = Integer.toString(tempint); //다시 문자열 형태로 바꿈
                                        hotclickUser = hotclick + userID + "/"; //유저 아이디를 붙여서 중복을 방지
                                        //카운터와 중복방지 아이디값을 서버로 전송
                                        Redundant_prevention();
                                }
                            }
                            else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //System.out.println(userSchool + "," + title + "," + content + "," + Whatboard + "," + hotclickUser);
                hotcheckRequest hotcheckRequest=new hotcheckRequest(userSchool, title, content, Whatboard, hotclickUser,responseListener);
                RequestQueue queue= Volley.newRequestQueue(Clickboard.this );
                queue.add(hotcheckRequest);
            }
        });
//게시판 삭제
        removebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        updatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        commentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT).show();
                commentItem item = (commentItem) parent.getItemAtPosition(position);

                final String userIDStr = item.getUserID(); // 해당 댓글 아이디
                final String commentsStr = item.getComments() ;
                final String timeStr = item.getTime();

                if(userIDStr.equals(userID)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Clickboard.this);

                    builder.setTitle("해당 항목을 선택하십시오");
                    builder.setMessage("댓글 수정 or 삭제");
                    builder.setPositiveButton("수정",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    commentUpdate(userIDStr, commentsStr, timeStr);
                                }
                            });
                    builder.setNegativeButton("삭제",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    commentDelete(userIDStr, commentsStr, timeStr);
                                }
                            });
                    builder.show();
                }
                else{

                }

            }
        });
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
            commentItemView view = new commentItemView(getApplicationContext());
            commentItem item = items.get(position);
            view.setContentuserID(getuserID); //게시물 글쓴이아이디
            view.setuserID(item.getUserID()); // 해당 댓글 아이디
            view.setComments(item.getComments());
            view.setTime(item.getTime());
            view.Newuser(userID);
            return view;
        }
    }

    void Redundant_prevention() { //중복방지 매세드 -> 서버로 카운터값과 아이디값을 전송
        Response.Listener<String> responseListener=new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    boolean success=jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(),"게시물을 추천했습니다.",Toast.LENGTH_SHORT).show();
                        hotCountTextview.setText(hotCount_temp);
                    }
                    else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        updatehotclickRequest updatehotclickRequest=new updatehotclickRequest(userSchool, title, content, Whatboard,hotCount_temp ,hotclickUser,responseListener);
        RequestQueue queue= Volley.newRequestQueue(Clickboard.this );
        queue.add(updatehotclickRequest);
    }
    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제");
        builder.setMessage("게시글을 삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(),"삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                        /*
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);*/
                                        withCommentDelete();
                                        Intent intent = new Intent(Clickboard.this, Notice_Board.class );
                                        /*
                                        intent.putExtra("userID", userID);
                                        //intent.putExtra("userPassword", userPassword);
                                        intent.putExtra("userSchool", userSchool);
                                        intent.putExtra("userName", userName);
                                        intent.putExtra("userEmail", userEmail);
                                        intent.putExtra("userGrade", userGrade);*/
                                        finish();
                                        startActivity(intent);
                                    } else {

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        removeRequest removeRequest = new removeRequest(getuserID, userSchool, title, content, Whatboard, time, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Clickboard.this);
                        queue.add(removeRequest);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
    void update(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Clickboard.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.updateboard, null);
        builder.setView(view);

        final EditText newtitle = (EditText) view.findViewById(R.id.EditText_title);
        final EditText newcontent = (EditText) view.findViewById(R.id.EditText_content);
        Button submit = (Button) view.findViewById(R.id.buttonSubmit);
        Button cencel = (Button) view.findViewById(R.id.buttonCanlcel);

        newtitle.setText(title);
        newcontent.setText(content);


        dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title_temp= newtitle.getText().toString();
                final String content_temp = newcontent.getText().toString();
                String comments = "";
                final String Whatboard= "Notice_Board";
                final String hotCount = 0 + "";
                final String hotclickUser = userID +"/";
                String formatDate = time;
                System.out.println(title_temp);
                /*
                // 현재시간을 msec 으로 구한다.
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                // nowDate 변수에 값을 저장한다.
                String formatDate = sdfNow.format(date);
                */
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//게시판 글작성 성공
                                Toast.makeText(getApplicationContext(),"게시판 글을 수정하셨습니다.",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Clickboard.this, Clickboard.class );

                                intent.putExtra("userID", getuserID);
                                intent.putExtra("title",  title_temp);
                                intent.putExtra("content", content_temp);
                                intent.putExtra("time", time);
                                intent.putExtra("hotCount", hotCount );
                                intent.putExtra("Whatboard", Whatboard);
                                intent.putExtra("hotclickUser", hotclickUser);

                                finish();
                                startActivity(intent);
                            }
                            else{ //게시판 글작성 실패
                                Toast.makeText(getApplicationContext(),"게시판 글 수정을 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 Volley를 이용해서 요청을 함.
                UpdateRequest_Board updateRequest_board = new UpdateRequest_Board(userID, userSchool ,title_temp, content_temp, comments,Whatboard, formatDate,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Clickboard.this);
                queue.add(updateRequest_board);
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
    void addComment(){
        final String content_userid = getuserID;
        final String content_userSchool = userSchool;
        final String content_title = title;
        final String content_content = content;
        final String content_Whatboard = Whatboard;
        final String content_time = time;
        final String Myuserid = userID;
        final String commentStr = commentEdiText.getText().toString();

        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        final String formatDate = sdfNow.format(date);

        AlertDialog.Builder builder = new AlertDialog.Builder(Clickboard.this);
        builder.setTitle("댓글란");
        builder.setMessage("댓글을 입력하시겠습니까?.");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//게시판 글작성 성공
                                UpdateCommentCount(1); //댓글갯수 db에 저장
                                Toast.makeText(getApplicationContext(),"댓글입력 성공",Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{ //게시판 글작성 실패
                                Toast.makeText(getApplicationContext(),"댓글입력 실패",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 Volley를 이용해서 요청을 함.
                CommentsRequest_Board commentsRequest_board = new CommentsRequest_Board(content_userid, content_userSchool, content_title, content_content, content_Whatboard, content_time, Myuserid, commentStr, formatDate,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Clickboard.this);
                queue.add(commentsRequest_board);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    void DBgetcomments(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                String content_userid = params[1];
                String content_userSchool = params[2];
                String content_title = params[3];
                String content_content = params[4];
                String content_Whatboard = params[5];
                String content_time = params[6];

                String postParameters = "content_userid=" + content_userid + "&content_userSchool=" + content_userSchool + "&content_title=" + content_title + "&content_content=" + content_content  +"&content_Whatboard=" + content_Whatboard + "&content_time=" + content_time;

                System.out.println(content_userid + " " +content_userSchool+ " "+content_title + " " + content_Whatboard + " "+ content_time + "타임값 어디갔어?");
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
        g.execute(url, getuserID, userSchool, title, content, Whatboard, time);
    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            commentsArray = jsonObj.getJSONArray(TAG_RESPONSE);

            for(int i = 0; i<commentsArray.length(); i++){
                JSONObject c = commentsArray.getJSONObject(i);
                String userid = c.getString(TAG_USERID);
                String comments = c.getString(TAG_COMMENTS);
                String time = c.getString(TAG_TIME);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_USERID, userid);
                persons.put(TAG_COMMENTS, comments);
                persons.put(TAG_TIME, time);
                System.out.println(persons + "persons");
                commentsList.add(persons);
                System.out.println( commentsList+ "commentsList");
            }

            System.out.println(commentsList);
            System.out.println(commentsList.size());

            System.out.println(commentsList);
            for(int i = 0; i < commentsList.size(); i++){
                HashMap<String, String> hashMap = commentsList.get(i);
                adapter.addItem(new commentItem(hashMap.get(TAG_USERID), hashMap.get(TAG_COMMENTS) ,hashMap.get(TAG_TIME)));
            }
            list.setAdapter(adapter);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    void commentUpdate(final String Upuserid, final String Upcomments, final String Uptime){
        AlertDialog.Builder builder = new AlertDialog.Builder(Clickboard.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.updatecomment, null);
        builder.setView(view);

        final EditText newcomment = (EditText) view.findViewById(R.id.EditText_comment);
        Button submit = (Button) view.findViewById(R.id.buttonSubmit);
        Button cencel = (Button) view.findViewById(R.id.buttonCanlcel);

        newcomment.setText(Upcomments);

        dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content_userid = getuserID;
                String content_userSchool = userSchool;
                String content_title = title;
                String content_content = content;
                String content_Whatboard = Whatboard;
                String content_time = time;
                String NewComments = newcomment.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//게시판 글작성 성공
                                Toast.makeText(getApplicationContext(),"댓글을 수정하셨습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{ //게시판 글작성 실패
                                Toast.makeText(getApplicationContext(),"댓글 수정을 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 Volley를 이용해서 요청을 함.
                UpdateRequest_Comment updateRequest_comment = new UpdateRequest_Comment(content_userid, content_userSchool ,content_title, content_content, content_Whatboard,content_time, Upuserid, NewComments, Uptime,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Clickboard.this);
                queue.add(updateRequest_comment);
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
    void commentDelete(final String Upuserid, final String Upcomments, final String Uptime){
        AlertDialog.Builder builder = new AlertDialog.Builder(Clickboard.this);

        builder.setTitle("댓글을 삭제하시겠습니까?");
        //builder.setMessage("");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String content_userid = getuserID;
                        String content_userSchool = userSchool;
                        String content_title = title;
                        String content_content = content;
                        String content_Whatboard = Whatboard;
                        String content_time = time;

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){//게시판 글작성 성공
                                        UpdateCommentCount(-1); //댓글갯수 db에 저장
                                        Toast.makeText(getApplicationContext(),"댓글을 삭제하셨습니다.",Toast.LENGTH_SHORT).show();
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                    else{ //게시판 글작성 실패
                                        Toast.makeText(getApplicationContext(),"댓글 삭제를 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 Volley를 이용해서 요청을 함.
                        DeleteRequest_Comment deleteRequest_comment = new DeleteRequest_Comment(content_userid, content_userSchool ,content_title, content_content, content_Whatboard,content_time, Upuserid, Upcomments, Uptime,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Clickboard.this);
                        queue.add(deleteRequest_comment);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    void withCommentDelete(){ //게시글과 댓글 동시 지움
        String content_userid = getuserID;
        String content_userSchool = userSchool;
        String content_title = title;
        String content_content = content;
        String content_Whatboard = Whatboard;
        String content_time = time;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){//게시판 글작성 성공

                    }
                    else{ //게시판 글작성 실패
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //서버로 Volley를 이용해서 요청을 함.
        DeleteRequest_WithComment deleteRequest_withComment = new DeleteRequest_WithComment(content_userid, content_userSchool ,content_title, content_content, content_Whatboard,content_time,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Clickboard.this);
        queue.add(deleteRequest_withComment);
    }
    void UpdateCommentCount(int Count){
        String Count_userid = getuserID;
        String Count_userSchool = userSchool;
        String Count_title = title;
        String Count_content = content;
        String Count_comment = String.valueOf(commentsList.size() + Count);
        String Count_Whatboard = Whatboard;
        String Count_time = time;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){//댓글 수 카운터 성공
                    }
                    else{ //댓글 수 카운터 실패
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //서버로 Volley를 이용해서 요청을 함.
        UpdateRequest_CommentCount updateRequest_commentCount = new UpdateRequest_CommentCount(Count_userid, Count_userSchool ,Count_title, Count_content,  Count_comment,Count_Whatboard, Count_time,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Clickboard.this);
        queue.add(updateRequest_commentCount);
    }
}
