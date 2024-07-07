package cn.stylefeng.guns.modular.meta_data.wrapper;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateUtil;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class QuartzTaskRecordsWrappeer extends BaseControllerWrapper {
    public QuartzTaskRecordsWrappeer(Map<String, Object> single) {
        super(single);
    }

    public QuartzTaskRecordsWrappeer(List<Map<String, Object>> multi) {
        super(multi);
    }

    public QuartzTaskRecordsWrappeer(Page<Map<String, Object>> page) {
        super(page);
    }

    public QuartzTaskRecordsWrappeer(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {

        map.put("statusName",ConstantFactory.me().getDictsByName("任务执行状态",(String)map.get("taskStatus")));

        Long executeTime=(Long)map.get("executeTime");
        Long lastModifyTime=(Long)map.get("lastModifyTime");
        map.put("times",(lastModifyTime-executeTime));
        String timeKeyValue= (String)map.get("timeKeyValue");
        SimpleDateFormat sdf=new SimpleDateFormat(timeKeyValue);
        map.put("executeTimeValue",sdf.format(executeTime));

    }
}
