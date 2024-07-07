package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 申请Wrapper
 *
 * @author yaying.liu
 * @since 2022-10-18 20:42:53
 */
public class LoanWrapper extends BaseControllerWrapper{

    public LoanWrapper(Map<String, Object> single) {
            super(single);
        }

        public LoanWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public LoanWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public LoanWrapper(PageResult<Map<String, Object>> pageResult) {
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
        }
}
