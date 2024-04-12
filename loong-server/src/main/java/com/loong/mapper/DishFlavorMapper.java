package com.loong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.loong.entity.DishFlavor;

@Mapper
public interface DishFlavorMapper {

    @Insert("insert into dish_flavor (dish_id, name, value) values (#{dishId}, #{name}, #{value})")
    void insert(DishFlavor dishFlavor);

    @Delete("delete from dish_flavor where dish_id = #{id}")
    void deleteByDishId(Integer id);

    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> selectByDishId(Integer id);

    
    void updateFlavors(DishFlavor dishFlavor);


}
