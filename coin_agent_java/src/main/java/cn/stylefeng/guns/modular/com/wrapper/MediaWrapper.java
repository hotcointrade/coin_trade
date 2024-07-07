package cn.stylefeng.guns.modular.com.wrapper;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 视频和音频Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:44:00
 */
public class MediaWrapper extends BaseControllerWrapper{

    public MediaWrapper(Map<String, Object> single) {
            super(single);
        }

        public MediaWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public MediaWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public MediaWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            String status = (String) map.get("type");
            if (status != null) {
                for (ProConst.MediaTypeEnum e : ProConst.MediaTypeEnum.values()) {
                    if(e.getCode().equals(status))
                    {
                        map.put("typeValue",e.getValue());

                    }
                }
            }
        }
}