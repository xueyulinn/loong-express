package com.loong.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.loong.constant.MessageConstant;
import com.loong.constant.StatusConstant;
import com.loong.dto.DishDTO;
import com.loong.dto.DishPageQueryDTO;
import com.loong.entity.Dish;
import com.loong.entity.DishFlavor;
import com.loong.entity.Setmeal;
import com.loong.exception.DeletionNotAllowedException;
import com.loong.mapper.CategoryMapper;
import com.loong.mapper.DishFlavorMapper;
import com.loong.mapper.DishMapper;
import com.loong.mapper.SetmealMapper;
import com.loong.result.PageResult;
import com.loong.service.DishService;
import com.loong.vo.DishPageQueryVO;
import com.loong.vo.DishVO;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void addDish(DishDTO dishDTO) {

        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);

        if (dishDTO.getFlavors() == null || dishDTO.getFlavors().isEmpty()) {
            return;
        }

        for (DishFlavor flavor : dishDTO.getFlavors()) {
            flavor.setDishId(dish.getId());
            dishFlavorMapper.insert(flavor);
        }
        ;
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<Dish> page = dishMapper.selectByPage(dishPageQueryDTO);

        List<DishPageQueryVO> records = new ArrayList<>();

        // convert to vo
        for (Dish dish : page.getResult()) {
            String categoryName = categoryMapper.selectNameById(dish.getCategoryId());
            records.add(DishPageQueryVO.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .categoryId(dish.getCategoryId())
                    .price(dish.getPrice())
                    .image(dish.getImage())
                    .description(dish.getDescription())
                    .status(dish.getStatus())
                    .categoryName(categoryName)
                    .updateTime(dish.getUpdateTime())
                    .build());
        }

        return PageResult.builder()
                .records(records)
                .total(page.getTotal())
                .build();
    }

    @Override
    public void deleteDish(List<Integer> ids) {

        for (Integer id : ids) {
            Dish dish = dishMapper.selectById(id);
            // check if dish is on sale
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

            Setmeal setmeal = setmealMapper.selectByDishId(id);

            if (setmeal != null) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }

            dishMapper.deleteById(id);

            dishFlavorMapper.deleteByDishId(id);

        }

    }

    @Override
    public DishVO queryById(Integer id) {
        Dish dish = dishMapper.selectById(id);
        DishVO dishVO = new DishVO();

        BeanUtils.copyProperties(dish, dishVO);

        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);

        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    public void editDish(DishDTO dishDTO) {
        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.update(dish);

        // delete all flavors and insert again
        // since we don't known whether it would be updated or inserted
        dishFlavorMapper.deleteByDishId(dishDTO.getId().intValue());
        for (DishFlavor flavor : dishDTO.getFlavors()) {
            flavor.setDishId(dishDTO.getId());
            dishFlavorMapper.insert(flavor);
        }

    }

    @Override
    public void modifyStatus(Integer status, Integer id) {
        dishMapper.updateStatus(status, id);
    }

    @Override
    public List <Dish> queryByCategoryId(Long categoryId) {
        List<Dish> allDishes =  dishMapper.selectByCategoryId(categoryId);
        List<Dish> onSaleDishes = new ArrayList<>();
        // ensure only on sale dishes are returned
        allDishes.forEach(dish -> {
            if (dish.getStatus() == StatusConstant.ENABLE) {
                onSaleDishes.add(dish);
            }
        });
        return onSaleDishes;
    }

}
