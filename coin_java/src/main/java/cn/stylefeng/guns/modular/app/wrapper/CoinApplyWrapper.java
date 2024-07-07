package cn.stylefeng.guns.modular.app.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 上币申请Wrapper
 *
 * @author yaying.liu
 * @since 2022-02-20 20:37:01
 */
public class CoinApplyWrapper extends BaseControllerWrapper{

    public CoinApplyWrapper(Map<String, Object> single) {
            super(single);
        }

        public CoinApplyWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public CoinApplyWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public CoinApplyWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}