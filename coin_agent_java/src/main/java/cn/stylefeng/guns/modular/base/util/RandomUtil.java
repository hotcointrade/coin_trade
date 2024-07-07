package cn.stylefeng.guns.modular.base.util;

import java.util.Date;
import java.util.UUID;

/**
 * 随机数工具类
 */
public class RandomUtil {

    /**
     * 生成随机编码
     * @param prefix 前缀
     * @return
     */
    public static String code(String prefix)
    {
        if(prefix==null)
        {
            prefix="SYS";
        }
        return prefix+(new Date()).getTime()+UUID.randomUUID().toString().replace("-","").substring(3,10);
    }
    public static String code()
    {
       return   code(null);
    }


    /**
     * 生成随机账号
     */
    public static String account(){
        return UUID.randomUUID().toString().replace("-","").substring(0,8);
    }

}
