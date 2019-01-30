package com.to8to.app.adaptersuperior.demo2;

/**
 * Created by same.li on 2018/12/20
 */
public class DemoModel3Impl  implements DemoModel3{

    String 服务器给的多余;
    String 垃圾数据哈比东西;
    String text;
    public DemoModel3Impl(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    public String get服务器给的多余() {
        return 服务器给的多余;
    }

    public void set服务器给的多余(String 服务器给的多余) {
        this.服务器给的多余 = 服务器给的多余;
    }

    public String get垃圾数据哈比东西() {
        return 垃圾数据哈比东西;
    }

    public void set垃圾数据哈比东西(String 垃圾数据哈比东西) {
        this.垃圾数据哈比东西 = 垃圾数据哈比东西;
    }

    public void setText(String text) {
        this.text = text;
    }
}
