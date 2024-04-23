package com.loong.service;

import com.loong.dto.OrdersPaymentDTO;
import com.loong.dto.OrdersSubmitDTO;
import com.loong.vo.OrderPaymentVO;
import com.loong.vo.OrderSubmitVO;

public interface OrderService {

    OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * 
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * 
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

}
