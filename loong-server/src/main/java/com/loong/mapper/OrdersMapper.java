package com.loong.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.loong.dto.OrdersPageQueryDTO;
import com.loong.entity.OrderDetail;
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

    @Select("select count(*) from orders where status = #{status}")
    Integer countByStatus(Integer status);

    @Select("select * from orders where id = #{id} and order_time < #{time}")
    List<Orders> selectByStatusAndTime(Integer id, LocalDateTime time);

    @Select("SELECT SUM(amount) FROM orders WHERE status = 5 AND DATE(order_time) = #{orderDate} group by order_time")
    Double selectSumByDate(String orderDate);

    @Select("SELECT * FROM orders WHERE DATE(order_time) BETWEEN #{begin} AND #{end}")
    List<Orders> selectByDate(String begin, String end);

    @Select("SELECT count(*) FROM orders WHERE status = #{completed} AND DATE(order_time) = #{date}")
    int selectByStatusAndDate(Integer completed, String date);

    @Select("SELECT count(*) FROM orders WHERE DATE(order_time) = #{string}")
    int countByDate(String string);

    List<OrderDetail> selectTop10ByDate(String begin, String end);

}
