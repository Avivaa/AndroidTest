package com.example.first;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class rateActivity extends AppCompatActivity {
    EditText rmb;
    TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=findViewById(R.id.rmb);
        show=findViewById(R.id.showout);
    }
    public void onClickRate(View btn){
        String str= rmb.getText().toString();
        float r=0;
        if(str.length()>0) {
             r= Float.parseFloat(str); }
             else Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
        float val=0;
        if(btn.getId()== R.id.btn_dollar){
            val= r*(1/6.5f); }
        if(btn.getId()== R.id.btn_euro){
            val= r*(1/7.5f); }

        if(btn.getId()== R.id.btn_won){
            val= r* 169; }
        show.setText(String.format("%.2f",val));


        }
}

