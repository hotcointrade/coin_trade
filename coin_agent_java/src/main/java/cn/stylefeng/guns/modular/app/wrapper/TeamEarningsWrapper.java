package cn.stylefeng.guns.modular.app.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 团队收益Wrapper
 *
 * @author yaying.liu
 * @since 2022-02-18 20:22:22
 */
public class TeamEarningsWrapper extends BaseControllerWrapper{

    public TeamEarningsWrapper(Map<String, Object> single) {
            super(single);
        }

        public TeamEarningsWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public TeamEarningsWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public TeamEarningsWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}