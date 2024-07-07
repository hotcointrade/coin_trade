package cn.stylefeng.guns.modular.bulletin.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 银行列表Wrapper
 *
 * @author yaying.liu
 * @Date 2019-09-11 14:24:35
 */
public class BankWrapper extends BaseControllerWrapper{

    public BankWrapper(Map<String, Object> single) {
        super(single);
    }

    public BankWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public BankWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public BankWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {

    }
}