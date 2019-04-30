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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class rateActivity extends AppCompatActivity implements Runnable{

    EditText rmb;
    TextView show;
    Handler handler;


    float dollarRate = 0.0f;
    float euroRate = 0.0f;
    float wonRate = 0.0f;
    String updateDate="";
    String todayStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showout);

        //获取sp里面保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);  //名称对照；可以多个文件
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this); //不可以自己起名字，只可以一个默认文件
        dollarRate=sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate=sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate=sharedPreferences.getFloat("won_rate",0.0f);
        updateDate=sharedPreferences.getString("update_date","");

        //获取当前系统时间
        Date today= Calendar.getInstance().getTime();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd"); //小写m表示分钟
        todayStr= sdf.format(today);


        //log日志
        Log.i("open","onCreate: sp dollarRate="+dollarRate);
        Log.i("open","onCreate: sp euroRate="+euroRate);
        Log.i("open","onCreate: sp wonRate="+wonRate);
        Log.i("open","onCreate: sp updateDate="+ updateDate);

        //判断时间
        if(!todayStr.equals(updateDate)){
            //开启子线程
            Thread t= new Thread(this);
            t.start();

        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    Bundle bdl=(Bundle)msg.obj;
                    dollarRate=bdl.getFloat("dollar-rate");
                    euroRate=bdl.getFloat("euro-rate");
                    wonRate=bdl.getFloat("won-rate");

                    Log.i("handle","handleMessage: dollarRate:" +dollarRate );
                    Log.i("handle","handleMessage: euroRate:" +euroRate );
                    Log.i("handle","handleMessage: wonRate:" +wonRate );

                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit(); //编辑改写都要用editor
                    editor.putString("update_date",todayStr);
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.apply();

                    Toast.makeText(rateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
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
        }else if(item.getItemId()==R.id.open_list){
           Intent list = new Intent(this,RateListActivity.class);
           startActivity(list);
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
            SharedPreferences.Editor editor= sharedPreferences.edit(); //编辑改写都要用editor
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

        //获取bundle用于存取汇率
        Bundle bundle = new Bundle();

        //获取网络数据
       /* URL url= null;
        try {
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http= (HttpURLConnection) url.openConnection();
            InputStream in= http.getInputStream();
            String html = inputStream2String(in);
            Log.i("open","run:html="+ html);
            Document doc =Jsoup.parse(html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i("open","run:html"+ doc.title());
            Elements tables= doc.getElementsByTag("table");
            /*int i =1;
            for(Element table: tables){
                Log.i("open","run:table["+i+"]="+table);
                i++;
            }*/
            Element table =tables.get(0);
            //Log.i("open","run:table"+ table);
            Elements tds= doc.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=6){
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i("open","run :"+td1.text()+"==>"+td2.text());
                String str1=td1.text();
                String val=td2.text();

                if(str1.equals("美元")){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if(str1.equals("欧元")){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                } else if(str1.equals("韩元")) {
                    bundle.putFloat("won-rate", 100f / Float.parseFloat(val));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }//获取汇率存进bundle

        //获取msg对象，用于返回主线程
        Message msg= handler.obtainMessage(3);
        //msg.what= 3;
        msg.obj= bundle;
        handler.sendMessage(msg);//将msg放回队列由Android平台来管理



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

