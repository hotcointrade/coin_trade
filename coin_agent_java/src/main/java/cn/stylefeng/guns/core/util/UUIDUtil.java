package cn.stylefeng.guns.core.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
public class UUIDUtil {

    /** 订单号生成(NEW) **/

    private static final AtomicInteger SEQ = new AtomicInteger(1000);

    private static final DateTimeFormatter DF_FMT_PREFIX = DateTimeFormatter.ofPattern("yyMMddHHmmssSS");

    private static ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    public static String getUUID(){
        LocalDateTime dataTime = LocalDateTime.now(ZONE_ID);
        if (SEQ.intValue()> 9990){
            SEQ.getAndSet(1000);
        }
        return dataTime.format(DF_FMT_PREFIX)+ SEQ.getAndIncrement();
    }

    public static void main(String[] args) {
        System.out.println(UUIDUtil.getUUID());
    }
}
