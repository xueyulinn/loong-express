package com.loong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.loong.dto.ShoppingCartDTO;
import com.loong.entity.ShoppingCart;

@Mapper
public interface ShoppingCartMapper {

    @Insert("insert into shopping_cart (create_time,user_id,dish_id, setmeal_id, dish_flavor, amount, name, image) values (#{createTime},#{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{amount}, #{name}, #{image})")
    void insert(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> list(Long userId);

    void delete(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    ShoppingCart selectById(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumber(ShoppingCart shoppingCart);


    
}
