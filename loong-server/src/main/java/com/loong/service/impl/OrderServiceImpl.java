package com.loong.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Websocket;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.loong.constant.MessageConstant;
import com.loong.context.BaseContext;
import com.loong.dto.OrdersCancelDTO;
import com.loong.dto.OrdersConfirmDTO;
import com.loong.dto.OrdersPageQueryDTO;
import com.loong.dto.OrdersPaymentDTO;
import com.loong.dto.OrdersRejectionDTO;
import com.loong.dto.OrdersSubmitDTO;
import com.loong.entity.AddressBook;
import com.loong.entity.OrderDetail;
import com.loong.entity.Orders;
import com.loong.entity.ShoppingCart;
import com.loong.entity.User;
import com.loong.exception.AddressBookBusinessException;
import com.loong.exception.OrderBusinessException;
import com.loong.exception.ShoppingCartBusinessException;
import com.loong.mapper.AddressBookMapper;
import com.loong.mapper.OrderDetailsMapper;
import com.loong.mapper.OrdersMapper;
import com.loong.mapper.ShoppingCartMapper;
import com.loong.mapper.UserMapper;
import com.loong.result.PageResult;
import com.loong.service.OrderService;
import com.loong.utils.WeChatPayUtil;
import com.loong.vo.OrderPaymentVO;
import com.loong.vo.OrderStatisticsVO;
import com.loong.vo.OrderSubmitVO;
import com.loong.vo.OrderVO;
import com.loong.websocket.WebSocketServer;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO) {

        // exception handler for address book
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();

        // build order
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(userId);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        ordersMapper.insert(order);

        // query shopping cart list
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(userId);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // insert order details
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(order.getId());
        for (ShoppingCart shoppingCart : shoppingCartList) {
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetailsMapper.insert(orderDetail);
        }

        // delete shopping cart by user id
        shoppingCartMapper.deleteByUserId(userId);

        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(order.getId());
        orderSubmitVO.setOrderNumber(order.getNumber());
        orderSubmitVO.setOrderAmount(order.getAmount());
        orderSubmitVO.setOrderTime(order.getOrderTime());

        return orderSubmitVO;
    }

    /**
     * order payment
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {

        // User user = userMapper.selectByUserId(BaseContext.getCurrentId());

        // // call wechat pay api
        // JSONObject jsonObject = weChatPayUtil.pay(
        //         ordersPaymentDTO.getOrderNumber(),
        //         new BigDecimal(0.01), // unit: yuan
        //         "Loong Express Order",
        //         user.getOpenid());

        // if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
        //     throw new OrderBusinessException("Order has been paid");
        // }

        // OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        // vo.setPackageStr(jsonObject.getString("package"));
        
        // here directly calls paySuccess for test purpose
        paySuccess(ordersPaymentDTO.getOrderNumber());
        OrderPaymentVO vo = new OrderPaymentVO();
        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String orderNumber) {
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = ordersMapper.selectByNumberAndUserId(orderNumber, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);

        Map map = new HashMap();
        map.put("type", 1);// 1 represents new order comes
        map.put("orderId", orders.getId());
        map.put("content", "OrderNumber: " + orderNumber);

        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    /**
     * user query history orders
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult queryhistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> orders = ordersMapper.selectLimit(ordersPageQueryDTO);
        List<OrderVO> orderVOs = new ArrayList<>();

        for (Orders order : orders) {
            OrderVO orderVO = new OrderVO();
            List<OrderDetail> orderDetails = orderDetailsMapper.selectByOrderId(order.getId());
            orderVO.setOrderDetailList(orderDetails);
            BeanUtils.copyProperties(order, orderVO);
            orderVOs.add(orderVO);
        }

        return new PageResult(orders.getTotal(), orderVOs);
    }

    @Override
    public OrderVO queryOrderDetail(Long id) {
        Orders order = ordersMapper.selectById(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);

        List<OrderDetail> orderDetails = orderDetailsMapper.selectByOrderId(id);
        orderVO.setOrderDetailList(orderDetails);
        String dishes = getOrderDishes(order);
        orderVO.setOrderDishes(dishes);
        return orderVO;
    }

    /**
     * user cancel order
     *
     * @param id
     */
    @Override
    public void cancelOrder(Long id) {
        Orders order = new Orders();
        order.setId(id);
        order.setStatus(Orders.CANCELLED);
        order.setCancelReason("Customer Cancel");
        order.setCancelTime(LocalDateTime.now());
        ordersMapper.update(order);
    }

    /**
     * user order again
     *
     * @param id
     */
    @Override
    public void orderAgain(Long id) {
        List<OrderDetail> orderDetails = orderDetailsMapper.selectByOrderId(id);
        for (OrderDetail orderDetail : orderDetails) {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * admin page query
     * 
     * @param ordersPageQueryDTO
     */
    @Override
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> orders = ordersMapper.selectLimit(ordersPageQueryDTO);

        // need VO cause I want to display orderDishes
        List<OrderVO> orderVOs = new ArrayList<>();

        orders.getResult().forEach(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            String dishes = getOrderDishes(order);
            orderVO.setOrderDishes(dishes);
            orderVOs.add(orderVO);
        });

        return new PageResult(orders.getTotal(), orderVOs);
    }

    /**
     * get orderDishes for orderVO
     * 
     * @param order
     */
    private String getOrderDishes(Orders order) {
        List<OrderDetail> orderDetails = orderDetailsMapper.selectByOrderId(order.getId());
        StringBuilder dishesBuilder = new StringBuilder();
        for (OrderDetail orderDetail : orderDetails) {
            dishesBuilder.append(orderDetail.getName())
                    .append("*")
                    .append(orderDetail.getNumber())
                    .append("; ");
        }
        return dishesBuilder.toString();
    }

    /**
     * admin order statistics
     * 
     * @return
     */
    @Override
    public OrderStatisticsVO orderStatistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(ordersMapper.countByStatus(Orders.TO_BE_CONFIRMED));
        orderStatisticsVO.setConfirmed(ordersMapper.countByStatus(Orders.CONFIRMED));
        orderStatisticsVO.setDeliveryInProgress(ordersMapper.countByStatus(Orders.DELIVERY_IN_PROGRESS));
        return orderStatisticsVO;
    }

    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        Orders order = new Orders();
        order.setCancelReason(ordersCancelDTO.getCancelReason());
        order.setId(ordersCancelDTO.getId());
        order.setCancelTime(LocalDateTime.now());
        order.setStatus(Orders.CANCELLED);
        ordersMapper.update(order);
    }

    @Override
    public void acceptOrder(OrdersConfirmDTO ordersConfirmDTO) {
        updateStatus(ordersConfirmDTO.getId(), Orders.CONFIRMED);
    }

    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        Orders order = new Orders();
        order.setId(ordersRejectionDTO.getId());
        order.setStatus(Orders.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        ordersMapper.update(order);
    }

    @Override
    public void deliverOrder(Long id) {
        updateStatus(id, Orders.DELIVERY_IN_PROGRESS);
    }

    @Override
    public void completeOrder(Long id) {
        Orders order = new Orders();
        order.setId(id);
        order.setStatus(Orders.COMPLETED);
        order.setDeliveryTime(LocalDateTime.now());
        ordersMapper.update(order);
    }

    public void updateStatus(Long id, Integer status) {
        Orders order = new Orders();
        order.setId(id);
        order.setStatus(status);
        ordersMapper.update(order);
    }

}
