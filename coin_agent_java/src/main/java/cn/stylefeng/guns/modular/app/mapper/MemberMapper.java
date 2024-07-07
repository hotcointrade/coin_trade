package cn.stylefeng.guns.modular.app.mapper;
import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.modular.app.dto.ApiTeamMemberDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.Member;
/**
 * 用户信息Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-06 09:50:49
 */
public interface MemberMapper extends BaseMapper<Member> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page,
                                               @Param("condition") String condition,
                                               @Param("refereeId")Long refereeId,
                                               @Param("memberId")Long memberId,
                                               @Param("recommendIds")String recommendIds);
    Page<Map<String,Object>> selectByConditionForTeam(@Param("page") Page page,
                                               @Param("condition") String condition,
                                               @Param("refereeId")Long refereeId,
                                               @Param("recommendIds")String recommendIds);
    List<Member> selectTeamForPRI(@Param("parentRefereeId")Long parentRefereeId);

    List<ApiTeamMemberDto> getDirectMembersByMemberId(Member member);

    List<Member> getDirectMembersList(@Param("memberId")Long memberId);

    /**
     * RCC  和 USDT  ，不支持 ETH
     * @param address
     * @return
     */
    Member getConvertMember(@Param("address")String address);

    List<Member> getNotSystemUpdateMember();

    /**
     * 根据节点等级升序排列
     * @return
     */
    List<Member> getNotSystemUpdateMemberByTypeAsc();

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree(@Param("memberId")Long memberId,@Param("recommendIds")String recommendIds);

    int updateTypeByMember(@Param("memberId")Long memberId, @Param("version")Long version, @Param("type")String type);

    /**
     * 获取用户信息
     * @param account
     * @return
     */
    Member getUserForOtherType(@Param("account")String account);

    /**
     * 获取团队总人数（不包括自己）
     * @param memberId
     * @return
     */
    int getTeamNumbers(Long memberId);

    /**
     *  团队下最高等级用户
     * @param memberId
     * @return
     */
    String getTeamHighLv(Long memberId);

    /**
     * 获取直推排单量(昨日)
     * @param memberId
     * @return
     */
    long getCountArrangeForDirect(Long memberId);

    /**
     * 伞下同级节点会员数
     * @param memberId
     * @param type
     * @return
     */
    long teamSameNumber(@Param("memberId")Long memberId,@Param("type") String type);
    /**
     * 伞下高级节点会员数
     * @param memberId
     * @param type
     * @return
     */
    long teamHighNumber(@Param("memberId")Long memberId,@Param("type") String type);

    /**
     * 获取大小boss用户列表
     * @return
     */
    List<Member> getSmallOrBigMember();

    List<Member> getSmallMember();

    List<Member> getBigMember();

    List<Member> downBigBossList(@Param("memberId")Long memberId);

    List<Long> memberIdList();


    Map<String, Object> getById(@Param("refereeId")Long refereeId);
    /**
     * 获取所有的冻结资产
     * @param memberId
     * @return
     */
    BigDecimal getAllByLockedPrice(@Param("memberId") Long memberId);

    Page<Map<String,Object>> selectByLevel3(@Param("page") Page page,@Param("refereeId")Long refereeId);
    Page<Map<String,Object>> selectByLevel4(@Param("page") Page page,@Param("refereeId")Long refereeId,@Param("recommendIds") String recommendIds);

}

