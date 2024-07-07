package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 生活支付开通记录Wrapper
 *
 * @author yaying.liu
 * @Date 2020-06-28 16:17:00
 */
public class LiveWrapper extends BaseControllerWrapper{

    public LiveWrapper(Map<String, Object> single) {
            super(single);
        }

        public LiveWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public LiveWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public LiveWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId = (Long) map.get("memberId");
            map.put("memberIdValue", F.me().getMember(memberId).getAccount());

        }
}