package cn.stylefeng.guns.modular.bulletin.service;
import cn.stylefeng.guns.modular.bulletin.entity.Article;
import cn.stylefeng.guns.modular.bulletin.mapper.ArticleMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;

/**
 * 简介文章Service
 *
 * @author yaying.liu
 * @Date 2019-08-16 14:30:41
 */
@Service
public  class ArticleService extends ServiceImpl<ArticleMapper,Article>{

    /**
     * 查询简介文章
     */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
     * 删除简介文章
     */
    public void deleteArticle(Long articleId) {
        Article entity=this.baseMapper.selectById(articleId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加简介文章
     */
    public void addArticle(Article article) {
        this.baseMapper.insert(article);
    }
}