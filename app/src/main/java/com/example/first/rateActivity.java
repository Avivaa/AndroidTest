package com.example.first;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class rateActivity extends AppCompatActivity implements Runnable{

    EditText rmb;
    TextView show;
    Handler handler;

    float dollarRate = 0.0f;
    float euroRate = 0.0f;
    float wonRate = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showout);

        //获取sp里面保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate=sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate=sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate=sharedPreferences.getFloat("won_rate",0.0f);
        //log日志
        Log.i("open","onCreate: sp dollarRate="+dollarRate);
        Log.i("open","onCreate: sp euroRate="+euroRate);
        Log.i("open","onCreate: sp wonRate="+wonRate);

        //开启子线程
        Thread t= new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    String str=(String)msg.obj;
                    Log.i("open","handlemessage: getMessage msg="+ str);
                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };
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

            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i("open","onActivityResult: 数据已保存到sp");
        }
    }

    @Override
    public void run() {
        Log.i("open","run: run()...");
        for (int i=1;i<6;i++){
            Log.i("open","run: i="+i);
            try{
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message msg= handler.obtainMessage(3);
        //msg.what= 3;
        msg.obj= "hello";
        handler.sendMessage(msg);//将msg放回队列由Android平台来管理

        //获取网络数据
        URL url= null;
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http= (HttpURLConnection) url.openConnection();
            InputStream in= http.getInputStream();
            String html = inputStream2String(in);
            Log.i("open","run:html="+ html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
       private String inputStream2String (InputStream inputStream) throws IOException {

           final int bufferSize = 1024;
           final char[] buffer = new char[bufferSize];
           final StringBuilder out = new StringBuilder();
           Reader in = new InputStreamReader(inputStream, "gb2312");
           for (; ; ) {
               int rsz = in.read(buffer, 0, buffer.length);
               if (rsz < 0)
                   break;
               out.append(buffer, 0, rsz);
           }
           return out.toString();


       }
}

