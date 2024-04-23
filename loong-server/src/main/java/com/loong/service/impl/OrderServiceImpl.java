package com.loong.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.loong.constant.MessageConstant;
import com.loong.context.BaseContext;
import com.loong.dto.OrdersDTO;
import com.loong.dto.OrdersPaymentDTO;
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
import com.loong.service.OrderService;
import com.loong.utils.WeChatPayUtil;
import com.loong.vo.OrderPaymentVO;
import com.loong.vo.OrderSubmitVO;

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
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.selectByUserId(userId);

        // 调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), // 商户订单号
                new BigDecimal(0.01), // 支付金额，单位 元
                "Loong Express Order", // 商品描述
                user.getOpenid() // 微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = ordersMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);
    }

}
