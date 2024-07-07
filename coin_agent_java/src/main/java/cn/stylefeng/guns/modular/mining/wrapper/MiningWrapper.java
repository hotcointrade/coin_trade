package cn.stylefeng.guns.modular.mining.wrapper;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
/**
 * 矿机Wrapper
 *
 * @author yaying.liu
 * @since 2022-06-07 13:24:41
 */
public class MiningWrapper extends BaseControllerWrapper{

    public MiningWrapper(Map<String, Object> single) {
            super(single);
        }

        public MiningWrapper(List<Map<String, Object>> multi) {
            super(multi);
        }

        public MiningWrapper(Page<Map<String, Object>> page) {
            super(page);
        }

        public MiningWrapper(PageResult<Map<String, Object>> pageResult) {
            super(pageResult);
        }

        @Override
        protected void wrapTheMap(Map<String, Object> map) {

        }
}