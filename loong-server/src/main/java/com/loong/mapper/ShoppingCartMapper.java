package com.loong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.loong.entity.ShoppingCart;

@Mapper
public interface ShoppingCartMapper {

    @Insert("insert into shopping_cart (user_id,dish_id, setmeal_id, dish_flavor) values (#{userId}, #{dishId}, #{setmealId}, #{dishFlavor})")
    void insert(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> list(Long userId);

    
}
