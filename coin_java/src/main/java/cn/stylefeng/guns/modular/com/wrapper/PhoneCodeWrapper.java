package cn.stylefeng.guns.modular.com.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 电话区号Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-20 17:59:48
 */
public class PhoneCodeWrapper extends BaseControllerWrapper{

    public PhoneCodeWrapper(Map<String, Object> single) {
            super(single);
        }

        public PhoneCodeWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public PhoneCodeWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public PhoneCodeWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}