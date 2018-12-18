package open.util.adaptersuperior;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import open.util.adaptersuperior.annotation.IAdapter;

/**
 * Created by same.li on 2018/12/18
 */
public abstract class MyRecyclerViewAdapter extends RecyclerView.Adapter implements IAdapter {


    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Object getItemData(int position) {
        return null;
    }

    @Override
    public List getDataSet() {
        return null;
    }

    @Override
    public void setDataSet(List itemListData) {

    }

}
