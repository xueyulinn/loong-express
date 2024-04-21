package com.loong.service;

import java.util.List;

import com.loong.dto.ShoppingCartDTO;
import com.loong.entity.ShoppingCart;

public interface ShoppingCartService {

    void add(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list();
    
}
