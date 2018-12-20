package com.to8to.app.adaptersuperior;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import com.to8to.app.adaptersuperior.annotation.AdapterHolder;
import com.to8to.app.adaptersuperior.annotation.IAdapterHolder;

/**
 * Created by same.li on 2018/12/18
 */
@AdapterHolder(layoutResName = "layout_test2", model = SampleModel2.class)
public class TestHolder2 extends RecyclerView.ViewHolder implements IAdapterHolder<SampleModel2> {

    TextView textView;
    public TestHolder2(View itemView) {
        super(itemView);
       textView = itemView.findViewById(R.id.text2);
    }


    @Override
    public void update(int position, SampleModel2 data, List<SampleModel2> list) {
        textView.setText(data.text);
    }
}
