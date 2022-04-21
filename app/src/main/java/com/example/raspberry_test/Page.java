package com.example.raspberry_test;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.raspberry_test.SSH.RemoteExecuteCommand;

public class Page extends AppCompatActivity {
private Button loge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_page);

        initView();
        loge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    //初始化
    private void initView() {
    loge=findViewById(R.id.loge);

    }



}