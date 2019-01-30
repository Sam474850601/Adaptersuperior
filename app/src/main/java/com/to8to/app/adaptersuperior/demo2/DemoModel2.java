package com.to8to.app.adaptersuperior.demo2;

import com.to8to.app.adaptersuperior.annotation.AdapterModel;

/**
 * Created by same.li on 2018/12/20
 */
@AdapterModel(viewType = 2)
public class DemoModel2 {
    public String text;

    public DemoModel2(String text) {
        this.text = text;
    }
}
