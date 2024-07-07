package cn.stylefeng.guns.modular.coin.wrapper;

import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 现货交易对Wrapper
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
public class OptionWrapper extends BaseControllerWrapper
{

    public OptionWrapper(Map<String, Object> single)
    {
        super(single);
    }

    public OptionWrapper(List<Map<String, Object>> multi)
    {
        super(multi);
    }

    public OptionWrapper(Page<Map<String, Object>> page)
    {
        super(page);
    }

    public OptionWrapper(PageResult<Map<String, Object>> pageResult)
    {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map)
    {
        Long memberId = (Long) map.get("memberId");
        if (memberId != null)
        {
            Member member = F.me().getMember(memberId);
            map.put("memberIdValue",
                    member!=null?member.getAccount():memberId);
        }




    }
}
