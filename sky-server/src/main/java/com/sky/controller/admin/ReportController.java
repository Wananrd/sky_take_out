package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;


    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额统计:{},{}", begin, end);
        TurnoverReportVO turnoverStatistics = reportService.getTurnoverStatistics(begin, end);
        return Result.success(turnoverStatistics);
    }

    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户数据统计:{}, {}",begin, end);
        UserReportVO userReportVO = reportService.getUserStatistics(begin, end);
        return Result.success(userReportVO);
    }

    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderStatics(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("订单统计:{}, {}", begin, end);
        OrderReportVO orderReportVO = reportService.getOrderStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("销量top10:{},{}", begin, end);
        SalesTop10ReportVO salesTop10ReportVO = reportService.getSalesTop10(begin, end);
        return Result.success(salesTop10ReportVO);
    }

    /**
     * 导出运营数据报表
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        reportService.exportBusinessData(response);
    }
}
