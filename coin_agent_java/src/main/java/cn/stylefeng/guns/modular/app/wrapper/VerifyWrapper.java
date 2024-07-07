package cn.stylefeng.guns.modular.app.wrapper;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.ImgToList;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 实名认证Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-11 16:31:58
 */
public class VerifyWrapper extends BaseControllerWrapper{

    public VerifyWrapper(Map<String, Object> single) {
            super(single);
        }

        public VerifyWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public VerifyWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public VerifyWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {
            Long memberId=(Long)map.get("memberId");
            map.put("memberIdValue", F.me().getMember(memberId).getAccount());
            //图片
            String img =  map.get("frontImg")+","+map.get("backImg");

            List<Map<String, Object>> list = ImgToList.toList(img);

            List<String> imgList=new ArrayList<>();
            if(null!=list)
            {
                for (Map<String,Object> p:list) {
                    imgList.add((String)p.get("src"));
                }
            }
            map.put("imgs",imgList);

            String status = (String) map.get("status");
            if (status != null) {
                for (ProConst.Status e : ProConst.Status.values()) {
                    if(e.code.equals(status))
                    {
                        map.put("statusValue",e.value);
                    }
                }
            }

        }
}