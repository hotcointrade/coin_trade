package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 锁仓合约配置Wrapper
 *
 * @author yaying.liu
 * @since 2020-09-25 10:44:07
 */
public class LockWrapper extends BaseControllerWrapper{

    public LockWrapper(Map<String, Object> single) {
            super(single);
        }

        public LockWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public LockWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public LockWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}