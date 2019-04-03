package com.example.first;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
TextView scoreA;
TextView scoreB;

protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreA=findViewById(R.id.textout2);
        scoreB=findViewById(R.id.textout2b);

    }

    public void btnAdd1(View btn) {
        if(btn.getId()==R.id.button1){
            showscore(3);
        }else showscore2(3);
}
    public void btnAdd2(View btn) {
        if(btn.getId()==R.id.button2){
            showscore(2);
        }else showscore2(2);
    }
    public void btnAdd3(View btn) {
        if(btn.getId()==R.id.button3){
            showscore(1);
        }else showscore2(3);
    }
    public void btnRes(View btn) {
        if(btn.getId()==R.id.reset) {
            scoreA.setText("0");
            scoreB.setText("0");
        }
    }
public void showscore(int inc){
    Log.i("show","inc="+inc);
    String oldscore= scoreA.getText().toString();
    int newscore=Integer.parseInt(oldscore)+inc;
    scoreA.setText("" + newscore);
}
public void showscore2(int inc){
    Log.i("show","inc="+inc);
    String oldscore= scoreB.getText().toString();
    int newscore=Integer.parseInt(oldscore)+inc;
    scoreB.setText("" + newscore);
}
}
