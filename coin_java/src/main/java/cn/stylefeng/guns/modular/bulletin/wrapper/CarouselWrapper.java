package cn.stylefeng.guns.modular.bulletin.wrapper;

import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.util.ImgToList;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 轮播图Wrapper
 *
 * @author yaying.liu
 * @Date 2019-08-14 10:52:55
 */
public class CarouselWrapper extends BaseControllerWrapper{

    public CarouselWrapper(Map<String, Object> single) {
        super(single);
    }

    public CarouselWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public CarouselWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public CarouselWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {


        String type=(String)map.get("type");
        if (type != null) {
            for (ProConst.CarouselType e : ProConst.CarouselType.values()) {
                if(e.getCode().equals(type))
                {
                    map.put("typeValue",e.getValue());
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