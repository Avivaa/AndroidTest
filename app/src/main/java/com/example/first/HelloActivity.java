package com.example.first;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HelloActivity extends AppCompatActivity implements View.OnClickListener{
    TextView out;
    EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        out=findViewById(R.id.txtout);
        edit=findViewById(R.id.input);

        Button btn= findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i("main","onClick msg....");
        String str=edit.getText().toString();
        int num=Integer.valueOf(str);
        double tem=num*33.8;
        java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.00");
        String temp = myformat.format(tem);
        out.setText("结果为:"+ temp);
    }
}
