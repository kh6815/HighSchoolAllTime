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
    String title, content, time, hotCount,userID, getuserID; //게시글의 정보 변수 선언
    String userSchool, Whatboard, hotclickUser;// 유저학교, 보드이름, 추천버튼누른 유저 문자열 선언
    TextView textView1, textView2, timeTextview, hotCountTextview; //게시글의 텍스트 뷰 선언
    EditText commentEdiText; //댓글작성 edittext선언
    Button hotbutton, removebutton, updatabutton, commentbutton; //게시글의 버튼 선언
    String hotCount_temp; //추천 카운터 예비 변수 선언
    int check = 0, deletetemp = 0; // check 확인 변수 선언
    String[] array; //문자열 배열 선언
    String userName,userGrade, userEmail; // 유저 정보 변수 선언
    private AlertDialog dialog; //다이얼로그 생성 변수

    private ListView list; //리스트 선언
    commentAdapter adapter; //어탭터 선언

    private static String TAG = "Clickboard"; //보드이름 태그 변수 선언
    String myJSON; //db값 결과값 변수 선언
    JSONArray commentsArray = null; //db JSON배열 변수 선언
    ArrayList<HashMap<String, String>> commentsList; //댓글 리스트 변수 선언

    private View commentView;
    Button commentupdateBtn, commentdeleteBtn;

    private static final String TAG_RESPONSE = "response"; //응답 태그 변수 선언
    private static final String TAG_USERID = "userid"; //유저 아이디 태그 변수 선언
    private static final String TAG_COMMENTS = "comments"; // 댓글 태그 변수 선언
    private static final String TAG_TIME = "time"; //시간 태그 변수 선언
    @Override
    public void onBackPressed() { //뒤로가기 버튼 클릭시 이벤트
        super.onBackPressed();
        //이전 창의 게시판으로 이동
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

        check = 0; //확인값 false로 선언
        //xml 아이템 링크
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

        Intent intent = getIntent(); //인텐트 생성

        //유저 정보를 use_user클래스에서 불러옴
        userID = ((use_user)this.getApplication()).getUserID(); // 해당하는 나의 아이디
        getuserID = intent.getExtras().getString("userID"); // 게시물의 유저 아이디
        userName = ((use_user)this.getApplication()).getUserName();
        userGrade = ((use_user)this.getApplication()).getUserGrade();
        userEmail = ((use_user)this.getApplication()).getUserEmail();

        //아이디에 따라 버튼모습 지정
        if(userID.equals(getuserID)){ //같은 아이디일떈 보여줌
            updatabutton.setVisibility(View.VISIBLE);
            removebutton.setVisibility(View.VISIBLE);
        }
        else{//다른아이디일떈 안보여줌
            updatabutton.setVisibility(View.INVISIBLE);
            removebutton.setVisibility(View.INVISIBLE);
        }

        //게시글 넘어온 인텐트값으로 변수 저장
        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        time = intent.getExtras().getString("time");
        hotCount = intent.getExtras().getString("hotCount");
        Whatboard = intent.getExtras().getString("Whatboard");
        userSchool = ((use_user)this.getApplication()).getUserSchool();
        hotclickUser = intent.getExtras().getString("hotclickUser");

        //textview에 게시글 값 저장
        textView1.setText("제목 : " + title);
        textView2.setText(content);
        timeTextview.setText(time);
        hotCountTextview.setText(hotCount);

        adapter = new commentAdapter(); //어탭터 생성
        commentsList = new ArrayList<HashMap<String, String>>(); //댓글 리스트 생성
        DBgetcomments("http://wkwjsrjekffk.dothome.co.kr/Comments_Get.php"); //db에서 값 불러오기

        //추천버튼 클릭 이벤트
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
                                String hotclick= jsonObject.getString("hotclickUser"); //추천을 누른  유저데이터 가져옴
                                System.out.println(hotclick + "클릭유저"); //출력확인
                                array = hotclick.split("/"); // /로 유저 데이터 나눔

                                //추천을 누른 유저인지 판별
                                for(int i =0 ; i < array.length; i++){
                                    if(array[i].equals(userID)){
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
                //volley를 사용해 서버로 요청
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
        //게시판 업데이트
        updatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        //댓글 추가버튼
        commentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });

        //댓글 리스트 아이템 클릭이벤트
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT).show();
                commentItem item = (commentItem) parent.getItemAtPosition(position); //클릭한 리스트 아이템 위치불러옴.

                final String userIDStr = item.getUserID(); // 해당 댓글 아이디
                final String commentsStr = item.getComments() ;// 댓글 내용 저장
                final String timeStr = item.getTime();  //댓글 시간 저장

                //자신의 아이디 일때 if 실행
                if(userIDStr.equals(userID)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Clickboard.this);

                    builder.setTitle("해당 항목을 선택하십시오");
                    builder.setMessage("댓글 수정 or 삭제");
                    builder.setPositiveButton("수정", //댓글 수정
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    commentUpdate(userIDStr, commentsStr, timeStr);
                                }
                            });
                    builder.setNegativeButton("삭제", //댓글 삭제
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
    //댓글 리스트에 등록할 어댑터 클래스
    class commentAdapter extends BaseAdapter {
        private ArrayList<commentItem> items = new ArrayList<>();

        public void addItem(commentItem item){items.add(item);}//생성자로 item을 등록

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
            commentItemView view = new commentItemView(getApplicationContext());//댓글 아이템데이터를 저장할 리스트아이템뷰 생성
            commentItem item = items.get(position);//아이템의 위치값 불러옴.
            view.setContentuserID(getuserID); //게시물 글쓴이아이디 가져와 저장
            view.setuserID(item.getUserID()); // 해당 댓글 아이디 가져와 저장
            view.setComments(item.getComments());//댓글 내용 가져와 저장
            view.setTime(item.getTime()); // 댓글 시간 가져와 저장
            view.Newuser(userID); //나의 아이디 저장.
            return view;
        }
    }

    void Redundant_prevention() { //추천 중복방지 매세드 -> 서버로 카운터값과 아이디값을 전송
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
        //volley를 사용해 서버로 요청
        updatehotclickRequest updatehotclickRequest=new updatehotclickRequest(userSchool, title, content, Whatboard,hotCount_temp ,hotclickUser,responseListener);
        RequestQueue queue= Volley.newRequestQueue(Clickboard.this );
        queue.add(updatehotclickRequest);
    }
    //게시글 삭제 메서드
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
                                        withCommentDelete(); //댓글 삭제 메서드로 이동
                                        onBackPressed(); //게시글 삭제후 자동 뒤로가기
                                    } else {

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //volley를 사용해 서버로 요청
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
    //게시글 업데이트
    void update(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Clickboard.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.updateboard, null); //xml링크
        builder.setView(view);

        //xml아이템 연동
        final EditText newtitle = (EditText) view.findViewById(R.id.EditText_title);
        final EditText newcontent = (EditText) view.findViewById(R.id.EditText_content);
        Button submit = (Button) view.findViewById(R.id.buttonSubmit);
        Button cencel = (Button) view.findViewById(R.id.buttonCanlcel);

        //이전값 셋팅
        newtitle.setText(title);
        newcontent.setText(content);


        dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //새로운 값 가져와 저장
                final String title_temp= newtitle.getText().toString();
                final String content_temp = newcontent.getText().toString();
                String comments = "";
                final String Whatboard= "Notice_Board";
                final String hotCount = 0 + "";
                final String hotclickUser = userID +"/";
                String formatDate = time;
                System.out.println(title_temp);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//게시판 글수정 성공
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
    //댓글 추가 메서드
    void addComment(){
        //유저 정보 변수값 지정
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
                            if(success){//댓글작성 성공
                                UpdateCommentCount(1); //댓글갯수 db에 저장
                                Toast.makeText(getApplicationContext(),"댓글입력 성공",Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{ //댓글작성 실패
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
    //db에서 게시글에 해당하는 댓글 가져오는 메서드
    void DBgetcomments(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                //넘겨받은 파라미터 값
                String uri = params[0];
                String content_userid = params[1];
                String content_userSchool = params[2];
                String content_title = params[3];
                String content_content = params[4];
                String content_Whatboard = params[5];
                String content_time = params[6];

                //php에 넘길 데이터 문자열 값
                String postParameters = "content_userid=" + content_userid + "&content_userSchool=" + content_userSchool + "&content_title=" + content_title + "&content_content=" + content_content  +"&content_Whatboard=" + content_Whatboard + "&content_time=" + content_time;

                System.out.println(content_userid + " " +content_userSchool+ " "+content_title + " " + content_Whatboard + " "+ content_time + "타임값 어디갔어?");
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();//url 연결


                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("POST"); //post형식
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
                myJSON = result; //결과값 저장
                showList(); // 결과값 리스트에 저장하기 위한 메서드로 이동
            }
        }
        GetDataJSON g = new GetDataJSON();//JSON형식으로 데이터 가져옴
        g.execute(url, getuserID, userSchool, title, content, Whatboard, time); //게시글 정보를 파라미터로 넘김
    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);//myJSON에 저장된 db 데이터값을 가져오기 위한 변수 생성
            commentsArray = jsonObj.getJSONArray(TAG_RESPONSE);//db에서 넘어온값을 peoples의 리스트에 저장

            for(int i = 0; i<commentsArray.length(); i++){//반복문을 이용하여 댓글리스트에 삽입
                JSONObject c = commentsArray.getJSONObject(i);
                //댓글 정보 저장
                String userid = c.getString(TAG_USERID);
                String comments = c.getString(TAG_COMMENTS);
                String time = c.getString(TAG_TIME);

                HashMap<String, String> persons = new HashMap<String, String>();//해쉬맵 배열을 리스트에 저장

                //키워드 형식으로 댓글 정보 저장
                persons.put(TAG_USERID, userid);
                persons.put(TAG_COMMENTS, comments);
                persons.put(TAG_TIME, time);
                commentsList.add(persons);
            }

            System.out.println(commentsList);//출력값 확인
            System.out.println(commentsList.size()); //리스트 사이즈값 확인

            for(int i = 0; i < commentsList.size(); i++){ //댓글 리스트의 크기많큼 어탭터의 item저장
                HashMap<String, String> hashMap = commentsList.get(i);//각 배열의 값을 해쉬맵으로 불러오기
                adapter.addItem(new commentItem(hashMap.get(TAG_USERID), hashMap.get(TAG_COMMENTS) ,hashMap.get(TAG_TIME)));//어탭터에 저장
            }
            list.setAdapter(adapter);//댓글 리스트에 어댑터 저장

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    //댓글 수정메서드
    void commentUpdate(final String Upuserid, final String Upcomments, final String Uptime){
        AlertDialog.Builder builder = new AlertDialog.Builder(Clickboard.this);
        LayoutInflater inflater = getLayoutInflater();
        //xml링크
        View view = inflater.inflate(R.layout.updatecomment, null);
        builder.setView(view);

        final EditText newcomment = (EditText) view.findViewById(R.id.EditText_comment);
        Button submit = (Button) view.findViewById(R.id.buttonSubmit);
        Button cencel = (Button) view.findViewById(R.id.buttonCanlcel);

        //기존 댓글 세팅
        newcomment.setText(Upcomments);

        dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //댓글에 대한 게시글 정보 저장
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
    //댓글 삭제 메서드
    void commentDelete(final String Upuserid, final String Upcomments, final String Uptime){
        AlertDialog.Builder builder = new AlertDialog.Builder(Clickboard.this);

        builder.setTitle("댓글을 삭제하시겠습니까?");
        //builder.setMessage("");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //댓글에 대한 게시글 정보 저장
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

    //게시글이 지워졌을 시 댓글도 같이 삭제하는 메서드
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
                    if(success){

                    }
                    else{
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
    //추천 갯수 업데이트메서드
    void UpdateCommentCount(int Count){
        //게시글 정보 저장
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
