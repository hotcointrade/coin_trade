package cn.stylefeng.guns.modular.bulletin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.util.RandomUtil;
import cn.stylefeng.guns.modular.bulletin.entity.Merchant;
import cn.stylefeng.guns.modular.bulletin.mapper.MerchantMapper;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MerchantService extends ServiceImpl<MerchantMapper, Merchant> {



    @Autowired
    UserService userService;



    /****************************************************************************************************************/
    /**
     ---------------------------------------------华丽分割线-------------------------------------------------------

     */
    /****************************************************************************************************************/

    /**
     * 查询商户
     */
    public Page<Map<String, Object>> selectByCondition(String condition, Long merchantId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition, merchantId);
    }

    /**
     * 删除商户
     */
    public void deleteMerchant(Long merchantId) {
        Merchant entity = this.baseMapper.selectById(merchantId);
        //将删除标志设置为Y，表示删除
        entity.setDelFlag("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加商户
     */
    public void addMerchant(Merchant merchant) {

        Long id = new Date().getTime();

        String UID = RandomUtil.code("MT");
        merchant.setOpenid(UID).setRegisterTime(new Date());

        Merchant merchantQ = new Merchant();
        merchantQ.setOpenid(UID);
        merchantQ = this.getOne(new QueryWrapper<>(merchantQ));
        if (merchantQ != null) {
            return;
        }

        merchant.setMerchantId(id);

        //生成账号
        User user = new User();
        String account = RandomUtil.account();
        merchant.setAccount(account);
        user.setAccount(account);
        String salt = ShiroKit.getRandomSalt(5);
        String md5Pwd = ShiroKit.md5("123456", salt);
        user.setUserId(merchant.getMerchantId());
        user.setPassword(md5Pwd);
        user.setSalt(salt);
        user.setStatus("ENABLE");
        user.setRoleId(Constant.MERCHANT_ROLE_ID);
        userService.save(user);

        //设置商户key
        merchant.setPassword(UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase());
        this.save(merchant);
    }

    /**
     * 重置商户密码
     *
     * @param merchantId
     */
    public void resetPwd(Long merchantId) {
        User user = userService.getById(merchantId);
        if (user == null) {
            return;
        }
        String newPwd = "123456";
        String md5Pwd = ShiroKit.md5(newPwd, user.getSalt());
        user.setPassword(md5Pwd);
        userService.updateById(user);


    }

    public Page<Map<String, Object>> selectRecordByCondition(String condition, Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return null;
        }
        String[] roles = user.getRoleId().split(",");
        boolean flag = true;
        for (String roleId : roles) {
            if (roleId.equals(Constant.MERCHANT_ROLE_ID)) {//如果是商户角色，将id传过去
                flag = false;
            }
        }
        if (flag) {
            id = null;
        }
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectRecordByCondition(page, condition, id);
    }







/****************************************************************************************************************/
/**
 ---------------------------------------------华丽分割线-------------------------------------------------------

 */
/****************************************************************************************************************/

}
