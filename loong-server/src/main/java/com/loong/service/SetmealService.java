package com.loong.service;

import java.util.List;

import com.loong.dto.SetmealDTO;
import com.loong.result.PageResult;
import com.loong.vo.SetmealVO;

public interface SetmealService {

    PageResult pageQuery(int page, int pageSize, Long categoryId, String name, Integer status);

    void addSetmeal(SetmealDTO setmealDTO);

    SetmealVO queryById(Long id);

    void editSetmeal(SetmealDTO setmealDTO);

    void deleteById(List<Long> ids);

    void updateStatus(Integer status, Long id);

    List<SetmealVO> listWithFlavor(Integer categoryId);

}
