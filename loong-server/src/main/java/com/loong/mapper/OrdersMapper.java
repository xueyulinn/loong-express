package com.loong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.loong.entity.Orders;

@Mapper
public interface OrdersMapper {

   
    void insert(Orders orders);

    @Select("select * from orders where number = #{outTradeNo} and user_id = #{userId}")
    Orders getByNumberAndUserId(String outTradeNo, Long userId);

    
    void update(Orders orders);
    
}
