package com.loong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.loong.entity.OrderDetail;

@Mapper
public interface OrderDetailsMapper {

    @Insert("insert into order_detail (name,image,order_id, dish_id, setmeal_id, dish_flavor, number, amount) values (#{name},#{image},#{orderId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount})")
    void insert(OrderDetail orderDetail);

    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail>  selectByOrderId(Long id);

    @Delete("delete from order_detail where order_id = #{id}")
    void deleteByOrderId(Long id);

}
