package cn.stylefeng.guns.modular.bulletin.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 版本号Wrapper
 *
 * @author yaying.liu
 * @Date 2019-09-04 18:44:14
 */
public class AppVersionWrapper extends BaseControllerWrapper{

    public AppVersionWrapper(Map<String, Object> single) {
        super(single);
    }

    public AppVersionWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public AppVersionWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public AppVersionWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {

    }
}