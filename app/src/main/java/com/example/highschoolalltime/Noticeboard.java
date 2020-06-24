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

        //xml연결
        view = inflater.inflate(R.layout.activity_noticeboard, container, false);//메인화면에 userid를 계속 띄우주기 위함.

        Button Notice_Button = view.findViewById(R.id.Notice_Button);
        Button FreeBoard_Button = view.findViewById(R.id.FreeBoard_Button);
        Button HotBoard_Button = view.findViewById(R.id.HotBoard_Button);
        Button SecretBoard_Button = view.findViewById(R.id.SecretBoard_Button);
        Button GraduationStudentBoard_Button = view.findViewById(R.id.GraduationStudentBoard_Button);
        Button LoveBoard_Button = view.findViewById(R.id.LoveBoard_Button);
        Button ConfessionBoard_Button = view.findViewById(R.id.ConfessionBoard_Button);
        Button MyclassBoard_Button = view.findViewById(R.id.MyclassBoard_Button);
        Button UsedTransactionBoard_Button = view.findViewById(R.id.UsedTransactionBoard_Button);
        Button ClubBoard_Button = view.findViewById(R.id.ClubBoard_Button);
        Button ContestBoard_Button = view.findViewById(R.id.ContestBoard_Button);
        Button VolunteerBoard_Button = view.findViewById(R.id.VolunteerBoard_Button);
        Button StudyBoard_Button = view.findViewById(R.id.StudyBoard_Button);

        //게시판 버튼 이벤트
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
        SecretBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Secret_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        GraduationStudentBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GraduationStudent_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        LoveBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Love_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        ConfessionBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Confession_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        MyclassBoard_Button .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Myclass_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        UsedTransactionBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UsedTransaction_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        ClubBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Club_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        ContestBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Contest_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        VolunteerBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Volunteer_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });
        StudyBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Study_Board.class);
                startActivity(intent); //액티비티 이동
            }
        });

        return view;
    }
}
