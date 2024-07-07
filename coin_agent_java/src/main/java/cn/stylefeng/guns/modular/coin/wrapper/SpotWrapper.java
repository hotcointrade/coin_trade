package cn.stylefeng.guns.modular.coin.wrapper;

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
public class SpotWrapper extends BaseControllerWrapper
{

    public SpotWrapper(Map<String, Object> single)
    {
        super(single);
    }

    public SpotWrapper(List<Map<String, Object>> multi)
    {
        super(multi);
    }

    public SpotWrapper(Page<Map<String, Object>> page)
    {
        super(page);
    }

    public SpotWrapper(PageResult<Map<String, Object>> pageResult)
    {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map)
    {
        String type = (String) map.get("type");
        if (type != null)
        {
            map.put("typeValue", type.equals("0") ? "平台币" : "火币");
        }

        map.put("spotFeeValue",new BigDecimal(map.get("spotFee").toString()).toPlainString()+"%");


    }
}