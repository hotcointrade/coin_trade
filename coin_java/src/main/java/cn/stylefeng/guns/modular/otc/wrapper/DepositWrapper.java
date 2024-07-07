package cn.stylefeng.guns.modular.otc.wrapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 用户押金Wrapper
 *
 * @author yaying.liu
 * @since 2020-07-01 11:55:46
 */
public class DepositWrapper extends BaseControllerWrapper{

    public DepositWrapper(Map<String, Object> single) {
            super(single);
        }

        public DepositWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public DepositWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public DepositWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId=(Long)map.get("memberId");
            Member member= F.me().getMember(memberId);
            map.put("memberIdValue",member!=null?member.getAccount():"");
            map.put("deposit",member!=null?member.getDeposit():"");

            String status=(String)map.get("status");
            map.put("statusValue",status!=null&&status.equals("1")?"全部缴纳":"待补缴");

             String remark=(String)map.get("remark");
            if (remark != null) {
                for (ProConst.WithdrawStatusEnum e : ProConst.WithdrawStatusEnum.values()) {
                    if(e.getCode().equals(remark))
                    {
                        map.put("remarkValue",e.getValue());
                        break;
                    }
                }
            }



        }
}