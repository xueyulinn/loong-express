package com.loong.service;

import java.util.List;

import com.loong.dto.DishDTO;
import com.loong.dto.DishPageQueryDTO;
import com.loong.entity.Dish;
import com.loong.result.PageResult;
import com.loong.vo.DishVO;

public interface DishService {

    void addDish(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(List<Integer> ids);

    DishVO queryById(Integer id);

    void editDish(DishDTO dishDTO);

    void modifyStatus(Integer status, Integer id);

    List <Dish> queryByCategoryId(Long categoryId);

    
} 


    
