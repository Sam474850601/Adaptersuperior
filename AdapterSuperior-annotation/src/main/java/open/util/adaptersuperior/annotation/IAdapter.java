package open.util.adaptersuperior.annotation;


import java.util.List;

/**
 * Created by same.li on 2018/4/5.
 */

public interface IAdapter {

    Object  getItemData(int position);

    List getDataSet();

    void setDataSet(List itemListData);


    //以下不需要实现。Adapter已经实现
    void notifyDataSetChanged();

    void notifyItemChanged(int position);

    void notifyItemChanged(int position, Object payload);

    void notifyItemRangeChanged(int positionStart, int itemCount);

    void notifyItemRangeChanged(int positionStart, int itemCount, Object payload);

    void notifyItemInserted(int position);

    void notifyItemMoved(int fromPosition, int toPosition);

    void notifyItemRangeInserted(int positionStart, int itemCount);

    void notifyItemRemoved(int position);

    void notifyItemRangeRemoved(int positionStart, int itemCount);
}
