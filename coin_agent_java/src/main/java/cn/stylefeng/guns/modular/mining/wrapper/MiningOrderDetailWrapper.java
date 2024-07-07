package cn.stylefeng.guns.modular.mining.wrapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.mining.entity.Mining;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 矿机收益Wrapper
 *
 * @author yaying.liu
 * @since 2022-06-07 13:36:19
 */
public class MiningOrderDetailWrapper extends BaseControllerWrapper{

    public MiningOrderDetailWrapper(Map<String, Object> single) {
            super(single);
        }

        public MiningOrderDetailWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public MiningOrderDetailWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public MiningOrderDetailWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId = (Long) map.get("memberId");
            Member member = F.me().getMember(memberId);

            map.put("memberIdValue", member!=null?member.getAccount():memberId);


            Long miningOrderId = (Long) map.get("miningOrderId");
            MiningOrder miningOrder = F.me().getMiningOrder(miningOrderId);

            map.put("miningName", miningOrder!=null?miningOrder.getName():miningOrderId);


            Long miningId =miningOrder.getMiningId();
            Mining mining = F.me().getMining(miningId);

            map.put("image2", mining!=null?mining.getImage2():miningId);
            map.put("image", mining!=null?mining.getImage():miningId);

        }
}
