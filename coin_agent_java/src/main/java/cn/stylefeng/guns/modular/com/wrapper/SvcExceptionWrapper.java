package cn.stylefeng.guns.modular.com.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 异常信息Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-24 10:20:02
 */
public class SvcExceptionWrapper extends BaseControllerWrapper{

    public SvcExceptionWrapper(Map<String, Object> single) {
            super(single);
        }

        public SvcExceptionWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public SvcExceptionWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public SvcExceptionWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}