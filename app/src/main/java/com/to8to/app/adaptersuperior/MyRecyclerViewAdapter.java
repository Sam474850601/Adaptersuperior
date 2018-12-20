package com.to8to.app.adaptersuperior;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import com.to8to.app.adaptersuperior.annotation.IAdapter;

/**
 * Created by same.li on 2018/12/18
 */
public abstract class MyRecyclerViewAdapter extends RecyclerView.Adapter implements IAdapter {

    List list;
    @Override
    public int getItemCount() {
        if(null == list)
            return 0;
        return list.size();
    }

    @Override
    public Object getItemData(int position) {
        return list.get(position);
    }

    @Override
    public List getDataSet() {
        return list;
    }

    @Override
    public void setDataSet(List itemListData) {
        this.list = itemListData;
    }

}
