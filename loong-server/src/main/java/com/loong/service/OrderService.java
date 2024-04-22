package com.loong.service;

import com.loong.dto.OrdersDTO;
import com.loong.vo.OrderSubmitVO;

public interface OrderService {

    OrderSubmitVO orderSubmit(OrdersDTO ordersDTO);
    
}
