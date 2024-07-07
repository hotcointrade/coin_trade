package cn.stylefeng.guns.modular.com.wrapper;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 收货地址Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-10 20:11:22
 */
public class ShippingWrapper extends BaseControllerWrapper{

    public ShippingWrapper(Map<String, Object> single) {
            super(single);
        }

        public ShippingWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public ShippingWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public ShippingWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId=(Long)map.get("memberId");
            map.put("memberId", F.me().getMember(memberId).getAccount());
        }
}