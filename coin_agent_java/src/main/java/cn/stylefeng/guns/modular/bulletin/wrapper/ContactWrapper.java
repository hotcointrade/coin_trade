package cn.stylefeng.guns.modular.bulletin.wrapper;

import cn.stylefeng.guns.modular.base.util.DateTimeUtil;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ContactWrapper extends BaseControllerWrapper {
    public ContactWrapper(Map<String, Object> single) {
        super(single);
    }

    public ContactWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ContactWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ContactWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Date start = (Date) map.get("start");
        Date end = (Date) map.get("end");

        String startTime = DateTimeUtil.dateToStr(start, "HH:mm");

        String endTime=DateTimeUtil.dateToStr(end,"HH:mm");

        map.put("startTime",startTime);
        map.put("endTime",endTime);

    }
}
