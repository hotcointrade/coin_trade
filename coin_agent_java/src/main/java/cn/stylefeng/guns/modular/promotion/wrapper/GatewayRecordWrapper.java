package cn.stylefeng.guns.modular.promotion.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 网关记录Wrapper
 *
 * @author yaying.liu
 * @Date 2019-10-11 10:44:44
 */
public class GatewayRecordWrapper extends BaseControllerWrapper{

    public GatewayRecordWrapper(Map<String, Object> single) {
            super(single);
        }

        public GatewayRecordWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public GatewayRecordWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public GatewayRecordWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}