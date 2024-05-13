package com.loong.service;

import java.time.LocalDate;

import com.loong.vo.BusinessDataVO;
import com.loong.vo.DishOverViewVO;
import com.loong.vo.OrderOverViewVO;
import com.loong.vo.SetmealOverViewVO;

public interface WorkspaceService {

    SetmealOverViewVO setmealsOverview();

    DishOverViewVO dishesOverview();

    OrderOverViewVO ordersOverview();

    BusinessDataVO businessOverview(LocalDate begin);
    
}
