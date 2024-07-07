package cn.stylefeng.guns.modular.meta_data.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public class ConfigWrapper  extends BaseControllerWrapper {
    public ConfigWrapper(Map<String, Object> single) {
        super(single);
    }

    public ConfigWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ConfigWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ConfigWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {

    }
}
