package cn.stylefeng.guns.modular.promotion.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.promotion.entity.Problem;
import cn.stylefeng.guns.modular.promotion.mapper.ProblemMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 常见问题Service
 *
 * @author yaying.liu
 * @Date 2019-10-14 14:11:51
 */
@Service
public  class ProblemService extends ServiceImpl<ProblemMapper,Problem>{

    /**
    * 查询常见问题
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除常见问题
    */
    public void deleteProblem(Long problemId) {
        Problem entity=this.baseMapper.selectById(problemId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加常见问题
    */
    public void addProblem(Problem problem) {
        this.baseMapper.insert(problem);
    }
}