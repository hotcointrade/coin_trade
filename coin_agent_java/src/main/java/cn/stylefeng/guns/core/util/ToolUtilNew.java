package cn.stylefeng.guns.core.util;

import cn.stylefeng.guns.core.support.StrKit;
import cn.stylefeng.roses.core.util.ToolUtil;

import java.util.Map;

/**
 * 新工具类拓展
 *
 * @Author yaying.liu
 * @Date 2019-08-08 00:16
 */
public class ToolUtilNew extends ToolUtil {
    /**
     * 首字母大写

     */
    public static String firstLetterToUpper(String val){
        return StrKit.firstCharToUpperCase(val);
    }

    /**
     * 首字母小写
     *
     * @author stylefeng
     * @Date 2017/5/7 22:02
     */
    public static String firstLetterToLower(String val){
        return StrKit.firstCharToLowerCase(val);
    }
    /**
     * 判断是否是windows操作系统

     */
    public static Boolean isWinOs(){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取临时目录

     */
    public static String getTempPath(){
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... values) {
        return StrKit.format(template, values);
    }

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param map      参数值对
     * @return 格式化后的文本
     */
    public static String format(String template, Map<?, ?> map) {
        return StrKit.format(template, map);
    }
}
