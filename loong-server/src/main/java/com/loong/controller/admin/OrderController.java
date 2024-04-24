package com.loong.controller.admin;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.dto.OrdersPageQueryDTO;
import com.loong.result.PageResult;
import com.loong.result.Result;
import com.loong.service.OrderService;

@RestController
@RequestMapping("admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // @GetMapping("/conditionSearch")
    // public Result<PageResult> pageQuery(@DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime,
    //         @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
    //         String number, String phone, Integer status, Integer page, Integer pageSize) {
    //             OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
    //             PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
    //             return Result.success(pageResult);
    // }
}
