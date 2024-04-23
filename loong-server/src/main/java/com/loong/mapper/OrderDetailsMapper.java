package com.loong.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.loong.entity.OrderDetail;

@Mapper
public interface OrderDetailsMapper {

    @Insert("insert into order_detail (name,image,order_id, dish_id, setmeal_id, dish_flavor, number, amount) values (#{name},#{image},#{orderId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount})")
    void insert(OrderDetail orderDetail);

}
