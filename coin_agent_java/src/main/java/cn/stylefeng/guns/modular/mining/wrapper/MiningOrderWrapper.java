package cn.stylefeng.guns.modular.mining.wrapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.mining.entity.Mining;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 矿机订单Wrapper
 *
 * @author yaying.liu
 * @since 2022-06-08 21:02:12
 */
public class MiningOrderWrapper extends BaseControllerWrapper{

    public MiningOrderWrapper(Map<String, Object> single) {
            super(single);
        }

        public MiningOrderWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public MiningOrderWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public MiningOrderWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long miningId = (Long) map.get("miningId");
            Mining mining = F.me().getMining(miningId);

            map.put("image2", mining!=null?mining.getImage2():miningId);
            Long memberId = (Long) map.get("memberId");
            Member member = F.me().getMember(memberId);

            map.put("memberIdValue", member!=null?member.getAccount():memberId);
        }
}
