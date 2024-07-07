package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 锁仓动态收益配置Wrapper
 *
 * @author yaying.liu
 * @since 2020-09-25 10:44:59
 */
public class LockAutoWrapper extends BaseControllerWrapper{

    public LockAutoWrapper(Map<String, Object> single) {
            super(single);
        }

        public LockAutoWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public LockAutoWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public LockAutoWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}