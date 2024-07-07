package cn.stylefeng.guns.modular.app.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 佣金划转Wrapper
 *
 * @author yaying.liu
 * @since 2022-02-18 20:54:40
 */
public class WalletTransferWrapper extends BaseControllerWrapper{

    public WalletTransferWrapper(Map<String, Object> single) {
            super(single);
        }

        public WalletTransferWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public WalletTransferWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public WalletTransferWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}