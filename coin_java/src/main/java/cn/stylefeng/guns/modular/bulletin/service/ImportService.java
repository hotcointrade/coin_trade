package cn.stylefeng.guns.modular.bulletin.service;

import cn.stylefeng.guns.modular.base.util.RandomUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.entity.ExcelProject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImportService {

    private static final Logger log = LoggerFactory.getLogger(ImportService.class);

//    @Autowired
//    ProjectService projectService;
//
//    @Autowired
//    TestTypeService testTypeService;
    /**
     * @Description:
     * 1.校验上传的文件类型及其对应的数据
     * 2.将通过（1）的数据转换为实体对象集合
     * 3.清空redis中的部分旧数据cleanCache()
     * 4.将对象集合传入cacheAndPublish()中
     * 5.封装本次数据校验结果并返回
     * @Param: [file]
     * @Retrun: com.ydc.excel_to_db.result.CodeMsg
     */
    @Transactional
    public Result verfiyExcel(MultipartFile file, Long testTypeId) {
        // 截取原始文件名里的类型名(最后一个“.”后的数据)
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        // 判断是否属于Excel表格的类型，不属于则直接返回“上传文件类型异常”(CodeMsg.FilE_ERROR)
        if (!".xls".equals(suffix) && !".xlsx".equals(suffix)) {
//            throw new Exception("error");
            System.out.println("ERROR 444");
            return Result.success("ERROR 444");
        }
        ImportParams importParams = new ImportParams();
        // 对上传的数据进行处理，详情信息请看-com.ydc.excel_to_db.handler.ExcelModelHandler;
//        IExcelDataHandler<ExcelModel> handler = new ExcelModelHandler();
//        handler.setNeedHandlerFields(new String[] {"姓名"});
//        importParams.setDataHanlder(handler);
        // 是否需要验证
        importParams.setNeedVerfiy(true);
        try {
            ExcelImportResult<ExcelProject> result = ExcelImportUtil.importExcelMore(file.getInputStream(), ExcelProject.class,
                    importParams);
            // 当结果中通过校验的数据(result.getList())为空时
            // 直接返回“上传Excel表格格式有误<br>或者<br> 上传Excel数据为空”(CodeMsg.Excel_FORMAT_ERROR)
            if (result.getList().size() == 0 || result.getList().get(0) == null) {
//                return CodeMsg.Excel_FORMAT_ERROR;
                System.out.println("Excel_FORMAT_ERROR");
                return Result.success("格式错误");
            }
            /**
             * 插入数据库内
             */
//            List<ExcelProject> resultList=result.getList();
//            for(ExcelProject model:resultList)
//            {
//
//                TestType resultTest=testTypeService.getById(testTypeId);
//                if(resultTest==null)
//                {
//                    return Result.success("该检测类型不存在");
//                }
//                Project entity=new Project();
//                BeanUtils.copyProperties(model,entity);
//                entity.setCode(RandomUtil.code("P"));
//                entity.setProjectType(resultTest.getTestType());
//                entity.setTypeCode(resultTest.getCode());
//                projectService.save(entity);
//            }
            // 清空redis中的部分旧数据
            // 将参数result中的部分数据存入redis中，并把格式校验成功的数据发布至对应频道中
//            cacheAndPublish(result);
            int succSize = result.getList().size();
            int failSize = result.getFailList().size();
            String msg = "在Excel数据格式校验环节中，共获得有效数据" + (succSize + failSize) + "条</br>其中," + succSize
                    + "条数据通过格式校验," + failSize + "条数据未通过格式校验 </br> 是否需要查看完整数据同步结果信息？";
            System.out.println(msg);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        // 当以上过程中抛出异常时，返回"服务端异常，请您联系管理员查看服务器日志"(CodeMsg.SERVER_ERROR)
     return Result.success("导入完成");
    }

//    /**
//     * @Description: 清空redis中的部分旧数据
//     * @Param: []
//     * @Retrun: void
//     */
//    @Transactional
//    public void cleanCache() {
//        List<String> keyList = new ArrayList<>(10);
//        keyList.add(Constant.failToDBKey);
//        keyList.add(Constant.succSizeTempKey);
//        keyList.add(Constant.failListKey);
//        keyList.add(Constant.failSizeKey);
//        keyList.add(Constant.succSizeKey);
//        redisDao.cleanCache(keyList);
//    }
//
//    /**
//     * @Description: 将参数result中的部分数据存入redis中，并把格式校验成功的数据发布至对应频道中
//     * @Param: [result]
//     * @Retrun: void
//     */
//    @Transactional
//    public void cacheAndPublish(ExcelImportResult<ExcelModel> result) {
//        // 通过校验的数据
//        List<ExcelModel> successList = result.getList();
//        // 未通过校验的数据
//        List<ExcelModel> failList = result.getFailList();
//        int succSize = successList.size();
//        int failSize = failList.size();
//        // 将未通过校验的数据存入redis中
//        redisDao.setStringKey(Constant.failListKey, failList);
//        redisDao.setStringKey(Constant.failSizeKey, failSize);
//        // 将通过校验的数据存入redis中
//        redisDao.setStringKey(Constant.succSizeKey, succSize);
//        // succSizeTempKey 用于判断消息队列中未消费数据的大小
//        redisDao.setStringKey(Constant.succSizeTempKey, succSize);
////        redisDao.setStringKey(Constant.successListKey,successList);
//        // 发布消息
//        redisDao.publish(Constant.receiveList, successList);
//    }
//
//    /**
//     * @Description: 将实体对象存入数据库中
//     * @Param: [excelModel]
//     * @Retrun: boolean 保存成功，返回true；保存失败，返回false；
//     */
//    public boolean save(ExcelModel excelModel) {
//        // INSERT IGNORE  存在重复主键时，返回0
//        return excelModelMapper.insert(excelModel) > 0;
//    }
//
//    /**
//     * @Description: 根据不同的失败类型的名称(failTypeName), 返回对应的数据
//     * @Param: [failTypeName]
//     * @Retrun: java.util.List<com.ydc.excel_to_db.domain.ExcelModel>
//     */
//    public List<ExcelModel> getFailList(String failTypeName) {
//        if ("format".equals(failTypeName)) {
//            return redisDao.getStringListValue(Constant.failListKey, ExcelModel.class);
//        } else if ("db".equals(failTypeName)) {
//            return redisDao.getListValue(Constant.failToDBKey, ExcelModel.class);
//        } else {
//            return new ArrayList<ExcelModel>();
//        }
//    }
//
//    /**
//     * @Description: 根据key值，返回redis中对应的结果
//     * @Param: [key]
//     * @Retrun: long
//     */
//    public long getTempSize(String key) {
//        return redisDao.getStringValue(key, long.class);
//    }
//
//    /**
//     * @Description: 获取同步结果页面中饼状图所需的数据
//     * @Param: []
//     * @Retrun: com.ydc.excel_to_db.vo.ExcelModelVo 封装的值对象
//     */
//    public ExcelModelVo getResultData() {
//        // 获取格式校验失败的数据大小
//        Long failSize = redisDao.getStringValue(Constant.failSizeKey, long.class);
//        // 获取格式校验成功的数据大小
//        Long succSize = redisDao.getStringValue(Constant.succSizeKey, long.class);
//        // 获取导入数据库失败的数据大小
//        Long failToDBSize = redisDao.getListSize(Constant.failToDBKey);
//        return new ExcelModelVo(succSize, failSize, failToDBSize);
//    }

}