package cn.stylefeng.guns.modular.chain.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Transfer;
import cn.stylefeng.guns.modular.chain.mapper.TransferMapper;
/**
 * 转账信息Service
 *
 * @author yaying.liu
 * @Date 2020-01-14 17:08:46
 */
@Service
public  class TransferService extends ServiceImpl<TransferMapper,Transfer>{

    /**
    * 查询转账信息
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除转账信息
    */
    public void deleteTransfer(Long transferId) {
        Transfer entity=this.baseMapper.selectById(transferId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加转账信息
    */
    public void addTransfer(Transfer transfer) {
        this.baseMapper.insert(transfer);
    }
}