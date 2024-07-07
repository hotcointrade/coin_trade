package cn.stylefeng.guns.modular.fin.mapper;
import cn.stylefeng.guns.modular.app.dto.WalletDtoLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.WalletHistory;
/**
 * 钱包历史变更Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:57
 */
public interface WalletHistoryMapper extends BaseMapper<WalletHistory> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

    List<WalletDtoLine> getLastDateHistory(WalletHistory walletHistory);
}