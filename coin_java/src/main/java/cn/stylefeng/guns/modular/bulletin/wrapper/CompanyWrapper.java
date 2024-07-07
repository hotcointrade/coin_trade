package cn.stylefeng.guns.modular.bulletin.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 平台账户Wrapper
 *
 * @author yaying.liu
 * @Date 2019-09-11 14:52:51
 */
public class CompanyWrapper extends BaseControllerWrapper{

    public CompanyWrapper(Map<String, Object> single) {
        super(single);
    }

    public CompanyWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public CompanyWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public CompanyWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {

    }
}