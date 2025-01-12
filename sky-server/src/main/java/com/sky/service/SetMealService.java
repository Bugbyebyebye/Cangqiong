package com.sky.service;

import com.sky.dto.SetMealDTO;
import com.sky.dto.SetMealPageQueryDTO;
import com.sky.entity.SetMeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {

    List<SetMeal> list(SetMeal setmeal);

    List<DishItemVO> getDishItemById(Long id);

    void saveWithDish(SetMealDTO setMealDTO);

    PageResult pageQuery(SetMealPageQueryDTO setMealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDish(Long id);

    void updateWithDish(SetMealDTO setMealDTO);

    void startOrStop(Integer status, Long id);
}
