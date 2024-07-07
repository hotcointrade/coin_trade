package cn.stylefeng.guns.modular.bulletin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.bulletin.entity.Contact;
import cn.stylefeng.guns.modular.bulletin.mapper.ContactMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 客服 服务类
 */
@Service
public class ContactService extends ServiceImpl<ContactMapper,Contact> {

    /**
     * 获取客服列表
     * @param condition
     * @return
     */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    public void addContact(Contact contact) {
        this.baseMapper.insert(contact);
    }

    public void deleteContact(Long contactId) {
        this.baseMapper.deleteById(contactId);
    }
}
