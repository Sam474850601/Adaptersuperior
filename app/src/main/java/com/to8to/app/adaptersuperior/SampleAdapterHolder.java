package com.to8to.app.adaptersuperior;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import com.to8to.app.adaptersuperior.annotation.AdapterHolder;
import com.to8to.app.adaptersuperior.annotation.IAdapterHolder;

/**
 * Created by same.li on 2018/12/18
 */
@AdapterHolder(layoutResName ="activity_main")
public class SampleAdapterHolder  extends RecyclerView.ViewHolder implements IAdapterHolder {

    public SampleAdapterHolder(@NonNull View itemView) {
        super(itemView);
    }


    @Override
    public void update(int position, Object data, List list, Object adapter) {

    }
}
