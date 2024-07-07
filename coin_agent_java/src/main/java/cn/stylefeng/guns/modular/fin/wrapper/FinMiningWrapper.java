package cn.stylefeng.guns.modular.fin.wrapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 矿机账户Wrapper
 *
 * @author yaying.liu
 * @since 2022-06-07 13:37:29
 */
public class FinMiningWrapper extends BaseControllerWrapper{

    public FinMiningWrapper(Map<String, Object> single) {
            super(single);
        }

        public FinMiningWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public FinMiningWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public FinMiningWrapper(PageResult<Map<String, Object>> pageResult) {
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
