package com.loong.service;

import java.time.LocalDate;

import com.loong.vo.OrderReportVO;
import com.loong.vo.SalesTop10ReportVO;
import com.loong.vo.TurnoverReportVO;
import com.loong.vo.UserReportVO;

public interface StatisticsService {

    TurnoverReportVO turnoverStatistics(String begin, String end);

    UserReportVO userStatistics(String begin, String end);

    OrderReportVO ordersStatistics(String begin, String end);

    SalesTop10ReportVO salesTop10(String begin, String end);
    
}
