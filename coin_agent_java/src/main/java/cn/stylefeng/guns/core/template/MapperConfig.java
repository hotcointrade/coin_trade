package cn.stylefeng.guns.core.template;

import cn.stylefeng.guns.modular.meta_data.model.TableDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper模板生成的配置
 *
 */
public class MapperConfig {

    private ContextConfig contextConfig;

    private String mapperPathTemplate;
    private String xmlPathTemplate;

    private String packageName;
    private List<TableDto> tableDtoList;//所引入的包

    private String entityPackage;



    private List<String> mapperImports;
    public void init() {
        ArrayList<String> imports = new ArrayList<>();
        imports.add("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        imports.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
        imports.add("org.apache.ibatis.annotations.Param");
        imports.add("java.util.Map");
        this.tableDtoList=contextConfig.getTableFieldList();
        this.mapperImports = imports;
        this.entityPackage="cn.stylefeng.guns.modular." + contextConfig.getModuleName() + ".entity";
        this.mapperPathTemplate = "\\src\\main\\java\\cn\\stylefeng\\guns\\modular\\" + contextConfig.getModuleName() + "\\mapper\\{}Mapper.java";
        this.xmlPathTemplate = "\\src\\main\\java\\cn\\stylefeng\\guns\\modular\\" + contextConfig.getModuleName() + "\\mapper\\mapping\\{}Mapper.xml";
        this.packageName = "cn.stylefeng.guns.modular." + contextConfig.getModuleName() + ".mapper";
    }

    public List<TableDto> getTableDtoList() {
        return tableDtoList;
    }

    public void setTableDtoList(List<TableDto> tableDtoList) {
        this.tableDtoList = tableDtoList;
    }

    public String getEntityPackage() {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMapperPathTemplate() {
        return mapperPathTemplate;
    }

    public void setMapperPathTemplate(String mapperPathTemplate) {
        this.mapperPathTemplate = mapperPathTemplate;
    }

    public String getXmlPathTemplate() {
        return xmlPathTemplate;
    }

    public void setXmlPathTemplate(String xmlPathTemplate) {
        this.xmlPathTemplate = xmlPathTemplate;
    }

    public ContextConfig getContextConfig() {
        return contextConfig;
    }

    public void setContextConfig(ContextConfig contextConfig) {
        this.contextConfig = contextConfig;
    }
    public List<String> getMapperImports() {
        return mapperImports;
    }

    public void setMapperImports(List<String> mapperImports) {
        this.mapperImports = mapperImports;
    }
}