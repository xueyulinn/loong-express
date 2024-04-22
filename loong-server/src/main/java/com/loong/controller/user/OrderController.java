package com.loong.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.dto.OrdersDTO;
import com.loong.result.Result;
import com.loong.service.OrderService;
import com.loong.vo.OrderSubmitVO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RequestMapping("/user/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public Result<OrderSubmitVO> orderSubmit(@RequestBody OrdersDTO ordersDTO) {
        OrderSubmitVO orderSubmitVO = orderService.orderSubmit(ordersDTO);
        return Result.success(orderSubmitVO);
    }
}
