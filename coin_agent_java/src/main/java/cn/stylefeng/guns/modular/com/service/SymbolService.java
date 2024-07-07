package cn.stylefeng.guns.modular.com.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.Symbol;
import cn.stylefeng.guns.modular.com.mapper.SymbolMapper;
/**
 * 币种Service
 *
 * @author yaying.liu
 * @Date 2020-03-09 11:03:53
 */
@Service
public  class SymbolService extends ServiceImpl<SymbolMapper,Symbol>{

    /**
    * 查询币种
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除币种
    */
    public void deleteSymbol(Long symbolId) {
        Symbol entity=this.baseMapper.selectById(symbolId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加币种
    */
    public void addSymbol(Symbol symbol) {
        this.baseMapper.insert(symbol);
    }
}