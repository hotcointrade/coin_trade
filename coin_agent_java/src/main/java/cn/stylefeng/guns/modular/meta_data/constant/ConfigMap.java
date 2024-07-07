package cn.stylefeng.guns.modular.meta_data.constant;

import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

public class ConfigMap  extends AbstractDictMap {
    @Override
    public void init() {
        put("configId","配置id");
        put("name","属性名");
        put("code","编码");
        put("value","属性值");
        put("remark","备注");
    }

    @Override
    protected void initBeWrapped() {

    }
}
