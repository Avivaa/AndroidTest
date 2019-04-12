package com.example.first;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class rateActivity extends AppCompatActivity {
    EditText rmb;
    TextView show;
    float dollarRate=1/6.5f;
    float euroRate=1/7.5f;
    float wonRate=169;
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
            val= r*dollarRate; }
        if(btn.getId()== R.id.btn_euro){
            val= r*euroRate; }

        if(btn.getId()== R.id.btn_won){
            val= r* wonRate; }
        show.setText(String.format("%.2f",val));

        }
        public void openOne(View btn){
            Intent config = new Intent(this,ConfigActivity.class);//打开其他工程文件
            //Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));//打开网页
            //Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:87981234"));//拨号界面
            config.putExtra("dollar_rate_key",dollarRate);
            config.putExtra("euro_rate_key",euroRate);
            config.putExtra("won_rate_key",wonRate);

            Log.i("open","openOne: dollarRate="+ dollarRate);
            Log.i("open","openOne: euroRate="+ euroRate);
            Log.i("open","openOne: wonRate="+ wonRate);

            startActivityForResult(config,1);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId()==R.id.menu_set){
           Intent config = new Intent(this,ConfigActivity.class);//打开其他工程文件

           config.putExtra("dollar_rate_key",dollarRate);
           config.putExtra("euro_rate_key",euroRate);
           config.putExtra("won_rate_key",wonRate);

           Log.i("open","openOne: dollarRate="+ dollarRate);
           Log.i("open","openOne: euroRate="+ euroRate);
           Log.i("open","openOne: wonRate="+ wonRate);

           startActivityForResult(config,1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==2){
            Bundle bundle=data.getExtras();
            dollarRate= bundle.getFloat("key_dollar",0.0f);
            euroRate= bundle.getFloat("key_euro",0.0f);
            wonRate= bundle.getFloat("key_won",0.0f);
        }
    }
}

