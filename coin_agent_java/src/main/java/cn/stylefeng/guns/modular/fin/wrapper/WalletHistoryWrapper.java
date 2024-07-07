package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 钱包历史变更Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:57
 */
public class WalletHistoryWrapper extends BaseControllerWrapper{

    public WalletHistoryWrapper(Map<String, Object> single) {
            super(single);
        }

        public WalletHistoryWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public WalletHistoryWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public WalletHistoryWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}