package cn.stylefeng.guns.modular.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转list格式
 */
public class ImgToList {

    /**
     * 图片转list
     * @param img
     * @return
     */
    public static List<Map<String,Object>> toList(String img) {
        List<Map<String,Object>> list=new ArrayList<>();
        if(img==null)
        {
           return null;
        }
        String[] arr = img.split(",");
        if (arr.length > 0)
        {
            for(int i=0;i<arr.length;i++)
            {
                Map<String,Object> map=new HashMap<>();
                map.put("src",arr[i]);
                list.add(map);
            }

        }
        return list;
    }


}
