package com.example.raspberry_test.Test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.raspberry_test.R;
import com.example.raspberry_test.SSH.RemoteExecuteCommand;
import com.example.raspberry_test.activity_test02;

public class activaty_Test extends AppCompatActivity {
    private Spinner spin_text;
    private EditText editText;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //spin_text=findViewById(R.id.spin_text);
       // editText.findViewById(R.id.e);
        //button.findViewById(R.id.T);
        Button btn1 = (Button) findViewById(R.id.T);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //SSH("192.168.0.100", "pi", "pi2021", "./Time_motor "+5);
                Intent intent=new Intent(activaty_Test.this, activity_test02.class);
                startActivity(intent);
                //activate_login.this.setResult(RESULT_OK,intent);
                //*结束本Activity*//*
                //MainActivity2.this.finish();
            }
        });

    }
    
    private int  SSH(String ip, String name, String password, String order) {
        RemoteExecuteCommand remoteExecuteCommand = new RemoteExecuteCommand(ip, name, password);
        // List<String> S = new ArrayList<>();
        // S.add(remoteExecuteCommand.execute("./test.sh"));
        remoteExecuteCommand.execute(order);
        return 0;
    }

}