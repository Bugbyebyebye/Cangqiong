package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 新增一个菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品表插入一个数据
        dishMapper.insert(dish);
        //向口味表插入多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishMapper.insertFlavors(flavors);
        }
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        // 判断菜品是否起售，起售则不能删除
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)){
                throw new RuntimeException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断菜品是否关联套餐，关联则不能删除
        List<Long> setMealIds = setMealMapper.getSetMealIdsByDishIds(ids);
        if (setMealIds != null && !setMealIds.isEmpty()){
            throw new RuntimeException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品
        for (Long id : ids) {
            dishMapper.deleteById(id);
            dishMapper.deleteFlavorByDishId(id);
        }
    }
}
