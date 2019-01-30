package com.to8to.app.adaptersuperior.testmoudle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.to8to.app.adaptersuperior.annotation.InjectAdapter;
import com.to8to.app.adaptersuperior.lib.AdapterSuperior;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by same.li on 2018/12/21
 */
public class CustomAdapterActivity extends Activity {


    RecyclerView list;

    @InjectAdapter(value = {TestViewHolder.class, Test2ViewHolder.class}, parent = MyAdapter.class)
    MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        AdapterSuperior.inject(this);
        initView();
        initData();
    }

    private void initView() {
        list = findViewById(R.id.list);
        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
    }

    private void initData() {
        //添加测试数据
        List list = new ArrayList();
        adapter.setDataSet(list);

        for (int i = 0; i < 5; i++) {
            list.add(new TestModel("暴走的大肥仔"+ UUID.randomUUID()+UUID.randomUUID()+UUID.randomUUID()));
        }

        for (int i = 0; i < 1000; i++) {
            list.add(new Test2Model("暴走的大肥婆"+ i));
        }

        adapter.notifyDataSetChanged();

    }

}
