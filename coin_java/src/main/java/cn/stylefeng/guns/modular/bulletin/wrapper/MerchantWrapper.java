package cn.stylefeng.guns.modular.bulletin.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 商户Wrapper
 *
 * @author yaying.liu
 * @Date 2019-09-02 20:11:57
 */
public class MerchantWrapper extends BaseControllerWrapper{

    public MerchantWrapper(Map<String, Object> single) {
        super(single);
    }

    public MerchantWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public MerchantWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public MerchantWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Double rate= (Double)map.get("rate");
        if(rate!=null)
        {
            map.put("rateValue",rate+"%");
        }
    }
}