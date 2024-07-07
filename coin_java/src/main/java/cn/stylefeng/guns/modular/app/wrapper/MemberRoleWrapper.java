package cn.stylefeng.guns.modular.app.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 用户角色关系Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-06 10:18:16
 */
public class MemberRoleWrapper extends BaseControllerWrapper{

    public MemberRoleWrapper(Map<String, Object> single) {
            super(single);
        }

        public MemberRoleWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public MemberRoleWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public MemberRoleWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}