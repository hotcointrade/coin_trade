package cn.stylefeng.guns.core.template.engine.base;


import cn.stylefeng.guns.core.template.*;
import lombok.Data;

/**
 * 模板生成父类
 */
@Data
public class AbstractTemplateEngine {

    protected ContextConfig contextConfig;                //全局配置
    protected ControllerConfig controllerConfig;          //控制器的配置
    protected PageConfig pageConfig;                      //页面的控制器
    protected MapperConfig mapperConfig;                  //Mapper配置
    protected ServiceConfig serviceConfig;                //Service配置
    protected WrapperConfig wrapperConfig;                //Wrapper配置
    protected MapConfig mapConfig;                          //Map配置
    protected MenuSqlConfig menuSqlConfig;                 //菜单sql配置
    protected EntityConfig entityConfig;                 //entity配置


    public void initConfig() {
        if (this.contextConfig == null) {
            this.contextConfig = new ContextConfig();
        }
        if (this.controllerConfig == null) {
            this.controllerConfig = new ControllerConfig();
        }
        if (this.pageConfig == null) {
            this.pageConfig = new PageConfig();
        }
        if (this.mapperConfig == null) {
            this.mapperConfig = new MapperConfig();
        }
        if (this.serviceConfig == null) {
            this.serviceConfig = new ServiceConfig();
        }
        if (this.wrapperConfig == null) {
            this.wrapperConfig = new WrapperConfig();
        }
         if (this.mapConfig == null) {
            this.mapConfig = new MapConfig();
        }
        if(this.menuSqlConfig==null)
        {
            this.menuSqlConfig=new MenuSqlConfig();
        }
        if(this.entityConfig==null)
        {
            this.entityConfig=new EntityConfig();
        }
        this.controllerConfig.setContextConfig(this.contextConfig);
        this.controllerConfig.init();

        this.serviceConfig.setContextConfig(this.contextConfig);
        this.serviceConfig.init();

        this.mapperConfig.setContextConfig(this.contextConfig);
        this.mapperConfig.init();

        this.pageConfig.setContextConfig(this.contextConfig);
        this.pageConfig.init();

        this.wrapperConfig.setContextConfig(this.contextConfig);
        this.wrapperConfig.init();

        this.mapConfig.setContextConfig(this.contextConfig);
        this.mapConfig.init();

        this.menuSqlConfig.setContextConfig(this.contextConfig);
        this.menuSqlConfig.init();

        this.entityConfig.setContextConfig(this.contextConfig);
        this.entityConfig.init();
    }


}