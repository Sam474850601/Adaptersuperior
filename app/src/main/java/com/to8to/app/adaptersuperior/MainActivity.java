package com.to8to.app.adaptersuperior;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Vector;

import com.to8to.app.adaptersuperior.annotation.IAdapter;
import com.to8to.app.adaptersuperior.annotation.InjectAdapter;
import com.to8to.app.adaptersuperior.lib.AdapterSuperior;

public class MainActivity extends AppCompatActivity {

    @InjectAdapter(value = {TestHolder1.class, TestHolder3.class},  parent = MyRecyclerViewAdapter.class)
    IAdapter adapter  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdapterSuperior.inject(this);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this, TestActivity.class));
        RecyclerView  recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
        SampleModel sampleModel = new SampleModel();
        sampleModel.text = "111";
        Vector vector = new Vector();
        vector.add(sampleModel);
        SampleModel2 sampleModel2 = new SampleModel2();
        sampleModel2.text = "1112";
        vector.add(sampleModel2);
        adapter.setDataSet(vector);
        adapter.notifyDataSetChanged();
        SampleModel2 sampleModel22 = new SampleModel2();
        sampleModel22.text = "1112safdas";
        vector.add(sampleModel22);
        vector.add(new TestModelImpl("你"));
        vector.add(new TestModelImpl("波波"));

        SampleModel2  ssss = new SampleModel2();
        ssss.text = "asdfasdfasfasdf";
        vector.add(ssss);
        adapter.notifyDataSetChanged();

    }

    public static class TestModelImpl implements TestModel1{
        String name;
        public TestModelImpl(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
