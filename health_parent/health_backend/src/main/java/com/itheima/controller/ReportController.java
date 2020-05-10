package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;


    /**
     * 会员报表统计
     *
     * @return Map
     */
    @RequestMapping("/getMemberReport")
    public Map getMemberReport() {
        Map<String, List> reportMap = new HashMap<String, List>();

        Calendar calendar = Calendar.getInstance();
//        使用默认时区和区域设置获取日历。
        System.out.println(calendar);
        calendar.add(Calendar.MONTH, -12);  //;//获得当前日期之前12个月的日期
//        根据日历的规则，将指定的时间量添加或减去给定的日历字段
        List<String> calendarList = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            calendarList.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
        reportMap.put("months", calendarList);


        /**
         * 根据日期查找会员数量
         */
        List<Integer> memberList = memberService.findMemberCount(calendarList);
        reportMap.put("memberCount", memberList);
        return reportMap;
    }


    /**
     * 套餐报表统计
     *
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        /* {
                    data:{
                        setmealNames:[],
                        setmealCount:[
                            {setmeal,23},
                        ]
                    }
                }*/
        /**
         * 查询套餐名称以及预约人数
         */
        try {
            //返回的map数据
            Map<String, Object> map = new HashMap<String, Object>();
//            套餐人数以及套餐人数map
            List<Map<String, Object>> list = setmealService.findSetMealAndCount();
            map.put("setmealCount", list);
//            套餐名字的数据
            List<String> setmealNamesList = new ArrayList<String>();
            //遍历集合,得到map的setmeal以及人数
            for (Map<String, Object> setMeal : list) {
                String name = (String) setMeal.get("name");
                setmealNamesList.add(name);
            }
            map.put("setmealNames", setmealNamesList);
            System.out.println(map);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }


    /**
     * 运营数据显示
     *
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = memberService.getBussinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }


    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            //获取所有报表数据
            Map<String, Object> bussinessReportData = memberService.getBussinessReportData();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) bussinessReportData.get("reportDate");
            Integer todayNewMember = (Integer) bussinessReportData.get("todayNewMember");
            Integer totalMember = (Integer) bussinessReportData.get("totalMember");
            Integer thisWeekNewMember = (Integer) bussinessReportData.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) bussinessReportData.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) bussinessReportData.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) bussinessReportData.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) bussinessReportData.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) bussinessReportData.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) bussinessReportData.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) bussinessReportData.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) bussinessReportData.get("hotSetmeal");

            //获取Excel模板文件路径 windows 和 linux 上的 路径不一致 /  \ separator
            String templateRealPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //读取模板文件
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(templateRealPath)));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数
            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数
            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数
            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
            int rowNum = 12;
            for (Map map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                String remark = (String) map.get("remark");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
                row.getCell(7).setCellValue(remark);//备注
            }
            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms‐excel");
            response.setHeader("content‐Disposition", "attachment;filename=report.xlsx");
            xssfWorkbook.write(out);
            out.flush();
            out.close();
            xssfWorkbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL, null);
        }

    }
}

