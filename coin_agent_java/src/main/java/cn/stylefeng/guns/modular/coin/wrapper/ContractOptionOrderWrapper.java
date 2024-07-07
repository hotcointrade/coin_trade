package cn.stylefeng.guns.modular.coin.wrapper;

import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 合约交易对Wrapper
 *
 * @author yaying.liu
 * @since 2020-08-25 10:49:35
 */
public class ContractOptionOrderWrapper extends BaseControllerWrapper{

    public ContractOptionOrderWrapper(Map<String, Object> single) {
            super(single);
        }

        public ContractOptionOrderWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public ContractOptionOrderWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public ContractOptionOrderWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId = (Long) map.get("memberId");
            Member member = F.me().getMember(memberId);

            map.put("memberIdValue", member!=null?member.getAccount():memberId);
        }
}
