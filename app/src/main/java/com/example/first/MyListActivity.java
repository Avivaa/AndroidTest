package com.example.first;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements Runnable{
Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        final ListView listView= findViewById(R.id.mylist);

        Thread t= new Thread();
        t.start();

        handler=new Handler(){
        public void handleMessage(Message msg) {
            if (msg.what == 3) {
                List<String> list1 = (List<String>) msg.obj;
                ListAdapter adapter = new ArrayAdapter<String>(MyListActivity.this, android.R.layout.simple_expandable_list_item_1, list1);
                listView.setAdapter(adapter);

            }
            super.handleMessage(msg);
        }
        };
}

    @Override
    public void run() {
        List<String> retList = new ArrayList<>();

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
            for (int i = 0; i < tds.size(); i += 6) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 5);
                Log.i("open", "run :" + td1.text() + "==>" + td2.text());
                retList.add(td1.text() + "==>" + td2.text());

                Message msg = handler.obtainMessage(3);
                msg.obj = retList;
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
