package cn.stylefeng.guns.modular.app.mapper;
import cn.stylefeng.guns.modular.app.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.WalletTransfer;
/**
 * 佣金划转Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-02-18 20:54:40
 */
public interface WalletTransferMapper extends BaseMapper<WalletTransfer> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition,
                                               @Param("beginTime") String beginTime, @Param("endTime") String endTime,
                                               @Param("members") List<Member> memberTeamForPRI);

}
