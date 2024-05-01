package com.loong.controller.user;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.controller.notify.PayNotifyController;
import com.loong.dto.OrdersPageQueryDTO;
import com.loong.dto.OrdersPaymentDTO;
import com.loong.dto.OrdersSubmitDTO;
import com.loong.result.PageResult;
import com.loong.result.Result;
import com.loong.service.OrderService;
import com.loong.vo.OrderPaymentVO;
import com.loong.vo.OrderSubmitVO;
import com.loong.vo.OrderVO;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/user/order")
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayNotifyController payNotifyController;

    @PostMapping("/repetition/{id}")
    public Result orderAgain(@PathVariable Long id) {
        log.info("received order id: {}", id);
        orderService.orderAgain(id);
        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Long id) {
        log.info("received order id: {}", id);
        orderService.cancelOrder(id);
        return Result.success();
    }

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> queryOrderDetail(@PathVariable Long id) {
        log.info("received order id: {}", id);
        OrderVO OrderVO = orderService.queryOrderDetail(id);
        return Result.success(OrderVO);
    }

    @GetMapping("/historyOrders")
    public Result<PageResult> queryhistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("queryhistoryOrders: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.queryhistoryOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

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
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        return Result.success(orderPaymentVO);
    }
}
