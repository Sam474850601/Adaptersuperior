package open.util.testmoundle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.to8to.app.adaptersuperior.lib.AppUtil;

/**
 * Created by same.li on 2018/12/18
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int identifier = AppUtil.getIdentifier("testId2", "id");
        Log.e("TestActivity", "identifier="+identifier);
    }
}
