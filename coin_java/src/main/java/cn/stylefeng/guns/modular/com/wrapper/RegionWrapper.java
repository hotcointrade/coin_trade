package cn.stylefeng.guns.modular.com.wrapper;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 地区Wrapper
 *
 * @author yaying.liu
 * @Date 2019-12-06 11:32:41
 */
public class RegionWrapper extends BaseControllerWrapper{

    public RegionWrapper(Map<String, Object> single) {
            super(single);
        }

        public RegionWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public RegionWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public RegionWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

            Long parentId = (Long) map.get("parentId");
            if (parentId != null&&parentId.longValue()!=Constant.SYS_PLATFORM) {
                map.put("parentIdValue", F.me().getRegion(parentId).getName());
            }
            else {
                map.put("parentIdValue","中国");
            }
            Long levelType = (Long) map.get("levelType");
            if (levelType != null) {
                for (ProConst.RegionLevelType e : ProConst.RegionLevelType.values()) {
                    if(e.getCode().equals(levelType))
                    {
                        map.put("levelTypeValue",e.getValue());

                    }
                }
            }

        }
}