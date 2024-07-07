package cn.stylefeng.guns.modular.com.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 行业资讯Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:09:15
 */
public class BusinessWrapper extends BaseControllerWrapper{

    public BusinessWrapper(Map<String, Object> single) {
            super(single);
        }

        public BusinessWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public BusinessWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public BusinessWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}