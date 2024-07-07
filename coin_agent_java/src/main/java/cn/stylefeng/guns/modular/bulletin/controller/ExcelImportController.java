package cn.stylefeng.guns.modular.bulletin.controller;

import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.service.ImportService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
/**
 * @description: 导入
 * @author: yaying.liu
 * @date: 2019-08-17 22:49
 **/
@Controller
@RequestMapping("/import")
public class ExcelImportController extends BaseController {
    @Autowired
    ImportService importService;

    private static final Logger log = LoggerFactory.getLogger(ExcelImportController.class);

    /**
     * @Description: 跳转至首页(也就是导入Excel的页面)
     * @Param: []
     * @Retrun: java.lang.String
     */
    @GetMapping("/toImport")
    public String toImport() {
        return "importExcel";
    }

    /**
     * @Description: 异步接收上传的Excel文件并传递至Service层
     * @Param: [file]
     * @Retrun: com.ydc.excel_to_db.result.Result<com.ydc.excel_to_db.result.CodeMsg>
     */
    @PostMapping("/doImport")
    @ResponseBody
    public ResponseData doImport(@RequestParam("file") MultipartFile file, Long testTypeId) {

         importService.verfiyExcel(file,testTypeId);
        return  SUCCESS_TIP;
    }

//    /**
//     * @Description: 导出Excel文件
//     * @Param: response
//     * @Param: failTypeName 失败类型的名称，例如:格式错误(format)/导入数据库失败(db)/excel模板下载(excelDemo)
//     * @Retrun: void
//     */
//    @GetMapping("/doExport")
//    public void doExport(HttpServletResponse response, @RequestParam("failTypeName") String failTypeName) {
//        try {
//            // 设置响应输出的头类型
//            response.setHeader("content-Type", "application/vnd.ms-excel");
//            // 导出文件名称
//            String exportExcelName;
//            if ("format".equals(failTypeName)) {
//                exportExcelName = "数据格式校验失败的数据";
//            } else if ("db".equals(failTypeName)) {
//                exportExcelName = "导入数据库失败的数据";
//            } else {
//                exportExcelName = "Excel数据格式模板";
//            }
//            // 下载文件的默认名称
//            response.setHeader("Content-Disposition", "attachment;filename=" +
//                    URLEncoder.encode(exportExcelName, "UTF-8") + ".xls");
//            ExportParams exportParams = new ExportParams();
//            /* exportParams.setDataHanlder(null); 设置handler来处理特殊数据 */
//            // 根据失败类型的名称(failTypeName),获取对应的List
//            List<ExcelModel> failList = importService.getFailList(failTypeName);
//            // 导出Excel
//            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ExcelModel.class, failList);
//            workbook.write(response.getOutputStream());
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    /**
//     * @Description: 异步获取当前消息队列中未被消费的队列大小
//     * @Param: []
//     * @Retrun: com.ydc.excel_to_db.result.Result<java.lang.Long>
//     */
//    @GetMapping("/getUndoSize")
//    @ResponseBody
//    public Result<Long> getUndoSize() {
//        return Result.success(importService.getTempSize(Constant.succSizeTempKey));
//    }
//
//    /**
//     * @Description: 跳转至同步结果页面
//     * @Param: []
//     * @Retrun: java.lang.String
//     */
//    @GetMapping("/toResult")
//    public String toResult() {
//        return "importResult";
//    }
//
//    /**
//     * @Description: 异步获取同步结果页面中饼状图所需的数据
//     * @Param: []
//     * @Retrun: com.ydc.excel_to_db.result.Result<com.ydc.excel_to_db.vo.ExcelModelVo>
//     */
//    @GetMapping("/getResultData")
//    @ResponseBody
//    public Result<ExcelModelVo> getResultData() {
//        ExcelModelVo resultData = importService.getResultData();
//        return Result.success(resultData);
//    }
}
