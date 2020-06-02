package com.example.highschoolalltime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Noticeboard extends Fragment {
    private View view;
    String userID;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_noticeboard, container, false);//메인화면에 userid를 계속 띄우주기 위함.

        Button Notice_Button = view.findViewById(R.id.Notice_Button);
        Button FreeBoard_Button = view.findViewById(R.id.FreeBoard_Button);
        Button HotBoard_Button = view.findViewById(R.id.HotBoard_Button);


        Notice_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Notice_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        FreeBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Free_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        HotBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Hot_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });

        return view;
    }
}
