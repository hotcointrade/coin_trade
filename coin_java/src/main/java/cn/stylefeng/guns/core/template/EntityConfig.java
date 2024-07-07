package cn.stylefeng.guns.core.template;

import cn.stylefeng.guns.modular.metaData.model.TableDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *  实体配置类
 */
@Data
public class EntityConfig {

    private ContextConfig contextConfig;
    private String entityPathTemplate;
    private String packageName;//包名称
    private List<String> entityImports;//所引入的包
    private List<TableDto> tableDtoList;//所引入的包

    public void init() {
        ArrayList<String> imports = new ArrayList<>();
        imports.add("com.baomidou.mybatisplus.annotation.*");
        imports.add("lombok.Data");
        imports.add("java.io.Serializable");
        imports.add("cn.stylefeng.guns.modular.base.entity.BaseEntity");
        imports.add("lombok.experimental.Accessors");
        this.tableDtoList=contextConfig.getTableFieldList();
        this.entityImports = imports;
        this.packageName = "cn.stylefeng.guns.modular." + contextConfig.getModuleName() + ".entity";
        this.entityPathTemplate = "\\src\\main\\java\\cn\\stylefeng\\guns\\modular\\" + contextConfig.getModuleName() + "\\entity\\{}.java";
    }
}
