package com.loong.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loong.entity.OrderDetail;
import com.loong.entity.Orders;
import com.loong.mapper.OrdersMapper;
import com.loong.mapper.UserMapper;
import com.loong.service.StatisticsService;
import com.loong.service.WorkspaceService;
import com.loong.vo.BusinessDataVO;
import com.loong.vo.OrderReportVO;
import com.loong.vo.SalesTop10ReportVO;
import com.loong.vo.TurnoverReportVO;
import com.loong.vo.UserReportVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkspaceService workspaceService;

    @Override
    public TurnoverReportVO turnoverStatistics(String begin, String end) {
        // Parse input dates
        LocalDate startDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);

        // Create date list (x-axis) using streams
        List<LocalDate> dateList = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        // Get values (y-axis) using streams and map operation
        List<Double> salesList = dateList.stream()
                .map(date -> ordersMapper.selectSumByDate(date.toString()))
                .map(sales -> sales == null ? 0.0 : sales)
                .collect(Collectors.toList());

        // Convert lists to comma-separated strings
        String dateListString = dateList.stream()
                .map(LocalDate::toString)
                .collect(Collectors.joining(","));

        String salesListString = salesList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        // Create and return TurnoverReportVO
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setDateList(dateListString);
        turnoverReportVO.setTurnoverList(salesListString);
        return turnoverReportVO;
    }

    @Override
    public UserReportVO userStatistics(String begin, String end) {
        LocalDate startDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        // Create date list (x-axis) using streams
        List<LocalDate> dateList = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        // Get values (y-axis) using streams and map operation

        List<Integer> newUserList = dateList.stream().map(date -> userMapper.selectByCreateDate(date.toString()))
                .map(newUser -> newUser == null ? 0 : newUser).collect(Collectors.toList());

        List<Integer> totalUserList = dateList.stream().map(date -> userMapper.selectBeforeDate(date.toString()))
                .map(totalUser -> totalUser == null ? 0 : totalUser).collect(Collectors.toList());

        String dateListString = dateList.stream()
                .map(LocalDate::toString)
                .collect(Collectors.joining(","));

        String newUserString = newUserList.stream()
                .map(t -> t.toString())
                .collect(Collectors.joining(","));

        String totalUserString = totalUserList.stream()
                .map(t -> t.toString())
                .collect(Collectors.joining(","));

        UserReportVO userReportVO = new UserReportVO();
        userReportVO.setDateList(dateListString);
        userReportVO.setNewUserList(newUserString);
        userReportVO.setTotalUserList(totalUserString);
        return userReportVO;
    }

    @Override
    public OrderReportVO ordersStatistics(String begin, String end) {
        LocalDate startDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        // Create date list (x-axis) using streams
        List<LocalDate> dateList = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        // count total orders and valid orders
        List<Orders> orders = ordersMapper.selectByDate(begin, end);

        Integer totalOrders = orders.size();
        Integer validOrder = 0;
        for (Orders order : orders) {
            if (order.getStatus() == Orders.COMPLETED) {
                validOrder++;
            }
        }

        // calculate order completion rate
        Double orderCompletionRate = (double) validOrder / totalOrders;

        // get lists
        List<Integer> validOrderCountList = dateList.stream()
                .map(date -> ordersMapper.selectByStatusAndDate(Orders.COMPLETED, date.toString()))
                .collect(Collectors.toList());

        List<Integer> totalOrderList = dateList.stream()
                .map(date -> ordersMapper.countByDate(date.toString()))
                .collect(Collectors.toList());

        for (LocalDate date : dateList) {
            int validOrderCount = ordersMapper.selectByStatusAndDate(Orders.COMPLETED, date.toString());
            validOrderCountList.add(validOrderCount);
            int totalOrderCount = ordersMapper.countByDate(date.toString());
            totalOrderList.add(totalOrderCount);
        }

        String dateListString = dateList.stream()
                .map(LocalDate::toString)
                .collect(Collectors.joining(","));
        String validOrderString = validOrderCountList.stream()
                .map(t -> t.toString())
                .collect(Collectors.joining(","));
        String totalOrderString = totalOrderList.stream()
                .map(t -> t.toString())
                .collect(Collectors.joining(","));

        OrderReportVO orderReportVO = new OrderReportVO();
        orderReportVO.setDateList(dateListString);
        orderReportVO.setTotalOrderCount(totalOrders);
        orderReportVO.setValidOrderCount(validOrder);
        orderReportVO.setOrderCompletionRate(orderCompletionRate);
        orderReportVO.setValidOrderCountList(validOrderString);
        orderReportVO.setOrderCountList(totalOrderString);
        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO salesTop10(String begin, String end) {
        List<OrderDetail> orderDetails = ordersMapper.selectTop10ByDate(begin, end);

        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetails) {
            String name = orderDetail.getName();
            int number = orderDetail.getNumber();
            nameList.add(name);
            numberList.add(number);
        }

        SalesTop10ReportVO top10 = new SalesTop10ReportVO();

        try {
            StringBuilder nameString = new StringBuilder();
            for (String name : nameList) {
                nameString.append(name).append(",");
            }
            nameString.deleteCharAt(nameString.lastIndexOf(","));

            StringBuilder numberString = new StringBuilder();
            for (Integer number : numberList) {
                numberString.append(number).append(",");
            }
            numberString.deleteCharAt(numberString.lastIndexOf(","));

            top10.setNameList(nameString.toString());
            top10.setNumberList(numberString.toString());
        } catch (Exception e) {
            // Handle the exception here
            log.error("No order details found.");
        }
        return top10;

    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        // 查询概览运营数据，提供给Excel模板文件
        BusinessDataVO businessData = workspaceService.businessOverview(LocalDate.now());
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            // 基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            // 获得Excel文件中的一个Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);
            // 获得第4行
            XSSFRow row = sheet.getRow(3);
            // 获取单元格
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                // 准备明细数据
                businessData = workspaceService.businessOverview(date);
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }
            // 通过输出流将文件下载到客户端浏览器中
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            // 关闭资源
            out.flush();
            out.close();
            excel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
