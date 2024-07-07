package cn.stylefeng.guns.modular.com.wrapper;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 签到Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-09 16:28:04
 */
public class CheckInWrapper extends BaseControllerWrapper{

    public CheckInWrapper(Map<String, Object> single) {
            super(single);
        }

        public CheckInWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public CheckInWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public CheckInWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId=(Long)map.get("memberId");
            map.put("memberIdValue", F.me().getMember(memberId).getAccount());

        }
}