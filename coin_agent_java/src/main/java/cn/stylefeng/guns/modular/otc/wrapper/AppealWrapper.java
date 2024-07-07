package cn.stylefeng.guns.modular.otc.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 申诉订单Wrapper
 *
 * @author yaying.liu
 * @since 2020-07-15 17:16:25
 */
public class AppealWrapper extends BaseControllerWrapper{

    public AppealWrapper(Map<String, Object> single) {
            super(single);
        }

        public AppealWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public AppealWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public AppealWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}