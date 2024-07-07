package cn.stylefeng.guns.core.bipay.http.client;


import cn.stylefeng.guns.core.bipay.http.ResponseMessage;

import java.util.Map;

public interface Client<T> {

    ResponseMessage<T> post(String url, Map<String, String> list);

    ResponseMessage<T> get(String url);

}
