package cn.stylefeng.guns.modular.com.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 币种Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-09 11:03:53
 */
public class SymbolWrapper extends BaseControllerWrapper{

    public SymbolWrapper(Map<String, Object> single) {
            super(single);
        }

        public SymbolWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public SymbolWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public SymbolWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}