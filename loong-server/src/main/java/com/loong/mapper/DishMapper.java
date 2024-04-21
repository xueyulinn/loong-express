package com.loong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.loong.annotation.AutoFill;
import com.loong.dto.DishPageQueryDTO;
import com.loong.entity.Dish;
import com.loong.entity.DishFlavor;
import com.loong.enumeration.OperationType;

import lombok.experimental.Delegate;

@Mapper
public interface DishMapper {

    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<Dish> selectByPage(DishPageQueryDTO dishPageQueryDTO);

    void batchDelete(List<Integer> ids);

    @Select("select * from dish where id = #{id}")
    Dish selectById(Integer id);

    @Delete("delete from dish where id = #{id}")
    void deleteById(Integer id);

    void update(Dish dish);

    @Update("update dish set status = #{status} where id = #{id}")
    void updateStatus(Integer status, Integer id);

    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> selectByCategoryId(Long categoryId);

    @Select("select * from dish where status = #{status} and category_id = #{categoryId}")
    List<Dish> selectByCondition(Dish dish);

}
