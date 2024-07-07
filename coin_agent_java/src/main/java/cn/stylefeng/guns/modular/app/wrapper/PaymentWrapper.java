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
 * 收款方式Wrapper
 *
 * @author yaying.liu
 * @Date 2020-03-12 11:23:03
 */
public class PaymentWrapper extends BaseControllerWrapper{

    public PaymentWrapper(Map<String, Object> single) {
            super(single);
        }

        public PaymentWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public PaymentWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public PaymentWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

            Long memberId=(Long)map.get("memberId");
            map.put("memberIdValue", F.me().getMember(memberId).getAccount());
            String status = (String) map.get("type");
            if (status != null) {
                for (ProConst.PayTypeEnum e : ProConst.PayTypeEnum.values()) {
                    if(e.code.equals(status))
                    {
                        map.put("typeValue",e.value);
                    }
                }
            }

            //图片
            String img = (String) map.get("img");

            List<Map<String, Object>> list = ImgToList.toList(img);

            List<String> imgList=new ArrayList<>();
            if(null!=list)
            {
                for (Map<String,Object> p:list) {
                    imgList.add((String)p.get("src"));
                }
            }
            map.put("imgs",imgList);
        }
}