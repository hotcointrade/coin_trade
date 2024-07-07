package cn.stylefeng.guns.modular.meta_data.constant.factory;

import cn.stylefeng.guns.modular.meta_data.entity.Config;
import cn.stylefeng.guns.modular.meta_data.mapper.ConfigMapper;
import cn.stylefeng.roses.core.util.SpringContextHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("springContextHolder")
public class MetaDataFactory implements IMetaDataFactory {

    private ConfigMapper configMapper=SpringContextHolder.getBean(ConfigMapper.class);

    public static IMetaDataFactory me(){
        return SpringContextHolder.getBean("metaDataFactory");
    }

    @Override
    public String getValueByCode(String code) {
        if (ToolUtil.isEmpty(code)) {
            return "";
        }else {
            Config param = new Config();
            param.setCode(code);
            param.setDelFlag("N");
            QueryWrapper<Config> queryWrapper = new QueryWrapper<>(param);
            Config config = configMapper.selectOne(queryWrapper);
            if (config == null) {
                return null;
            } else {
                return config.getValue();
            }
        }
    }
}
