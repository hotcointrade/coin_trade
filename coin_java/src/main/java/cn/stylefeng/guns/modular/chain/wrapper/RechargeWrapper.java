package cn.stylefeng.guns.modular.chain.wrapper;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 充币记录Wrapper
 *
 * @author yaying.liu
 * @Date 2020-01-14 16:00:54
 */
public class RechargeWrapper extends BaseControllerWrapper{

    public RechargeWrapper(Map<String, Object> single) {
            super(single);
        }

        public RechargeWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public RechargeWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public RechargeWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId=(Long)map.get("memberId");
            map.put("memberIdValue", F.me().getMember(memberId).getAccount());

            String status = (String) map.get("status");
            if (status != null) {
                for (ProConst.WithdrawStatusEnum e : ProConst.WithdrawStatusEnum.values()) {
                    if(e.getCode().equals(status))
                    {
                        map.put("statusValue",e.getValue());

                    }
                }
            }
        }
}