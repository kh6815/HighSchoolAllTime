package com.example.highschoolalltime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Notice_Board extends AppCompatActivity {

    private ListView list;
    Button btn;
    List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        list = (ListView)findViewById(R.id.notice_board_list);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        btn = (Button) findViewById(R.id.Addbutton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(Notice_Board.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("글쓰기");

                final EditText et = new EditText(Notice_Board.this);
                ad.setView(et);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //확인 버튼을 누르면 제목과 내용을 데이터베이스에 저장하고 게시판을 새로고침!
                        String result = et.getText().toString();
                        data.add(result);
                        dialog.dismiss();
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //최소버튼을 누르면 그냥 원상복귀
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });


        /*
        data.add("홍드로이드");
        data.add("안드로이드");
        data.add("사과");
        adapter.notifyDataSetChanged();
        */

    }
}
