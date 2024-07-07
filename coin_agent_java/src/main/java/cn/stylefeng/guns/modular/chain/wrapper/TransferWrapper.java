package cn.stylefeng.guns.modular.chain.wrapper;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 转账信息Wrapper
 *
 * @author yaying.liu
 * @Date 2020-01-14 17:08:46
 */
public class TransferWrapper extends BaseControllerWrapper{

    public TransferWrapper(Map<String, Object> single) {
            super(single);
        }

        public TransferWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public TransferWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public TransferWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId=(Long)map.get("memberId");
            map.put("memberId", F.me().getMember(memberId).getAccount());
        }
}