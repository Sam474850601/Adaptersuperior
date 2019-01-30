package com.to8to.app.adaptersuperior.testmoudle;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.to8to.app.adaptersuperior.annotation.AdapterConfig;
import com.to8to.app.adaptersuperior.annotation.IAdapter;

import java.util.List;

/**
 * Created by same.li on 2018/12/21
 */

@AdapterConfig(overrideGetItemViewType = true, overrideOnBindViewHolder = true, overrideOnCreateViewHolder = true)
public abstract class MyAdapter extends RecyclerView.Adapter implements IAdapter {

    public List itemListData;

    public MyAdapter() {
    }

    public int getItemCount() {
        return null != this.itemListData ? this.itemListData.size() : 0;
    }

    public Object getItemData(int position) {
        return this.itemListData.get(position);
    }

    public List getDataSet() {
        return this.itemListData;
    }

    public void setDataSet(List itemListData) {
        this.itemListData = itemListData;
    }



}
