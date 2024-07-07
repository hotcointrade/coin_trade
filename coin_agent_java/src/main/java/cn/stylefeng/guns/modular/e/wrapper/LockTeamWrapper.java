package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 锁仓团队奖配置Wrapper
 *
 * @author yaying.liu
 * @since 2020-09-25 10:45:37
 */
public class LockTeamWrapper extends BaseControllerWrapper{

    public LockTeamWrapper(Map<String, Object> single) {
            super(single);
        }

        public LockTeamWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public LockTeamWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public LockTeamWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}