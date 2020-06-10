package com.example.highschoolalltime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import android.widget.Button;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Activity_Home extends Fragment {
    ListView myScheList,timeList;
    String weekDay, myJSON, userId;
    String result = "tv_result";//Edit Text로 입력 받은 데이터를 삽입할 TextView


    private static String TAG = "Activity_Home";//JAVA 파일 명
    private static final String TAG_RESPONSE = "response";//DB 스키마
    private static final String TAG_SUBJECT = "Subject";//DB 스키마
    private static final String TAG_POSITION = "Position";//DB 스키마

    JSONArray peoples = null;//응담으로 인한 데이터 저장 배열
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
        view = inflater.inflate(R.layout.activity__home, container, false);
        //EditText에 현재 입력되어 있는 값을 get해온다.
        Bundle bundle2 = getArguments();
        userId = bundle2.getString("userID");


        //시간표
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime()); //해당요일
        switch (weekDay) {//Switch문을 이용해 문자열과 Layout 내 TextView ID 값들과 비교해 TextView ID값을 정의, <m:월, t:화, w:수, tu:목, f:금>, 문자열 제일 뒤는 수강 시간으로 ID값을 지정
            case "월요일":
                result = result + "_m";
                break;
            case "화요일":
                result = result + "_t";
                break;
            case "수요일":
                result = result + "_w";
                break;
            case "목요일":
                result = result + "_tu";
                break;
            case "금요일":
                result = result + "_f";
                break;
        }
        System.out.println(result);
        personList = new ArrayList<HashMap<String, String>>();
        timeList = view.findViewById(R.id.myTimetable);

        getData("http://highschool.dothome.co.kr/HomegetTimeTable.php", result);
        return view;
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


                personList.add(persons);
            }
            System.out.println(personList);
            System.out.println(personList.size());

            for(int i = 0; i < personList.size(); i++){
                HashMap<String, String> hashMap = personList.get(i);
                String sub = hashMap.get(TAG_SUBJECT);
                String pos = hashMap.get(TAG_POSITION);

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

}

