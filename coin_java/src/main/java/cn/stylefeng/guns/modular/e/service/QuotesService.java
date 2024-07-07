package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Quotes;
import cn.stylefeng.guns.modular.e.mapper.QuotesMapper;
/**
 * 行情涨跌控制Service
 *
 * @author yaying.liu
 * @Date 2020-03-23 11:44:38
 */
@Service
public  class QuotesService extends ServiceImpl<QuotesMapper,Quotes>{

    /**
    * 查询行情涨跌控制
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除行情涨跌控制
    */
    public void deleteQuotes(Long quotesId) {
        Quotes entity=this.baseMapper.selectById(quotesId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加行情涨跌控制
    */
    public void addQuotes(Quotes quotes) {
        this.baseMapper.insert(quotes);
    }
}