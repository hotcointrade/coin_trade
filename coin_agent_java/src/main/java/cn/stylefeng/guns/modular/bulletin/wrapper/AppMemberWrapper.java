package cn.stylefeng.guns.modular.bulletin.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
public class AppMemberWrapper extends BaseControllerWrapper{

    public AppMemberWrapper(Map<String, Object> single) {
        super(single);
    }

    public AppMemberWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public AppMemberWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public AppMemberWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String status=(String)map.get("status");
        map.put("statusValue","Y".equals(status)?"启用":"禁用");
    }
}
