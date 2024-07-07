package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 第三方接口记录表Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:15:58
 */
public class PayRecordWrapper extends BaseControllerWrapper{

    public PayRecordWrapper(Map<String, Object> single) {
            super(single);
        }

        public PayRecordWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public PayRecordWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public PayRecordWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}