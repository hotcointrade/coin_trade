package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * 账户信息Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-12 17:05:14
 */
public class WalletWrapper extends BaseControllerWrapper{

    public WalletWrapper(Map<String, Object> single) {
            super(single);
        }

        public WalletWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public WalletWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public WalletWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Long memberId=(Long)map.get("memberId");
        if(memberId!=null)
        {
            Member member= F.me().getMember(memberId);
            map.put("memberIdValue",member==null?"无":member.getAccount());
        }

        BigDecimal lockedPrice=(BigDecimal)map.get("lockedPrice");
        map.put("lockedPrice",lockedPrice.toPlainString());


        String remark=(String)map.get("remark");
        if(remark!=null)
        {
            for (ProConst.WalletBigType e : ProConst.WalletBigType.values()) {
                if(e.code.equals(remark))
                {
                    map.put("remarkValue",e.value);
                }
            }
        }
    }
}