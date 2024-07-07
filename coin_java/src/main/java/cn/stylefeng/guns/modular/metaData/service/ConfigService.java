package cn.stylefeng.guns.modular.metaData.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.metaData.entity.Config;
import cn.stylefeng.guns.modular.metaData.mapper.ConfigMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConfigService extends ServiceImpl<ConfigMapper, Config> {

    public Page<Map<String, Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition);
    }

    public Page<Map<String, Object>> selectByConditionCommon(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByConditionCommon(page, condition);
    }
    public Page<Map<String, Object>> selectByConditionTeam(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByConditionTeam(page, condition);
    }
    public Page<Map<String, Object>> selectByConditionType(String condition,String configType) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByConditionType(page, condition,configType);
    }
    public Page<Map<String, Object>> selectByConditionCheckIn(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByConditionCheckIn(page, condition);
    }
    public Page<Map<String, Object>> selectByConditionContractOption(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByConditionContractOption(page, condition);
    }
    public List<Config> selectByConditionTeamNoPage() {
        return this.baseMapper.selectByConditionTeamNoPage();
    }
    public Page<Map<String, Object>> selectByConditionSms(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByConditionSms(page, condition);
    }
    public Page<Map<String, Object>> selectByConditionEmail(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByConditionEmail(page, condition);
    }
    public Page<Map<String, Object>> selectByConditionDomain(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByConditionDomain(page, condition);
    }


    public void deleteConfig(Long configId) {
        Config config = this.baseMapper.selectById(configId);
        //将删除标志设置为Y，表示删除
        config.setDelFlag("Y");
        this.baseMapper.updateById(config);
    }

    /**
     * 添加系统参数
     *
     * @param config
     */
    public void addConfig(Config config) {
        config.setType("SYSTEM");
        this.baseMapper.insert(config);
    }

    /**
     * 添加公共参数
     *
     * @param config
     */
    public void addConfigCommon(Config config) {
        config.setType("COMMON");
        this.baseMapper.insert(config);
    }

}
