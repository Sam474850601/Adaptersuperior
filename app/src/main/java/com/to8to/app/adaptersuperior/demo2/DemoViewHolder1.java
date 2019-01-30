package com.to8to.app.adaptersuperior.demo2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.to8to.app.adaptersuperior.R;
import com.to8to.app.adaptersuperior.annotation.AdapterHolder;
import com.to8to.app.adaptersuperior.annotation.IAdapterHolder;

import java.util.List;

/**
 * Created by same.li on 2018/12/20
 */
@AdapterHolder(layoutResName = "adapter_demo1", model = DemoModel1.class)
public class DemoViewHolder1 extends RecyclerView.ViewHolder implements IAdapterHolder<DemoModel1> {


    TextView tvText;

    public DemoViewHolder1(View itemView) {
        super(itemView);
        tvText = itemView.findViewById(R.id.tv_text);
    }

    @Override
    public void update(int i, DemoModel1 demoModel1, List list, Object o) {
        tvText.setText(""+ demoModel1.text);
    }


}
