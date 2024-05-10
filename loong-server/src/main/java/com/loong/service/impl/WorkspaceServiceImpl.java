package com.loong.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loong.entity.Orders;
import com.loong.mapper.DishMapper;
import com.loong.mapper.OrdersMapper;
import com.loong.mapper.SetmealMapper;
import com.loong.mapper.UserMapper;
import com.loong.service.WorkspaceService;
import com.loong.vo.BusinessDataVO;
import com.loong.vo.DishOverViewVO;
import com.loong.vo.OrderOverViewVO;
import com.loong.vo.SetmealOverViewVO;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public SetmealOverViewVO setmealsOverview() {
        Integer disabledSetmeal = setmealMapper.countByStatus(0);
        Integer onSaleSetmeal = setmealMapper.countByStatus(1);

        SetmealOverViewVO setmealOverViewVO = new SetmealOverViewVO();
        setmealOverViewVO.setDiscontinued(disabledSetmeal);
        setmealOverViewVO.setSold(onSaleSetmeal);
        return setmealOverViewVO;
    }

    @Override
    public DishOverViewVO dishesOverview() {
        Integer disabledDish = dishMapper.countByStatus(0);
        Integer onSaleDish = dishMapper.countByStatus(1);

        DishOverViewVO dishOverViewVO = new DishOverViewVO();
        dishOverViewVO.setDiscontinued(disabledDish);
        dishOverViewVO.setSold(onSaleDish);
        return dishOverViewVO;

    }

    @Override
    public OrderOverViewVO ordersOverview() {
        Integer sum = ordersMapper.countByStatus(null);
        Integer cancelled = ordersMapper.countByStatus(Orders.CANCELLED);
        Integer completed = ordersMapper.countByStatus(Orders.COMPLETED);
        Integer deliveredOrders = ordersMapper.countByStatus(Orders.CONFIRMED);
        Integer waitingOrders = ordersMapper.countByStatus(Orders.TO_BE_CONFIRMED);

        OrderOverViewVO orderOverViewVO = new OrderOverViewVO();
        orderOverViewVO.setAllOrders(sum);
        orderOverViewVO.setCancelledOrders(cancelled);
        orderOverViewVO.setCompletedOrders(completed);
        orderOverViewVO.setDeliveredOrders(deliveredOrders);
        orderOverViewVO.setWaitingOrders(waitingOrders);
        return orderOverViewVO;
    }

    @Override
    public BusinessDataVO businessOverview() {
        String today = LocalDate.now().toString();
        int newUsers = userMapper.selectByCreateDate(today);
        int validOrderCount = ordersMapper.selectByStatusAndDate(Orders.COMPLETED, today);
        int sum = ordersMapper.countByStatus(null);
        double orderCompleteRate = (double) (validOrderCount/sum);
        double turnover = ordersMapper.sumByStatusAndDate(Orders.COMPLETED,today);
        double unitPrice = turnover/validOrderCount;

        BusinessDataVO businessDataVO = new BusinessDataVO();
        businessDataVO.setValidOrderCount(validOrderCount);
        businessDataVO.setNewUsers(newUsers);
        businessDataVO.setOrderCompletionRate(orderCompleteRate);
        businessDataVO.setTurnover(turnover);
        businessDataVO.setUnitPrice(unitPrice);
        return businessDataVO;

    }

}
