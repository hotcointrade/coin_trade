package cn.stylefeng.guns.modular.chain.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 平台地址Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-16 09:31:12
 */
public class PlatformAddressWrapper extends BaseControllerWrapper{

    public PlatformAddressWrapper(Map<String, Object> single) {
            super(single);
        }

        public PlatformAddressWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public PlatformAddressWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public PlatformAddressWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}