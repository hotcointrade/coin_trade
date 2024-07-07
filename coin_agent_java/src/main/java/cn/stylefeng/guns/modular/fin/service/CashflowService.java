package cn.stylefeng.guns.modular.fin.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.Cashflow;
import cn.stylefeng.guns.modular.fin.mapper.CashflowMapper;
/**
 * 流水记录Service
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:14
 */
@Service
public  class CashflowService extends ServiceImpl<CashflowMapper,Cashflow>{

    /**
    * 查询流水记录
    */
    public Page<Map<String,Object>> selectByCondition(String condition,String flowType,String itemCode,Long memberId
            ,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,flowType,itemCode,memberId,recommendIds);
    }

    /**
    * 删除流水记录
    */
    public void deleteCashflow(Long cashflowId) {
        Cashflow entity=this.baseMapper.selectById(cashflowId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加流水记录
    */
    public void addCashflow(Cashflow cashflow) {
        this.baseMapper.insert(cashflow);
    }
}