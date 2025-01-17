package com.example.first;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable {
    Handler handler;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);
        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List","lastRateDateStr=" + logDate);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 3) {
                    List<String> list1 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this, android.R.layout.simple_expandable_list_item_1, list1);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
    }


    @Override
    public void run() {
        List<String> retList = new ArrayList<>();
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run","curDateStr:" + curDateStr + " logDate:" + logDate);
        if(curDateStr.equals(logDate)) {
            //如果相等，则不从网络中获取数据
            Log.i("run", "日期相等，从数据库中获取数据");
            RateManager manager= new RateManager(this);
            for(RateItem item: manager.listAll()){
                retList.add(item.getCurName()+"-->"+item.getCurRate());
            }


        }else{
            //从网络获取数据
             Document doc = null;
             try {
                doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
                Log.i("open", "run:html" + doc.title());
                Elements tables = doc.getElementsByTag("table");
                /*int i =1;
                for(Element table: tables){
                    Log.i("open","run:table["+i+"]="+table);
                   i++;
                }*/
                Element table = tables.get(0);
               //Log.i("open","run:table"+ table);
                Elements tds = doc.getElementsByTag("td");
                List<RateItem> rateList = new ArrayList<RateItem>();
                for (int i = 0; i < tds.size(); i += 6) {
                    Element td1 = tds.get(i);
                    Element td2 = tds.get(i + 5);
                    Log.i("open", "run :" + td1.text() + "==>" + td2.text());
                    retList.add(td1.text() + "==>" + td2.text());
                    rateList.add(new RateItem(td1.text(), td2.text()));
                }
                //把数据写入到数据库中
                RateManager manager= new RateManager(this);
                manager.deleteAll();
                manager.addAll(rateList);

                //更新记录日期
                 SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
                 SharedPreferences.Editor edit = sp.edit();
                 edit.putString(DATE_SP_KEY, curDateStr);
                 edit.commit();
                 Log.i("run","更新日期结束：" + curDateStr);

                 } catch (IOException e) {
               e.printStackTrace();
             }
        }
        Message msg = handler.obtainMessage(3);
        msg.obj = retList;
        handler.sendMessage(msg);
    }
}

