package cn.stylefeng.guns.modular.bulletin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.base.util.RandomUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.entity.Bank;
import cn.stylefeng.guns.modular.bulletin.mapper.BankMapper;
import cn.stylefeng.guns.modular.bulletin.model.BankDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BankService extends ServiceImpl<BankMapper, Bank> {

    /**
     * 获取银行类别列表
     *
     * @return
     */
    public Result apiList() {
        Bank query = new Bank();
        query.setDel("N");
        query.setStatus("Y");
        List<Bank> list = this.list(new QueryWrapper<>(query));
        if (list.size() == 0) {
            return Result.fail(404, "银行类别未找到，请联系管理员");
        }
        List<BankDto> dtoList=new ArrayList<>();
        for (Bank entity : list) {
            BankDto dto=new BankDto();
            BeanUtil.copyProperties(entity,dto);
            dtoList.add(dto);
        }
        return Result.success("返回结果",200,dtoList);
    }


    /**
     * 查询银行列表
     */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
     * 删除银行列表
     */
    public void deleteBank(Long bankId) {
        Bank entity=this.baseMapper.selectById(bankId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加银行列表
     */
    public void addBank(Bank bank) {

        bank.setCode(RandomUtil.code("B"));
        this.baseMapper.insert(bank);
    }

}
