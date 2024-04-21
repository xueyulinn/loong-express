package com.loong.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.result.Result;
import com.loong.service.SetmealService;
import com.loong.vo.SetmealVO;

@RequestMapping("/user/setmeal")
@RestController("userSetmealController")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Cacheable(value = "setmeal", key = "#categoryId")
    @GetMapping("/list")
    public Result<List<SetmealVO>> list(Integer categoryId) {
        List<SetmealVO> list = setmealService.listWithFlavor(categoryId);
        return Result.success(list);
    }
}
