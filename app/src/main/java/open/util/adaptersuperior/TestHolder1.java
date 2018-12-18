package open.util.adaptersuperior;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import open.util.adaptersuperior.annotation.AdapterHolder;
import open.util.adaptersuperior.annotation.IAdapterHolder;

/**
 * Created by same.li on 2018/12/18
 */
@AdapterHolder(layoutResName = "layout_test1", viewTypeIdResName = "testId1")
public class TestHolder1 extends RecyclerView.ViewHolder  implements IAdapterHolder {

    TextView textView;

    public TestHolder1(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.text1);
    }

    @Override
    public void update(int position, List list) {
         Object object = list.get(position);
         if(object instanceof SampleModel){
             textView.setText(((SampleModel) object).text);
         }
    }
}