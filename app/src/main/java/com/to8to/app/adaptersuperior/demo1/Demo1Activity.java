package com.to8to.app.adaptersuperior.demo1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.to8to.app.adaptersuperior.R;
import com.to8to.app.adaptersuperior.annotation.IAdapter;
import com.to8to.app.adaptersuperior.annotation.InjectAdapter;
import com.to8to.app.adaptersuperior.lib.AdapterSuperior;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by same.li on 2018/12/20
 */
public class Demo1Activity extends Activity {


    RecyclerView recyclerView;

    @InjectAdapter(DemoViewHolder0.class)
    IAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo0);
        initView();
        initData();
    }

    private void initView() {

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterSuperior.inject(this);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
    }

    private void initData() {

        //添加测试数据
        List list = new ArrayList();
        for (int i = 0; i < 5; i++) {
            list.add(new DemoModel0("哈哈哒"+i));
        }

        adapter.setDataSet(list);
        adapter.notifyDataSetChanged();
        for (int i = 0; i < 1000; i++) {
            list.add(new DemoModel0("呵呵哒哒"+i));
        }
        adapter.notifyDataSetChanged();
    }


}
