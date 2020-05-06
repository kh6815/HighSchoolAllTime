package com.example.highschoolalltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Find_IDPW_login extends AppCompatActivity {

    Button FindPWbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__i_d_p_w_login);

        FindPWbutton = findViewById(R.id.FindPWbutton);
        FindPWbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Find_IDPW_login.this, SetPW_login.class);
                startActivity(intent);
            }
        });
    }
}
