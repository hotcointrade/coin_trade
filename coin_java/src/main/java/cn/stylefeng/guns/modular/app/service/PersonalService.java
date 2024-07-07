package cn.stylefeng.guns.modular.app.service;

import cn.stylefeng.guns.modular.base.util.*;
import cn.stylefeng.guns.modular.bulletin.entity.*;
import cn.stylefeng.guns.modular.bulletin.service.ArticleService;
import cn.stylefeng.guns.modular.bulletin.service.ContactService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class PersonalService {

    @Autowired
    RedisUtil redisUtil;


    @Autowired
    ArticleService articleService;

    @Autowired
    ContactService contactService;

    /**
     * 获取文章
     *
     * @param articleType
     * @return
     */
    public Result getArticle(String articleType) {
        if (articleType == null || StringUtils.isEmpty(articleType)) {
            return Result.fail(5001, "类型不能为空");
        }
        Article article = new Article();
        article.setArticleType(articleType);
        article.setDel("N");
        Article result = articleService.getOne(new QueryWrapper<>(article));
        if (result == null) {
            return Result.fail(404, "未找到业务文章，请联系管理员");
        }
        if (result.getContent() == null || "".equals(result.getContent().trim())) {
            return Result.fail(404, "未找到业务文章，请联系管理员");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("content", result.getContent());
        map.put("articleType", result.getArticleType());
        return Result.success(map);
    }


    public Result contact() {
        Contact query = new Contact();
        query.setStatus("ENABLE");
        Contact contact = contactService.getOne(new QueryWrapper<>(query));
        Map<String, Object> map = new HashMap<>();
        map.put("phone", contact.getPhone());
        String startTime = DateTimeUtil.dateToStr(contact.getStart(), "HH:mm");
        String endTime = DateTimeUtil.dateToStr(contact.getEnd(), "HH:mm");
        map.put("online", startTime + "-" + endTime);
        if (contact == null) {
            return Result.fail(404, "未找到客服信息，请联系管理员");
        }
        return Result.success(map);
    }


}


