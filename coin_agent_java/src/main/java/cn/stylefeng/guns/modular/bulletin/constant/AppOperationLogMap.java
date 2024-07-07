package cn.stylefeng.guns.modular.bulletin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 日志Map
 *
 * @author yaying.liu
 * @Date 2019-09-09 09:10:57
 */
public class AppOperationLogMap extends AbstractDictMap {

    @Override
    public void init() {
        put("appOperationLogId","日志id");
    }

    @Override
    protected void initBeWrapped() {

    }
}