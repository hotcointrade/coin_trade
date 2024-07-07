package cn.stylefeng.guns.modular.seviceException;

import cn.stylefeng.guns.modular.base.state.F;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 钱包异常类
 */
public class WalletException extends Exception {


    public WalletException(String method,String msg,JSONObject jsonObject) {
        F.me().saveWalletException(method,msg,jsonObject);
    }

    /**
     *
     * @param method 方法
     * @param msg 错误信息
     * @param map 数据
     */
    public WalletException(String method,String msg,Map map) {
        new WalletException(method,msg,new JSONObject(map));
    }

}
