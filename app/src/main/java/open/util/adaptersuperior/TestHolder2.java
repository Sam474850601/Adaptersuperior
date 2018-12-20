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
@AdapterHolder(layoutResName = "layout_test2", model = SampleModel2.class)
public class TestHolder2 extends RecyclerView.ViewHolder implements IAdapterHolder<SampleModel2> {

    TextView textView;
    public TestHolder2(View itemView) {
        super(itemView);
       textView = itemView.findViewById(R.id.text2);
    }


    @Override
    public void update(int position, SampleModel2 data, List<SampleModel2> list) {
        textView.setText(data.text);
    }
}
