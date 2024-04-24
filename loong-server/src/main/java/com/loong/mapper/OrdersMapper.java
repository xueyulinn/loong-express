package com.loong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.loong.dto.OrdersPageQueryDTO;
import com.loong.entity.Orders;

@Mapper
public interface OrdersMapper {

   
    void insert(Orders orders);

    @Select("select * from orders where number = #{outTradeNo} and user_id = #{userId}")
    Orders getByNumberAndUserId(String outTradeNo, Long userId);

    
    void update(Orders orders);

    Page<Orders> selectLimit(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    Orders selectById(Long id);

    
    
}
