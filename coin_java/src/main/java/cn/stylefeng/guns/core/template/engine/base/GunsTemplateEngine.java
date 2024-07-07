package cn.stylefeng.guns.core.template.engine.base;

import cn.stylefeng.guns.core.util.ToolUtilNew;
//import com.sun.javafx.PlatformUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 项目模板生成 引擎
 */
public abstract class GunsTemplateEngine extends AbstractTemplateEngine {

    protected GroupTemplate groupTemplate;

    public GunsTemplateEngine() {
        initBeetlEngine();
    }

    public void initBeetlEngine() {
        Properties properties = new Properties();
        properties.put("RESOURCE.root", "");
        properties.put("DELIMITER_STATEMENT_START", "<%");
        properties.put("DELIMITER_STATEMENT_END", "%>");
        properties.put("HTML_TAG_FLAG", "##");
        Configuration cfg = null;
        try {
            cfg = new Configuration(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
        groupTemplate = new GroupTemplate(resourceLoader, cfg);
        groupTemplate.registerFunctionPackage("tool", new ToolUtilNew());
    }

    public void configTemplate(Template template){
        template.binding("controller", super.getControllerConfig());
        template.binding("context", super.getContextConfig());
        template.binding("mapper", super.getMapperConfig());
        template.binding("service", super.getServiceConfig());
        template.binding("wrapper", super.getWrapperConfig());
        template.binding("constant", super.getMapConfig());
        template.binding("menuSql", super.getMenuSqlConfig());
        template.binding("entity", super.getEntityConfig());
        template.binding("page", super.getPageConfig());
    }

    public void generateFile(String template,String filePath){
        Template pageTemplate = groupTemplate.getTemplate(template);
        configTemplate(pageTemplate);
//        if(PlatformUtil.isLinux()){
//            filePath = filePath.replaceAll("/+|\\\\+","/");
//        }else{
//            filePath = filePath.replaceAll("/+|\\\\+","\\\\");
//        }
        filePath = filePath.replaceAll("/+|\\\\+","\\\\");
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        try {
            pageTemplate.renderTo(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        //配置之间的相互依赖
        super.initConfig();

        //生成模板
        if (super.contextConfig.getControllerSwitch()) {
            generateController();
        }
        if (super.contextConfig.getIndexPageSwitch()) {
            generatePageHtml();
        }
        if (super.contextConfig.getAddPageSwitch()) {
            generatePageAddHtml();
        }
        if (super.contextConfig.getEditPageSwitch()) {
            generatePageEditHtml();
        }
        if (super.contextConfig.getJsSwitch()) {
            generatePageJs();
        }

        if (super.contextConfig.getAddJsSwitch()) {
            generatePageAddJs();
        }
        if (super.contextConfig.getEditJsSwitch()) {
            generatePageEditJs();
        }
        if (super.contextConfig.getInfoJsSwitch()) {
            generatePageInfoJs();
        }
        if (super.contextConfig.getMapperSwitch()) {
            generateMapper();
        }
        if (super.contextConfig.getServiceSwitch()) {
            generateService();
        }
        if (super.contextConfig.getWrapperSwitch()) {
            generateWrapper();
        }
        if (super.contextConfig.getMapSwitch()) {
            generateMap();
        }
        //生成菜单脚本
        if (super.contextConfig.getMenuSwitch()) {
            generateMenuSql();
        }
        if (super.contextConfig.getEntitySwitch()) {
            generateEntity();
        }
        if (super.contextConfig.getMapperXmlSwitch())
        {
            generateMapperXml();
        }

    }

    protected abstract void generatePageEditHtml();

    protected abstract void generatePageAddHtml();

    protected abstract void generatePageInfoJs();

    protected abstract void generatePageAddJs();

    protected abstract void generatePageEditJs();

    protected abstract void generatePageJs();

    protected abstract void generatePageHtml();

    protected abstract void generateController();

    protected abstract void generateMapper();

    protected abstract void generateService();

    protected abstract void generateWrapper();

    protected abstract void generateMap();
    //生成菜单脚本
    protected abstract void generateMenuSql();

    //生成实体类脚本
    protected abstract void generateEntity();
    //生成mapper.xml文件
    protected abstract void generateMapperXml();

}
