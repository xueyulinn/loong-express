package com.loong.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.result.Result;
import com.loong.service.WorkspaceService;
import com.loong.vo.BusinessDataVO;
import com.loong.vo.DishOverViewVO;
import com.loong.vo.OrderOverViewVO;
import com.loong.vo.SetmealOverViewVO;

@RestController
@RequestMapping("/admin/workspace")
public class Workspace {
    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> setmealsOverview() {
        SetmealOverViewVO setmealOverViewVO = workspaceService.setmealsOverview();
        return Result.success(setmealOverViewVO);
    }

    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> dishesOverview() {
        DishOverViewVO dishOverViewVO = workspaceService.dishesOverview();
        return Result.success(dishOverViewVO);
    }

    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> ordersOverview() {
        OrderOverViewVO orderOverViewVO = workspaceService.ordersOverview();
        return Result.success(orderOverViewVO);
    }

    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessOverview() {
        BusinessDataVO businessDataVO = workspaceService.businessOverview();
        return Result.success(businessDataVO);
    }
}
