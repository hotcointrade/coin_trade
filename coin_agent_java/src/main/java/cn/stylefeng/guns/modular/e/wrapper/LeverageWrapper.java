package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 杠杆倍率Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-23 09:19:33
 */
public class LeverageWrapper extends BaseControllerWrapper{

    public LeverageWrapper(Map<String, Object> single) {
            super(single);
        }

        public LeverageWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public LeverageWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public LeverageWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}