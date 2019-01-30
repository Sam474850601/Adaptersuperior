package com.to8to.app.adaptersuperior.demo2;

import com.to8to.app.adaptersuperior.annotation.AdapterModel;

/**
 * Created by same.li on 2018/12/20
 */
@AdapterModel(viewType = 1)
public class DemoModel1 {
    public String text;
    public DemoModel1(String text) {
        this.text = text;
    }
}
