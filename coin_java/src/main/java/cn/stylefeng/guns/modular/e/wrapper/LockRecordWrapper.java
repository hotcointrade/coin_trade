package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 锁仓记录Wrapper
 *
 * @author yaying.liu
 * @since 2020-09-25 10:46:59
 */
public class LockRecordWrapper extends BaseControllerWrapper{

    public LockRecordWrapper(Map<String, Object> single) {
            super(single);
        }

        public LockRecordWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public LockRecordWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public LockRecordWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId=(Long)map.get("memberId");
            map.put("memberIdValue", F.me().getMember(memberId).getAccount());
        }
}