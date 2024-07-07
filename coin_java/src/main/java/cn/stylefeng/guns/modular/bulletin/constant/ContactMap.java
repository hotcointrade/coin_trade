package cn.stylefeng.guns.modular.bulletin.constant;

import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

public class ContactMap extends AbstractDictMap {
    @Override
    public void init() {
        put("contactId","联系人id");
        put("contactName","联系人");
        put("phone","联系电话");
        put("status","状态");
        put("remark","备注");
    }

    @Override
    protected void initBeWrapped() {

    }
}
