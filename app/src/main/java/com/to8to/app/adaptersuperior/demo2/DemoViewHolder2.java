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
@AdapterHolder(layoutResName = "adapter_demo2", model = DemoModel2.class)
public class DemoViewHolder2 extends RecyclerView.ViewHolder implements IAdapterHolder<DemoModel2> {


    TextView tvText;

    public DemoViewHolder2(View itemView) {
        super(itemView);
        tvText = itemView.findViewById(R.id.tv_text);
    }

    @Override
    public void update(int i, DemoModel2 demoModel2, List list, Object o) {
        tvText.setText(""+ demoModel2.text);
    }


}
