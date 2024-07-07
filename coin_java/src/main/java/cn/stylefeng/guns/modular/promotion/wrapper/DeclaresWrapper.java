package cn.stylefeng.guns.modular.promotion.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 用户协议Wrapper
 *
 * @author yaying.liu
 * @Date 2019-10-12 19:31:14
 */
public class DeclaresWrapper extends BaseControllerWrapper{

    public DeclaresWrapper(Map<String, Object> single) {
            super(single);
        }

        public DeclaresWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public DeclaresWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public DeclaresWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}