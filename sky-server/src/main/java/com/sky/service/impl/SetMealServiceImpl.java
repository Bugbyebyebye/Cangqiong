package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetMealDTO;
import com.sky.dto.SetMealPageQueryDTO;
import com.sky.entity.SetMeal;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<SetMeal> list(SetMeal setmeal) {
        List<SetMeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetMealId(id);
    }

    @Override
    public void saveWithDish(SetMealDTO setMealDTO) {
        SetMeal setMeal = new SetMeal();
        setMeal.setCategoryId(setMealDTO.getCategoryId());
        setMeal.setName(setMealDTO.getName());
        setMeal.setPrice(setMealDTO.getPrice());
        setMeal.setStatus(setMealDTO.getStatus());
        setMeal.setDescription(setMealDTO.getDescription());
        setMeal.setImage(setMealDTO.getImage());
        setmealMapper.insert(setMeal);
    }

    @Override
    public PageResult pageQuery(SetMealPageQueryDTO setMealPageQueryDTO) {
        PageHelper.startPage(setMealPageQueryDTO.getPage(),setMealPageQueryDTO.getPageSize());
        Page<SetMeal> page = setmealMapper.pageQuery(setMealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        setmealMapper.deleteByIds(ids);
    }

    @Override
    public SetmealVO getByIdWithDish(Long id) {
        SetmealVO setmealVO = setmealMapper.getByIdWithDish(id);
        //setmealVO.setSetMealDishes(); TODO
        return setmealVO;
    }

    @Override
    public void updateWithDish(SetMealDTO setMealDTO) {
        SetMeal setMeal = new SetMeal();
        setMeal.setId(setMealDTO.getId());
        setMeal.setCategoryId(setMealDTO.getCategoryId());
        setMeal.setName(setMealDTO.getName());
        setMeal.setPrice(setMealDTO.getPrice());
        setMeal.setStatus(setMealDTO.getStatus());
        setMeal.setDescription(setMealDTO.getDescription());
        setMeal.setImage(setMealDTO.getImage());
        setmealMapper.update(setMeal);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        SetMeal setMeal = SetMeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setMeal);
        //if (status == 1) { TODO
        //    List<Long> dishIds = dishMapper.getDishIdsBySetMealId(id);
        //    dishMapper.updateStatusByDishIds(dishIds, status);
        //}
        //if (status == 0) {
        //    List<Long> dishIds = dishMapper.getDishIdsBySetMealId(id);
        //    dishMapper.updateStatusByDishIds(dishIds, status);
        //}
    }
}
