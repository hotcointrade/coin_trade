package cn.stylefeng.guns.modular.app.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 官方社区Wrapper
 *
 * @author yaying.liu
 * @since 2022-10-18 18:24:14
 */
public class CommunityWrapper extends BaseControllerWrapper{

    public CommunityWrapper(Map<String, Object> single) {
            super(single);
        }

        public CommunityWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public CommunityWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public CommunityWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}