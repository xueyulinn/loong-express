package com.loong.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.loong.entity.Orders;
import com.loong.mapper.OrdersMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * check timed out order every minute
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void processUnpaidOrder() {
        // get current time
        LocalDateTime time = LocalDateTime.now();
        log.info("processUnpaidOrder task was executed!");
        time.minusMinutes(15);
        List<Orders> orders = ordersMapper.selectByStatusAndTime(Orders.UN_PAID, time);
        if (orders != null || !orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("Order Timed Out");
                order.setCancelTime(LocalDateTime.now());
                ordersMapper.update(order);
            }
        }
    }

    /**
     * auto complete order before closed
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processIncompletedOrder() {
        LocalDateTime time = LocalDateTime.now();
        log.info("processUnpaidOrder task was executed!");
        time.minusHours(1);
        List<Orders> orders = ordersMapper.selectByStatusAndTime(Orders.UN_PAID, time);
        if (orders != null || !orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                order.setCancelTime(LocalDateTime.now());
                ordersMapper.update(order);
            }
        }
    }
}
