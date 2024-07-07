package cn.stylefeng.guns.modular.bulletin.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 日志Wrapper
 *
 * @author yaying.liu
 * @Date 2019-09-09 09:10:57
 */
public class AppOperationLogWrapper extends BaseControllerWrapper{

    public AppOperationLogWrapper(Map<String, Object> single) {
        super(single);
    }

    public AppOperationLogWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public AppOperationLogWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public AppOperationLogWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {

    }
}