package cn.stylefeng.guns.modular.e.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Date;
import java.util.Map;

import cn.stylefeng.guns.modular.e.entity.LiveRecord;
import cn.stylefeng.guns.modular.e.mapper.LiveRecordMapper;

/**
 * 生活支付打款Service
 *
 * @author yaying.liu
 * @Date 2020-06-29 17:01:20
 */
@Service
public class LiveRecordService extends ServiceImpl<LiveRecordMapper, LiveRecord>
{

    /**
     * 查询生活支付打款
     */
    public Page<Map<String, Object>> selectByCondition(String condition, String orderNo, String status)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition, orderNo, status);
    }

    /**
     * 删除生活支付打款
     */
    public void deleteLiveRecord(Long liveRecordId)
    {
        LiveRecord entity = this.baseMapper.selectById(liveRecordId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加生活支付打款
     */
    public void addLiveRecord(LiveRecord liveRecord)
    {
        this.baseMapper.insert(liveRecord);
    }

    public void finish(Long liveRecordId)
    {
        LiveRecord entity = this.baseMapper.selectById(liveRecordId);
        entity
                .setPayTime(new Date())
                .setStatus("Y");
        this.baseMapper.updateById(entity);

    }
}