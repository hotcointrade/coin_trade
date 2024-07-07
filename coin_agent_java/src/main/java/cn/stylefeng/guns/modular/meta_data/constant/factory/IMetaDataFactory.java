package cn.stylefeng.guns.modular.meta_data.constant.factory;


/**
 * 配置参数生产工厂的接口
 */
public interface IMetaDataFactory {

    /**
     * 根据属性编码获取值
     * @param code
     * @return
     */
    String getValueByCode(String code);

}
