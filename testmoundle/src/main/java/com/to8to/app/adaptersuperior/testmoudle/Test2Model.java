package com.to8to.app.adaptersuperior.testmoudle;

import com.to8to.app.adaptersuperior.annotation.AdapterModel;

/**
 * Created by same.li on 2018/12/20
 */
@AdapterModel(viewTypeIdResName = "testId")
public class Test2Model  {
   public String text;

    public Test2Model(String text) {
        this.text = text;
    }
}
