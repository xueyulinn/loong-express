package com.loong.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.dto.OrdersCancelDTO;
import com.loong.dto.OrdersConfirmDTO;
import com.loong.dto.OrdersPageQueryDTO;
import com.loong.dto.OrdersRejectionDTO;
import com.loong.result.PageResult;
import com.loong.result.Result;
import com.loong.service.OrderService;
import com.loong.vo.OrderStatisticsVO;
import com.loong.vo.OrderVO;

import lombok.extern.slf4j.Slf4j;

@RestController("adminOrderController")
@RequestMapping("admin/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable Long id){
        orderService.completeOrder(id);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    public Result deliverOrder(@PathVariable Long id){
        orderService.deliverOrder(id);
        return Result.success();
    }

    @PutMapping("/rejection")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/confirm")
    public Result acceptOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.acceptOrder(ordersConfirmDTO);
        return Result.success();
    }

    @GetMapping("/details/{id}")
    public Result<OrderVO> queryOrderDetail(@PathVariable Long id) {
        OrderVO orderVO = orderService.queryOrderDetail(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> orderStatistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.orderStatistics();
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/conditionSearch")
    public Result<PageResult> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("ordersPageQueryDTO:{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
        log.info("pageResult:{}", pageResult);
        return Result.success(pageResult);
    }
}
