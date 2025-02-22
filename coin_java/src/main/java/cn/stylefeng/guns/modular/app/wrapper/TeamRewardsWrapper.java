package cn.stylefeng.guns.modular.app.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 社区奖励Wrapper
 *
 * @author yaying.liu
 * @since 2022-07-07 17:45:57
 */
public class TeamRewardsWrapper extends BaseControllerWrapper{

    public TeamRewardsWrapper(Map<String, Object> single) {
            super(single);
        }

        public TeamRewardsWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public TeamRewardsWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public TeamRewardsWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}