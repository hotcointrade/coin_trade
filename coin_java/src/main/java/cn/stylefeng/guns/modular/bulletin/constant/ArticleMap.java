package cn.stylefeng.guns.modular.bulletin.constant;

import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 简介文章Map
 *
 * @author yaying.liu
 * @Date 2019-08-16 14:30:41
 */
public class ArticleMap extends AbstractDictMap {

    @Override
    public void init() {
        put("articleId","简介文章id");
    }

    @Override
    protected void initBeWrapped() {

    }
}