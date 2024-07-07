package cn.stylefeng.guns.modular.chain.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 公账提币Wrapper
 *
 * @author yaying.liu
 * @Date 2020-01-17 18:55:07
 */
public class PublicWrapper extends BaseControllerWrapper{

    public PublicWrapper(Map<String, Object> single) {
            super(single);
        }

        public PublicWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public PublicWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public PublicWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}