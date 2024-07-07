package cn.stylefeng.guns.core.util;

import java.util.Calendar;
import java.util.Date;

public class TimeCompare {
/*时间比大小*/

public static int timeCompare(Date t1, Date t2){
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        c1.setTime(t1);
        c2.setTime(t2);
        int result=c1.compareTo(c2);

        return result;

    }

}
