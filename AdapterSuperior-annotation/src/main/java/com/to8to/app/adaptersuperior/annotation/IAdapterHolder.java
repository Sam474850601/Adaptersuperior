package com.to8to.app.adaptersuperior.annotation;


import java.util.List;


/**
 * Created by SAME.LI on 2018/12/17
 */
public interface IAdapterHolder<T> {
    void update(int position, T data, List list, Object adapter);
}
