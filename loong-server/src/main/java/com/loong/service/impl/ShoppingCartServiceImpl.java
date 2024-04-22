package com.loong.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loong.context.BaseContext;
import com.loong.dto.ShoppingCartDTO;
import com.loong.entity.Dish;
import com.loong.entity.Setmeal;
import com.loong.entity.ShoppingCart;
import com.loong.mapper.DishMapper;
import com.loong.mapper.SetmealMapper;
import com.loong.mapper.ShoppingCartMapper;
import com.loong.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        ShoppingCart existedShoppingCart = shoppingCartMapper.selectById(shoppingCart);

        if (existedShoppingCart != null) {
            // item already exists in the shopping cart
            existedShoppingCart.setNumber(existedShoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumber(existedShoppingCart);
            return;
        }

        if (shoppingCartDTO.getDishId() == null) {
            // added item is a setmeal
            Setmeal setmeal = setmealMapper.selectById(shoppingCartDTO.getSetmealId());
            shoppingCart.setAmount(setmeal.getPrice());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setName(setmeal.getName());
        } else {
            // added item is a dish
            Dish dish = dishMapper.selectById(shoppingCartDTO.getDishId().intValue());
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setName(dish.getName());
        }

        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCart.setUserId(BaseContext.getCurrentId());

        shoppingCartMapper.insert(shoppingCart);
    }

    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartMapper.list(userId);
        return list;
    }

    @Override
    public void subtract(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        ShoppingCart existedShoppingCart = shoppingCartMapper.selectById(shoppingCart);
        if (existedShoppingCart.getNumber() == 1) {
            shoppingCartMapper.delete(existedShoppingCart);
            return;
        }
        existedShoppingCart.setNumber(existedShoppingCart.getNumber() - 1);
        shoppingCartMapper.updateNumber(existedShoppingCart);
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

}
