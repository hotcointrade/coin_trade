package cn.stylefeng.guns.modular.meta_data.wrapper;

import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class QuartzTaskInformationsWrappeer extends BaseControllerWrapper {
    public QuartzTaskInformationsWrappeer(Map<String, Object> single) {
        super(single);
    }

    public QuartzTaskInformationsWrappeer(List<Map<String, Object>> multi) {
        super(multi);
    }

    public QuartzTaskInformationsWrappeer(Page<Map<String, Object>> page) {
        super(page);
    }

    public QuartzTaskInformationsWrappeer(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("statusName",ConstantFactory.me().getDictsByName("任务状态",(String)map.get("frozenStatus")));
         String executeNo=   (String)map.get("executorNo");
        map.put("executorNoValue",ConstantFactory.me().getUserNameById(Long.parseLong(executeNo)));

        SimpleDateFormat sdf=new SimpleDateFormat((String)map.get("timeKey"));
        map.put("createTimeValue",sdf.format(map.get("createTime")));
        map.put("lastModifyTimeValue",sdf.format(map.get("lastModifyTime")));

    }
}
