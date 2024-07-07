package cn.stylefeng.guns.modular.chain.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 钱包调用api记录Wrapper
 *
 * @author yaying.liu
 * @since 2020-08-18 19:46:56
 */
public class DappWrapper extends BaseControllerWrapper{

    public DappWrapper(Map<String, Object> single) {
            super(single);
        }

        public DappWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public DappWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public DappWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}