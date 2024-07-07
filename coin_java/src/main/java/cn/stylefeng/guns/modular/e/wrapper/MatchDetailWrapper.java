package cn.stylefeng.guns.modular.e.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 撮合交易成交明细Wrapper
 *
 * @author yaying.liu
 * @Date 2020-05-20 10:29:28
 */
public class MatchDetailWrapper extends BaseControllerWrapper{

    public MatchDetailWrapper(Map<String, Object> single) {
            super(single);
        }

        public MatchDetailWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public MatchDetailWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public MatchDetailWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}