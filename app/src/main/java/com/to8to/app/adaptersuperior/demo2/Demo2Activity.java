package com.to8to.app.adaptersuperior.demo2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.to8to.app.adaptersuperior.R;
import com.to8to.app.adaptersuperior.annotation.IAdapter;
import com.to8to.app.adaptersuperior.annotation.InjectAdapter;
import com.to8to.app.adaptersuperior.demo1.DemoModel0;
import com.to8to.app.adaptersuperior.demo1.DemoViewHolder0;
import com.to8to.app.adaptersuperior.lib.AdapterSuperior;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by same.li on 2018/12/20
 */
public class Demo2Activity extends Activity {


    RecyclerView list0;
    RecyclerView list1;
    RecyclerView list2;

    @InjectAdapter(DemoViewHolder0.class)
    IAdapter list0Adapter;

    @InjectAdapter({DemoViewHolder1.class,DemoViewHolder0.class,DemoViewHolder2.class , DemoViewHolder3.class })
    IAdapter list1Adapter;


    @InjectAdapter({DemoViewHolder1.class,DemoViewHolder0.class,DemoViewHolder2.class })
    IAdapter list2Adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);

        initView();
        initData();
    }

    private void initView() {

        list0 = findViewById(R.id.list0);
        list1 = findViewById(R.id.list1);
        list2 = findViewById(R.id.list2);

        list0.setLayoutManager(new LinearLayoutManager(this));
        list1.setLayoutManager(new LinearLayoutManager(this));
        list2.setLayoutManager(new LinearLayoutManager(this));

        AdapterSuperior.inject(this);


        list0.setAdapter((RecyclerView.Adapter) list0Adapter);
        list1.setAdapter((RecyclerView.Adapter) list1Adapter);
        list2.setAdapter((RecyclerView.Adapter) list2Adapter);


    }

    private void initData() {
        initList0();
        initList1();
    }



    private void initList0() {
        //添加测试数据
        List<Object> list = new ArrayList();
        for (int i = 0; i < 2; i++) {
            list.add(new DemoModel0("哈哈哒"+i));
        }

        list0Adapter.setDataSet(list);
        list0Adapter.notifyDataSetChanged();
        for (int i = 0; i < 3; i++) {
            list.add(new DemoModel0("呵呵哒哒"+i));
        }
        list0Adapter.notifyDataSetChanged();
    }


    private void initList1() {
        List<Object> list = new ArrayList();
        list1Adapter.setDataSet(list);
        list.add(new DemoModel0("111111111111111111"));
        list.add(new DemoModel1("111111111111111111"));
        list.add(new DemoModel2("22222222222"));
        list.add(new DemoModel3Impl("3333as3"));
        list.add(new DemoModel3Impl("333asdfd33"));
        list.add(new DemoModel2("123123134255 s横说竖说"));
        list1Adapter.notifyDataSetChanged();

        //如果多传

        list2Adapter.setDataSet(list);
        list2Adapter.notifyDataSetChanged();
    }

}
