package com.to8to.app.adaptersuperior.lib;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import com.to8to.app.adaptersuperior.annotation.IAdapter;

/**
 * Created by same.li on 2018/12/18
 */
public abstract class SupportDefaultAdapter extends RecyclerView.Adapter  implements IAdapter {

    public List itemListData;


    @Override
    public int getItemCount() {
        return null != itemListData ?itemListData.size():0;
    }

    @Override
    public Object getItemData(int position) {
        return itemListData.get(position);
    }

    @Override
    public List getDataSet() {
        return itemListData;
    }

    @Override
    public void setDataSet(List itemListData) {
        this.itemListData = itemListData;
    }




}
