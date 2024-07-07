package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 平台流水Wrapper
 *
 * @author yaying.liu
 * @Date 2020-01-03 18:06:11
 */
public class PlatformCashflowWrapper extends BaseControllerWrapper{

    public PlatformCashflowWrapper(Map<String, Object> single) {
            super(single);
        }

        public PlatformCashflowWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public PlatformCashflowWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public PlatformCashflowWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}