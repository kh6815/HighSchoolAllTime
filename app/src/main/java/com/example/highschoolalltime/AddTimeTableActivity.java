package com.example.highschoolalltime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AddTimeTableActivity extends Fragment {

    private Button add, del;//추가버튼
    String result[] = {"m", "tu", "w", "t", "f"};//String을 이용해 데이터 저장 할 위치값 찾을 시 추가할 문자열
    String[] re_day, re_time;//Spiner값을 비교할 배열
    String sub, text, sub_text, myJSON, userId;//TextView ID값과 입력한 데이터를 갖을 임시 변수
    TextView tv_result;//Edit Text로 입력 받은 데이터를 삽입할 TextView

    private static String TAG = "AddTimeTableActivity";//JAVA 파일 명
    private static final String TAG_RESPONSE = "response";//DB 스키마
    private static final String TAG_SUBJECT = "Subject";//DB 스키마
    private static final String TAG_POSITION = "Position";//DB 스키마

    JSONArray peoples = null;//응답으로 인한 데이터 저장 배열
    ArrayList<HashMap<String, String>> personList;//HashMap 배열
    List<String> data = new ArrayList<>();//데이터 저장 리스트

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_time_table, container, false);


        Bundle bundle1 = getArguments();//MainFrame에서 받은 bundle1값을 가져온다.
        userId = bundle1.getString("userID");//bundle1에 저장된 유저아이디값 저장

        add = view.findViewById(R.id.btn_add);//버튼 아이디 값 불러오기
        del = view.findViewById(R.id.btn_del);
        re_day = getResources().getStringArray(R.array.day);//Spiner의 값을 비교할 변수 불러오기
        re_time = getResources().getStringArray(R.array.time);//Spiner의 값을 비교할 변수 불러오기

        personList = new ArrayList<HashMap<String, String>>();

        add.setOnClickListener(new View.OnClickListener() {//버튼 클릭 이벤트
            @Override
            public void onClick(View v) {
                show();//데이터 입력 화면으로 전환하는 함수
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());//데이터를 입력받을 화면 다이얼로그로 띄우기 위한 Builder
                LayoutInflater inflater = getLayoutInflater();//다이얼로그 내 띄울 Layout 불러오기
                View view = inflater.inflate(R.layout.dialog_delete, null);
                ad.setView(view);//불러온 Layout을 다이얼로그에 띄우기
                final Button submit = (Button) view.findViewById(R.id.del_buttonSubmit);//다이얼로그 내 버튼 ID정의(Posivive<확인> 버튼)
                final Button done = (Button) view.findViewById(R.id.del_buttonDone);//다이얼로그 내 버튼 ID정의(Negative<취소> 버튼)

                final Spinner day = (Spinner) view.findViewById(R.id.del_edittextDay);//요일 입력받을 Spiner ID 정의
                final Spinner time = (Spinner) view.findViewById(R.id.del_edittextTime);//수업시간 입력받을 Spiner ID 정의

                final AlertDialog dialog = ad.create();//다이얼로그 생성
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sub = "tv_result_";//TextView ID값을 String으로 비교하기 위한 임시 문자열 변수 초기화

                        text = day.getSelectedItem().toString();//Sriner로 입력받은 요일 임시 저장
                        for (int i = 0; i < re_day.length; i++) {
                            if (text.equals(re_day[i])) {
                                sub = sub + result[i];
                            }
                        }
                        //for문을 이용해 반복적으로 임시 저장한 요일 값을 요일 배열과 비교해 참인 경우 초기화 했던 문자열 뒤에 추가
                        text = time.getSelectedItem().toString();//Sriner로 입력받은 수강시간 임시 저장
                        for (int i = 0; i < re_time.length; i++) {
                            if (text.equals(re_time[i])) {
                                sub = sub + (i + 1);
                            }
                        }//for문을 이용해 반복적으로 임시 저장한 수강시간 값을 요일 배열과 비교해 참인 경우 문자열 뒤에 추가

                        delDB(sub, userId);
                        dialog.dismiss();
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        getData("http://highschool.dothome.co.kr/getTimeTable.php"); //시간표 데이터를 가져오기위한 메서드
        return view;
    }

    //DB 요청해 삭제
    void delDB(String sub, String userId){
        Response.Listener<String> reponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(getContext(), "수업을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(AddTimeTableActivity.this).attach(AddTimeTableActivity.this).commit();
                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };//Delete_Time.php 호출
        Delete_Time delete_time = new Delete_Time(sub, userId, reponseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(delete_time);
    }
    //시간표에 db 데이터를 저장하기 위한 메서드
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);//myJSON에 저장된 db 데이터값을 가져오기 위한 변수 생성
            peoples = jsonObj.getJSONArray(TAG_RESPONSE);//db에서 넘어온값을 peoples의 리스트에 저장

            for(int i = 0; i<peoples.length(); i++){//반복문을 이용하여 리스트에 삽입
                JSONObject c = peoples.getJSONObject(i);
                String subject = c.getString(TAG_SUBJECT);//과목값을 저장하는 변수
                String position = c.getString(TAG_POSITION);//위치(교시)를 저장하는 변수

                HashMap<String, String> persons = new HashMap<String, String>();//해쉬맵 형태의 배열을 생성

                persons.put(TAG_SUBJECT, subject); //키워드 형식으로 과목 저장
                persons.put(TAG_POSITION, position);//키워드 형식으로 위치(교시) 저장


                personList.add(persons);//해쉬맵 배열을 리스트에 저장
            }
            System.out.println(personList);//출력값 확인
            System.out.println(personList.size());//리스트 사이즈값 확인

            //Collections.reverse(personList);
            for(int i = 0; i < personList.size(); i++){
                HashMap<String, String> hashMap = personList.get(i);//각 배열의 값을 해쉬맵으로 불러오기
                String sub = hashMap.get(TAG_SUBJECT);
                String pos = hashMap.get(TAG_POSITION);
                //Switch문을 이용해 문자열과 Layout 내 TextView ID 값들과 비교해 TextView ID값을 정의
                    // <m:월, t:화, w:수, tu:목, f:금>, 문자열 제일 뒤는 수강 시간으로 ID값을 지정
                switch (pos) {
                    case "tv_result_m1":
                        tv_result = view.findViewById(R.id.tv_result_m1);
                        break;
                    case "tv_result_m2":
                        tv_result = view.findViewById(R.id.tv_result_m2);
                        break;
                    case "tv_result_m3":
                        tv_result = view.findViewById(R.id.tv_result_m3);
                        break;
                    case "tv_result_m4":
                        tv_result = view.findViewById(R.id.tv_result_m4);
                        break;
                    case "tv_result_m5":
                        tv_result = view.findViewById(R.id.tv_result_m5);
                        break;
                    case "tv_result_m6":
                        tv_result = view.findViewById(R.id.tv_result_m6);
                        break;
                    case "tv_result_m7":
                        tv_result = view.findViewById(R.id.tv_result_m7);
                        break;

                    case "tv_result_tu1":
                        tv_result = view.findViewById(R.id.tv_result_tu1);
                        break;
                    case "tv_result_tu2":
                        tv_result = view.findViewById(R.id.tv_result_tu2);
                        break;
                    case "tv_result_tu3":
                        tv_result = view.findViewById(R.id.tv_result_tu3);
                        break;
                    case "tv_result_tu4":
                        tv_result = view.findViewById(R.id.tv_result_tu4);
                        break;
                    case "tv_result_tu5":
                        tv_result = view.findViewById(R.id.tv_result_tu5);
                        break;
                    case "tv_result_tu6":
                        tv_result = view.findViewById(R.id.tv_result_tu6);
                        break;
                    case "tv_result_tu7":
                        tv_result = view.findViewById(R.id.tv_result_tu7);
                        break;

                    case "tv_result_w1":
                        tv_result = view.findViewById(R.id.tv_result_w1);
                        break;
                    case "tv_result_w2":
                        tv_result = view.findViewById(R.id.tv_result_w2);
                        break;
                    case "tv_result_w3":
                        tv_result = view.findViewById(R.id.tv_result_w3);
                        break;
                    case "tv_result_w4":
                        tv_result = view.findViewById(R.id.tv_result_w4);
                        break;
                    case "tv_result_w5":
                        tv_result = view.findViewById(R.id.tv_result_w5);
                        break;
                    case "tv_result_w6":
                        tv_result = view.findViewById(R.id.tv_result_w6);
                        break;
                    case "tv_result_w7":
                        tv_result = view.findViewById(R.id.tv_result_w7);
                        break;

                    case "tv_result_t1":
                        tv_result = view.findViewById(R.id.tv_result_t1);
                        break;
                    case "tv_result_t2":
                        tv_result = view.findViewById(R.id.tv_result_t2);
                        break;
                    case "tv_result_t3":
                        tv_result = view.findViewById(R.id.tv_result_t3);
                        break;
                    case "tv_result_t4":
                        tv_result = view.findViewById(R.id.tv_result_t4);
                        break;
                    case "tv_result_t5":
                        tv_result = view.findViewById(R.id.tv_result_t5);
                        break;
                    case "tv_result_t6":
                        tv_result = view.findViewById(R.id.tv_result_t6);
                        break;
                    case "tv_result_t7":
                        tv_result = view.findViewById(R.id.tv_result_t7);
                        break;

                    case "tv_result_f1":
                        tv_result = view.findViewById(R.id.tv_result_f1);
                        break;
                    case "tv_result_f2":
                        tv_result = view.findViewById(R.id.tv_result_f2);
                        break;
                    case "tv_result_f3":
                        tv_result = view.findViewById(R.id.tv_result_f3);
                        break;
                    case "tv_result_f4":
                        tv_result = view.findViewById(R.id.tv_result_f4);
                        break;
                    case "tv_result_f5":
                        tv_result = view.findViewById(R.id.tv_result_f5);
                        break;
                    case "tv_result_f6":
                        tv_result = view.findViewById(R.id.tv_result_f6);
                        break;
                    case "tv_result_f7":
                        tv_result = view.findViewById(R.id.tv_result_f7);
                        break;
                }
                tv_result.setText(sub);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    void getData(String url) {//db php 주소 받음
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];//0번째 파라미터의 주소값 저장
                String userID = params[1];//1번째 파라미터의 유저아이디값 저장
                String postParameters = "userID=" + userID;


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
        GetDataJSON g = new GetDataJSON(); //JSON형식으로 데이터 가져옴
        g.execute(url, userId); //url,유저아이디넘김
    }

    void show() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());//데이터를 입력받을 화면 다이얼로그로 띄우기 위한 Builder
        LayoutInflater inflater = getLayoutInflater();//다이얼로그 내 띄울 Layout 불러오기
        View view2 = inflater.inflate(R.layout.dialog_add, null);
        ad.setView(view2);//불러온 Layout을 다이얼로그에 띄우기
        final Button submit = (Button) view2.findViewById(R.id.buttonSubmit);//다이얼로그 내 버튼 ID정의(Posivive<확인> 버튼)
        final Button done = (Button) view2.findViewById(R.id.buttonDone);//다이얼로그 내 버튼 ID정의(Negative<취소> 버튼)
        final EditText subject = (EditText) view2.findViewById(R.id.edittextSubject);//과목명을 입력받을 Edit Text ID 정의
        final Spinner day = (Spinner) view2.findViewById(R.id.edittextDay);//요일 입력받을 Spiner ID 정의
        final Spinner time = (Spinner) view2.findViewById(R.id.edittextTime);//수업시간 입력받을 Spiner ID 정의

        final AlertDialog dialog = ad.create();//다이얼로그 생성
        submit.setOnClickListener(new View.OnClickListener() {//Positive(확인) 버튼
            @Override
            public void onClick(View v) {
                sub_text = subject.getText().toString();//Edit Text 임시 저장 변수

                sub = "tv_result_";//TextView ID값을 String으로 비교하기 위한 임시 문자열 변수 초기화

                text = day.getSelectedItem().toString();//Sriner로 입력받은 요일 임시 저장
                for (int i = 0; i < re_day.length; i++) {
                    if (text.equals(re_day[i])) {
                        sub = sub + result[i];
                    }
                }
                //for문을 이용해 반복적으로 임시 저장한 요일 값을 요일 배열과 비교해 참인 경우 초기화 했던 문자열 뒤에 추가
                text = time.getSelectedItem().toString();//Sriner로 입력받은 수강시간 임시 저장
                for (int i = 0; i < re_time.length; i++) {
                    if (text.equals(re_time[i])) {
                        sub = sub + (i + 1);
                    }
                }//for문을 이용해 반복적으로 임시 저장한 수강시간 값을 요일 배열과 비교해 참인 경우 문자열 뒤에 추가
                //Switch문을 이용해 문자열과 Layout 내 TextView ID 값들과 비교해 TextView ID값을 정의
                    // <m:월, t:화, w:수, tu:목, f:금>, 문자열 제일 뒤는 수강 시간으로 ID값을 지정
                switch (sub) {
                    case "tv_result_m1":
                        tv_result = view.findViewById(R.id.tv_result_m1);
                        break;
                    case "tv_result_m2":
                        tv_result = view.findViewById(R.id.tv_result_m2);
                        break;
                    case "tv_result_m3":
                        tv_result = view.findViewById(R.id.tv_result_m3);
                        break;
                    case "tv_result_m4":
                        tv_result = view.findViewById(R.id.tv_result_m4);
                        break;
                    case "tv_result_m5":
                        tv_result = view.findViewById(R.id.tv_result_m5);
                        break;
                    case "tv_result_m6":
                        tv_result = view.findViewById(R.id.tv_result_m6);
                        break;
                    case "tv_result_m7":
                        tv_result = view.findViewById(R.id.tv_result_m7);
                        break;

                    case "tv_result_tu1":
                        tv_result = view.findViewById(R.id.tv_result_tu1);
                        break;
                    case "tv_result_tu2":
                        tv_result = view.findViewById(R.id.tv_result_tu2);
                        break;
                    case "tv_result_tu3":
                        tv_result = view.findViewById(R.id.tv_result_tu3);
                        break;
                    case "tv_result_tu4":
                        tv_result = view.findViewById(R.id.tv_result_tu4);
                        break;
                    case "tv_result_tu5":
                        tv_result = view.findViewById(R.id.tv_result_tu5);
                        break;
                    case "tv_result_tu6":
                        tv_result = view.findViewById(R.id.tv_result_tu6);
                        break;
                    case "tv_result_tu7":
                        tv_result = view.findViewById(R.id.tv_result_tu7);
                        break;

                    case "tv_result_w1":
                        tv_result = view.findViewById(R.id.tv_result_w1);
                        break;
                    case "tv_result_w2":
                        tv_result = view.findViewById(R.id.tv_result_w2);
                        break;
                    case "tv_result_w3":
                        tv_result = view.findViewById(R.id.tv_result_w3);
                        break;
                    case "tv_result_w4":
                        tv_result = view.findViewById(R.id.tv_result_w4);
                        break;
                    case "tv_result_w5":
                        tv_result = view.findViewById(R.id.tv_result_w5);
                        break;
                    case "tv_result_w6":
                        tv_result = view.findViewById(R.id.tv_result_w6);
                        break;
                    case "tv_result_w7":
                        tv_result = view.findViewById(R.id.tv_result_w7);
                        break;

                    case "tv_result_t1":
                        tv_result = view.findViewById(R.id.tv_result_t1);
                        break;
                    case "tv_result_t2":
                        tv_result = view.findViewById(R.id.tv_result_t2);
                        break;
                    case "tv_result_t3":
                        tv_result = view.findViewById(R.id.tv_result_t3);
                        break;
                    case "tv_result_t4":
                        tv_result = view.findViewById(R.id.tv_result_t4);
                        break;
                    case "tv_result_t5":
                        tv_result = view.findViewById(R.id.tv_result_t5);
                        break;
                    case "tv_result_t6":
                        tv_result = view.findViewById(R.id.tv_result_t6);
                        break;
                    case "tv_result_t7":
                        tv_result = view.findViewById(R.id.tv_result_t7);
                        break;

                    case "tv_result_f1":
                        tv_result = view.findViewById(R.id.tv_result_f1);
                        break;
                    case "tv_result_f2":
                        tv_result = view.findViewById(R.id.tv_result_f2);
                        break;
                    case "tv_result_f3":
                        tv_result = view.findViewById(R.id.tv_result_f3);
                        break;
                    case "tv_result_f4":
                        tv_result = view.findViewById(R.id.tv_result_f4);
                        break;
                    case "tv_result_f5":
                        tv_result = view.findViewById(R.id.tv_result_f5);
                        break;
                    case "tv_result_f6":
                        tv_result = view.findViewById(R.id.tv_result_f6);
                        break;
                    case "tv_result_f7":
                        tv_result = view.findViewById(R.id.tv_result_f7);
                        break;
                }

                //DB요청
                Response.Listener<String> reponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Re_Time();
                            } else {
                                Add_Time();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                };
                //TimeTable_Request.php 호출
                TimeTable_Request timetable_request = new TimeTable_Request(sub, userId, reponseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(timetable_request);

                dialog.dismiss();
            }
            //DB수정
            private void Re_Time() {
                Response.Listener<String> reponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getContext(), "수업을 수정했습니다.",
                                        Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(AddTimeTableActivity.this).attach(AddTimeTableActivity.this).commit();
                            } else {
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //Update_Time.php 호출
                Update_Time update_time = new Update_Time(sub_text, sub, userId, reponseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(update_time);
            }
            //DB추가
            private void Add_Time() {
                Response.Listener<String> reponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getContext(), "수업을 추가했습니다.", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(AddTimeTableActivity.this).attach(AddTimeTableActivity.this).commit();
                            } else {
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //Register_Time.php 호출
                Register_Time register_time = new Register_Time(sub_text, sub, userId, reponseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(register_time);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {//Negative<취소> 버튼
            @Override
            public void onClick(View v) {
                dialog.dismiss();//<취소> 버튼을 클릭 시 값을 입력 받았음에도 아무런 동작 없이 다이얼로그 종료
            }
        });
        dialog.show();//다이얼로그 화면에 띄우는 함수
    }


}