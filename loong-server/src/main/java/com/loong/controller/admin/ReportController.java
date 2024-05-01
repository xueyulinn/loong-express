package com.loong.controller.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.result.Result;
import com.loong.service.StatisticsService;
import com.loong.vo.OrderReportVO;
import com.loong.vo.SalesTop10ReportVO;
import com.loong.vo.TurnoverReportVO;
import com.loong.vo.UserReportVO;

@RestController
@RequestMapping("/admin/report")
public class ReportController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(String begin, String end) {
        TurnoverReportVO turnoverReportVO = statisticsService.turnoverStatistics(begin, end);
        return Result.success(turnoverReportVO);
    }

    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(String begin, String end) {
        UserReportVO userReportVO = statisticsService.userStatistics(begin, end);
        return Result.success(userReportVO);
    }

    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(String begin, String end) {
        OrderReportVO orderReportVO = statisticsService.ordersStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> salesTop10(String begin, String end) {
        SalesTop10ReportVO salesTop10ReportVO = statisticsService.salesTop10(begin, end);
        return Result.success(salesTop10ReportVO);
    }
}
