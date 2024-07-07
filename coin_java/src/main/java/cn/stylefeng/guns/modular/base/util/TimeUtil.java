package cn.stylefeng.guns.modular.base.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间工具类
 */
public class TimeUtil {


    /**
     * 切割时间段
     */
       public static Map<String,Date> splitTime(String limitTime)
       {
           Map<String,Date> map=new HashMap<>();
           String[] arr=limitTime.split(" - ");
           if(arr.length>0)
           {
               map.put("start",DateTimeUtil.strToDate(arr[0]) );
               map.put("end",DateTimeUtil.strToDate(arr[1]) );
           }
           return map;
       }

}
