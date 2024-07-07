package cn.stylefeng.guns.modular.promotion.wrapper;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 网关定义Wrapper
 *
 * @author yaying.liu
 * @Date 2019-10-11 11:20:42
 */
public class GatewayDefineWrapper extends BaseControllerWrapper{

    public GatewayDefineWrapper(Map<String, Object> single) {
            super(single);
        }

        public GatewayDefineWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public GatewayDefineWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public GatewayDefineWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            String status = (String) map.get("status");
            if (status != null) {
                for (ProConst.GatewayStatusEnum e : ProConst.GatewayStatusEnum.values()) {
                    if(e.getCode().equals(status))
                    {
                        map.put("statusValue",e.getValue());

                    }
                }
            }
        }
}