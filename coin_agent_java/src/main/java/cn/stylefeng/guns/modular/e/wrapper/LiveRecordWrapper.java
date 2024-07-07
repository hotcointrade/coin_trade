package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 生活支付打款Wrapper
 *
 * @author yaying.liu
 * @Date 2020-06-29 17:01:20
 */
public class LiveRecordWrapper extends BaseControllerWrapper{

    public LiveRecordWrapper(Map<String, Object> single) {
            super(single);
        }

        public LiveRecordWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public LiveRecordWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public LiveRecordWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            String status=(String)map.get("status");

            map.put("statusValue",status==null?"":status.equals("Y")?"已打款":"待打款");

        }
}