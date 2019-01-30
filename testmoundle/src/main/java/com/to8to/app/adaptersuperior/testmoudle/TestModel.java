package com.to8to.app.adaptersuperior.testmoudle;

import com.to8to.app.adaptersuperior.annotation.AdapterModel;

/**
 * Created by same.li on 2018/12/20
 */
@AdapterModel(viewTypeIdResName = "test2Id")
public class TestModel {
    public String text;
    public TestModel(String text) {
        this.text = text;
    }
}
