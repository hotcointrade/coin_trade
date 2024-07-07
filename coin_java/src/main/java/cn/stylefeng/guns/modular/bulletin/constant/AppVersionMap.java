package cn.stylefeng.guns.modular.bulletin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 版本号Map
 *
 * @author yaying.liu
 * @Date 2019-09-04 18:44:14
 */
public class AppVersionMap extends AbstractDictMap {

    @Override
    public void init() {
        put("appVersionId","版本号id");
    }

    @Override
    protected void initBeWrapped() {

    }
}