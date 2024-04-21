package com.loong.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loong.entity.Category;
import com.loong.result.Result;
import com.loong.service.CategoryService;

@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * select category by type
     * 
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Integer categoryId) {

        List<Category> list = categoryService.list(categoryId);
        return Result.success(list);
    }
}
