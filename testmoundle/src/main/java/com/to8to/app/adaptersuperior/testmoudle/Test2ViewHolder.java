package com.to8to.app.adaptersuperior.testmoudle;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.to8to.app.adaptersuperior.annotation.AdapterHolder;
import com.to8to.app.adaptersuperior.annotation.IAdapterHolder;
import com.to8to.app.adaptersuperior.lib.AdapterSuperiorAppUtil;

import java.util.List;

/**
 * Created by same.li on 2018/12/21
 */
@AdapterHolder(layoutResName = "adapter_ddddd", model = Test2Model.class)
public class Test2ViewHolder extends RecyclerView.ViewHolder implements IAdapterHolder<Test2Model> {

    TextView textView;

    public Test2ViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(AdapterSuperiorAppUtil.getViewResId("tv_qq"));
    }

    @Override
    public void update(int i, Test2Model o, List list, Object o2) {
        textView.setText(o.text);
    }
}
