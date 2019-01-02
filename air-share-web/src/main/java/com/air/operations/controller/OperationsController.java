package com.air.operations.controller;

import java.beans.IntrospectionException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.SystemCode;
import com.air.operations.service.OperationsService;
import com.air.pojo.entity.ExcelBean;
import com.air.pojo.entity.Series;
import com.air.pojo.vo.ConsumeAnalysisVO;
import com.air.pojo.vo.OperationsVO;
import com.air.pojo.vo.PayHabitVO;
import com.air.pojo.vo.StockVO;
import com.air.utils.DateFormats;
import com.air.utils.ExcelUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/operations")
public class OperationsController {
	@Autowired
	private OperationsService operationsService;
	
	/**
	 * 
	 * <p>Title: consumeStatistics</p>
	 * <p>Description: 消费使用记录</p>
	 * @param operationsVO
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="consumeStatistics",method=RequestMethod.GET)  
	@RequiresPermissions(value= {"consumeCount"})
	public Dto consumeStatistics(OperationsVO operationsVO) {
		if (operationsVO.getPageNum() == null || operationsVO.getPageSize() == null) {
			return DtoUtil.returnFail("分页参数错误", SystemCode.ERROR);
		}
		
		if (operationsVO.getBeginDate() != null && !DateFormats.isValidDate(operationsVO.getBeginDate())) {
			return DtoUtil.returnFail("开始日期格式错误", SystemCode.ERROR);
		}

		if (operationsVO.getEndDate() != null && !DateFormats.isValidDate(operationsVO.getEndDate())) {
			return DtoUtil.returnFail("结束日期格式错误", SystemCode.ERROR);
		}
		PageInfo<OperationsVO> pageInfo = operationsService.consumeStatistics(operationsVO);
		return DtoUtil.returnDataSuccess(pageInfo);
    }
	/**
	 * 导出消费使用记录Excel
	 * @throws ParseException 
	 * @throws IntrospectionException 
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@ResponseBody
	@RequestMapping(value="exportExcel",method=RequestMethod.GET)  
	@RequiresPermissions(value= {"consumeCount"})
	public Dto exportExcelconsumeStatistics(HttpServletRequest request,HttpServletResponse response) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, IntrospectionException, ParseException {
		//获取数据
		OperationsVO operationsVO =new OperationsVO();
		operationsVO.setPageNum(1);
		operationsVO.setPageSize(10000);
		List<OperationsVO> list = operationsService.consumeStatisticsList(operationsVO);
		
    	//excel文件名
    	String fileName = "消费统计表";

    	//sheet名
    	String sheetName = "消费统计";
    
    	    for(int i=0;i<list.size();i++){  
    	        //查询财务名字  
    	        String username = list.get(i).getUsername();
    	        String telephone = list.get(i).getAppUser().getTelephone();
    	        String address = list.get(i).getAppUser().getAddress();
    	        BigDecimal carbon = list.get(i).getAppUser().getCarbon();
    	        Integer num = list.get(i).getNum();
    	        BigDecimal price = list.get(i).getPrice();  
    	    }  
    	    List<ExcelBean> excel=new ArrayList<>();  
    	    Map<Integer,List<ExcelBean>> map=new LinkedHashMap<>();  
    	    XSSFWorkbook wb=null;  
    	    //设置标题栏  
    	    excel.add(new ExcelBean("用户昵称","username",0));  
    	    excel.add(new ExcelBean("用户电话","telephone",0));  
    	    excel.add(new ExcelBean("用户地址","address",0));  
    	    excel.add(new ExcelBean("碳指标(g)","carbon",0));  
    	    excel.add(new ExcelBean("成交数量","num",0));  
    	    excel.add(new ExcelBean("成交金额(元)","price",0));  
    	     
    	    map.put(0, excel);  
    	 
    	    //调用ExcelUtil的方法  
    	    wb = ExcelUtil.createExcelFile(OperationsVO.class, list, map, sheetName);
    	    //响应到客户端
    	    response.reset(); //清除buffer缓存  
            //Map<String,Object> map =new HashMap<String,Object>();  
            // 指定下载的文件名  
            response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");  
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");  
            response.setHeader("Pragma", "no-cache");  
            response.setHeader("Cache-Control", "no-cache");  
            response.setDateHeader("Expires", 0);  
             
            OutputStream output;  
            try {  
                output = response.getOutputStream();  
                BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);  
                bufferedOutPut.flush();  
                wb.write(bufferedOutPut);  
                bufferedOutPut.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
			return null;  
	
	}
	
	
	/**
	 * 
	 * <p>Title: consumeMethodStatistics</p>
	 * <p>Description: 消费方式统计</p>
	 * @param operationsVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="consumeMethodStatistics",method=RequestMethod.GET)
	@RequiresPermissions(value= {"consumeUseAnalyse"})
	public Dto consumeMethodStatistics(PayHabitVO payHabit) {
		if (payHabit.getBeginDate() != null && !DateFormats.isValidDate(payHabit.getBeginDate())) {
			return DtoUtil.returnFail("开始日期格式错误", SystemCode.ERROR);
		}

		if (payHabit.getEndDate() != null && !DateFormats.isValidDate(payHabit.getEndDate())) {
			return DtoUtil.returnFail("结束日期格式错误", SystemCode.ERROR);
		}
		List<PayHabitVO> list = operationsService.payHabitStatisticsByMethod(payHabit);
		return DtoUtil.returnDataSuccess(list);
    }
	
	/**
	 * 
	 * <p>Title: consumeTimeStatistics</p>
	 * <p>Description: 消费时间段统计</p>
	 * @param payHabit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="consumeTimeStatistics",method=RequestMethod.GET)
	@RequiresPermissions(value= {"fansAnalyse"})
	public Dto consumeTimeStatistics(PayHabitVO payHabit) {
		if (payHabit.getBeginDate() != null && !DateFormats.isValidDate(payHabit.getBeginDate())) {
			return DtoUtil.returnFail("开始日期格式错误", SystemCode.ERROR);
		}

		if (payHabit.getEndDate() != null && !DateFormats.isValidDate(payHabit.getEndDate())) {
			return DtoUtil.returnFail("结束日期格式错误", SystemCode.ERROR);
		}
		List<PayHabitVO> list = operationsService.payHabitStatisticsByTime(payHabit);
		return DtoUtil.returnDataSuccess(list);
    }
	
	/**
	 * 
	 * <p>Title: custumeTop</p>
	 * <p>Description: 消费前10</p>
	 * @param num
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="custumeTop",method=RequestMethod.GET, produces="application/json;charset=UTF-8")   
	@RequiresPermissions(value= {"consumeTOP"})
	public Dto custumeTop(@RequestParam("num")Integer num, String seriesName) {
		if (num <= 0) {
			return DtoUtil.returnFail("排名数量错误", SystemCode.ERROR);
		}
		List<Series> list = operationsService.custumeTop(num, seriesName);
		return DtoUtil.returnDataSuccess(list);
    }
	
	/**
	 * 
	 * <p>Title: custumeLater</p>
	 * <p>Description: 消费后10</p>
	 * @param num
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="custumeLater",method=RequestMethod.GET)   
	@RequiresPermissions(value= {"consumeTOP"})
	public Dto custumeLater(@RequestParam("num")Integer num, String seriesName) {
		if (num <= 0) {
			return DtoUtil.returnFail("排名数量错误", SystemCode.ERROR);
		}
		List<Series> list = operationsService.custumeLater(num, seriesName);
		return DtoUtil.returnDataSuccess(list);
    }
	
	/**
	 * 
	 * <p>Title: custumeLater</p>
	 * <p>Description: 消费使用分析</p>
	 * @param num
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="consumeAnalysis",method=RequestMethod.GET) 
	@RequiresPermissions(value= {"consumeUseAnalyse"})
	public Dto consumeAnalysis(ConsumeAnalysisVO consumeAnalysisVO) {
		List<ConsumeAnalysisVO> analysis = operationsService.consumeAnalysis(consumeAnalysisVO);
		return DtoUtil.returnDataSuccess(analysis);
    }
	
	/**
	 * 
	 * <p>Title: stockStatistics</p>
	 * <p>Description: 存量统计</p>
	 * @param stockVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="stockStatistics",method=RequestMethod.GET)   
	@RequiresPermissions(value= {"stockCount"})
	public Dto stockStatistics(StockVO stockVO) {
		if (stockVO.getPageNum() == null || stockVO.getPageSize() == null) {
			return DtoUtil.returnFail("分页参数错误", SystemCode.ERROR);
		}
		
		PageInfo<StockVO> stocks = operationsService.stockStatistics(stockVO);
		return DtoUtil.returnDataSuccess(stocks);
    }
	
	/**
	 * 
	 * <p>Title: consumeTrend</p>
	 * <p>Description: 消费趋势分析</p>
	 * @param macList
	 * @param dayCount
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="consumeTrend",method=RequestMethod.GET) 
	@RequiresPermissions(value= {"consumeTrendAnalyse"})
	public Dto consumeTrend(Integer[] macList, @RequestParam("dayCount")Integer dayCount) {
		if (macList == null || macList.length == 0 || dayCount <= 0) {
			return DtoUtil.returnFail("参数错误", SystemCode.ERROR);
		}
		List<Series> list = operationsService.consumeTrend(new ArrayList<>(Arrays.asList(macList)), dayCount);
		return DtoUtil.returnDataSuccess(list);
    }
}
