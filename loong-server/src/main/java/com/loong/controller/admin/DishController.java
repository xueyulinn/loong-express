package com.loong.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loong.dto.DishDTO;
import com.loong.dto.DishPageQueryDTO;
import com.loong.entity.Dish;
import com.loong.result.PageResult;
import com.loong.result.Result;
import com.loong.service.DishService;
import com.loong.vo.DishVO;

@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    public Result<List> queryByCategoryId(Long categoryId) {
        List<Dish> dishes = dishService.queryByCategoryId(categoryId);
        return Result.success(dishes);
    }

    @PutMapping
    public Result<String> editDish(@RequestBody DishDTO dishDTO) {

        dishService.editDish(dishDTO);

        cleanCache(dishDTO.getCategoryId());
        return Result.success();
    }

    @GetMapping("{id}")
    public Result<DishVO> queryById(@PathVariable Integer id) {
        DishVO dishVO = dishService.queryById(id);
        return Result.success(dishVO);
    }

    @PostMapping
    public Result<String> addDish(@RequestBody DishDTO dishDTO) {
        dishService.addDish(dishDTO);
        cleanCache(dishDTO.getCategoryId());
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> pageQuery(@RequestParam int page, int pageSize, String name, Integer categoryId,
            Integer status) {
        DishPageQueryDTO dishPageQueryDTO = DishPageQueryDTO.builder()
                .page(page)
                .pageSize(pageSize)
                .name(name)
                .categoryId(categoryId)
                .status(status)
                .build();

        return Result.success(dishService.pageQuery(dishPageQueryDTO));
    }

    @DeleteMapping
    public Result<String> deleteDish(@RequestParam List<Integer> ids) {
        dishService.deleteDish(ids);
        cleanCache(null);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result<String> modifyStatus(@PathVariable Integer status, Integer id) {
        dishService.modifyStatus(status, id);
        cleanCache(dishService.queryById(id).getCategoryId());
        return Result.success();
    }

    public void cleanCache(Long categoryId) {
        if (categoryId == null) {
            redisTemplate.delete("dish_*");
            return;
        }
        redisTemplate.delete("dish_" + categoryId);
    }
}
