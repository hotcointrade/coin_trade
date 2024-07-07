package cn.stylefeng.guns.modular.otc.wrapper;

import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 退还押金Wrapper
 *
 * @author yaying.liu
 * @since 2020-07-09 14:33:04
 */
public class BackWrapper extends BaseControllerWrapper
{

    public BackWrapper(Map<String, Object> single)
    {
        super(single);
    }

    public BackWrapper(List<Map<String, Object>> multi)
    {
        super(multi);
    }

    public BackWrapper(Page<Map<String, Object>> page)
    {
        super(page);
    }

    public BackWrapper(PageResult<Map<String, Object>> pageResult)
    {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map)
    {
        Long memberId = (Long) map.get("memberId");
        Member member = F.me().getMember(memberId);
        map.put("memberIdValue",member==null?"":member.getAccount());

        String status=(String)map.get("status");
        if (status != null) {
            for (ProConst.WithdrawStatusEnum e : ProConst.WithdrawStatusEnum.values()) {
                if(e.getCode().equals(status))
                {
                    map.put("statusValue",e.getValue());
                    break;
                }
            }
        }
    }
}