package com.rrkj.dtz.client;

/**
 * 返回值
 * Created by Limaoran on 2017/12/6.
 */
@FunctionalInterface
public interface CallbackResult<T>{
    void callback(T t);
}
