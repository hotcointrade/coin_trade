package cn.stylefeng.guns.modular.coin.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 合约交易对Wrapper
 *
 * @author yaying.liu
 * @since 2020-08-25 10:49:35
 */
public class SwapWrapper extends BaseControllerWrapper{

    public SwapWrapper(Map<String, Object> single) {
            super(single);
        }

        public SwapWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public SwapWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public SwapWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}