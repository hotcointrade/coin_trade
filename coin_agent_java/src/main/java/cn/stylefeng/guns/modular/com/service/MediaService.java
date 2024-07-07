package cn.stylefeng.guns.modular.com.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.Media;
import cn.stylefeng.guns.modular.com.mapper.MediaMapper;
/**
 * 视频和音频Service
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:44:00
 */
@Service
public  class MediaService extends ServiceImpl<MediaMapper,Media>{

    /**
    * 查询视频和音频
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除视频和音频
    */
    public void deleteMedia(Long mediaId) {
        Media entity=this.baseMapper.selectById(mediaId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加视频和音频
    */
    public void addMedia(Media media) {
        this.baseMapper.insert(media);
    }
}