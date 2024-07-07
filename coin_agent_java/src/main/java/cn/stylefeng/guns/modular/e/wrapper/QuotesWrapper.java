package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 行情涨跌控制Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-23 11:44:38
 */
public class QuotesWrapper extends BaseControllerWrapper{

    public QuotesWrapper(Map<String, Object> single) {
            super(single);
        }

        public QuotesWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public QuotesWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public QuotesWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}