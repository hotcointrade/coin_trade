package cn.stylefeng.guns.modular.bulletin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 商户Map
 *
 * @author yaying.liu
 * @Date 2019-09-02 20:11:57
 */
public class MerchantMap extends AbstractDictMap {

    @Override
    public void init() {
        put("merchantId","商户id");
    }

    @Override
    protected void initBeWrapped() {

    }
}