package cn.stylefeng.guns.modular.chain.wrapper;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 用户钱包地址Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-10 18:24:36
 */
public class CoinWrapper extends BaseControllerWrapper{

    public CoinWrapper(Map<String, Object> single) {
            super(single);
        }

        public CoinWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public CoinWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public CoinWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId=(Long)map.get("memberId");
            map.put("memberId", F.me().getMember(memberId).getAccount());
        }
}