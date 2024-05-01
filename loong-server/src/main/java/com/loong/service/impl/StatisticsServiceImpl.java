package com.loong.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loong.entity.OrderDetail;
import com.loong.entity.Orders;
import com.loong.mapper.OrdersMapper;
import com.loong.mapper.UserMapper;
import com.loong.service.StatisticsService;
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

    @Override
    public TurnoverReportVO turnoverStatistics(String begin, String end) {

        // create datelist (x-axis)
        LocalDate beginDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        List<LocalDate> dateList = new ArrayList<>();
        while (beginDate.isBefore(endDate)) {
            dateList.add(beginDate);
            beginDate = beginDate.plusDays(1);
        }
        dateList.add(endDate);

        StringBuilder dateString = new StringBuilder();
        for (LocalDate date : dateList) {
            dateString.append(date.toString()).append(",");
        }
        dateString.deleteCharAt(dateString.lastIndexOf(","));
        // get values (y-axis)
        List<Double> salesList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Double sales = ordersMapper.selectSumByDate(date.toString());
            if (sales == null) {
                sales = 0.0;
            }
            salesList.add(sales);
        }

        StringBuilder salesString = new StringBuilder();
        for (Double sales : salesList) {
            salesString.append(sales).append(",");
        }
        salesString.deleteCharAt(salesString.lastIndexOf(","));

        // return
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setDateList(dateString.toString());
        turnoverReportVO.setTurnoverList(salesString.toString());
        return turnoverReportVO;
    }

    @Override
    public UserReportVO userStatistics(String begin, String end) {
        LocalDate beginDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        List<LocalDate> dateList = new ArrayList<>();
        while (beginDate.isBefore(endDate)) {
            dateList.add(beginDate);
            beginDate = beginDate.plusDays(1);
        }
        dateList.add(endDate);

        List<Integer> newUsers = new ArrayList<>();
        List<Integer> totalUsers = new ArrayList<>();

        for (LocalDate date : dateList) {
            int newUser = userMapper.selectByCreateDate(date.toString());
            newUsers.add(newUser);
            int totalUser = userMapper.selectBeforeDate(date.toString());
            totalUsers.add(totalUser);
        }

        // construct vo attribute
        StringBuilder dateString = new StringBuilder();
        for (LocalDate date : dateList) {
            dateString.append(date.toString()).append(",");
        }
        dateString.deleteCharAt(dateString.lastIndexOf(","));

        StringBuilder newUserString = new StringBuilder();
        for (Integer newUser : newUsers) {
            newUserString.append(newUser).append(",");
        }
        newUserString.deleteCharAt(newUserString.lastIndexOf(","));

        StringBuilder totalUserString = new StringBuilder();
        for (Integer totalUser : totalUsers) {
            totalUserString.append(totalUser).append(",");
        }
        totalUserString.deleteCharAt(totalUserString.lastIndexOf(","));

        UserReportVO userReportVO = new UserReportVO();
        userReportVO.setDateList(dateString.toString());
        userReportVO.setNewUserList(newUserString.toString());
        userReportVO.setTotalUserList(totalUserString.toString());
        return userReportVO;
    }

    @Override
    public OrderReportVO ordersStatistics(String begin, String end) {
        LocalDate beginDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        List<LocalDate> dateList = new ArrayList<>();
        while (beginDate.isBefore(endDate)) {
            dateList.add(beginDate);
            beginDate = beginDate.plusDays(1);
        }
        dateList.add(endDate);
        StringBuilder dateString = new StringBuilder();
        for (LocalDate date : dateList) {
            dateString.append(date.toString()).append(",");
        }
        dateString.deleteCharAt(dateString.lastIndexOf(","));

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
        List validOrderCountList = new ArrayList();
        List totalOrderList = new ArrayList();
        for (LocalDate date : dateList) {
            int validOrderCount = ordersMapper.selectByStatusAndDate(Orders.COMPLETED, date.toString());
            validOrderCountList.add(validOrderCount);
            int totalOrderCount = ordersMapper.countByDate(date.toString());
            totalOrderList.add(totalOrderCount);
        }

        StringBuilder validOrderString = new StringBuilder();
        for (Object validOrderCount : validOrderCountList) {
            validOrderString.append(validOrderCount).append(",");
        }
        validOrderString.deleteCharAt(validOrderString.lastIndexOf(","));
        StringBuilder totalOrderString = new StringBuilder();
        for (Object totalOrderCount : totalOrderList) {
            totalOrderString.append(totalOrderCount).append(",");
        }
        totalOrderString.deleteCharAt(totalOrderString.lastIndexOf(","));

        OrderReportVO orderReportVO = new OrderReportVO();
        orderReportVO.setDateList(dateString.toString());
        orderReportVO.setTotalOrderCount(totalOrders);
        orderReportVO.setValidOrderCount(validOrder);
        orderReportVO.setOrderCompletionRate(orderCompletionRate);
        orderReportVO.setValidOrderCountList(validOrderString.toString());
        orderReportVO.setOrderCountList(totalOrderString.toString());
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

}
