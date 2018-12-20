package com.to8to.app.adaptersuperior;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.to8to.app.adaptersuperior.annotation.AdapterHolder;
import com.to8to.app.adaptersuperior.annotation.IAdapterHolder;
import com.to8to.app.adaptersuperior.lib.AdapterSuperiorAppUtil;

import java.util.List;


/**
 * Created by same.li on 2018/12/20
 */
@AdapterHolder(layoutResName = "layout_test3", model = TestModel1.class)
public class TestHolder3  extends RecyclerView.ViewHolder implements IAdapterHolder<TestModel1> {


    TextView textView;

    public TestHolder3(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(AdapterSuperiorAppUtil.getViewResId("tv_ca"));
    }


    @Override
    public void update(int position, TestModel1 data, List list, Object adapter) {
        textView.setText(data.getName());
    }
}
