package cn.stylefeng.guns.modular.fin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.Contract;
/**
 * 合约账户Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:09:49
 */
public interface ContractMapper extends BaseMapper<Contract> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition,
                                               @Param("memberId")Long memberId,@Param("recommendIds")String recommendIds);

    int updateWallet(Contract contract);

    int countN(@Param("memberId") Long memberId,@Param("coin") String coin);
}