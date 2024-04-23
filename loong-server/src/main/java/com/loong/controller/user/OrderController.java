package com.loong.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.dto.OrdersPaymentDTO;
import com.loong.dto.OrdersSubmitDTO;
import com.loong.result.Result;
import com.loong.service.OrderService;
import com.loong.vo.OrderPaymentVO;
import com.loong.vo.OrderSubmitVO;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/user/order")
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public Result<OrderSubmitVO> orderSubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        OrderSubmitVO orderSubmitVO = orderService.orderSubmit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * order payment
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        // return Result.success(orderPaymentVO);
        return null;
    }
}
