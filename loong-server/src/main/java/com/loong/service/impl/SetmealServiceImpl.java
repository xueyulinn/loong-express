package com.loong.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.loong.dto.SetmealDTO;
import com.loong.dto.SetmealPageQueryDTO;
import com.loong.entity.Setmeal;
import com.loong.entity.SetmealDish;
import com.loong.mapper.CategoryMapper;
import com.loong.mapper.SetmealDishMapper;
import com.loong.mapper.SetmealMapper;
import com.loong.result.PageResult;
import com.loong.service.SetmealService;
import com.loong.vo.SetmealPageQueryVO;
import com.loong.vo.SetmealVO;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageResult pageQuery(int page, int pageSize, Long categoryId, String name, Integer status) {
        PageHelper.startPage(page, pageSize);

        SetmealPageQueryDTO setmealPageQueryDTO = SetmealPageQueryDTO.builder().page(page).pageSize(pageSize).name(name)
                .categoryId(categoryId).status(status)
                .build();

        Page<Setmeal> rs = setmealMapper.pageQuery(setmealPageQueryDTO);

        ArrayList records = new ArrayList<SetmealPageQueryVO>();

        // set vo
        for (Setmeal setmeal : rs) {
            String categoryName = categoryMapper.selectNameById(setmeal.getCategoryId());
            SetmealPageQueryVO vo = new SetmealPageQueryVO();
            BeanUtils.copyProperties(setmeal, vo);
            vo.setCategoryName(categoryName);
            records.add(vo);
        }

        return new PageResult(rs.getTotal(), records);
    }

    @Override
    @Transactional
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealMapper.insert(setmeal);

        for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
            setmealDish.setSetmealId(setmeal.getId());
            setmealDishMapper.insert(setmealDish);
        }

    }

    @Override
    public SetmealVO queryById(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealVO vo = new SetmealVO();
        BeanUtils.copyProperties(setmeal, vo);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
        vo.setSetmealDishes(setmealDishes);
        return vo;
    }

    @Override
    public void editSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // delete and insert setmealDish
        setmealDishMapper.deleteBySetmealId(setmeal.getId());

        for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
            setmealDish.setSetmealId(setmeal.getId());
            setmealDishMapper.insert(setmealDish);
        }
    }

    @Override
    public void deleteById(List<Long> ids) {
        for (Long id : ids) {
            setmealMapper.deleteById(id);
            setmealDishMapper.deleteBySetmealId(id);
        }
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        setmealMapper.updateStatus(status, id);
    }

    @Override
    public List<SetmealVO> listWithFlavor(Integer categoryId) {
        List<Setmeal> setmeals = setmealMapper.selectByCategoryId(categoryId);
        List<SetmealVO> vos = new ArrayList<>();
        for (Setmeal setmeal : setmeals) {
            SetmealVO vo = new SetmealVO();
            BeanUtils.copyProperties(setmeal, vo);
            vos.add(vo);
        }
        return vos;
    }

}
