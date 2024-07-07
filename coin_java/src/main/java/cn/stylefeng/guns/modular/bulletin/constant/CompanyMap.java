package cn.stylefeng.guns.modular.bulletin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 平台账户Map
 *
 * @author yaying.liu
 * @Date 2019-09-11 14:52:51
 */
public class CompanyMap extends AbstractDictMap {

    @Override
    public void init() {
        put("companyId","平台账户id");
    }

    @Override
    protected void initBeWrapped() {

    }
}