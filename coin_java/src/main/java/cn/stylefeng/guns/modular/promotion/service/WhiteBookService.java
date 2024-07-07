package cn.stylefeng.guns.modular.promotion.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.promotion.entity.Problem;
import cn.stylefeng.guns.modular.promotion.entity.WhiteBook;
import cn.stylefeng.guns.modular.promotion.mapper.WhiteBookMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
public  class WhiteBookService extends ServiceImpl<WhiteBookMapper, WhiteBook>{

    /**
    * 查询
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除
    */
    public void deleteProblem(Long id) {
       this.baseMapper.deleteById(id);
    }

    /**
    * 添加
    */
    public void addProblem(WhiteBook problem) {
        this.baseMapper.insert(problem);
    }
}