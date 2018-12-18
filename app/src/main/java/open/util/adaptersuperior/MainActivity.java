package open.util.adaptersuperior;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Vector;

import open.util.adaptersuperior.annotation.IAdapter;
import open.util.adaptersuperior.annotation.InjectAdapter;
import open.util.adaptersuperiorlib.SupportDefaultAdapter;
import open.util.testmoundle.TestActivity;

public class MainActivity extends AppCompatActivity {

    @InjectAdapter(value = {TestHolder2.class})
    IAdapter adapter = new MainActivity$$Adapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        adapter.notifyDataSetChanged();
    }
}
