package com.example.first;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {
    public final String TAG= "ConfigActivity";

    EditText dollarText;
    EditText euroText;
    EditText wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent=getIntent();
        float dollar2= intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2= intent.getFloatExtra("euro_rate_key",0.0f);
        float won2= intent.getFloatExtra("won_rate_key",0.0f);

        Log.i(TAG,"onCreate: dollar="+dollar2);
        Log.i(TAG,"onCreate: euro="+euro2);
        Log.i(TAG,"onCreate: won="+won2);

        dollarText=findViewById(R.id.dollar_rate);
        euroText=findViewById(R.id.euro_rate);
        wonText=findViewById(R.id.won_rate);

        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));
    }
    public void save(View btn){
        Log.i("cfg","save");
        //获取新的值
        float newDollar= Float.parseFloat(dollarText.getText().toString());
        float newEuro= Float.parseFloat(euroText.getText().toString());
        float newWon= Float.parseFloat(wonText.getText().toString());

        Log.i(TAG,"save:获取到新的值");
        Log.i(TAG,"onCreate: newDollar="+newDollar);
        Log.i(TAG,"onCreate: newEuro="+newEuro);
        Log.i(TAG,"onCreate: newWon="+newWon);

        //返回到Bundle或者放入到Extra
        Intent intent=getIntent();
        Bundle bdl= new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);
        setResult(2,intent);

        //返回到调用界面
        finish();

    }

}
