package com.to8to.app.adaptersuperior.demo1;

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
@AdapterHolder(layoutResName = "adapter_demo0", model = DemoModel0.class)
public class DemoViewHolder0 extends RecyclerView.ViewHolder implements IAdapterHolder<DemoModel0> {

    TextView tvPosition;

    TextView tvText;

    public DemoViewHolder0(View itemView) {
        super(itemView);
        tvPosition = itemView.findViewById(R.id.tv_position);
        tvText = itemView.findViewById(R.id.tv_text);
    }

    @Override
    public void update(int i, DemoModel0 demoModel0, List list, Object o) {
        if(null == demoModel0)
            return;
        tvPosition.setText(""+i);
        tvText.setText(""+ demoModel0.text);
    }


}
