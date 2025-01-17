package com.example.first;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    List<String> data=new ArrayList<String>();
    private String TAG="MyList";
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

         ListView listView= findViewById(R.id.mylist);
         //init data
         for(int i=0;i<10;i++){
             data.add("item"+i);
         }

         adapter = new ArrayAdapter<String>(MyListActivity.this, android.R.layout.simple_expandable_list_item_1, data);
         listView.setAdapter(adapter);
         listView.setEmptyView(findViewById(R.id.nodata)); //没有数据时候的提示
         listView.setOnItemClickListener(this);

            }

    @Override
    public void onItemClick(AdapterView<?> listv, View view, int position, long id) {
        adapter.remove(listv.getItemAtPosition(position)); //在ArrayAdapter可以直接通过Adapter删除
        //adapter.notifyDataSetChanged(); //remove自动更新

    }
}

