package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 锁仓静态收益记录Wrapper
 *
 * @author yaying.liu
 * @since 2020-09-25 10:46:24
 */
public class LockProfitWrapper extends BaseControllerWrapper{

    public LockProfitWrapper(Map<String, Object> single) {
            super(single);
        }

        public LockProfitWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public LockProfitWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public LockProfitWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}