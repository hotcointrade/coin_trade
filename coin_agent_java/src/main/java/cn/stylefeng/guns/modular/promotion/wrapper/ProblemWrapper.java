package cn.stylefeng.guns.modular.promotion.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 常见问题Wrapper
 *
 * @author yaying.liu
 * @Date 2019-10-14 14:11:51
 */
public class ProblemWrapper extends BaseControllerWrapper{

    public ProblemWrapper(Map<String, Object> single) {
            super(single);
        }

        public ProblemWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public ProblemWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public ProblemWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}