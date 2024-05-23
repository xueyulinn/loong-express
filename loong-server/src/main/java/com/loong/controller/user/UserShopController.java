package com.loong.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.result.Result;

@RestController
@RequestMapping("/user/shop")
public class UserShopController {
    private String key = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    public Result<Integer> queryStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(key);
        return Result.success(status);
    }


   
}
