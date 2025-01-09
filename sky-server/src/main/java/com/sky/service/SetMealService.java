package com.sky.service;

import com.sky.entity.SetMeal;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface SetMealService {

    List<SetMeal> list(SetMeal setmeal);

    List<DishItemVO> getDishItemById(Long id);
}
