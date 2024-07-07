package cn.stylefeng.guns.modular.bulletin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.base.util.PageUtils;
import cn.stylefeng.guns.modular.base.util.Query;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.entity.AppOperationLog;
import cn.stylefeng.guns.modular.bulletin.mapper.AppOperationLogMapper;
import cn.stylefeng.guns.modular.bulletin.model.AppOperationLogDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppOperationLogService extends ServiceImpl<AppOperationLogMapper, AppOperationLog> {

    @Autowired
    RedisUtil redisUtil;
//    @Autowired
//    AppMemberService appMemberService;
//
//    public Result getLog(String token, Integer pageSize, Integer pageNumber) {
//        /*
//         * token验证
//         */
//        if (redisUtil.get(token) == null)
//            return Result.fail(StatusCode.LOGIN_FAIL, "登录失效");
//        String memberId = (String) redisUtil.get(token);
//        AppMember member = appMemberService.getById(memberId);
//        if (member == null) {
//            return Result.fail(StatusCode.NOT_FOUND, "用户未找到");
//        }
//        //判断是否失效
//        if (member.getStatus().equals("N")) {
//            redisUtil.del(token);
//            return Result.fail(5003, "用户已被禁用");
//        }
//        Map<String, Object> params = new HashMap<>();
//        params.put("pageNumber", pageNumber);
//        params.put("pageSize", pageSize);
//        params.put("memberId", member.getMemberId());
//        //封装Query进行查询
//        Query query = new Query(params);
//        List<AppOperationLog> list = this.getBaseMapper().getLog(query);
//        int total = this.getBaseMapper().getLogCount(query);
//        List<AppOperationLogDto> logList = new ArrayList<>();
//        if (list.size() > 0) {
//            for (AppOperationLog entity : list) {
//                AppOperationLogDto dto=new AppOperationLogDto();
//                BeanUtil.copyProperties(entity,dto);
//                logList.add(dto);
//            }
//        }
//        return Result.success("",StatusCode.SUCCESS,new PageUtils(total, logList));
//    }


    /**
     * 七天删除一次日志
     */
    public void deleteLog() {
        //获取当前日期
        this.baseMapper.deleteLog();

    }



    /**
     * 查询日志
     */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
     * 删除日志
     */
    public void deleteAppOperationLog(Long appOperationLogId) {
        AppOperationLog entity=this.baseMapper.selectById(appOperationLogId);
        //将删除标志设置为Y，表示删除
//        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加日志
     */
    public void addAppOperationLog(AppOperationLog appOperationLog) {
        this.baseMapper.insert(appOperationLog);
    }

}
