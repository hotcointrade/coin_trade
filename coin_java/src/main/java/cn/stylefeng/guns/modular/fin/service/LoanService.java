package cn.stylefeng.guns.modular.fin.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.Loan;
import cn.stylefeng.guns.modular.fin.mapper.LoanMapper;
/**
 * 申请Service
 *
 * @author yaying.liu
 * @since 2022-10-18 20:42:53
 */
@Service
public  class LoanService extends ServiceImpl<LoanMapper,Loan>{

    /**
    * 查询申请
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除申请
    */
    public void deleteLoan(Long loanId) {
        Loan entity=this.baseMapper.selectById(loanId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加申请
    */
    public void addLoan(Loan loan) {
        this.baseMapper.insert(loan);
    }
}