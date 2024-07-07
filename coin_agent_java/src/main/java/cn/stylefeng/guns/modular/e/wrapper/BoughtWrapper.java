package cn.stylefeng.guns.modular.e.wrapper;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 法币交易Wrapper
 *
 * @author yaying.liu
 * @Date 2020-04-03 09:48:13
 */
public class BoughtWrapper extends BaseControllerWrapper{

    public BoughtWrapper(Map<String, Object> single) {
            super(single);
        }

        public BoughtWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public BoughtWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public BoughtWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId = (Long) map.get("memberId");
            map.put("memberIdValue", F.me().getMember(memberId).getAccount());
            String status = (String) map.get("status");
            if (status != null) {
                for (ProConst.LegalStatus e : ProConst.LegalStatus.values()) {
                    if(e.code.equals(status))
                    {
                        map.put("statusValue",e.value);

                    }
                }
            }

        }
}