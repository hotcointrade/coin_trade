package cn.stylefeng.guns.modular.bulletin.wrapper;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 简介文章Wrapper
 *
 * @author yaying.liu
 * @Date 2019-08-16 14:30:41
 */
public class ArticleWrapper extends BaseControllerWrapper{

    public ArticleWrapper(Map<String, Object> single) {
        super(single);
    }

    public ArticleWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ArticleWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ArticleWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String status = (String)map.get("articleType");
        if (status != null) {
            for (ProConst.ArticleTypeEnum e : ProConst.ArticleTypeEnum.values()) {
                if(e.getCode().equals(status))
                {
                    map.put("articleTypeValue",e.getValue());

                }
            }
        }
    }
}