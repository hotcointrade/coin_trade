package cn.stylefeng.guns.modular.bulletin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.PageUtils;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.entity.News;
import cn.stylefeng.guns.modular.bulletin.mapper.NewsMapper;
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
public class NewsService extends ServiceImpl<NewsMapper, News> {


    public Result getList() {

        News carousel=new News();
        carousel.setDel("N");
        List<News> list = this.list(new QueryWrapper<>(carousel));
        List< Map<String, Object>> dtoList=new ArrayList<>();

        if (list.size() > 0) {
            for(News entity:list)
            {
                Map<String, Object> map = new HashMap<>();
               map.put("carouselId",entity.getCarouselId());
               map.put("content",entity.getContent());
               map.put("title",entity.getTitle());
                dtoList.add(map);
            }
        }
        return Result.success(new PageUtils(dtoList.size(),dtoList));
    }

    /**
     * 查询轮播图
     */
    public Page<Map<String, Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition);
    }

    public Result detail(Long id) {
        News carousel = this.getById(id);
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

    public Result carousel() {
        News carousel=new News();
        carousel.setDel("N");
        carousel = this.getOne(new QueryWrapper<>(carousel).last("limit 1"));
        Map<String,Object> map=new HashMap<>();
        if(carousel!=null)
        {
            map.put("img",carousel.getImg());
        }
        return Result.success("轮播图",map);

    }
}
