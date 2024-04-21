package com.loong.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loong.dto.SetmealDTO;
import com.loong.result.PageResult;
import com.loong.result.Result;
import com.loong.service.SetmealService;
import com.loong.vo.SetmealVO;

@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;


    @CacheEvict(value = "setmeal", allEntries = true)
    @PostMapping("/status/{status}")
    public Result modifyStatus(@PathVariable Integer status, Long id) {
        setmealService.updateStatus(status, id);
        return Result.success();
    }

    @CacheEvict(value = "setmeal", allEntries = true)
    @DeleteMapping
    public Result deleteById(@RequestParam List<Long> ids) {
        setmealService.deleteById(ids);
        return Result.success();
    }

    @CacheEvict(value = "setmeal", key = "#setmealDTO.categoryId")
    @PutMapping
    public Result<SetmealVO> editSetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.editSetmeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("{id}")
    public Result<SetmealVO> queryById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.queryById(id);
        return Result.success(setmealVO);
    }

    @CacheEvict(value = "setmeal", key = "#setmealDTO.categoryId")
    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }


    @GetMapping("/page")
    public Result<PageResult> pageQuery(int page, int pageSize, Long categoryId, String name, Integer status) {
        PageResult result = setmealService.pageQuery(page, pageSize, categoryId, name, status);
        return Result.success(result);
    }
}
