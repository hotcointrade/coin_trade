package cn.stylefeng.guns.modular.fin.mapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
/**
 * 账户信息Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-12 17:05:14
 */
public interface WalletMapper extends BaseMapper<Wallet> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page,
                                               @Param("condition") String condition,
                                               @Param("memberId") Long memberId,
                                               @Param("recommendIds") String recommendIds);

    List<Wallet> zeroList();
}
