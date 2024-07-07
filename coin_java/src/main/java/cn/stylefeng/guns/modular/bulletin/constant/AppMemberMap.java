package cn.stylefeng.guns.modular.bulletin.constant;
import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;
/**
 * 用户信息Map
 *
 * @author yaying.liu
 * @Date 2019-08-13 09:23:15
 */
public class AppMemberMap extends AbstractDictMap {
    @Override
    public void init() {
        put("memberId","用户信息id");
    }

    @Override
    protected void initBeWrapped() {

    }
}
