package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 系统汇率Wrapper
 *
 * @author yaying.liu
 * @since 2022-10-18 13:18:11
 */
public class PlatfomrRateWrapper extends BaseControllerWrapper{

    public PlatfomrRateWrapper(Map<String, Object> single) {
            super(single);
        }

        public PlatfomrRateWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public PlatfomrRateWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public PlatfomrRateWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}
