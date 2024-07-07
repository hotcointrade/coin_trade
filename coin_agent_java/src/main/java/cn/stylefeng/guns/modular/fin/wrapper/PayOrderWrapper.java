package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 支付订单Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:14:16
 */
public class PayOrderWrapper extends BaseControllerWrapper{

    public PayOrderWrapper(Map<String, Object> single) {
            super(single);
        }

        public PayOrderWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public PayOrderWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public PayOrderWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}