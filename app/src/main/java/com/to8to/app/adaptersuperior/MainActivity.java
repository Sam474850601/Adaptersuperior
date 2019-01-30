package com.to8to.app.adaptersuperior;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.to8to.app.adaptersuperior.demo1.Demo1Activity;
import com.to8to.app.adaptersuperior.demo2.Demo2Activity;
import com.to8to.app.adaptersuperior.testmoudle.CustomAdapterActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onDemo1Click(View view) {
        startActivity(new Intent(this, Demo1Activity.class));
    }

    public void onDemo2Click(View view) {
        startActivity(new Intent(this, Demo2Activity.class));
    }

    public void onDemo3Click(View view) {
        startActivity(new Intent(this, CustomAdapterActivity.class));
    }
}
