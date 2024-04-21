package com.loong.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.loong.context.BaseContext;
import com.loong.dto.ShoppingCartDTO;
import com.loong.entity.ShoppingCart;
import com.loong.mapper.ShoppingCartMapper;
import com.loong.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCartMapper.insert(shoppingCart);
    }


    @Override
    public List<ShoppingCart> list() {
        Long userId =  BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartMapper.list(userId);
        return list;
    }
    
}
