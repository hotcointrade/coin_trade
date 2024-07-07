package cn.stylefeng.guns.core.template.engine;

import cn.stylefeng.guns.core.template.engine.base.GunsTemplateEngine;
import cn.stylefeng.guns.core.util.ToolUtilNew;

/**
 * 通用的模板生成引擎
 *
 * @author yaying.liu
 * @date 2019-08-10
 */
public class SimpleTemplateEngine extends GunsTemplateEngine {

    @Override
    protected void generatePageEditHtml() {
        String path = ToolUtilNew.format(super.getContextConfig().getProjectPath() + getPageConfig().getPageEditPathTemplate(),
                super.getContextConfig().getBizEnName(),super.getContextConfig().getBizEnName());
        generateFile("lyyTemplate/page_edit.html.btl", path);
        System.out.println("生成编辑页面成功!");
    }

    @Override
    protected void generatePageAddHtml() {
        String path = ToolUtilNew.format(super.getContextConfig().getProjectPath() + getPageConfig().getPageAddPathTemplate(),
                super.getContextConfig().getBizEnName(),super.getContextConfig().getBizEnName());
        generateFile("lyyTemplate/page_add.html.btl", path);
        System.out.println("生成添加页面成功!");
    }

    @Override
    protected void generatePageInfoJs() {
//        String path = ToolUtilNew.format(super.getContextConfig().getProjectPath() + getPageConfig().getPageInfoJsPathTemplate(),
//                super.getContextConfig().getBizEnName(),super.getContextConfig().getBizEnName());
//        generateFile("lyyTemplate/page_info.js.btl", path);
//        System.out.println("生成页面详情js成功!");
    }

    @Override
    protected void generatePageAddJs() {
        String path = ToolUtilNew.format(super.getContextConfig().getProjectPath() + getPageConfig().getPageAddJsPathTemplate(),
                super.getContextConfig().getBizEnName(),super.getContextConfig().getBizEnName());
        generateFile("lyyTemplate/page_add.js.btl", path);
        System.out.println("生成页面添加js成功!");
    }

    @Override
    protected void generatePageEditJs() {
        String path = ToolUtilNew.format(super.getContextConfig().getProjectPath() + getPageConfig().getPageEditJsPathTemplate(),
                super.getContextConfig().getBizEnName(),super.getContextConfig().getBizEnName());
        generateFile("lyyTemplate/page_edit.js.btl", path);
        System.out.println("生成页面编辑js成功!");
    }

    @Override
    protected void generatePageJs() {
        String path = ToolUtilNew.format(super.getContextConfig().getProjectPath() + getPageConfig().getPageJsPathTemplate(),
                super.getContextConfig().getBizEnName(),super.getContextConfig().getBizEnName());
        generateFile("lyyTemplate/page.js.btl", path);
        System.out.println("生成页面js成功!");
    }

    @Override
    protected void generatePageHtml() {
        String path = ToolUtilNew.format(super.getContextConfig().getProjectPath() + getPageConfig().getPagePathTemplate(),
                super.getContextConfig().getBizEnName(),super.getContextConfig().getBizEnName());
        generateFile("lyyTemplate/page.html.btl", path);
        System.out.println("生成页面成功!");
    }

    @Override
    protected void generateController() {
        String controllerPath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getControllerConfig().getControllerPathTemplate(),
                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
        generateFile("lyyTemplate/Controller.java.btl", controllerPath);
        System.out.println("生成控制器成功!");
    }

    @Override
    protected void generateMapper() {
        String mapperPath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getMapperConfig().getMapperPathTemplate(),
                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
        generateFile("lyyTemplate/Mapper.java.btl", mapperPath);
        System.out.println("生成Mapper成功!");

        String mappingPath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getMapperConfig().getXmlPathTemplate(),
                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
        generateFile("lyyTemplate/Mapping.xml.btl", mappingPath);
        System.out.println("生成Mapper Mapping xml成功!");
    }

    @Override
    protected void generateService() {
        String servicePath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getServiceConfig().getServicePathTemplate(),
                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
        generateFile("lyyTemplate/Service.java.btl", servicePath);
        System.out.println("生成Service成功!");

//        String serviceImplPath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getServiceConfig().getServiceImplPathTemplate(),
//                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
//        generateFile("lyyTemplate/ServiceImpl.java.btl", serviceImplPath);
//        System.out.println("生成ServiceImpl成功!");
    }

    @Override
    protected void generateWrapper() {
        String wrapperPath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getWrapperConfig().getWrapperPathTemplate(),
                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
        generateFile("lyyTemplate/Wrapper.java.btl", wrapperPath);
        System.out.println("生成Wrapper成功!");
    }

    @Override
    protected void generateMap() {
        String mapPath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getMapConfig().getMapPathTemplate(),
                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
        generateFile("lyyTemplate/Map.java.btl", mapPath);
        System.out.println("生成Map成功!");
    }

    @Override
    protected void generateMenuSql(){
        String menuSqlPath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getMenuSqlConfig().getMenuSqlPathTemplate(),
                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
        generateFile("lyyTemplate/menuSql.sql.btl", menuSqlPath);
        System.out.println("生成MenuSql成功!");
    }

    @Override
    protected void generateEntity() {
        String entityPath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getEntityConfig().getEntityPathTemplate(),
                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
        generateFile("lyyTemplate/Entity.java.btl", entityPath);
        System.out.println("生成Entity成功!");
    }

    @Override
    protected void generateMapperXml() {
//        String mapperXmlPath = ToolUtilNew.format(super.getContextConfig().getProjectPath() + super.getMapperXmlConfig().getMapperXmlPathTemplate(),
//                ToolUtilNew.firstLetterToUpper(super.getContextConfig().getBizEnName()));
//        generateFile("lyyTemplate/MapperXml.sql.btl", mapperXmlPath);
//        System.out.println("生成MapperXml成功!");
    }

}