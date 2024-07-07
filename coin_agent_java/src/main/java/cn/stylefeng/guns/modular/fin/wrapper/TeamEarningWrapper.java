package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 团队佣金Wrapper
 *
 * @author yaying.liu
 * @since 2022-02-18 18:53:54
 */
public class TeamEarningWrapper extends BaseControllerWrapper{

    public TeamEarningWrapper(Map<String, Object> single) {
            super(single);
        }

        public TeamEarningWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public TeamEarningWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public TeamEarningWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}