package cn.stylefeng.guns.modular.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.entity.AppCarousel;
import cn.stylefeng.guns.modular.bulletin.mapper.AppCarouselMapper;
import cn.stylefeng.guns.modular.bulletin.model.AppCarouselDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppCarouselService extends ServiceImpl<AppCarouselMapper, AppCarousel> {


    public Result getList(String type,String showType) {
        AppCarousel carousel=new AppCarousel();
        carousel.setDel("N").setType(type).setShowType(showType);
        List<AppCarousel> list = this.list(new QueryWrapper<>(carousel));
        List<AppCarouselDto> dtoList=new ArrayList<>();
        if (list.size() >0) {
            for(AppCarousel entity:list)
            {
                AppCarouselDto dto=new AppCarouselDto();
                BeanUtil.copyProperties(entity,dto);
                String content=entity.getContent();
                if(content!=null&&StringUtils.isNotEmpty(content)){
                    dto.setLink(F.me().getSysConfigValueByCode("URL")+"/running/article/carouse/"+entity.getCarouselId());
                }else{
                    dto.setLink(entity.getLink());
                }
                dtoList.add(dto);
            }
        }
        return Result.success(dtoList);
    }

    /**
     * 查询轮播图
     */
    public Page<Map<String, Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition);
    }

    public Result detail(Long id) {
        AppCarousel carousel = this.getById(id);
        Map<String,Object> map=new HashMap<>();
//        map.put("content",carousel.getContent());
        String content=carousel.getContent();
        if(content!=null&&StringUtils.isNotEmpty(content))
        {
            map.put("link", F.me().getSysConfigValueByCode("URL")+"/running/article/carouse/"+carousel.getCarouselId());
        }
        else
        {
            map.put("link",carousel.getLink());
        }
//        map.put("ownerLink","/running/article/carouse/"+carousel.getCarouselId());
//        map.put("link",carousel.getLink());
        return Result.success(map);
    }

}
