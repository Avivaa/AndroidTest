package com.example.first;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    Handler handler;
    private List<HashMap<String, String>> listItems; //存放文字图片信息
    private SimpleAdapter listItemAdapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        //MyAdapter myAdapter = new MyAdapter(this, R.layout.list_item, listItems);
        this.setListAdapter(listItemAdapter);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 3) {
                    listItems= (List<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(MyList2Activity.this, listItems, //数据源
                            R.layout.list_item,//XML布局实现
                            new String[]{"ItemTitle", "ItemDetail" }, //数据项的key
                            new int[]{R.id.itemTitle, R.id.itemDetail}//布局的id
                    );
                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate:" + i);//标题文字
            map.put("ItemDetail", "Detail:" + i);//详情描述
            listItems.add(map);
        }
        //生成适配器的item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, //数据源
                R.layout.list_item,//XML布局实现
                new String[]{"ItemTitle", "ItemDetail" }, //数据项的key
                new int[]{R.id.itemTitle, R.id.itemDetail}  //布局的id
        );
    }

    @Override
    public void run() {
        List<HashMap<String, String>> ratList = new ArrayList<HashMap<String, String>>();
        Document doc = null;

        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i("open", "run:html" + doc.title());
            Elements tables = doc.getElementsByTag("table");

            Element table1 = tables.get(0);
            //Log.i("open","run:table"+ table);
            Elements tds = doc.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i += 6) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 5);

                String str1 = td1.text();
                String val = td2.text();

                Log.i("open", "run :" + td1.text() + "==>" + td2.text());
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", str1);
                map.put("ItemDetail", val);
                ratList.add(map);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(3);
        msg.obj = ratList;
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map= (HashMap<String, String>) getListView().getItemAtPosition(position);
        String titleStr= map.get("ItemTitle");
        String detailStr= map.get("ItemDetail");

        TextView title=view.findViewById(R.id.itemTitle);
        TextView detail=view.findViewById(R.id.itemDetail);
        String title2= (String) title.getText();
        String detail2= (String) detail.getText();

        //打开新的页面并传入参数
        Intent ratecalc = new Intent(this,RateCalcActivity.class);
        ratecalc.putExtra("title",titleStr);
        ratecalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(ratecalc);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        //删除操作，此时不是arrayadapter，没有remove方法，此时需要先删除数据，然后通知adapter更改数据
        // listItems.remove(position);
        //listItemAdapter.notifyDataSetChanged();
        //构造对话框进行确认操作
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listItems.remove(position);
                listItemAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("否",null);
        builder.create().show();

        return true;//长按结束后也会触发短按事件，用true和false返回值来区分
    }
}