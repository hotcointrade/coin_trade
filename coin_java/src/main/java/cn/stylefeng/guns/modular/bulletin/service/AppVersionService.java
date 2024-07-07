package cn.stylefeng.guns.modular.bulletin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.entity.AppVersion;
import cn.stylefeng.guns.modular.bulletin.mapper.AppVersionMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppVersionService extends ServiceImpl<AppVersionMapper, AppVersion> {

    public Result version(AppVersion version) {
        if (version == null) {
            return Result.fail(404, "参数为空");
        }
        if (StringUtils.isBlank(version.getVersion())) {
            return Result.fail(404, "版本为空");
        }
        if (StringUtils.isBlank(version.getType())) {
            return Result.fail(404, "版本类型为空");
        }
        Map<String,Object> map=new HashMap<>();
        AppVersion query = new AppVersion();
        query.setType(version.getType());
        AppVersion versionR = this.getOne(new QueryWrapper<>(query));
        if(versionR==null)
        {
            return Result.fail(404,"版本信息为空，联系管理员");
        }
        if(versionR.getVersion().equals(version.getVersion()))
        {
            map.put("update","N");
            map.put("content",versionR.getContent());
            map.put("address",versionR.getAddress());
            map.put("version",versionR.getVersion());
        }
        else {
            map.put("update","Y");
            map.put("content",versionR.getContent());
            map.put("address",versionR.getAddress());
            map.put("version",versionR.getVersion());
        }
        return Result.success("返回结果",map);

    }

    /**
     * 查询版本号
     */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
     * 删除版本号
     */
    public void deleteAppVersion(Long appVersionId) {
        AppVersion entity=this.baseMapper.selectById(appVersionId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加版本号
     */
    public void addAppVersion(AppVersion appVersion) {
        this.baseMapper.insert(appVersion);
    }

}
