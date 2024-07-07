package cn.stylefeng.guns.modular.bulletin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 银行列表Map
 *
 * @author yaying.liu
 * @Date 2019-09-11 14:24:35
 */
public class BankMap extends AbstractDictMap {

    @Override
    public void init() {
        put("bankId","银行列表id");
    }

    @Override
    protected void initBeWrapped() {

    }
}