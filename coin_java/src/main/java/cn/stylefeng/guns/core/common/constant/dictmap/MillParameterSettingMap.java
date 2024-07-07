package cn.stylefeng.guns.core.common.constant.dictmap;

import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

public class MillParameterSettingMap extends AbstractDictMap {

	@Override
	public void init() {
		put("mpsId", "参数id");
        put("type", "类型");
	}

	@Override
	protected void initBeWrapped() {
	}

}
