package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.fin.entity.Contract;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 合约账户Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:09:49
 */
public class ContractWrapper extends BaseControllerWrapper{

    public ContractWrapper(Map<String, Object> single) {
            super(single);
        }

        public ContractWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public ContractWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public ContractWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId=(Long)map.get("memberId");
            String type = (String) map.get("type");
            if(memberId!=null)
            {
                Member member= F.me().getMember(memberId);
                map.put("memberIdValue",member==null?"无":member.getAccount());
                Contract contractObj=(Contract) F.me().redisUtil.get(String.format(Constant.CONTRACT_CODE, type) + memberId);
                map.put("worthPrice",contractObj!=null?contractObj.getWorthPrice().toPlainString():0);

            }

            //typeValue

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