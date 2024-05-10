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

    @Select("select * from orders where number = #{orderNumber} and user_id = #{userId}")
    Orders selectByNumberAndUserId(String orderNumber, Long userId);

    void update(Orders orders);

    Page<Orders> selectLimit(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    Orders selectById(Long id);

    Integer countByStatus(Integer status);

    @Select("select * from orders where id = #{id} and order_time < #{time}")
    List<Orders> selectByStatusAndTime(Integer id, LocalDateTime time);

    @Select("SELECT SUM(amount) FROM orders WHERE status = 5 AND DATE(order_time) = #{orderDate}")
    Double selectSumByDate(String orderDate);

    @Select("SELECT * FROM orders WHERE DATE(order_time) BETWEEN #{begin} AND #{end}")
    List<Orders> selectByDate(String begin, String end);

    @Select("SELECT count(*) FROM orders WHERE status = #{status} AND DATE(order_time) = #{date}")
    int selectByStatusAndDate(Integer status, String date);

    @Select("SELECT count(*) FROM orders WHERE DATE(order_time) = #{date}")
    int countByDate(String date);

    List<OrderDetail> selectTop10ByDate(String begin, String end);

    @Select("select if (sum(amount) is null, 0, sum(amount)) amount from orders where status = #{status} and date(order_time) = #{today}")
    Double sumByStatusAndDate(Integer status, String today);

}
